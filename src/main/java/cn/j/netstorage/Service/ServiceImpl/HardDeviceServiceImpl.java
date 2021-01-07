package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Mapper.HardDeviceMapper;
import cn.j.netstorage.Service.HardDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public HardDiskDevice get(Long id) {
        return hardDeviceMapper.findById(id).get();
    }

    @Override
    public List<HardDiskDevice> getHardDevices() {
        return hardDeviceMapper.findAll();
    }

    @Override
    public List<HashMap<String, String>> getSpace() {
        List<HardDiskDevice> list=this.getHardDevices();
        List<HashMap<String, String>> mapList = new ArrayList<>();
        for (HardDiskDevice hardDiskDevice : list) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                File file=hardDiskDevice.get();
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
