package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.File.HardDiskDevice;

import java.util.HashMap;
import java.util.List;

public interface HardDeviceService {

    Boolean update(HardDiskDevice hardDiskDevice);

    Boolean add(HardDiskDevice hardDiskDevice);

    Boolean del(HardDiskDevice hardDiskDevice);

    Boolean move(HardDiskDevice hardDiskDevice1,HardDiskDevice hardDiskDevice2);

    HardDiskDevice get();

    HardDiskDevice get(Long id);

    List<HardDiskDevice> getHardDevices();

    List<HashMap<String,String>> getSpace();

}
