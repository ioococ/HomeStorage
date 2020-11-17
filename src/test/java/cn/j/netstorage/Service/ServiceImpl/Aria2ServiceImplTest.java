package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.NetstorageApplication;
import cn.j.netstorage.Service.Aria2Service;
import cn.j.netstorage.tool.FilesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NetstorageApplication.class)
class Aria2ServiceImplTest {

    @Autowired
    Aria2Service aria2Service;

    @Test
    void download() {
//        OriginFileDTO originFile = new OriginFileDTO();
//        originFile.setPath("7A94A171F77073D6DB44E60A8D073CFD38F36145.torrent");
//        User user = new User();
//        user.setUid(2);
//        System.out.println(aria2Service.downloadTorrent("test-1", 111L, user, originFile));
    }

    @Test
    void downloadList() {
        User user = new User();
        user.setUid(4L);
        System.out.println(aria2Service.downloadList("QXJpYU5nXzE2MDQ0MTAwOTBfMC4xNjA3NTIyMTc3ODM0NTk1",user));
    }

    @Test
    void stoppedList() {

    }

    @Test
    void downloadDone() {
    }

    @Test
    void pause() {
    }

    @Test
    void unpause() {
    }

    @Test
    void DownloadDetail() {
        String gid = "ebaf7fab6aad4352";
        aria2Service.Detail("detail", gid);
    }

    @Test
    void parse() {
        String data = "{\"id\":\"detail\",\"jsonrpc\":\"2.0\",\"result\":{\"bittorrent\":{\"announceList\":[[\"http:\\/\\/tr.cili001.com:8070\\/announce\"],[\"http:\\/\\/tracker.trackerfix.com:80\\/announce\"],[\"udp:\\/\\/9.rarbg.me:2710\\/announce\"],[\"udp:\\/\\/9.rarbg.to:2710\\/announce\"],[\"udp:\\/\\/open.demonii.com:1337\"],[\"udp:\\/\\/p4p.arenabg.com:1337\"],[\"udp:\\/\\/tracker.opentrackr.org:1337\\/announce\"],[\"wss:\\/\\/tracker.btorrent.xyz\"],[\"wss:\\/\\/tracker.fastcast.nz\"],[\"wss:\\/\\/tracker.openwebtorrent.com\"],[\"udp:\\/\\/tracker.coppersurfer.tk:6969\\/announce\"],[\"udp:\\/\\/tracker.opentrackr.org:1337\\/announce\"],[\"udp:\\/\\/tracker.leechers-paradise.org:6969\\/announce\"],[\"udp:\\/\\/p4p.arenabg.ch:1337\\/announce\"],[\"http:\\/\\/p4p.arenabg.com:1337\\/announce\"],[\"udp:\\/\\/9.rarbg.to:2710\\/announce\"],[\"udp:\\/\\/9.rarbg.me:2710\\/announce\"],[\"udp:\\/\\/exodus.desync.com:6969\\/announce\"],[\"udp:\\/\\/tracker.cyberia.is:6969\\/announce\"],[\"udp:\\/\\/retracker.lanta-net.ru:2710\\/announce\"],[\"udp:\\/\\/open.stealth.si:80\\/announce\"],[\"udp:\\/\\/tracker.torrent.eu.org:451\\/announce\"],[\"udp:\\/\\/tracker.tiny-vps.com:6969\\/announce\"],[\"udp:\\/\\/tracker.moeking.me:6969\\/announce\"],[\"http:\\/\\/tracker4.itzmx.com:2710\\/announce\"],[\"udp:\\/\\/tracker3.itzmx.com:6961\\/announce\"],[\"udp:\\/\\/ipv4.tracker.harry.lu:80\\/announce\"],[\"udp:\\/\\/bt1.archive.org:6969\\/announce\"],[\"http:\\/\\/tracker1.itzmx.com:8080\\/announce\"],[\"udp:\\/\\/explodie.org:6969\\/announce\"]],\"info\":{\"name\":\"神烦警探.Brooklyn.Nine-Nine.S07E11.中英字幕.HDTVrip.720P-人人影视.mp4\"},\"mode\":\"single\"},\"completedLength\":\"0\",\"connections\":\"0\",\"dir\":\"Download\",\"downloadSpeed\":\"0\",\"errorCode\":\"12\",\"errorMessage\":\"InfoHash d3dfedd194cc30abf8a4b3600e3582b156eec2d8 is already registered.\",\"files\":[{\"completedLength\":\"0\",\"index\":\"1\",\"length\":\"246277324\",\"path\":\"Download\\/神烦警探.Brooklyn.Nine-Nine.S07E11.中英字幕.HDTVrip.720P-人人影视.mp4\",\"selected\":\"true\",\"uris\":[]}],\"gid\":\"ebaf7fab6aad4352\",\"infoHash\":\"d3dfedd194cc30abf8a4b3600e3582b156eec2d8\",\"numPieces\":\"241\",\"numSeeders\":\"0\",\"pieceLength\":\"1024000\",\"status\":\"error\",\"totalLength\":\"0\",\"uploadLength\":\"0\",\"uploadSpeed\":\"0\"}}\n";
        JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        JsonObject bittorrent = result.getAsJsonObject("bittorrent");
        System.out.println(bittorrent);
        System.out.println("=========================================");
        JsonArray files = result.getAsJsonArray("files");
        files.forEach(System.out::println);
        //根据file创建文件
        JsonObject info = bittorrent.getAsJsonObject("info");
        System.out.println("=========================================");
        System.out.println(info);
        //根据info创建文件夹
        System.out.println("=========================================");
        Files folder = FilesUtil.createFolder(info.get("name").getAsString(), "/", FilesUtil.setUserList(4L));

        System.out.println(folder);
        System.out.println("=========================================");
        files.forEach((value) -> {
            String selfName = new File(value.getAsJsonObject().get("path").getAsString()).getName();
            Files file = FilesUtil.createFiles(selfName, folder.getParentName() + folder.getSelfName(), FilesUtil.setUserList(4L));
            OriginFile originFile = new OriginFile();
        });
    }
}