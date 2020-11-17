package cn.j.netstorage.Controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiresUser
public class PageController {

    @GetMapping("/storage")

    public String indexAliases() {
        return "index.html";
    }

    @GetMapping("/User/login")
    public String login() {
        return "index.html";
    }

    @GetMapping("/User/register")
    public String register() {
        return "index.html";
    }

    @GetMapping("/video")
    public String video(){
        return "index.html";
    }
}
