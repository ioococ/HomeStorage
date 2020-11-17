package cn.j.netstorage;


import cn.j.netstorage.Entity.Aria2Entity;
import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Entity.Vo.UserVo;
import cn.j.netstorage.Entity.plugin.Plugin;
import cn.j.netstorage.Service.DLNAService;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.PluginService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.Aria2Http;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HttpUtil;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NetstorageApplication.class)
public class NetstorageApplicationTests {


    @Test
    public void contextLoads() {
        System.out.println("1111");
    }

    @Autowired
    FilesService filesService;

    @Test
    public void test() {
        System.out.println("FindBySelfName");
        FilesDTO filesDTO=new FilesDTO();
        filesDTO.setSelfName("周华健");
        filesService.searchFiles(filesDTO,FilesUtil.setUser(4L)).forEach(System.out::println);
    }

    @Test
    public void test2() {
        System.out.println("FindByNickName");
        UserVo userVo=new UserVo();
        userVo.setNickName("Test");
        userService.search(userVo).forEach(System.out::println);
    }

    @Autowired
    PluginService pluginService;
    @Test
    public void test3() {
        System.out.println("FindByPluginName");
        Plugin p=new Plugin();
        p.setPluginName("测");
        pluginService.searchPlugins(p).forEach(System.out::println);
    }

//    @Test
//    public void insertUser() {
//        User user = new User();
//        user.setCreateDate(System.currentTimeMillis());
//        user.setEmailAccount("714308273@qq.com");
//        user.setPassword("xiening123");
//        user.setNickName("J");
//        Role role = new Role();
//        role.setRid(1);
//        user.setRole((role));
//        userService.Register(user);
//    }

    @Autowired
    UserService userService;

//    @Test
//    public void addRole() {
//        Role role = new Role();
//        role.setName("test");
//        role.setStatus(true);
//        userService.addRole(role);
//    }
//
//    @Test
//    public void findUser() {
////        userService.getUsers(null).forEach((value) -> {
////            System.out.println(value.getRole().toString());
////        });
//    }


//    @Test
//    public void addFiles() {
//        Files files = new Files();
//        files.setIsDir(Files.no_dir);
//        User user = new User();
//        user.setUid(2);
//        files.setUser(new ArrayList<User>() {
//            {
//                add(user);
//            }
//        });
//        files.setSelfName("test.txt");
////        filesService.uploadFile(files);
//    }


//    @Test
//    public void addFolders() {
//        Files files = new Files();
//        files.setIsDir(Files.is_dir);
//        files.setParentName("/");
//        User user = new User();
//        user.setUid(2);
//        files.setUser(new ArrayList<User>() {
//            {
//                add(user);
//            }
//        });
//        files.setSelfName("testDir");
////        filesService.uploadFile(files);
//    }

    @Test
    public void ListFiles() {

    }
//
//    @Test
//    public void checkMd5Exists() {
//
//        System.out.println(filesService.checkUpload("1111"));
//    }

    private String[] rowName = new String[]{"gid",
            "totalLength",
            "completedLength",
            "uploadSpeed",
            "downloadSpeed",
            "connections",
            "numSeeders",
            "seeder",
            "status",
            "errorCode",
            "verifiedLength",
            "verifyIntegrityPending",
            "files",
            "bittorrent",
            "infoHash"};

//    @Test
//    public void aria2() {
//        String url="test";
//        String filePath="D://test//";
//        Aria2Entity aria2Entity = new Aria2Entity();
//        aria2Entity.setId("tellActive-1");
//        aria2Entity.setMethod("aria2.tellActive");
//        aria2Entity.setParams(new Object[]{new String []{url},Collections.singletonMap("dir",filePath)});
//        String jsonString = new Gson().toJson(aria2Entity);
//        System.out.println(jsonString);
////        String result = Aria2Http.post("http://localhost:6800/jsonrpc", jsonString);
////        System.out.println(result);
////        try {
////            System.out.println(HttpUtil.post("http://localhost:6800/jsonrpc",
////                    "123",string));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            client = Aria2RPCWebSocket.getClient();
////            String text = Aria2RPCWebSocket.createJsonRPC("search-1", "aria2.tellActive",
////                    new Object[]{rowName});
////            System.out.println(text);
////            client.send(text);
////        } catch (URISyntaxException e) {
////            e.printStackTrace();
////        } finally {
////            client.close();
////        }
//    }

    @Test
    public void download() {
//        Aria2RPCWebSocket client = null;
//        try {
//            client = Aria2RPCWebSocket.getClient();
//
//            String uri = "magnet:?xt=urn:btih:fb84d65ac456d6c98d64b7b3174aa33ba047a204&tr=http://open.acgtracker.com:1096/announce";
//            String filePath = "D://test//";
//            String text = Aria2RPCWebSocket.createJsonRPC(
//                    "download-1",
//                    "aria2.addUri",
//                    new Object[]{uri}, Collections.singletonMap("dir", filePath));
//            System.out.println(text);
//            client.send(text);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } finally {
//            client.close();
//        }
    }

    @Test
    public void checkDoneMission() {
//    {"jsonrpc":"2.0","method":"aria2.tellStopped","id":"QXJpYU5nXzE2MDE2MjYxNTZfMC44Njc2NzE3ODQyNTE0MTIx","params":[-1,1000,["gid","totalLength","completedLength","uploadSpeed","downloadSpeed","connections","numSeeders","seeder","status","errorCode","verifiedLength","verifyIntegrityPending"]]}
//        Aria2RPCWebSocket client = null;
//        try {
//            client = Aria2RPCWebSocket.getClient();
//
//            String text = Aria2RPCWebSocket.createJsonRPC(
//                    "Stopped-1",
//                    "aria2.tellStopped",
//                    -1, 100,rowName);
//            System.out.println(text);
//            client.send(text);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } finally {
//            client.close();
//        }
    }


//    @Test
//    public void testUtil(){
//        Plugin p=new Plugin();
//        p.setIp("192.168.43.174");
//        p.setPort("8080");
//        p.setMapping("/dashboard/");
//        p.setPluginName("测试");
//        p.setStatus(true);
//        pluginService.save(p);
//    }
}
