package cn.j.netstorage.Config;

import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


public class ShiroRealm extends AuthorizingRealm {
    @Resource
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection.getPrimaryPrincipal() == null)
            return null;
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        userService.getRole(principalCollection.getPrimaryPrincipal().toString());
//        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null)
            return null;
        String name = authenticationToken.getPrincipal().toString();
        String password = new String((char[]) authenticationToken.getCredentials());
        User user = userService.getUser(name);
        if (user == null) {
            throw new IncorrectCredentialsException("登录失败，用户名或密码错误");
        }
        //验证账户信息
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user.getEmailAccount(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getEmailAccount()),
                getName());
        return simpleAuthenticationInfo;
    }
}
