package top.songm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.songm.mapper.ManagerMapper;
import top.songm.mapper.ProductMapper;
import top.songm.mapper.ShoppingCartMapper;
import top.songm.model.request.ShoppingCart;
import top.songm.model.response.ShoppingCartRow;
import top.songm.service.ShoppingCartService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 13:52
 */
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public void addOneShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartMapper.save(shoppingCart);
    }

    @Override
    public List<ShoppingCartRow> findListByOpenidWithPage(String openid, int position, int pageSize) {
        return shoppingCartMapper.findListByOpenidWithPage(openid, position, pageSize);
    }

    @Override
    public void removeByOpenidAndProductIds(String openid, int[] productIds) {
        for (int productId : productIds) {
            shoppingCartMapper.removeByOpenidAndProductIds(openid, productId);
        }
    }
}
