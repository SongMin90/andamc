package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author songm
 * @datetime 2019/2/13 9:27
 */
@Data
public class Appraise {

    private int id;
    private String openid;
    private int orderId;
    private int productId;
    private int star;
    private String content;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
