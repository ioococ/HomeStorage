package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.User.Role;
import cn.j.netstorage.Entity.User.User;
import cn.j.netstorage.NetstorageApplication;
import cn.j.netstorage.Service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NetstorageApplication.class)
class UserServiceImplTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;
//
    @Test
    void register() {
        String [] array=new String[]{
                "714308273@qq.com","1913610050@qq.com"
        };
        for (int i=0;i<array.length;i++){
            User user = new User();
            user.setCreateDate(System.currentTimeMillis());
            user.setEmailAccount(array[i]);
            user.setPassword(new Md5Hash("xiening123", array[i], 1024).toHex());
            user.setNickName("Test-"+i);
            Role role = new Role();
            role.setRid(3);
            user.setRole((role));
            userService.Register(user);
        }

    }

//    @Test
//    void test(){
//        userService.plugins().forEach(System.out::println);
//    }
//    @Autowired
//    HttpMapper httpMapper;
//    @Test
//    void saveHttp(){
//        Http http=new Http();
//        http.setIp("127.0.0.1");
//        http.setName("测试插件");
//        http.setPort("8011");
//        http.setStatus(true);
//        httpMapper.save(http);
//    }
//
//
//    @Test
//    void login() {
//        logger.info(userService.Login("714308273@qq.com",new Md5Hash("xiening123", "714308273@qq.com", 1024).toHex(),false).toString());
//    }
//
//
//
//    @Test
//    void getUser() {
//        logger.info(userService.getUser("714308273@qq.com").toString());
//    }
//
//    @Test
//    void getUser1() {
//        logger.info(userService.getUser("714308273@qq.com","xiening123").toString());
//    }
//
//    @Test
//    void getUsers() {
//    }
//
//    @Test
//    void getPermission() {
//    }
//
//    @Test
//    void getRole() {
//        logger.info(userService.getRole("714308273@qq.com").toString());
//    }
//
    @Test
    void addRole() {
        Role role=new Role();
        role.setName("JunitTest-1");
        role.setStatus(true);
        Boolean result=userService.addRole(role);
        logger.info(result.toString());
    }

//    SimpleAccountRealm sar=new SimpleAccountRealm();
//
//    @Before
//    public void addUser() {
//        //模拟数据库的数据
//
//    }
//    @Test
//    public void test(){
//        sar.addAccount("714308273@qq.com",new Md5Hash("xiening123", "714308273@qq.com", 1024).toHex());
//
//        //构建secuityManage环境，创建对象
//        DefaultSecurityManager dsm= new DefaultSecurityManager();
//        //将relam加入到securityManage环境中
//        dsm.setRealm(sar);
//        //主体提交认证请求
//        SecurityUtils.setSecurityManager(dsm);
//        Subject sub=SecurityUtils.getSubject();
//        //获取账号和密码(模拟客户端提供的数据)
//        UsernamePasswordToken token=new UsernamePasswordToken("714308273@qq.com",new Md5Hash("xiening123", "714308273@qq.com", 1024).toHex(),true);
//        //认证
//        sub.login(token);
//        System.out.println("是否认证："+sub.isAuthenticated());//true
//        //退出认证
//        sub.logout();
//        System.out.println("是否认证："+sub.isAuthenticated());//false
//    }

}