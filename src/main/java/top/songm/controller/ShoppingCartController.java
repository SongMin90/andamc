package top.songm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.BaseLogger;
import top.songm.model.request.ShoppingCart;
import top.songm.model.response.Msg;
import top.songm.model.response.ShoppingCartRow;
import top.songm.service.ShoppingCartService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:23
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController extends BaseLogger<ShoppingCartController> {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Msg addShoppingCart(Msg msg, @RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.addOneShoppingCart(shoppingCart);
        return msg;
    }

    @GetMapping("/list")
    public Msg list(Msg msg, @RequestParam("openid") String openid, @RequestParam("position") int position, @RequestParam("pageSize") int pageSize) {
        List<ShoppingCartRow> list = shoppingCartService.findListByOpenidWithPage(openid, position, pageSize);
        msg.setData(list);
        return msg;
    }

    @PostMapping("/buyOrder")
    public Msg buyOrder(Msg msg, @RequestBody List<ShoppingCartRow> shoppingCartRows) {
        JSONObject jsonObject = shoppingCartService.buyOrder(shoppingCartRows);
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping("/removeByProductIds")
    public Msg removeByProductIds(Msg msg, @RequestParam("openid") String openid, @RequestBody int[] productIds) {
        shoppingCartService.removeByOpenidAndProductIds(openid, productIds);
        return msg;
    }
}
