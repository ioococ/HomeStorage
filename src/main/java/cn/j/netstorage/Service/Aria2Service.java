package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.File.Aria2File;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.User.User;

import java.util.List;

public interface Aria2Service {

    Boolean download(String id, String url);

    List<String> downloadList(String id, User user);

    List<Aria2File> StoppedList(String id, User user);

    Boolean pause(String id, String gid);

    Boolean unpause(String id, String gid);

    String Detail(String id, String gid);

    Boolean downloadTorrent(String id, Long fid, User user, OriginFile originFile);

}
