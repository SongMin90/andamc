package top.songm.model.response;

import lombok.Data;
import top.songm.model.request.Order;

/**
 * @author songm
 * @datetime 2019/2/16 22:11
 */
@Data
public class OrderRow extends Order {

    private String name;
    private String discountDesc;
    private double discountedMoney;
    private String firstPicUrl;
}
