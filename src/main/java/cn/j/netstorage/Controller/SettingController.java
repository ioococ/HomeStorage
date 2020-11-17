package cn.j.netstorage.Controller;

import cn.j.netstorage.Entity.Config;
import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.File.HardDiskDevice;
import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Entity.Vo.UserVo;
import cn.j.netstorage.Mapper.RoleMapper;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.ResultBuilder;
import cn.j.netstorage.tool.StatusCode;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiresUser
@RequestMapping("/api/v1/admin/")
public class SettingController {
    @Autowired
    FilesService filesService;
    @Autowired
    UserService userService;

    @PostMapping("/AlterConfig")
    public ResultBuilder<Boolean> AlterConfig(@RequestBody Config config) {
//      创建脚本文件
        return new ResultBuilder<>(true, StatusCode.SUCCESS);
    }

    @GetMapping("DiskStatus")
    public ResultBuilder<List<HardDiskDevice>> hardDiskDeviceResultBuilder() {
        return new ResultBuilder<>(filesService.hardDevices(), StatusCode.SUCCESS);
    }

    @GetMapping("/hard_Devices")
    public ResultBuilder<List<HardDiskDevice>> hardDiskDevices() {
        return new ResultBuilder<>(filesService.hardDevices(), StatusCode.SUCCESS);
    }

    @PostMapping("/insertHardDevices")
    public ResultBuilder<Boolean> hardDiskDevices(@RequestBody HardDiskDevice hardDiskDevice) {
        return new ResultBuilder<>(filesService.saveHardDevice(hardDiskDevice), StatusCode.SUCCESS);
    }

    @GetMapping("/PatternData")
    public ResultBuilder<List<HashMap<String,String>>> PatternData(){
        return new ResultBuilder<>(filesService.PatternData(), StatusCode.SUCCESS);
    }

    @GetMapping("/deleteHardDevice")
    public ResultBuilder<Boolean> hardDiskDevices(String id) {
        HardDiskDevice hardDiskDevice = new HardDiskDevice();
        hardDiskDevice.setId(Long.valueOf(id));
        return new ResultBuilder<>(filesService.deleteHardDevice(hardDiskDevice), StatusCode.SUCCESS);
    }

    @GetMapping("/users")
    public ResultBuilder<List<UserDTO>> Users() {
        return new ResultBuilder<>(userService.getUsers(), StatusCode.SUCCESS);
    }

    @PostMapping("/addUser")
    public ResultBuilder<Boolean> addUser(@RequestBody UserVo userVo){
        User user =new User();
        user.setEmailAccount(userVo.getEmail());
        user.setPassword(userVo.getPassword());
        user.setNickName(userVo.getNickName());
        Role role=new Role();
        role.setRid(Long.valueOf(userVo.getRid()));
        user.setRole(role);
        return new ResultBuilder<>(userService.Register(user), StatusCode.SUCCESS);
    }

    @GetMapping("/AlterUser")
    public ResultBuilder<Boolean> alterUser() {
//        修个屁
        return new ResultBuilder<>(false,StatusCode.FALL);
    }

    @GetMapping("/getRoles")
    public ResultBuilder<List<Role>> Roles() {
        return new ResultBuilder<>(userService.roles(), StatusCode.SUCCESS);
    }

    @GetMapping("/AlterRole")
    public ResultBuilder<Boolean> Roles(@RequestBody Role role) {
        return new ResultBuilder<>(userService.AlterRole(role), StatusCode.SUCCESS);
    }

    @Value("${spring.datasource.username}")
    private String username;

//    @Value("${spring.datasource.password}")
//    private String password;

    @Value("${ip}")
    private String ip;

    @GetMapping("/config")
    public ResultBuilder<Config> configs(){
        Config config = new Config();
        config.setIp(ip);
        config.setUser(username);
        config.setPassword("*******");
        return new ResultBuilder<>(config,StatusCode.SUCCESS);
    }
}
