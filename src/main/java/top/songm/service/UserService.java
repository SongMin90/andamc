package top.songm.service;

import com.alibaba.fastjson.JSONObject;
import top.songm.model.request.User;

/**
 * @author songm
 * @datetime 2019/2/12 14:57
 */
public interface UserService {

    JSONObject login(String js_code);

    void register(User user);

    User info(String openid);
}
