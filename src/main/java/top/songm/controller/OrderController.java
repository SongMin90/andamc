package top.songm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.songm.model.request.Order;
import top.songm.model.response.Msg;
import top.songm.model.response.OrderRow;
import top.songm.service.OrderService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:23
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/findByOpenidWithPage")
    public Msg findByOpenidWithPage(Msg msg, @RequestParam(value = "openid") String openid, @RequestParam(value = "position") int position, @RequestParam(value = "pageSize") int pageSize) {
        List<Order> list = orderService.findByOpenidWithPage(openid, position, pageSize);
        msg.setData(list);
        return msg;
    }
}
