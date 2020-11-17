package cn.j.netstorage.Controller;

import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.ResultBuilder;
import cn.j.netstorage.tool.StatusCode;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FilesService filesService;

    @GetMapping("Music")
    public ResultBuilder<Files> getMusics() {
        return new ResultBuilder<>(StatusCode.SUCCESS);
    }

    @GetMapping("Doc")
    public ResultBuilder<Files> getDocuments() {
        return new ResultBuilder<>(StatusCode.SUCCESS);
    }

    @GetMapping("pic")
    public ResultBuilder<Files> getPics() {
        return new ResultBuilder<>(StatusCode.SUCCESS);
    }
}
