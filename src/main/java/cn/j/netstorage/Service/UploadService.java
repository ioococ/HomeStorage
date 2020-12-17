package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.User.User;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    Boolean common_upload(MultipartFile uploadFile, String storagePath, User user);

    Boolean slice_upload(MultipartFile file, String fileName, String dst, int currentIndex, User user);

    Boolean exist_upload(String filePath, String storagePath, User user);

    Boolean merge_upload(String fileName, String diskPath, String storagePath, int start, int end, User user);

    Boolean checkMd5AndTransfer(String md5,String parentName,String selfName,User user);

}
