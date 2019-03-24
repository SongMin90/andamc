package top.songm.model.response;

import lombok.Data;
import top.songm.model.request.Product;

import java.util.List;

/**
 * 产品详情
 * @author songm
 * @datetime 2019/2/13 0:41
 */
@Data
public class ProductInfo extends Product {

    // 销量
    private int salesVolume;
    // 好评率
    private int appraiseCount;
    // 评价总数
    private int appraiseAllCount;
    // 图片
    private List<String> imageList;
}
