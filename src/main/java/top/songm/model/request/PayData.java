package top.songm.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songm
 * @datetime 2019/3/30 17:20
 */
@Getter
@Setter
public class PayData {

    private int productId;
    private int productNum;
    private String productName;
    private double productMoney;
    private int discountType;
    private String discountDesc;
    private double discount;
    private double discountTemp;
    private String firstPicUrl;

    private double realMoney;

}
