package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import top.songm.model.request.ShoppingCart;
import top.songm.model.response.ShoppingCartRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 13:55
 */
@Mapper
public interface ShoppingCartMapper {

    @Insert("INSERT INTO `shopping_cart` (`openid`, `product_id`, `number`, `remove_status`, `update_time`, `create_time`) VALUES (#{openid,jdbcType=VARCHAR}, #{productId,jdbcType=INTEGER}, #{number,jdbcType=INTEGER}, '0', NOW(), NOW())")
    long save(ShoppingCart shoppingCart);

    @Select("SELECT\n" +
            "\tpr.`id`,\n" +
            "\tpr.`name`,\n" +
            "\tsum(sc.`number`) as number,\n" +
            "\tpr.`money`,\n" +
            "\tpr.`discount_type`,\n" +
            "\tpr.`discount_desc`,\n" +
            "\tpr.`discount`,\n" +
            "\tpr.`discount_temp`,\n" +
            "\tpr.`first_pic_url`\n" +
            "FROM\n" +
            "\t`shopping_cart` sc\n" +
            "INNER JOIN \n" +
            "\t`product` pr ON sc.`product_id` = pr.`id`\n" +
            "AND sc.`remove_status` = 0\n" +
            "AND sc.`openid` = #{openid,jdbcType=VARCHAR} \n" +
            "GROUP BY sc.`openid`, sc.`product_id` \n" +
            "ORDER BY sc.`update_time` DESC \n" +
            "LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<ShoppingCartRow> findListByOpenidWithPage(@Param("openid") String openid, @Param("position") int position, @Param("pageSize") int pageSize);

    @Update("UPDATE `shopping_cart`\n" +
            "SET `remove_status` = 1,\n" +
            "`update_time` = NOW()\n" +
            "WHERE\n" +
            "\t`openid` = #{openid,jdbcType=VARCHAR}\n" +
            "AND `product_id` = #{productId,jdbcType=INTEGER}")
    long removeByOpenidAndProductIds(@Param("openid") String openid, @Param("productId") int productId);
}
