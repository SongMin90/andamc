package top.songm.model.response;

import lombok.Data;
import top.songm.model.request.Appraise;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 17:35
 */
@Data
public class AppraiseRow extends Appraise {

    // 评价图片url列表
    private List<String> imageList;
    // 用户昵称
    private String userNickName;
    // 用户头像url
    private String userAvatarUrl;
    // 评价时间
    private String appraiseTime;

}
