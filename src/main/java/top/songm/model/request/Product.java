package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * 产品类
 * @author songm
 * @datetime 2019/2/13 0:10
 */
@Data
public class Product {

    private int id;
    private int managerId;
    private String name;
    private double money;
    private int discountType;
    private String discountDesc;
    private double discount;
    private double discountTemp;
    private String firstPicUrl;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
