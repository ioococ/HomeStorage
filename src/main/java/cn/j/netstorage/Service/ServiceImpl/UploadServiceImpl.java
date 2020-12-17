package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Service.*;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HashCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    HardDeviceService hardDeviceService;

    @Autowired
    OriginFileService originFileService;

    @Autowired
    FileService2 fileService2;

    @Override
    public Boolean common_upload(MultipartFile uploadFile, String storagePath, User user) {
        HardDiskDevice hardDiskDevice = hardDeviceService.get();
        String path = hardDiskDevice.getFolderName() + "/" + uploadFile.getOriginalFilename();
        try {
            OriginFile originFile = originFileService.originFile(uploadFile, hardDiskDevice);

            uploadFile.transferTo(new File(path));
            //写原始文件
            originFile = originFileService.saveOriginFile(originFile);

            if (originFile.getOid() == 0) return false;
            //写用户文件
            int count = fileService2.checkFilesCount(storagePath, uploadFile.getOriginalFilename(), user);
            String finalName = String.format("%s%s%s",
                    uploadFile.getOriginalFilename().substring(0, uploadFile.getOriginalFilename().lastIndexOf(".")),
                    count == 0 ? "" : "(" + count + ")",
                    uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."))
            );

            Files files = fileService2.file(finalName, originFile, storagePath, user);
            fileService2.save(files);

            if (files.getFid() == 0) return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean slice_upload(MultipartFile file, String fileName, String dst, int currentIndex, User user) {
        String name = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + currentIndex + ".tmp";
        try {
            file.transferTo(new File(dst + name));
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean exist_upload(String filePath, String storagePath, User user) {
        return null;
    }

    @Override
    public Boolean merge_upload(String fileName, String diskPath, String storagePath, int start, int end, User user) {
        HardDiskDevice hardDiskDevice = hardDeviceService.get();
        File newFile = new File(hardDiskDevice.getFolderName() + "/" +
                +System.currentTimeMillis()
                + fileName.substring(fileName.lastIndexOf(".")));

        try (FileOutputStream outputStream = new FileOutputStream(newFile, true);) {
            FileInputStream fileInputStream = null; //分片文件
            byte[] byt = new byte[10 * 1024 * 1024];
            int len;
            for (int i = start; i < end; i++) {
                fileInputStream = new FileInputStream(new File(
                        diskPath + fileName.substring(0, fileName.lastIndexOf(".")) + "_" + i + ".tmp")
                );

                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }

                fileInputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.gc();
        for (int i = start; i < end; i++) {
            new File(diskPath + fileName.substring(0, fileName.lastIndexOf(".")) + "_" + i + ".tmp").delete();
        }

        //OriginFile 插入
        OriginFile originFile = null;
        try {
            originFile = originFileService.originFile(newFile, hardDiskDevice);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        originFile = originFileService.saveOriginFile(originFile);

        if (originFile.getOid() == 0) return false;

        //Files插入
        int count = fileService2.checkFilesCount(storagePath, fileName, user);
        String finalName = String.format("%s%s%s",
                fileName.substring(0, fileName.lastIndexOf(".")),
                count == 0 ? "" : "(" + count + ")",
                fileName.substring(fileName.lastIndexOf("."))
        );

        Files files = fileService2.file(finalName, originFile, storagePath, user);
        fileService2.save(files);

        if (files.getFid() == 0) return false;

        return true;
    }

    @Override
    public Boolean checkMd5AndTransfer(String md5, String parentName, String selfName, User user) {
        OriginFile originFile = originFileService.originFile(md5);
        if (originFile == null) {
            return false;
        }

        //插入该用户的文件夹
        int count = fileService2.checkFilesCount(parentName, selfName, user);
        String finalName = String.format("%s%s%s",
                selfName.substring(0, selfName.lastIndexOf(".")),
                count == 0 ? "" : "(" + count + ")",
                selfName.substring(selfName.lastIndexOf("."))
        );

        Files files = fileService2.file(finalName, originFile, parentName, user);
        return fileService2.save(files);
    }
}
