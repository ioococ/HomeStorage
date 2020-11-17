package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.Token;
import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Entity.Vo.UserVo;
import cn.j.netstorage.Mapper.PermissionMapper;
import cn.j.netstorage.Mapper.RoleMapper;
import cn.j.netstorage.Mapper.TokenMapper;
import cn.j.netstorage.Mapper.UserMapper;
import cn.j.netstorage.Service.UserService;
import cn.j.netstorage.tool.FilesUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    RoleMapper roleMapper;
@Autowired
    TokenMapper tokenMapper;

    /**
     * 登陆，在这里调用ShiroRealm
     *
     * @param Email      邮箱
     * @param password   密码
     * @param rememberMe 记住我
     * @return 登陆结果
     */
    @Override
    public Boolean Login(String Email, String password, Boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();
        //生成token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                Email, password, rememberMe);
        try {
            //传入subject
            subject.login(usernamePasswordToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 注册,写数据库
     *
     * @param user 用户信息 最低需要邮箱 密码 nickName
     * @return 注册结果
     */
    @Override
    public Boolean Register(User user) {
        user.Md5Hash();
        user = userMapper.save(user);
        if (user.getUid() != 0){
            Token token=new Token(user.getUid(),UUID.randomUUID().toString());
            token = tokenMapper.save(token);
            return token.getId()!=0&&user.getUid()!=0;
        }
        return false;
    }

    @Override
    public Boolean freezeUser(User user) {
        return null;
    }

    /**
     * 根据token获得用户信息
     *
     * @param token Shiro的token
     * @return 用户
     */
    @Override
    public User getUser(Object token) {
        User user = new User();
        //token是邮箱
        user.setEmailAccount(token.toString());
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnorePaths("uid", "create_date", "nick_name", "role", "password"));
        Optional<User> list = userMapper.findOne(example);
        System.out.println(list.get().toString());
        return list.get();
    }

    @Override
    public User getUser(Token token) {
        return getUser(tokenMapper.findByToken(token.getToken()).getUser());
    }

    public User getUser(Long id){
        return userMapper.findById(id).get();
    }

    @Override
    public User getUser(String account, String password) {
        User user = new User();
        user.setPassword(password);
        user.setEmailAccount(account);
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnorePaths("uid", "create_date", "nick_name", "role"));
        return userMapper.findOne(example).get();
    }

    @Override
    public List<UserDTO> getUsers() {
        return UserDTO.userDTOS(userMapper.findAll());
    }

    /**
     * 获取用户的权限
     *
     * @param token Shiro的token
     * @return 用户的权限列表
     */
    @Override
    public Set<String> getPermission(Object token) {
        return null;
    }

    /**
     * 获取用户的角色组
     *
     * @param token Shiro的token
     * @return 用户的角色组
     */
    @Override
    public List<Role> getRole(Object token) {
        return Collections.singletonList(getUser(token).getRole());
    }

    @Override
    public Boolean addRole(Role role) {
        return roleMapper.save(role).getRid() > 0;
    }

    @Override
    public List<Role> roles() {
        return roleMapper.findAll();
    }

    @Override
    public Boolean AlterRole(Role role) {
        return roleMapper.save(role).getRid() != 0;
    }

    @Override
    public Boolean delRole(Role role) {
        return FilesUtil.delete(roleMapper, role);
    }

    @Override
    public List<UserDTO> search(UserVo userVo) {
        List<UserDTO> userDTOS=new ArrayList<>();
        userMapper.findAllByNickNameContaining(userVo.getNickName()).forEach((value)->userDTOS.add(new UserDTO(value)));
        return userDTOS;
    }
}