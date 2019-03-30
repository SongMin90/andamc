package top.songm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.songm.mapper.UserMapper;
import top.songm.model.request.User;
import top.songm.service.UserService;
import top.songm.utils.Constant;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 14:58
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject login(String js_code, String ip) {
        // 获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + Constant.appid + "&secret=" + Constant.secret + "&js_code=" + js_code + "&grant_type=" + Constant.grant_type;
        String responseStr = HttpClient.get(url).asString();
        JSONObject responseJSON = JSONObject.parseObject(responseStr);
        // 返回结果
        JSONObject json = new JSONObject();
        json.put("isRegister", false);
        if (!responseJSON.containsKey("errcode") && responseJSON.containsKey("openid")) {
            String openid = responseJSON.getString("openid");
            String session_key = responseJSON.getString("session_key");
            // 查询用户表是否存在
            List<User> userList = userMapper.findByOpenid(openid);
            if (userList.isEmpty()) {
                // 新增openid
                userMapper.registerUser(openid, ip);
            } else if (StringUtils.isEmpty(userList.get(0).getNickName()) || StringUtils.isEmpty(userList.get(0).getAvatarUrl())) {
            } else {
                json.put("isRegister", true);
            }
            // 设置openid
            json.put("openid", openid);
            json.put("session_key", session_key);
        } else {
            throw new RuntimeException("请求信息不正确！");
        }
        return json;
    }

    @Override
    public void register(User user) {
        userMapper.updateUserByOpenid(user);
    }

    @Override
    public User info(String openid) {
        return userMapper.findByOpenid(openid).get(0);
    }
}
