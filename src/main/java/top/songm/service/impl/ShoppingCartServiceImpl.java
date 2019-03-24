package top.songm.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.songm.mapper.ManagerMapper;
import top.songm.mapper.ProductMapper;
import top.songm.mapper.ShoppingCartMapper;
import top.songm.model.request.Manager;
import top.songm.model.request.ShoppingCart;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ShoppingCartRow;
import top.songm.service.ShoppingCartService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 13:52
 */
@Service
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
    public JSONObject buyOrder(List<ShoppingCartRow> shoppingCartRows) {
        // 取出产品id，计算总金额
        double totalMoney = 0.0D;
        String phone = null;
        for (ShoppingCartRow shoppingCartRow : shoppingCartRows) {
            totalMoney += (shoppingCartRow.getMoney() * shoppingCartRow.getNumber());
            ProductInfo product = productMapper.findById(shoppingCartRow.getId());
            Manager manager = managerMapper.findById(product.getManagerId());
            phone = manager.getPhone();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("totalMoney", totalMoney);
        return jsonObject;
    }

    @Override
    public void removeByOpenidAndProductIds(String openid, int[] productIds) {
        for (int productId : productIds) {
            shoppingCartMapper.removeByOpenidAndProductIds(openid, productId);
        }
    }
}
