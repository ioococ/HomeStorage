package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.DTO.FilesVersionDTO;
import cn.j.netstorage.Entity.DTO.OriginFileDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.FilesVersion;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Mapper.FilesVersionMapper;
import cn.j.netstorage.NetstorageApplication;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.FilesVersionService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HashCodeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NetstorageApplication.class)
class FilesVersionServiceImplTest {
    @Autowired
    FilesService filesService;
    @Autowired
    FilesVersionService filesVersionService;
    @Autowired
    UserService userService;
    @Value(value = "${download.common-file-path}")
    String common_files_path;
    @Value(value = "${download.upload-path}")
    String common_upload_path;

    @Test
    void save() {
        Long fid = 183L;
        Double version = 1.2;
        Files files = filesService.findByFid(fid);
        FilesVersion filesVersion = new FilesVersion();
        System.out.println(files.getOriginFile());
        System.out.println(files.getOriginFile().size());
        if (files.getOriginFile() != null && files.getOriginFile().size() > 0) {
            filesVersion.setOriginFileSet(files.getOriginFile());
            filesVersion.setDesc_("TestFilesVersion");
            filesVersion.setGroupName(files.getParentName() + files.getSelfName());
            filesVersion.setVersion(version);
            filesVersion.setUpdateDate(new Date());
            filesVersion.setUsers(FilesUtil.setUserSet(2L));
            System.out.println(filesVersionService.createFileVersionControl(filesVersion));
        }

    }

    @Test
    void delete() {
        FilesVersion filesVersion = new FilesVersion();
        filesVersion.setGroupId(179L);
        System.out.println(filesVersionService.delete(filesVersion));
    }

    @Test
    void filesVersionList() {
        List<FilesVersionDTO> versionList = filesVersionService.filesVersionList(2L);
        versionList.forEach(System.out::println);
    }

    @Test
    void add() {
        double version = 1.9;
        FilesVersion filesVersion = new FilesVersion();
        Long GroupId = 186L;
        FilesVersion filesVersion1 = filesVersionService.GetFileVersionByGroupId(GroupId);
        filesVersion.setGroupName(filesVersion1.getGroupName());
        try {
            Path path = Paths.get("C:\\Users\\Shinelon\\Pictures\\Saved Pictures\\20180914_143714.jpg");
            File file = new File(path.toUri());
            filesVersion.setVersion(version);
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), fileInputStream);
            filesVersionService.add(filesVersion, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    FilesVersionMapper filesVersionMapper;

    @Test
    void test() {
        //filesVersionMapper.getGroup(2L).forEach(System.out::println);
        int[] array = IntStream.range(0, 10).toArray();

        int j = 0;
        System.out.println(array[j + 1]);//直接加 1
        System.out.println(array[2]);//直接看 1
        System.out.println(array[j++]);// 后加 0
        System.out.println(array[++j]);//先加 2 因为前面有一个++
    }

}