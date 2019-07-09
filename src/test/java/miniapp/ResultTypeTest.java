package miniapp;

import com.chenxin.miniapp.Application;
import com.chenxin.miniapp.mapper.UserMapper;
import com.chenxin.miniapp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class ResultTypeTest {
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier(value = "UserMapper")
    private UserMapper userMapper;

    @Test
    public void test1(){
        try {
            System.out.println(userService.listUser());
            System.out.println(userService.listUserPojo());
            System.out.println(userService.getUser("miniapp"));
            System.out.println(userMapper.userListByResultMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
