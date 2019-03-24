package top.songm.service;

import top.songm.model.response.OrderRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 22:09
 */
public interface OrderService {

    List<OrderRow> findByOpenidWithPage(String openid, int position, int pageSize);
}
