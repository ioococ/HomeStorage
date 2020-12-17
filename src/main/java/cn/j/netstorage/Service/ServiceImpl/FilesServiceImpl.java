package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Mapper.FileMapper;
import cn.j.netstorage.Service.FileService2;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.FilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class FilesServiceImpl implements FileService2 {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    UserService userService;


    @Override
    public Boolean save(Files files) {
        return fileMapper.save(files).getFid() != 0;
    }

    @Override
    public Boolean del(Files files) {
        try {
            fileMapper.deleteById(files.getFid());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean move(Files files, String path) {
        return null;
    }

    @Override
    public List<Files> files(Long... id) {
        return fileMapper.findAllById(Arrays.asList(id));
    }

    @Override
    public List<Files> files() {
        return null;
    }

    @Override
    public Files file(String finalName, OriginFile originFile, String storagePath, User user) {
        Files files = new Files();
        files.setParentName(storagePath);
        files.setUser(Collections.singletonList(user));
        files.setIsDir(Files.no_dir);
        files.setCreateDate(new Date());
        files.setOriginFile(Collections.singleton(originFile));
        files.setSelfName(finalName);

        Type type = Type.getInstance(FilesUtil.getExt(finalName));
        files.setType(type.getType());

        return files;
    }

    @Override
    public int checkFilesCount(String parentName, String fileName, User user) {
        int pos = fileName.lastIndexOf(".");
        String likeName = fileName.substring(0, pos) + "%" + fileName.substring(pos);
        return fileMapper.findAllBySelfNameLikeAndUserAndParentName(likeName, user, parentName).size();
    }


}
