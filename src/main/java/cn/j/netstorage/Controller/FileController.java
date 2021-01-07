package cn.j.netstorage.Controller;

import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.DTO.OriginFileDTO;
import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.File.Aria2File;
import cn.j.netstorage.Entity.File.File;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.Folder;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Service.Aria2Service;
import cn.j.netstorage.Service.FileService2;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.EncrypDes;
import cn.j.netstorage.tool.ResultBuilder;
import cn.j.netstorage.tool.StatusCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.zip.ZipOutputStream;

@RestController
@RequiresUser
@RequestMapping("/disk/")
public class FileController {

    @Autowired
    private FilesService filesService;

    @Autowired
    private FileService2 fileService2;

    @Autowired
    private UserService userService;

    @Autowired
    private Aria2Service aria2Service;

    @PostMapping("/uploadFolder")
    @RequiresUser
    @RequiresPermissions("上传")
    public ResultBuilder<Boolean> createFolder(@RequestBody FilesDTO filesDTO) {
        UserDTO userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
        filesDTO.setIsDir(Files.is_dir);
        filesDTO.setCreateDate(new Date());
        Files files = filesDTO.ConvertFiles();
        files.setUser(userDTO.convertUsers());
        Boolean result = filesService.uploadFile(files, null, null);
        return new ResultBuilder<>(result, result ? StatusCode.SUCCESS : StatusCode.FALL);
    }

    @PostMapping("/uploadFile")
    @RequiresUser
    @RequiresPermissions("上传")
    public ResultBuilder<Boolean> UploadFile(@RequestParam("upload") MultipartFile multipartFile, @RequestParam("parentName") String parentName) {
        FilesDTO filesDTO = new FilesDTO();
        UserDTO userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
        filesDTO.setIsDir(Files.no_dir);
        filesDTO.setCreateDate(new Date());
        filesDTO.setSelfName(multipartFile.getOriginalFilename());
        filesDTO.setParentName(parentName);

        Files files = filesDTO.ConvertFiles();
        files.setUser(userDTO.convertUsers());
        Boolean result = filesService.uploadFile(files, null, multipartFile);
        return new ResultBuilder<>(result, result ? StatusCode.SUCCESS : StatusCode.FALL);
    }


    @PostMapping("/transferFile")
    @RequiresUser
    public ResultBuilder<Boolean> transfer(@RequestBody TreeMap<String, String> treeMap) {
        FilesDTO filesDTO = new FilesDTO();
        UserDTO userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
        filesDTO.setIsDir(Files.no_dir);
        filesDTO.setCreateDate(new Date());
        filesDTO.setSelfName(treeMap.get("name"));
        filesDTO.setParentName(treeMap.get("parentName"));
        Files files = filesDTO.ConvertFiles();
        files.setUser(userDTO.convertUsers());
        Boolean result = filesService.uploadFile(files, null, null);
        return new ResultBuilder<>(result, result ? StatusCode.SUCCESS : StatusCode.FALL);
    }


    @GetMapping("/fileList")
    @RequiresUser
    public ResultBuilder<List<FilesDTO>> getFileList(String parentName, String path) {
        UserDTO userDTO = null;
        userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
        List<FilesDTO> fileList = filesService.UserFile(parentName, userDTO.getUid());
        return new ResultBuilder<>(fileList, StatusCode.SUCCESS);
    }

//    @GetMapping("/SFile")
//    @RequiresUser
//    public ResultBuilder<List<FilesDTO>> folderList(Long fid) {
//        UserDTO userDTO = null;
//        userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
//        System.out.println(fid);
//        List<FilesDTO> fileList = filesService.UserFile(fid, userDTO.getUid());
//        return new ResultBuilder<>(fileList, StatusCode.SUCCESS);
//    }

    @GetMapping("/type")
    @RequiresUser
    public ResultBuilder<List<FilesDTO>> getFileListByType(String type) {
        UserDTO userDTO = new UserDTO(userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()));
        List<FilesDTO> fileList = filesService.UserFile(type, userDTO.getUid());
        return new ResultBuilder<>(fileList, StatusCode.SUCCESS);
    }

    @PostMapping("/delete")
    @RequiresUser
    public ResultBuilder<Boolean> deleteFileById(@RequestBody Map<String, String> map) {
        if (map != null && map.size() >= 1 && map.containsKey("fid")) {
            Long id = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString()).getUid();
            Long fid = Long.valueOf(map.get("fid"));
            Files files = filesService.getFilesById(Long.valueOf(map.get("fid"))).ConvertFiles();
            Boolean result = files.getIsDir() == Files.is_dir ?
                    filesService.deleteFolders(files.getParentName(), files.getSelfName(), fid, id) : filesService.deleteUserFiles(id, fid);
            return new ResultBuilder<>(result, result ? StatusCode.SUCCESS : StatusCode.FALL);
        }
        return new ResultBuilder<>(StatusCode.FALL);
    }

    @PutMapping("/Rename/{id}")
    @RequiresUser
    public ResultBuilder<Boolean> Rename(@PathVariable("id") Long fid, String targetName) {
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
            fileService2.RenameFile(user, fid, targetName);
            return new ResultBuilder<>(StatusCode.SUCCESS);
        }
        return new ResultBuilder<>(StatusCode.REQUEST_PARAM_ERROR);
    }

    @GetMapping("/getFile")
    @RequiresUser
    public ResultBuilder<OriginFileDTO> Files(@RequestParam String pathName, @RequestParam String fileName) {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        try {
            pathName = URLDecoder.decode(pathName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResultBuilder<>(StatusCode.REQUEST_PARAM_ERROR);
        }
        return new ResultBuilder<>(new OriginFileDTO(filesService.findByParentNameAndAndUserAndAndSelfName(pathName, user, fileName)), StatusCode.SUCCESS);
    }

    @GetMapping("/getFileById")
    @RequiresUser
    public ResultBuilder<OriginFileDTO> getFiles(String fid) {
        FilesDTO filesDTO = filesService.getFilesById(Long.valueOf(fid));
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        return new ResultBuilder<OriginFileDTO>(new OriginFileDTO(filesService.findByParentNameAndAndUserAndAndSelfName(filesDTO.getParentName(), user, filesDTO.getSelfName())), StatusCode.SUCCESS);
    }


    @GetMapping("/aria2Torrent")
    @RequiresUser
    public ResultBuilder<Boolean> downloadTorrent(String fid) {
        FilesDTO filesDTO = filesService.getFilesById(Long.valueOf(fid));
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        OriginFile originFile = filesService.findByParentNameAndAndUserAndAndSelfName(filesDTO.getParentName(), user, filesDTO.getSelfName());
        return new ResultBuilder<Boolean>(aria2Service.downloadTorrent(String.format("download-%s", user.getUid()), Long.valueOf(fid), user, originFile), StatusCode.SUCCESS);
    }

    @GetMapping("/aria2DownloadList")
    @RequiresUser
    public ResultBuilder<List<String>> downloadList() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        return new ResultBuilder<List<String>>(aria2Service.downloadList(String.format("downloadList-%s", user.getUid()), user), StatusCode.SUCCESS);
    }

    @GetMapping("/aria2Detail")
    @RequiresUser
    public ResultBuilder<String> ariaMissionDetail(String gid) {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        return new ResultBuilder<>(aria2Service.Detail(String.format("detail-%s", user.getUid()), gid), StatusCode.SUCCESS);
    }

    @GetMapping("/StoppedList")
    @RequiresUser
    public ResultBuilder<List<Aria2File>> StoppedList() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        return new ResultBuilder<>(aria2Service.StoppedList(String.format("detail-%s", user.getUid()), user), StatusCode.SUCCESS);
    }

    @GetMapping("/searchFiles")
    @RequiresUser
    public ResultBuilder<List<FilesDTO>> searchFiles(String keyword) {
        FilesDTO filesDTO = new FilesDTO();
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        filesDTO.setSelfName(keyword);
        return new ResultBuilder<>(filesService.searchFiles(filesDTO, user), StatusCode.SUCCESS);
    }

    @PutMapping("/folder")
    @RequiresUser
    public ResultBuilder<Boolean> shareFolder(Long fid, String email) {
        User user = userService.getUser(email);
        Boolean result = fileService2.shareFolder(fid, user);
        return new ResultBuilder(result, result ? StatusCode.SUCCESS : StatusCode.FALL);
    }

    @GetMapping("/folder")
    @RequiresUser
    public ResultBuilder<List<FilesDTO>> getShareFolder() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        //查出所有该用户拥有的共享文件夹
        return new ResultBuilder(fileService2.folders(user), StatusCode.SUCCESS);
    }

    @GetMapping("/folder/{id}")
    public ResultBuilder getShareFolder(@PathVariable("id") Long id) {
        return new ResultBuilder(fileService2.getFolder(id), StatusCode.SUCCESS);
    }


    @DeleteMapping("/folder/{id}")
    public ResultBuilder deleteShareFolder(@PathVariable("id") Long id) {
        boolean result = fileService2.deleteFolders(id);
        return new ResultBuilder(result ? StatusCode.SUCCESS : StatusCode.FALL);
    }

    @PostMapping("/download")
    @ResponseBody
    public void download(HttpServletResponse response, Long... fid) {
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("压缩包名称.zip".getBytes("GB2312"), "ISO-8859-1"));  // 需要编码否则中文乱码
            response.setContentType("application/zip;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        ZipOutputStream zipOutputStream=null;
        try {zipOutputStream = new ZipOutputStream(response.getOutputStream());
            fileService2.zip(zipOutputStream, user, fid);
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
