package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Entity.Vo.UserVo;
import cn.j.netstorage.Entity.plugin.Plugin;
import cn.j.netstorage.Mapper.HardDeviceMapper;
import cn.j.netstorage.NetstorageApplication;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.PluginService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HashCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NetstorageApplication.class)
class FileServiceImplTest {
    @Autowired
    FilesService filesService;
    @Autowired
    UserService userService;

    @Test
    public void test() {
        System.out.println("FindBySelfName");
        FilesDTO filesDTO = new FilesDTO();
        filesDTO.setSelfName("周华健");
        filesService.searchFiles(filesDTO, FilesUtil.setUser(4L)).forEach(System.out::println);
    }

    @Test
    public void test2() {
        System.out.println("FindByNickName");
        UserVo userVo = new UserVo();
        userVo.setNickName("Test");
        userService.search(userVo).forEach(System.out::println);
    }

    @Autowired
    PluginService pluginService;

    @Test
    public void test3() {
        System.out.println("FindByPluginName");
        Plugin p = new Plugin();
        p.setPluginName("测");
        pluginService.searchPlugins(p).forEach(System.out::println);
    }

    @Test
    void userFiles() {
//        System.out.println(filesService.UserFiles("/", 2));
    }


    @Test
    void delete() {
        filesService.deleteUserFiles(4L, 27L);
        filesService.deleteUserFiles(4L, 29L);

    }

    //
//    @Test
//    void deleteUserFiles() {
//
//        for (FilesDTO filesDTO:filesService.UserFiles("test",2)){
//            System.out.println(filesService.deleteUserFiles(2, Long.valueOf(filesDTO.getFid())));
//        }
//
//
//    }
//
    @Test
    void getFilesById() {
        Long fid = 158L;
        Double version = 1.00;
        User user = new User();
        user.setUid(2L);
        Files files = filesService.findByFid(fid);
        System.out.println(files.toString());
    }

    //
//    @Test
//    void deleteFolders(){
//        Files files=filesService.getFilesById(44).ConvertFiles();
//        System.out.println(filesService.deleteFolders(files.getParentName(),files.getSelfName(),files.getFid(), 2L));
//    }
//
//    @Test
//    public void test() {
//        Files file = new Files();
//        file.setSelfName("test.json");
//        int num = file.getSelfName().lastIndexOf(".");
//        if (num != -1) {
//            String s1 = file.getSelfName().substring(0,num);
//            String s2 = file.getSelfName().substring(num);
//            file.setSelfName(String.format(s1+"(%s)"+s2,1));
//        }
//    }
//
//    @Test
//    void checkUpload() {
//        try {
//            Path path = Paths.get("C:\\Users\\Shinelon\\Desktop\\netstorage\\pom.xml");
//            File file = new File(path.toUri());
//            filesService.checkUpload(HashCodeUtil.getHashCode(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void createFolder() {
//        //for (int i=0;i<3;i++){
//            List<FilesDTO> filesList=filesService.UserFiles("/",2);
//            cn.j.netstorage.Entity.File.Files parent = filesList.size()==0?null:filesList.get(0).ConvertFiles();
//
//            Files files=new Files();
//
//            files.setIsDir(cn.j.netstorage.Entity.File.Files.is_dir);
//            files.setSelfName("testFolder");
//            if (parent==null){
//                files.setParentName("/");
//            }else{
//                files.setParentName(parent.getParentName()+parent.getSelfName()+"/");
//            }
//
//            User user = new User();
//            user.setUid(2);
//            files.setUser(Collections.singletonList(user));
//            filesService.uploadFile(files, null);
//
//    }
//
    @Test
    void uploadFile() {
        for (int j = 0; j < 3; j++) {
            long[] ids = new long[]{
                    2
            };
            try {
                for (int i = 0; i < ids.length; i++) {
                    Path path = Paths.get("C:\\Users\\Shinelon\\Desktop\\netstorage\\src\\main\\resources\\application.properties");
                    File file = new File(path.toUri());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(), fileInputStream);

                    cn.j.netstorage.Entity.File.Files files = new cn.j.netstorage.Entity.File.Files();

                    User user = new User();
                    user.setUid(ids[i]);
                    files.setUser(Collections.singletonList(user));

                    files.setParentName("/");
                    files.setSelfName(file.getName());
                    files.setIsDir(cn.j.netstorage.Entity.File.Files.no_dir);

                    filesService.uploadFile(files, "", multipartFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Autowired
    HardDeviceMapper hardDeviceMapper;

    @Test
    void saveHardDevice() {
        String[] arr = new String[]{
                "F:\\software\\testFolder -1", "D:\\nmyslog"
        };
        for (int i = 0; i < arr.length; i++) {
            HardDiskDevice hardDiskDevice = new HardDiskDevice();
            hardDiskDevice.setDeviceName("随机-" + i);
            hardDiskDevice.setCustomName("/api/Files_" + i);
            hardDiskDevice.setRules("*");
            hardDiskDevice.setFolderName(arr[i]);
            hardDiskDevice = hardDeviceMapper.save(hardDiskDevice);
            if (hardDiskDevice.getId() != 0) {
                new File(hardDiskDevice.getFolderName()).mkdirs();
            }
        }


    }
//
//    @Test
//    void findByParentNameAndAndUserAndAndSelfName() {
//        String parentName = "/";
//        String selfName = "application.properties";
//        long uid = 2;
//        User user = new User();
//        user.setUid(uid);
//
//        System.out.println(filesService.findByParentNameAndAndUserAndAndSelfName(parentName, user, selfName).toString());
//
//    }
}