package cn.j.netstorage.Controller;

import cn.j.netstorage.Entity.DTO.FilesDTO;
import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Service.FilesService;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.FilesUtil;
import cn.j.netstorage.tool.ResultBuilder;
import cn.j.netstorage.tool.StatusCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Type/")
@RequiresUser
public class TypeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FilesService filesService;

    @GetMapping("/Music")
    public ResultBuilder<List<Files>> getMusics() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        List<FilesDTO> filesList = filesService.getByType(user, Type.Music);
        return new ResultBuilder(filesList, StatusCode.SUCCESS);
    }

    @GetMapping("/Doc")
    public ResultBuilder getDocuments() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        List<FilesDTO> filesList = filesService.getByType(user, Type.Music);
        return new ResultBuilder(filesList,StatusCode.SUCCESS);
    }
    @GetMapping("/Video")
    public ResultBuilder getVideo(){
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        List<FilesDTO> filesList=filesService.getByType(user,Type.Video);
        return new ResultBuilder(filesList,StatusCode.SUCCESS);
    }

    @GetMapping("/Picture")
    public ResultBuilder getPics() {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        List<FilesDTO> filesList=filesService.getByType(user,Type.Picture);
        return new ResultBuilder(filesList,StatusCode.SUCCESS);
    }

    @GetMapping("/Other")
    public ResultBuilder getSpec(){
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        List<FilesDTO> filesList=filesService.getByType(user,Type.Other);
        return new ResultBuilder(filesList,StatusCode.SUCCESS);
    }
}
