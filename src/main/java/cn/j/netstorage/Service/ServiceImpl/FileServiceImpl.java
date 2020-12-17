package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.DTO.OriginFileDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Entity.File.OriginFile;
import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Mapper.FileMapper;
import cn.j.netstorage.Mapper.HardDeviceMapper;
import cn.j.netstorage.Mapper.OriginFileMapper;
import cn.j.netstorage.Mapper.UserMapper;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.HashCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class FileServiceImpl implements FilesService {
    @Autowired
    HardDeviceMapper hardDeviceMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    OriginFileMapper originFileMapper;

    @Override
    public Boolean insertFile() {
        return null;
    }

    @Override
    public List<FilesDTO> UserFile(String path, long uid) {
        List<FilesDTO> filesDTOS = UserFiles(path, uid, true);
        filesDTOS.addAll(UserFiles(path, uid, false));
        return filesDTOS;
    }

    /**
     * 用户访问页面返回文件夹和文件的列表
     *
     * @param uid 用户id
     * @return 文件夹和文件的混合列表
     */
    @Override
    public List<FilesDTO> UserFiles(String path, long uid, Boolean isDir) {

        Files files = new Files();
        files.setParentName(path);
        User user = new User();
        user.setUid(uid);
        files.setUser(Collections.singletonList(user));

        List<FilesDTO> files1 = new ArrayList<>();
        Short is_dir = isDir ? Files.is_dir : Files.no_dir;
        fileMapper.findAllByParentNameAndUser_uidAndIsDir(path, (uid), is_dir).forEach((value) -> {
            FilesDTO filesDTO = new FilesDTO(value);
            files1.add(filesDTO);
        });
        return files1;
    }


    /**
     * 删除文件的时候删除文件
     *
     * @param uid 用户id
     * @param fid 文件id
     * @return 删除结果
     */

    @Override
    public Boolean deleteUserFiles(long uid, long fid) {
        Files files = new Files();
        User user = new User();
        user.setUid(uid);
        files.setUser(Collections.singletonList(user));
        files.setFid(fid);
        return FilesUtil.delete(fileMapper, files);
    }

    /**
     * 根据id获得文件
     *
     * @param fid 文件id
     * @return 文件
     */

    @Override
    public FilesDTO getFilesById(long fid) {
        Optional<Files> files = fileMapper.findById(fid);
        return new FilesDTO(files.orElse(null));
    }

    @Override
    public OriginFile findByParentNameAndAndUserAndAndSelfName(String parentName, User user, String selfName) {
        System.out.println(parentName);
        System.out.println(selfName);
        Files files = fileMapper.findByParentNameAndAndUserAndAndSelfName(parentName, user, selfName);
        return files.getOriginFile() != null ? new ArrayList<OriginFile>(files.getOriginFile()).get(0) : new OriginFile();
    }

    /**
     * 避免多次上传的md5检测
     *
     * @param md5 文件的md5值
     * @return 是否存在相同md5结果
     */

    @Override
    public List<OriginFile> checkUpload(String md5) {
        OriginFile originFile = new OriginFile();
        originFile.setMd5(md5);
        Example<OriginFile> example = Example.of(originFile,
                ExampleMatcher.matching().withIgnorePaths("oid", "file_name"));
        return originFileMapper.findAll(example);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */

    private Files trySearchFileExist(Files files, Files file, String fileName_, String exg) {
        String str = "";
        int i = 0;
        do {
            if (i == 0)
                str = fileName_ + exg;
            else
                str = String.format(fileName_ + "(%s)" + exg, i);
            file.setSelfName(str);
            i++;
            files = fileMapper.findByParentNameAndAndUserAndAndSelfName(file.getParentName(), file.getUser().get(0), file.getSelfName());
        } while (files != null);
        return file;
    }

    private Files trySearchFolderExist(Files files, Files file, String fileName_) {
        String str = "";
        int i = 0;
        do {
            if (i == 0)
                str = fileName_;
            else
                str = String.format(fileName_ + "(%s)", i);
            file.setSelfName(str);
            i++;
            files = fileMapper.findByParentNameAndAndUserAndAndSelfName(file.getParentName(), file.getUser().get(0), file.getSelfName());
        } while (files != null);
        return file;
    }

    @Override
    public OriginFile insertFolder(Files file) {
        trySearchFolderExist(null, file, file.getSelfName());
        OriginFile originFile = originFileMapper.getOriginFileByFileName("FileType");
        if (originFile == null || originFile.getOid() == 0) {
            originFile = new OriginFile();
            originFile.setFileName("FileType");
            originFile.setMd5(System.currentTimeMillis() + "");
            originFileMapper.save(originFile);
        }
        return originFile;
    }

    @Override
    public OriginFile insertFiles(List<HardDiskDevice> hardDiskDevices, Files file, MultipartFile tempFile) throws IOException {
        int num = file.getSelfName().lastIndexOf(".");
        String fileName_ = file.getSelfName().substring(0, num);
        String exg = file.getSelfName().substring(num);
        if (hardDiskDevices.isEmpty()) {
            return null;
        }
        int randomInt = new Random().nextInt(hardDiskDevices.size());
        HardDiskDevice hardDiskDevice = hardDiskDevices.get(randomInt);

        trySearchFileExist(null, file, fileName_, exg);
        String ext = file.getSelfName().substring(file.getSelfName().lastIndexOf(".") + 1);
        String fileName = FilesUtil.getCurrentNameWithExt("." + ext);

        java.io.File dst = new java.io.File(hardDiskDevice.getFolderName() + "/" + fileName);
        tempFile.transferTo(dst);


        OriginFile originFile = new OriginFile();
        originFile.setFileName(fileName);
        String md5 = HashCodeUtil.getHashCode(dst);

        List<OriginFile> originFiles = checkUpload(md5);

        if (checkUpload(HashCodeUtil.getHashCode(dst)).size() >= 1) {
            return originFiles.get(0);
        }

        originFile = new OriginFile();
        originFile.setFileName(fileName);
        originFile.setMd5(md5);
        originFile.setSize(tempFile.getSize());
        originFile.setHardDiskDevice(Collections.singleton(hardDiskDevice));
        originFile = originFileMapper.save(originFile);
        return originFile;
    }


    @Override
    public Boolean uploadFile(Files file, String preSuffix, MultipartFile tempFile) {
        if (file == null && tempFile == null) {
            return false;
        }
        try {
            if (file.getIsDir() == Files.is_dir) {
                file.setType(Type.Folder.getType());
            } else {
                Type type = Type.getInstance(FilesUtil.getExt(file.getSelfName()));
                file.setType(type.getType());
            }

            List<HardDiskDevice> hardDiskDevices = hardDeviceMapper.findAll();
            OriginFile originFile =
                    file.getIsDir() == Files.is_dir ?
                            insertFolder(file) : insertFiles(hardDiskDevices, file, tempFile);

            if (originFile != null) {
                file.setOriginFile(Collections.singleton(originFile));
                file.setCreateDate(new Date());
                file = fileMapper.save(file);
            } else {
                return false;
            }
            return file.getFid() != 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private OriginFile getOriginFile(HardDiskDevice hardDiskDevice, Files files, File file) {
        String originName = hardDiskDevice.getFolderName()
                + System.currentTimeMillis()
                + FilesUtil.getExt(file.getAbsolutePath());
        boolean result = file.renameTo(new File(originName));
        OriginFile originFile = null;
        if (result) {
            originFile = new OriginFile();
            originFile.setHardDiskDevice(Collections.singleton(hardDiskDevice));
            originFile.setSize(file.length());
            originFile.setFileName(originName);
            try {
                originFile.setMd5(HashCodeUtil.getHashCode(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            originFile = originFileMapper.save(originFile);
        }
        return originFile.getOid() == 0L ? null : originFile;
    }

    public Boolean uploadFile(Files data, File file) {
        List<HardDiskDevice> hardDiskDevices = hardDeviceMapper.findAll();
        OriginFile originFile = getOriginFile(hardDiskDevices.get(new Random().nextInt(hardDiskDevices.size())), data, file);
        if (originFile != null) {
            data.setOriginFile(Collections.singleton(originFile));
            data.setCreateDate(new Date());
            data = fileMapper.save(data);
        }
        return data.getFid() != 0;
    }


    @Override
    public Boolean RenameFile(Files files) {
        return null;
    }

    @Override
    public OriginFileDTO getFileByFileName(String FileName) {
        return new OriginFileDTO(originFileMapper.getOriginFileByFileName(FileName));
    }

    @Override
    @Transactional
    public Boolean deleteFolders(String parentName, String selfName, Long fid, Long uid) {
        return fileMapper.deleteAllByFidIsOrParentNameIsLike(fid, parentName + selfName + "/%") > 0 && this.deleteUserFiles(uid, fid);
    }

    @Override
    public Files findByFid(Long fid) {
        return fileMapper.findById(fid).orElse(null);
    }

    @Override
    public Boolean saveOriginFiles(OriginFile originFile) {
        OriginFile originFile1 = originFileMapper.save(originFile);
        return originFile1.getOid() != 0;
    }

    @Override
    public List<HardDiskDevice> hardDevices() {
        return hardDeviceMapper.findAll();
    }

    @Override
    public Boolean saveHardDevice(HardDiskDevice hardDiskDevice) {
        return hardDeviceMapper.save(hardDiskDevice).getId() != 0;
    }

    @Override
    public Boolean deleteHardDevice(HardDiskDevice hardDiskDevice) {
        return FilesUtil.delete(hardDeviceMapper, hardDiskDevice);
    }

    @Override
    public List<HashMap<String, String>> PatternData() {
        File[] roots = File.listRoots();// 获取磁盘分区列表
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (File file : roots) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            long free = file.getFreeSpace();
            long total = file.getTotalSpace();
            long use = total - free;
            hashMap.put("PatternPath", file.getPath());
            hashMap.put("free", change(free) + "G");
            hashMap.put("used", change(use) + "G");
            hashMap.put("total", change(total) + "G");
            hashMap.put("bfb", bfb(use, total));
            mapList.add(hashMap);
        }
        return mapList;
    }

    @Override
    public Files getByType(User user, Type type) {
        type.getType();
        return null;
    }

    @Override
    public List<FilesDTO> searchFiles(FilesDTO filesDTO, User user) {
        List<FilesDTO> filesDTOS = new ArrayList<>();
        fileMapper.findAllBySelfNameContainingAndUserIs(filesDTO.getSelfName(), user).forEach((value) -> filesDTOS.add(new FilesDTO(value)));
        return filesDTOS;
    }

    public static long change(long num) {
        // return num;
        return num / 1024 / 1024 / 1024;
    }

    private static String bfb(Object num1, Object num2) {
        double val1 = Double.valueOf(num1.toString());
        double val2 = Double.valueOf(num2.toString());
        if (val2 == 0) {
            return "0.0%";
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(val1 / val2 * 100) + "%";
        }
    }

//
//    @Override
//    public Boolean mkdirs(Folders folders) {
//        return foldersMapper.save(folders).getId()!=0;
//    }

}
