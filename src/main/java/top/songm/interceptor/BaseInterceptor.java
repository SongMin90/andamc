package top.songm.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.songm.mapper.UserMapper;
import top.songm.model.request.User;
import top.songm.model.response.Msg;
import top.songm.utils.AESUtil;
import top.songm.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 自定义拦截器
 *
 * @author SongM
 * @date 2017/3/9
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        String uri = request.getRequestURI();

        // 小程序校验openid
        if(uri.contains("/shoppingCart/add") || uri.contains("/user/register") || uri.contains("/appraise/add") || uri.contains("/shoppingCart/removeByProductIds") || uri.contains("/pay/getRequestPaymentData") || uri.contains("/pay/getRequestPaymentData2")) {
            String openid = request.getHeader("openid");
            List<User> list = userMapper.findByOpenid(openid);
            if (list.isEmpty()) {
                throw new RuntimeException(Msg.MessageEnum.VERIFY_USER_FAIL.getName());
            }
        }
        // 管理员后台校验
        if(uri.contains("/manager/findProductListByManagerWithPage") || uri.contains("/manager/remove") || uri.contains("/manager/productInfo") || uri.contains("/manager/addOrEditProductInfo") || uri.contains("/image/upload")) {
            HttpSession session = request.getSession();
            Object object = session.getAttribute("token");
            if (object != null) {
                Integer.valueOf(AESUtil.decrypt(Constant.secretKey, object.toString()));
            } else {
                throw new RuntimeException(Msg.MessageEnum.VERIFY_USER_FAIL.getName());
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
    }
}
