package top.songm.service;

import top.songm.model.request.ShoppingCart;
import top.songm.model.response.ShoppingCartRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 13:52
 */
public interface ShoppingCartService {

    void addOneShoppingCart(ShoppingCart shoppingCart);

    List<ShoppingCartRow> findListByOpenidWithPage(String openid, int position, int pageSize);

    void removeByOpenidAndProductIds(String openid, int[] productIds);
}
