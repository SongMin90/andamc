package top.songm.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.songm.BaseLogger;
import top.songm.mapper.OrderMapper;
import top.songm.mapper.ShoppingCartMapper;
import top.songm.model.request.Order;
import top.songm.model.request.PayData;
import top.songm.service.PayService;
import top.songm.utils.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songm
 * @datetime 2019/3/30 17:10
 */
@Service
@Transactional
public class PayServiceImpl extends BaseLogger<PayServiceImpl> implements PayService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public Map<String, String> getRequestPaymentData(HttpServletRequest request, List<PayData> payDataList) {
        int relAmount = 0;
        String openid = request.getHeader("openid");
        String ip = IpUtil.getIp(request);
        //商品订单号
        String orderSn = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
        // 购买的产品列表
        for (PayData payData : payDataList) {
            // 实际金额
            double realMoney = payData.getRealMoney();
            // 创建订单
            Order order = new Order();
            order.setOpenid(openid);
            order.setOrderSn(orderSn);
            order.setProductId(payData.getProductId());
            order.setNumber(payData.getProductNum());
            order.setMoney(payData.getProductMoney());
            order.setName(payData.getProductName());
            order.setDiscountType(payData.getDiscountType());
            order.setDiscountDesc(payData.getDiscountDesc());
            order.setDiscount(payData.getDiscount());
            order.setDiscountTemp(payData.getDiscountTemp());
            order.setFirstPicUrl(payData.getFirstPicUrl());
            order.setRealMoney(realMoney);
            order.setIp(ip);
            order.setState(0);
            order.setRemoveStatus(0);
            orderMapper.insert(order);
            // 删除其在购物车
            shoppingCartMapper.removeByOpenidAndProductIds(openid, payData.getProductId());
            // 合计
            relAmount += ((int) (realMoney * 100));
        }
        return getPayData(relAmount, orderSn, openid, ip);
    }

    @Override
    public Map<String, String> getRequestPaymentData2(HttpServletRequest request, int id) {
        int relAmount = 0;
        String openid = request.getHeader("openid");
        String ip = IpUtil.getIp(request);
        //商品订单号
        String orderSn = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");

        // 找到原商品
        Order order = orderMapper.findById(id);
        // 实际金额
        double realMoney = order.getRealMoney();
        // 合计
        relAmount += (int) realMoney * 100;
        // 创建订单
        order.setOrderSn(orderSn);
        order.setIp(ip);
        order.setState(0);
        orderMapper.insert(order);
        // 删除原订单
        orderMapper.removeById(id);

        return getPayData(relAmount, orderSn, openid, ip);
    }

    /**
     * 获取待支付订单信息
     *
     * @param relAmount
     * @param openid
     * @param ip
     * @return
     */
    private Map<String, String> getPayData(int relAmount, String orderSn, String openid, String ip) {
        // 商户登录 PJnVJX98_C.ndXE

        String nonceStr = WeChatAppPayUtils.getNonceStr();
        String APP_ID = WeChatAppPayUtils.APP_ID;
        String MCH_ID = WeChatAppPayUtils.MCH_ID;
        String BODY = WeChatAppPayUtils.BODY;
        String NOTIFY_URL = WeChatAppPayUtils.NOTIFY_URL;


        // 加密，这里只列举必填字段
        Map<String, String> map = new HashMap<String, String>();
        map.put("body", BODY);//商品描述
        map.put("mch_id", MCH_ID);//商户平台id
        map.put("appid", APP_ID);//小程序ID
        map.put("nonce_str", nonceStr);//随机字符串
        map.put("notify_url", NOTIFY_URL);//异步回调api
        map.put("spbill_create_ip", ip);//支付ip
        map.put("out_trade_no", orderSn);//商品订单号
        map.put("total_fee", (int) relAmount + "");//真实金额
        map.put("trade_type", "JSAPI");//JSAPI、h5调用
        map.put("openid", openid);//支付用户openid

        //将参数字典序列排序
        String stringSignTemp = WeChatAppPayUtils.formatUrlMap(map, false, false);

        stringSignTemp = stringSignTemp + "&key=wsxedcrfvvfrcdexswqaztgbyhnmjuik";

        //得到签名
        String sign = MD5Util.MD5encode(stringSignTemp).toUpperCase();

        String xml = "<xml>" +
                "<appid>" + APP_ID + "</appid>" +
                "<body>" + BODY + "</body>" +
                "<mch_id>" + MCH_ID + "</mch_id>" +
                "<nonce_str>" + nonceStr + "</nonce_str>" +
                "<notify_url>" + NOTIFY_URL + "</notify_url>" +
                "<openid>" + openid + "</openid>" +
                "<out_trade_no>" + orderSn + "</out_trade_no>" +
                "<spbill_create_ip>" + ip + "</spbill_create_ip>" +
                "<total_fee>" + (int) relAmount + "" + "</total_fee>" +
                "<trade_type>JSAPI</trade_type>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";

        LOGGER.info("发送给微信的报文：" + xml);
        LOGGER.info("加密后的的签名字符串：" + sign);

        // 请求
        String response = HttpUtils.sentPost(WeChatAppPayUtils.PAYURL, xml, "UTF-8");
        LOGGER.info("请求/pay/unifiedorder下单接口后返回数据：" + response);
        // 处理请求结果
        // 将返回的xml转为map
        Map<String, String> resultMap = WeChatAppPayUtils.readStringXmlOut(response);
        String prepay_id = resultMap.getOrDefault("prepay_id", "");

        String return_code = resultMap.getOrDefault("return_code", "");
        if (return_code != null && "SUCCESS".equals(return_code) && prepay_id != null && !"".equals(prepay_id)) {
            LOGGER.info(" prepay_id  [ {} ]", prepay_id);
            //要返回给app端的支付参数
            //APP端调起支付的参数列表
            Map<String, String> paraMapApp = new HashMap<>();
            //微信开放平台审核通过的应用APPID
            paraMapApp.put("appId", APP_ID);
            paraMapApp.put("signType", "MD5");
            paraMapApp.put("package", "prepay_id=" + prepay_id);
            paraMapApp.put("nonceStr", nonceStr);
            String timeStamp = String.valueOf(WeChatAppPayUtils.getSecondTimestamp(new Date()));
            paraMapApp.put("timeStamp", timeStamp);

            String stringSignTempApp = WeChatAppPayUtils.formatUrlMap(paraMapApp, false, false);
            stringSignTempApp = stringSignTempApp + "&key=" + WeChatAppPayUtils.MCH_ID_KEY;
            LOGGER.info("stringSignTempApp [ {} ]", stringSignTempApp);
            //得到app支付签名
            String paySign = MD5Util.MD5encode(stringSignTempApp).toUpperCase();
            paraMapApp.put("paySign", paySign);

            LOGGER.info("返回给app的参数 [ {} ]", paraMapApp);
            return paraMapApp;
        } else {
            throw new RuntimeException("微信支付下单失败");
        }
    }

    @Override
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = new HashMap<>();
        params.put("body", getBody(request));
        //读取xml内容转为map
        params = WeChatAppPayUtils.readStringXmlOut(params.getOrDefault("body", ""));

        LOGGER.info("微信支付回调参数 [ {} ]", params);

        //商户订单号
        String out_trade_no = params.getOrDefault("out_trade_no", "");
        //业务结果SUCCESS或者FAIL
        String result_code = params.getOrDefault("result_code", "");
        //支付金额 (分)
        String total_fee = params.getOrDefault("total_fee", "");
        //支付完成时间
        String time_end = params.getOrDefault("time_end", "");

        //签名
        String sign = params.getOrDefault("sign", "");

        //商家数据包，原样返回
        String attach = params.getOrDefault("attach", "");

        //排除sign参数的所有参数，参与签名 , 验证微信回调的签名
        Map<String, String> map = new HashMap<>();
        for (String s : params.keySet()) {
            String value = params.get(s);
            if (StringUtils.isNotBlank(value) && !StringUtils.equals(s, "sign")) {
                map.put(s, value);
            }
        }

        String comSign = WeChatAppPayUtils.getSign(map, WeChatAppPayUtils.MCH_ID_KEY);

        if (null != result_code && "SUCCESS".equals(result_code)) {
            if (StringUtils.equals(sign, comSign)) {
                //TODO 通过out_trade_no或者attach查询到数据库里的订单,再进行验证等
                orderMapper.updateStateByOrderSn(out_trade_no, 1); // 设为已付款
                try {
                    ServletOutputStream out = response.getOutputStream();
                    OutputStreamWriter ow = new OutputStreamWriter(out,"UTF-8");
                    ow.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
                    ow.flush();
                    ow.close();
                } catch (Exception e) {}
            } else {
                LOGGER.info("验证签名失败 [ {} ]", params);
                orderMapper.updateStateByOrderSn(out_trade_no, -1); // 设为付款失败
            }
        } else {
            LOGGER.info("支付失败 [ {} ]", params);
            orderMapper.updateStateByOrderSn(out_trade_no, -1); // 设为付款失败
        }
    }

    /**
     * 回调getBody
     *
     * @param request
     * @return
     */
    private String getBody(HttpServletRequest request) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            CharArrayWriter data = new CharArrayWriter();
            char[] buf = new char[8192];
            int ret;
            while ((ret = in.read(buf, 0, 8192)) != -1) {
                data.write(buf, 0, ret);
            }
            return data.toString();
        } catch (Exception e) {
            LOGGER.error("接收BODY内容失败：" + e);
        }
        return null;
    }
}
