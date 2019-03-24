package top.songm.model.request;

import lombok.Data;

import java.util.Date;

/**
 * 图片类
 * @author songm
 * @datetime 2019/2/13 0:13
 */
@Data
public class Image {

    private int id;
    private int dataId;
    private String url;
    private int type;

    private int removeStatus;
    private Date updateTime;
    private Date createTime;
}
