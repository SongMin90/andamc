package top.songm.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.songm.BaseLogger;
import top.songm.model.response.Msg;
import top.songm.utils.HttpUtils;
import top.songm.utils.MD5Util;
import top.songm.utils.WeChatAppPayUtils;
import top.songm.utils.WxPaySignatureUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author songm
 * @datetime 2019/3/28 22:37
 */
@RestController
@RequestMapping("/pay")
public class PayController extends BaseLogger<PayController> {

    @PostMapping("/getRequestPaymentData")
    public Msg getRequestPaymentData(Msg msg, HttpServletRequest request) {
        String openid = request.getHeader("openid");
        Map<String, String> stringStringMap = doOrder(openid);
        // PJnVJX98_C.ndXE
//        String appId = "wx545267dffd88ff64";
//        String timeStamp = String.valueOf(new Date().getTime());
//        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
//        String package1 = "prepay_id=wx29002755924638cb1d696d744159134329";
//        String signType = "MD5";
//
//        String stringSignTemp = "appId=" + appId + "&timeStamp=" + timeStamp + "&nonceStr=" + nonceStr + "&package=" + package1 + "&signType=" + signType + "&key=wsxedcrfvvfrcdexswqaztgbyhnmjuik"; // 注：key为商户平台设置的密钥key
//        String paySign = MD5Util.MD5encode(stringSignTemp).toUpperCase();
//
//        JSONObject json = new JSONObject();
//        json.put("timeStamp", timeStamp);
//        json.put("nonceStr", nonceStr);
//        json.put("package", package1);
//        json.put("signType", signType);
//        json.put("paySign", paySign);

        msg.setData(stringStringMap);
        return msg;
    }

    int i = 1;

    private Map<String, String> doOrder(String openid) {
        int relAmount = 1;
        String WX_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";//暂时不变
        String WX_PAY_KEY = "songm";
        String WX_APPID = "wx545267dffd88ff64";
        String MCHID = "1530322721";
        String body = "安达门窗支付中心-商品付款";
        String WX_PAY_CALLBACK = "https://songm.top/pay";
        String ip = "27.38.119.138";
        String orderSn = "20150806125346" + (i++);

        // 加密，这里只列举必填字段
        Map<String, String> map = new HashMap<String, String>();
        map.put("body", body);//商品描述
        map.put("mch_id", MCHID);//商户平台id
        map.put("appid", WX_APPID);//小程序ID
        map.put("nonce_str", nonceStr);//随机字符串
        map.put("notify_url", WX_PAY_CALLBACK);//异步回调api
        map.put("spbill_create_ip", ip);//支付ip
        map.put("out_trade_no", orderSn);//商品订单号
        map.put("total_fee", (int) relAmount + "");//真实金额
        map.put("trade_type", "JSAPI");//JSAPI、h5调用
        map.put("openid", openid);//支付用户openid

        //将参数字典序列排序
        String stringSignTemp =  WeChatAppPayUtils.formatUrlMap(map, false, false);

        stringSignTemp = stringSignTemp + "&key=wsxedcrfvvfrcdexswqaztgbyhnmjuik";

        //得到签名
        String sign = MD5Util.MD5encode(stringSignTemp).toUpperCase();

//        String sign = WxPaySignatureUtils.signature(map, WX_PAY_KEY);
        String xml = "<xml>" +
                "<appid>"+ WX_APPID +"</appid>"+
                "<body>"+ body +"</body>"+
                "<mch_id>"+ MCHID +"</mch_id>"+
                "<nonce_str>"+ nonceStr +"</nonce_str>"+
                "<notify_url>"+ WX_PAY_CALLBACK +"</notify_url>"+
                "<openid>"+ openid +"</openid>"+
                "<out_trade_no>"+ orderSn +"</out_trade_no>"+
                "<spbill_create_ip>"+ ip +"</spbill_create_ip>"+
                "<total_fee>"+ (int) relAmount + "" +"</total_fee>"+
                "<trade_type>JSAPI</trade_type>"+
                "<sign>"+ sign +"</sign>"+
                "</xml>";

        LOGGER.info("发送给微信的报文：" + xml);
        LOGGER.info("加密后的的签名字符串：" + sign);

        // 请求
        String response = "";
        try {
            response = HttpUtils.sentPost(WX_UNIFIEDORDER, xml, "UTF-8");
        } catch (Exception e) {
            //TODO
            return null;
        }
        LOGGER.info("请求/pay/unifiedorder下单接口后返回数据：" + response);
        // 处理请求结果
        //将返回的xml转为map
        Map<String,String> resultMap = WeChatAppPayUtils.readStringXmlOut(response);
        String prepay_id = resultMap.getOrDefault("prepay_id", "");

        String return_code = resultMap.getOrDefault("return_code", "");
        if(return_code != null && "SUCCESS".equals(return_code)  && prepay_id != null && !"".equals(prepay_id)){
            LOGGER.info(" prepay_id  [ {} ]",prepay_id);
            //要返回给app端的支付参数
            //APP端调起支付的参数列表
            Map<String, String> paraMapApp = new HashMap<>();
            //微信开放平台审核通过的应用APPID
            paraMapApp.put("appId",WeChatAppPayUtils.APPID);
            paraMapApp.put("signType","MD5");
            paraMapApp.put("package","prepay_id=" + prepay_id);
            paraMapApp.put("nonceStr",WeChatAppPayUtils.getNonceStr());
            String timeStamp = String.valueOf(WeChatAppPayUtils.getSecondTimestamp(new Date()));
            paraMapApp.put("timeStamp",timeStamp);

            String stringSignTempApp = WeChatAppPayUtils.formatUrlMap(paraMapApp,false,false);
            stringSignTempApp = stringSignTempApp + "&key=" + WeChatAppPayUtils.MCH_ID_KEY;
            LOGGER.info("stringSignTempApp [ {} ]",stringSignTempApp);
            //得到app支付签名
            String paySign = MD5Util.MD5encode(stringSignTempApp).toUpperCase();
            paraMapApp.put("paySign",paySign);

            LOGGER.info("返回给app的参数 [ {} ]",paraMapApp);
            return paraMapApp;

        }else {
            LOGGER.info("获取prepay_id失败 [ {} ]");
        }

        return null;
    }
}
