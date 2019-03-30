package top.songm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.BaseLogger;
import top.songm.model.request.User;
import top.songm.model.response.Msg;
import top.songm.service.UserService;
import top.songm.utils.IpUtil;
import top.songm.utils.WeChatAppPayUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户Controller
 * @author songm
 * @datetime 2019/2/12 14:47
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseLogger<UserController> {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Msg login(Msg msg, @RequestParam(value = "js_code") String js_code, HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        JSONObject json = userService.login(js_code, ip);
        msg.setData(json);
        return msg;
    }

    @PostMapping("/register")
    public Msg register(Msg msg, @RequestBody User user) {
        userService.register(user);
        return msg;
    }

    @GetMapping("/info/{openid}")
    public Msg info(Msg msg, @PathVariable("openid") String openid) {
        User user = userService.info(openid);
        msg.setData(user);
        return msg;
    }

    @GetMapping("/getUserInfo")
    public Msg getUserInfo(Msg msg, @RequestParam("encryptedData") String encryptedData, @RequestParam("session_key") String session_key, @RequestParam("iv") String iv) {
        JSONObject userInfo = WeChatAppPayUtils.getUserInfo(encryptedData, session_key, iv);
        msg.setData(userInfo);
        return msg;
    }

}
