package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.File.File;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.User.Permission;
import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Mapper.FileMapper;
import cn.j.netstorage.Mapper.FolderMapper;
import cn.j.netstorage.tool.FilesUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    FileService2 fileService2;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    FolderMapper folderMapper;

    @Test
    void addRole() {

        User user=userService.getUser(6L);
        System.out.println(fileMapper.findAllBySelfNameLikeAndUserAndParentName("itemname.zip",user,"/").size());
        System.out.println("test.jar".substring("test.jar".lastIndexOf(".")));
//        Role role = userService.role(10L);
//
//        Permission permission = new Permission();
//        permission.setName("下载");
//        Long pid = userService.savePermission(permission);
//        permission.setPid(pid);
//
//        role.getPermission().add(permission);
//        userService.addRole(role);

    }

    @Test
    void roles() {
        User user = userService.getUser("admin@test.com");
        Role role = new Role();
        role.setRid(10L);
        user.getRole().add(role);
        userService.saveUser(user);

    }

    @Test
    void alterRole() {
//        System.out.println("list:"+fileService2.folders(FilesUtil.setUser(6L)));
        System.out.println("list:"+folderMapper.findByUsersAndFolderPath(FilesUtil.setUser(6L), "/测试文件夹/测/"));
    }

    @Test
    void delRole() {
        fileMapper.findByParentNameLikeAndUser("/测试文件夹/", FilesUtil.setUser(100L)).forEach(value->{
            System.out.println(new FilesDTO(value));
        });
//        fileService2.zip(FilesUtil.setUser(6L),76L,78L,105L);

    }
}