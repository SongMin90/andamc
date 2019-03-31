package top.songm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.BaseLogger;
import top.songm.model.request.PayData;
import top.songm.model.response.Msg;
import top.songm.service.PayService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author songm
 * @datetime 2019/3/28 22:37
 */
@RestController
@RequestMapping("/pay")
public class PayController extends BaseLogger<PayController> {

    @Autowired
    private PayService payService;

    @PostMapping("/getRequestPaymentData")
    public Msg getRequestPaymentData(Msg msg, HttpServletRequest request, @RequestBody List<PayData> payDataList) {
        Map<String, String> stringStringMap = payService.getRequestPaymentData(request, payDataList);
        msg.setData(stringStringMap);
        return msg;
    }

    /**
     * 再次付款
     * @param msg
     * @param request
     * @param id
     * @return
     */
    @PostMapping("/getRequestPaymentData2")
    public Msg getRequestPaymentData2(Msg msg, HttpServletRequest request, @RequestBody int id) {
        Map<String, String> stringStringMap = payService.getRequestPaymentData2(request, id);
        msg.setData(stringStringMap);
        return msg;
    }

    /**
     * 微信支付后异步回调
     * @param request
     */
    @GetMapping("/callback")
    public void callback(HttpServletRequest request){
        payService.callback(request);
    }
}
