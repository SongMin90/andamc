package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author songm
 * @datetime 2019/2/13 9:28
 */
@Data
public class Order {

    private int id;
    private String openid;
    private int productId;
    private int number;
    private double money; // 产品单价

    private String orderSn;
    private String name;
    private int discountType;
    private String discountDesc;
    private double discount;
    private double discountTemp;
    private String firstPicUrl;
    private double realMoney; // 产品实付金额

    private String ip;
    private int state;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
