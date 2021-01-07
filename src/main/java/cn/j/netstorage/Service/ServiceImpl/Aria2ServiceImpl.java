package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.Aria2Entity;
import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.File.Aria2File;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Mapper.AriaMapper;
import cn.j.netstorage.Mapper.FileMapper;
import cn.j.netstorage.Mapper.OriginFileMapper;
import cn.j.netstorage.Mapper.UserMapper;
import cn.j.netstorage.Service.Aria2Service;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.HardDeviceService;
import cn.j.netstorage.tool.Aria2Http;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Aria2ServiceImpl implements Aria2Service {

    @Autowired
    private AriaMapper ariaMapper;
    private String uri = "http://localhost:6800/jsonrpc";
    @Autowired
    private FilesService filesService;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    OriginFileMapper originFileMapper;
    @Autowired
    HardDeviceService hardDeviceService;

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


    @Override
    public Boolean download(String id, String url) {
        return false;
    }

    @Override
    public List<String> downloadList(String id, User user) {
        return activeList(id,StoppedList(id,user).stream().map(Aria2File::getGid).collect(Collectors.toList()));
    }

    private List<String> activeList(String id, List<String> list) {
        Aria2Entity aria2Entity = createEntity(id, "aria2.tellActive", Arrays.asList(rowName));
        String jsonString = new Gson().toJson(aria2Entity);
        String resultString = Aria2Http.post(uri, jsonString);
        List<String> resultList=new ArrayList<>();
        JsonArray result = new Gson().fromJson(resultString, JsonObject.class).get("result").getAsJsonArray();
        result.forEach((value)->{
            String gid=value.getAsJsonObject().get("gid").getAsString();
            if (list.contains(gid)){
                resultList.add(new Gson().toJson(value));
            }
        });
        return resultList;
    }


    /**
     * 获得当前用户在数据库中的所有任务
     * @param id
     * @param user
     * @return
     */
    @Override
    public List<Aria2File> StoppedList(String id, User user) {
        return ariaMapper.findAllByUserIs(user);
    }


    @Override
    public Boolean pause(String id, String gid) {
//        aria2.forcePause
        Aria2Entity aria2Entity=createEntity(id,"aria2.forcePause",Collections.singleton(gid));
        String result= Aria2Http.post(uri,new Gson().toJson(aria2Entity));
        if (StringUtils.hasText(result)){
//            result: "492d9e87968f4e45"
            JsonObject jsonObject=new Gson().fromJson(result,JsonObject.class);
            String Gid=jsonObject.get("result").getAsString();
            return gid.equals(Gid);
        }
        return null;
    }

    @Override
    public Boolean unpause(String id, String gid) {
        String method="aria2.unpause";
        Aria2Entity aria2Entity=createEntity(id,method,Collections.singletonList(gid));
        String result=Aria2Http.post(uri,new Gson().toJson(aria2Entity));
        if (StringUtils.hasText(result)){
            JsonObject jsonObject=new Gson().fromJson(result,JsonObject.class);
            String Gid=jsonObject.get("result").getAsString();
            return gid.equals(Gid);
        }
        return false;
    }


    @Override
    public String Detail(String id, String gid) {
//        {"id":"detail-1","jsonrpc":"2.0","result":{"bittorrent":{"announceList":[["http:\/\/open.acgtracker.com:1096\/announce"]]},"completedLength":"0","connections":"0","dir":"D:\/\/test\/\/","downloadSpeed":"0","files":[{"completedLength":"0","index":"1","length":"0","path":"[METADATA]64923d9b6f7fd291470f68edb2c4dbaa1346a389","selected":"true","uris":[]}],"gid":"c102abe5feda0996","infoHash":"64923d9b6f7fd291470f68edb2c4dbaa1346a389","numPieces":"0","numSeeders":"0","pieceLength":"16384","seeder":"false","status":"active","totalLength":"0","uploadLength":"0","uploadSpeed":"0"}}
//        tellStatus
        Aria2Entity aria2Entity = createEntity(id, "aria2.tellStatus", gid);
        String result = Aria2Http.post(uri, new Gson().toJson(aria2Entity));
        return result;
    }

    private Aria2Entity createEntity(String id, String method, Object... params) {
        Aria2Entity aria2Entity = new Aria2Entity();
        aria2Entity.setId(id);
        aria2Entity.setJsonrpc("2.0");
        aria2Entity.setMethod(method);
        aria2Entity.setParams(params);
        return aria2Entity;
    }


    @Override
    public Boolean downloadTorrent(String id, Long fid, User user, OriginFile originFile) {
        try {
            /* base64转码后加入下载列表 */
            File file = new File(originFile.getPath());
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();

            String base64code = new BASE64Encoder().encode(buffer);
            //随机存储位置
            HardDiskDevice hardDiskDevice = hardDeviceService.get();
//            HardDiskDevice hardDiskDevice = hardDiskDevices.get(new Random().nextInt(hardDiskDevices.size()));
            //传参
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dir", hardDiskDevice.getFolderName());
            Aria2Entity aria2Entity = createEntity("QXJpYU5nXzE2MDQzMzM5MTlfMC4zMjU5NDUwODU3NzUzNDA1NA==", "aria2.addTorrent", base64code, Collections.EMPTY_LIST, hashMap);
            String jsonString = new Gson().toJson(aria2Entity);
            System.out.println(jsonString);
            //获得结果
            String result = Aria2Http.post(uri, jsonString);
            System.out.println(result);
            /* 写数据库*/
            if (StringUtils.hasText(result)) {
                //获得gid
                String gid = new Gson().fromJson(result, JsonObject.class).get("result").getAsString();
                String detail = Detail(id, gid);
                JsonObject jsonObject = new Gson().fromJson(detail, JsonObject.class);
                JsonObject detailResult = jsonObject.get("result").getAsJsonObject();
                JsonObject bittorrent = detailResult.getAsJsonObject("bittorrent");
                JsonArray files = detailResult.getAsJsonArray("files");
                //根据file创建文件
                JsonObject info = bittorrent.getAsJsonObject("info");
                String [] arr= FilesUtil.getFileNameAndExt(info.get("name").getAsString());
                String name =arr[0].equals("true")?arr[1]:"任务组";

                Aria2File aria2File = new Aria2File();
                aria2File.setName(name);
                aria2File.setGid(gid);
                aria2File.setUser(Collections.singletonList(user));
                ariaMapper.save(aria2File);

                FilesDTO torrentFile = filesService.getFilesById(fid);//获得原始种子文件

                Files folder = FilesUtil.createFolder(name, torrentFile.getParentName(), Collections.singletonList(user));
                folder.setOriginFile(Collections.singleton(filesService.insertFolder(folder)));
                fileMapper.save(folder);
                files.forEach((value) -> {
                    String selfName = new File(value.getAsJsonObject().get("path").getAsString()).getName();

                    Files f = FilesUtil.createFiles(selfName, folder.getParentName() + folder.getSelfName()+"/", FilesUtil.setUserList(4L));

                    OriginFile mapperFile = new OriginFile();
                    mapperFile.setHardDiskDevice(FilesUtil.convert(hardDiskDevice));
                    mapperFile.setSize(Long.valueOf(value.getAsJsonObject().get("length").getAsString()));
                    mapperFile.setMd5(UUID.randomUUID().toString().replaceAll("-", ""));
                    mapperFile.setFileName(selfName);
                    f.setOriginFile(FilesUtil.convert(mapperFile));
                    originFileMapper.save(mapperFile);
                    fileMapper.save(f);
                });
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
