package top.songm.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.songm.mapper.ManagerMapper;
import top.songm.model.request.Manager;
import top.songm.service.ManagerService;
import top.songm.utils.AESUtil;
import top.songm.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/17 10:21
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public void login(Manager manager, HttpServletRequest request) {
        List<Manager> list = managerMapper.findByPhoneAndPassword(manager.getPhone(), manager.getPassword());
        if (list.isEmpty()) {
            throw new RuntimeException("手机号码或密码错误！");
        }
        // 设置token到session
        String token = AESUtil.encrypt(Constant.secretKey, String.valueOf(list.get(0).getId()));
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        session.setMaxInactiveInterval(Constant.SESSION_SAVE_TIME);
    }
}
