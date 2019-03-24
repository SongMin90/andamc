package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import top.songm.model.request.Appraise;
import top.songm.model.request.Order;
import top.songm.model.response.OrderRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 22:10
 */
@Mapper
public interface OrderMapper {

    @Select("SELECT\n" +
            "\tord.*, pr.`name`,\n" +
            "\tpr.`discount_desc`,\n" +
            "\t(ord.`number` * pr.`money` - ord.`money`) AS discounted_money,\n" +
            "\tpr.`first_pic_url`\n" +
            "FROM\n" +
            "\t`order` ord\n" +
            "INNER JOIN `product` pr ON ord.`product_id` = pr.id\n" +
            "AND ord.`remove_status` = 0\n" +
            "AND ord.`openid` = #{openid,jdbcType=VARCHAR}\n" +
            "ORDER BY\n" +
            "\tord.`create_time`\n" +
            "LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<OrderRow> findByOpenidWithPage(@Param("openid") String openid, @Param("position") int position, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM `order` WHERE id=#{id,jdbcType=INTEGER} AND `remove_status` = 0 LIMIT 1")
    Order findById(@Param("id") int id);

    @Update("UPDATE `order` SET `state`=#{state,jdbcType=INTEGER}, `update_time`= NOW() WHERE (`id`=#{id,jdbcType=INTEGER})")
    long updateStateById(@Param("id") int id, @Param("state") int state);
}
