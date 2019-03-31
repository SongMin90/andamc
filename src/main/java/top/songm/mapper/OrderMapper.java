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

    /*@Select("SELECT\n" +
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
    List<OrderRow> findByOpenidWithPage(@Param("openid") String openid, @Param("position") int position, @Param("pageSize") int pageSize);*/

    @Select("SELECT * FROM `order` WHERE `remove_status` = 0 AND `openid` = #{openid,jdbcType=VARCHAR} ORDER BY `create_time` DESC LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<Order> findByOpenidWithPage(@Param("openid") String openid, @Param("position") int position, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM `order` WHERE id=#{id,jdbcType=INTEGER} AND `remove_status` = 0 LIMIT 1")
    Order findById(@Param("id") int id);

    @Update("UPDATE `order` SET `state`=#{state,jdbcType=INTEGER}, `update_time`= NOW() WHERE (`id`=#{id,jdbcType=INTEGER})")
    long updateStateById(@Param("id") int id, @Param("state") int state);

    @Insert("INSERT INTO `order` (\n" +
            "\t`openid`,\n" +
            "\t`product_id`,\n" +
            "\t`number`,\n" +
            "\t`money`,\n" +
            "\t`state`,\n" +
            "\t`order_sn`,\n" +
            "\t`name`,\n" +
            "\t`discount_type`,\n" +
            "\t`discount_desc`,\n" +
            "\t`discount`,\n" +
            "\t`discount_temp`,\n" +
            "\t`first_pic_url`,\n" +
            "\t`real_money`,\n" +
            "\t`ip`,\n" +
            "\t`update_time`,\n" +
            "\t`create_time`\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t\t#{openid,jdbcType=VARCHAR},\n" +
            "\t\t#{productId,jdbcType=INTEGER},\n" +
            "\t\t#{number,jdbcType=INTEGER},\n" +
            "\t\t#{money,jdbcType=DOUBLE},\n" +
            "\t\t#{state,jdbcType=INTEGER},\n" +
            "\t\t#{orderSn,jdbcType=VARCHAR},\n" +
            "\t\t#{name,jdbcType=VARCHAR},\n" +
            "\t\t#{discountType,jdbcType=INTEGER},\n" +
            "\t\t#{discountDesc,jdbcType=VARCHAR},\n" +
            "\t\t#{discount,jdbcType=DOUBLE},\n" +
            "\t\t#{discountTemp,jdbcType=DOUBLE},\n" +
            "\t\t#{firstPicUrl,jdbcType=VARCHAR},\n" +
            "\t\t#{realMoney,jdbcType=DOUBLE},\n" +
            "\t\t#{ip,jdbcType=VARCHAR},\n" +
            "\t\tNOW(),\n" +
            "\t\tNOW()\n" +
            "\t)")
    long insert(Order order);

    @Update("UPDATE `order`\n" +
            "SET `remove_status` = 1,\n" +
            "`update_time` = NOW()\n" +
            "WHERE\n" +
            "\t`id` = #{id,jdbcType=INTEGER}")
    long removeById(@Param("id") int id);

    @Update("UPDATE `order` SET `state`=#{state,jdbcType=INTEGER}, `update_time`= NOW() WHERE (`order_sn`=#{out_trade_no,jdbcType=VARCHAR} AND `remove_status` = 0)")
    long updateStateByOrderSn(@Param("out_trade_no") String out_trade_no, @Param("state") int state);
}
