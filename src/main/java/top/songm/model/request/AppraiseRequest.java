package top.songm.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/3/31 11:48
 */
@Getter
@Setter
public class AppraiseRequest extends Appraise {

    private List<String> imgUrlList;
}
