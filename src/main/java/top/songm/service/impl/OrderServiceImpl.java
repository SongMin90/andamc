package top.songm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.songm.mapper.OrderMapper;
import top.songm.model.request.Order;
import top.songm.service.OrderService;
import top.songm.service.PayService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 22:09
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayService payService;

    @Override
    public List<Order> findByOpenidWithPage(String openid, int position, int pageSize) {
        List<Order> orderList = orderMapper.findByOpenidWithPage(openid, position, pageSize);

        for (Order order : orderList) {
            // 查询支付结果
            if (order.getState() == 0) {
                boolean paySuccess = payService.getPayState(order.getOrderSn());
                if (paySuccess) {
                    orderMapper.updateStateById(order.getId(), 1);
                    order.setState(1);
                } else {
                    orderMapper.updateStateById(order.getId(), -1);
                    order.setState(-1);
                }
            }
        }

        return orderList;
    }
}
