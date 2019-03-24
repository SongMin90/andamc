package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author songm
 * @datetime 2019/2/13 9:28
 */
@Data
public class ShoppingCart {

    private int id;
    private String openid;
    private int productId;
    private int number;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;

    private String token;
}
