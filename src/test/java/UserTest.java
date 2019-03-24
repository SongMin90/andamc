import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.songm.mapper.UserMapper;
import top.songm.model.request.User;
import top.songm.service.UserService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 16:43
 */
public class UserTest extends TmallApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void login() {
        List<User> list = userMapper.findByOpenid("1");
        System.out.println(123);
    }

    @Test
    public void register() {
        userMapper.registerUser("4");
        System.out.println(123);
    }
}
