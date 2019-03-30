package top.songm.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

/**
 * @author songm
 * @datetime 2019/3/29 0:07
 */
public class WeChatAppPayUtils {

    //小程序appid
    public static final String APP_ID = "wx545267dffd88ff64";

    //小程序appsecret
    public static final String APPSECRET = "2eb40a3134487ce040f7bd5eb63f6223";

    //商户号
    public static final String MCH_ID = "1530322721";

    //商户密钥
    public static final String MCH_ID_KEY = "wsxedcrfvvfrcdexswqaztgbyhnmjuik";

    //统一下单地址
    public static final String PAYURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //异步回调地址
    public static final String NOTIFY_URL = "https://songm.top/pay";

    //支付标题
    public static final String BODY = "安达门窗支付中心-商品付款";


    /**
     * 获取精确到秒的时间戳 10 位数
     *
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    /**
     * @param xml
     * @return Map
     * @description 将xml字符串转换成map
     */
    public static Map<String, String> readStringXmlOut(String xml) {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            @SuppressWarnings("unchecked")
            List<Element> list = rootElt.elements();// 获取根节点下所有节点
            for (Element element : list) { // 遍历节点
                map.put(element.getName(), element.getText()); // 节点的name为map的key，text为map的value
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*
     * 生成32位随机字符串
     * */
    public static String getNonceStr() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }


    /**
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    /**
     * 获取参数签名
     * @param paramMap  签名参数
     * @param paySecret 签名密钥
     * @return
     */
    public static String  getSign(Map<String , String> paramMap , String paySecret){
        SortedMap<String, String> smap = new TreeMap<String, String>(paramMap);

        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))&&!"sign".equals(m.getKey())){
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }

        String argPreSign = stringBuffer.append("key=").append(paySecret).toString();
        return MD5Util.MD5encode(argPreSign).toUpperCase();
    }

    /**
     * 解密用户信息
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
