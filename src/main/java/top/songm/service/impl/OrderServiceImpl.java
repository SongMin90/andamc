package top.songm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.songm.mapper.OrderMapper;
import top.songm.model.request.Order;
import top.songm.model.response.OrderRow;
import top.songm.service.OrderService;

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

    @Override
    public List<Order> findByOpenidWithPage(String openid, int position, int pageSize) {
        return orderMapper.findByOpenidWithPage(openid, position, pageSize);
    }
}
