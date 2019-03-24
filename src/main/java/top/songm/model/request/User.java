package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * 用户类
 * @author songm
 * @datetime 2019/2/12 14:53
 */
@Data
public class User {

    private String openid;
    private String nickName;
    private int gender;
    private String avatarUrl;
    private String province;
    private String city;
    private String country;
    private String language;
    private String phone;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
