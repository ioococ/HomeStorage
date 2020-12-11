package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.DTO.UserDTO;
import cn.j.netstorage.Entity.Token;
import cn.j.netstorage.Entity.User.Permission;
import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.Entity.Vo.UserVo;

import java.util.List;
import java.util.Set;

public interface UserService {

    Boolean Login(String Email, String password, Boolean rememberMe);

    Boolean Register(User user);

    Boolean freezeUser(User user);

    User getUser(Object token);

    User getUser(Token token);

    User getUser(String account, String password);

    List<UserDTO> getUsers();

    Set<String> getPermission(Object token);

    Set<Role> getRole(Object token);

    Role role(Long id);

    Boolean addRole(Role role);

    Long savePermission(Permission permission);

    Long saveUser(User user);

    List<Role> roles();

    Boolean AlterRole(Role role);

    Boolean delRole(Role role);

    List<UserDTO> search(UserVo userVo);
}
