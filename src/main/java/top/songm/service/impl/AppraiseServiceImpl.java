package top.songm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.songm.mapper.AppraiseMapper;
import top.songm.mapper.ImageMapper;
import top.songm.mapper.OrderMapper;
import top.songm.mapper.UserMapper;
import top.songm.model.request.*;
import top.songm.model.response.AppraiseRow;
import top.songm.service.AppraiseService;
import top.songm.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 17:34
 */
@Service
@Transactional
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private AppraiseMapper appraiseMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<AppraiseRow> findByProductIdWithPage(int productId, int position, int pageSize) {
        // 获取评价信息
        List<AppraiseRow> list = appraiseMapper.findByProductIdWithPage(productId, position, pageSize);
        for (AppraiseRow appraiseRow : list) {
            // 设置图片
            List<Image> imageList = imageMapper.findByDataId(appraiseRow.getId(), 1);
            List<String> imgUrlList = new ArrayList();
            for (Image image : imageList) {
                imgUrlList.add(image.getUrl());
            }
            appraiseRow.setImageList(imgUrlList);
            // 设置用户信息
            User user = userMapper.findByOpenid(appraiseRow.getOpenid()).get(0);
            appraiseRow.setUserNickName(user.getNickName());
            appraiseRow.setUserAvatarUrl(user.getAvatarUrl());
            // 设置评价时间
            appraiseRow.setAppraiseTime(TimeUtil.formatTime(appraiseRow.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        return list;
    }

    @Override
    public void add(AppraiseRequest appraiseRequest) {
        // 查询订单信息
        Order order = orderMapper.findById(appraiseRequest.getOrderId());
        // 添加评价
        appraiseRequest.setProductId(order.getProductId());
        int appraiseId = appraiseMapper.saveReturnId(appraiseRequest);
        // 评价图片
        for (String imgUrl : appraiseRequest.getImgUrlList()) {
            Image image = new Image();
            image.setDataId(appraiseId);
            image.setUrl(imgUrl);
            image.setType(1);
            imageMapper.save(image);
        }
        // 设置订单状态为已评价
        orderMapper.updateStateById(order.getId(), 2);
    }
}
