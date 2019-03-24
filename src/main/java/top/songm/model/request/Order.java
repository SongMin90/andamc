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
    private double money;
    private int state;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
