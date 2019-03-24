package top.songm.model.response;

import lombok.Data;
import top.songm.model.request.Product;

/**
 * 产品列表
 * @author songm
 * @datetime 2019/2/13 0:41
 */
@Data
public class ProductRow extends Product {

    // 销量
    private int salesVolume;
    // 评价率
    private int appraiseCount;
}
