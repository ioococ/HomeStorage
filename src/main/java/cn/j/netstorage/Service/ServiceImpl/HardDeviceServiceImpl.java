package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Mapper.HardDeviceMapper;
import cn.j.netstorage.Service.HardDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Random;

@Service
public class HardDeviceServiceImpl implements HardDeviceService {

    @Autowired
    HardDeviceMapper hardDeviceMapper;
    @Override
    public Boolean update(HardDiskDevice hardDiskDevice) {
        return null;
    }

    @Override
    public Boolean add(HardDiskDevice hardDiskDevice) {
        return hardDeviceMapper.save(hardDiskDevice).getId()!=0;
    }

    @Override
    public Boolean del(HardDiskDevice hardDiskDevice) {
        return null;
    }

    @Override
    public Boolean move(HardDiskDevice hardDiskDevice1, HardDiskDevice hardDiskDevice2) {
        return null;
    }

    @Override
    public HardDiskDevice get() {
        List<HardDiskDevice> hardDiskDeviceList=hardDeviceMapper.findAll();
        return hardDiskDeviceList!=null||hardDiskDeviceList.size()>0?
                hardDiskDeviceList.get(new Random().nextInt(hardDiskDeviceList.size())):
                CreateTempDevice();
    }

    @Value("${tempDir}")
    private String tempDir;

    public HardDiskDevice CreateTempDevice(){
        HardDiskDevice hardDiskDevice=new HardDiskDevice();
        hardDiskDevice.setFolderName(tempDir);
        hardDiskDevice.setDeviceName("/tempFiles/");
        hardDiskDevice.setCustomName("临时目录");
        hardDiskDevice.setRules("*");
        hardDiskDevice.setSize(new File(tempDir).getFreeSpace());
        return hardDeviceMapper.save(hardDiskDevice);
    }
}
