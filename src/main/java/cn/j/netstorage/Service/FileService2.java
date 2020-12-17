package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.User.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService2 {

    Boolean save(Files files);

    Boolean del(Files files);

    Boolean move(Files files,String path);

    List<Files> files(Long ... id);

    List<Files> files();

    Files file(String finalName, OriginFile originFile, String storagePath, User user);

    int checkFilesCount(String parentName,String fileName,User user);



}
