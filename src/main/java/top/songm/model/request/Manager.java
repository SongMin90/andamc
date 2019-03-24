package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author songm
 * @datetime 2019/2/13 8:58
 */
@Data
public class Manager {

    private int id;
    private String phone;
    private String password;
    private String email;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
