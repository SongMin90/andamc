package top.songm.model.response;

import lombok.Data;

/**
 * @author songm
 * @datetime 2019/2/16 15:28
 */
@Data
public class ShoppingCartRow {

    private int id;
    private String name;
    private int number;
    private double money;
    private int discountType;
    private String discountDesc;
    private double discount;
    private String firstPicUrl;
    private double discountTemp;
}
