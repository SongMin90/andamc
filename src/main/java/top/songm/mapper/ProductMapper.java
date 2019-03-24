package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import top.songm.model.request.Product;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ProductRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:18
 */
@Mapper
public interface ProductMapper {

    @Select("SELECT pro.`id`, pro.`name`, pro.`money`, pro.`discount_type`, pro.`discount_desc`, pro.`discount`, pro.`first_pic_url`, \n" +
            "(SELECT COUNT(1) FROM `order` ord WHERE ord.`product_id` = pro.`id` AND ord.`remove_status` = 0) AS sales_volume,\n" +
            "(SELECT COUNT(1) FROM `appraise` appr WHERE appr.`product_id` = pro.`id` AND appr.`star` > 2 AND appr.`remove_status` = 0) AS appraise_count\n" +
            "FROM `product` pro WHERE pro.`remove_status` = 0 LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<ProductRow> findAllByPage(@Param("position") int position, @Param("pageSize") int pageSize);

    @Select("SELECT pro.`id`, pro.`manager_id`, pro.`name`, pro.`money`, pro.`discount_type`, pro.`discount_desc`, pro.`discount`, pro.`first_pic_url`,\n" +
            "(SELECT COUNT(1) FROM `order` ord WHERE ord.`product_id` = pro.`id` AND ord.`remove_status` = 0) AS sales_volume,\n" +
            "(SELECT COUNT(1) FROM `appraise` appr WHERE appr.`product_id` = pro.`id` AND appr.`star` > 2 AND appr.`remove_status` = 0) AS appraise_count\n" +
            "FROM `product` pro WHERE pro.`id` = #{id,jdbcType=INTEGER} LIMIT 1")
    ProductInfo findById(@Param("id") int id);

    @Select("SELECT * FROM `product` WHERE `manager_id` = #{managerId,jdbcType=INTEGER} AND `remove_status` IN (-1, 0) LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<Product> findByManagerAndStatusWithPage(@Param("position") int position, @Param("pageSize") int pageSize, @Param("managerId") int managerId);

    @Update("UPDATE `product` SET `remove_status`='1', `update_time`=NOW() WHERE (`id`=#{id,jdbcType=INTEGER})")
    long deleteById(@Param("id") int id);

    @Select("SELECT * FROM `product` WHERE id = #{id,jdbcType=INTEGER} LIMIT 1")
    ProductInfo getById(@Param("id") int id);

    @Select("CALL save_product_return_id(#{managerId,mode=IN,jdbcType=INTEGER}, #{name,mode=IN,jdbcType=VARCHAR}, #{money,mode=IN,jdbcType=DOUBLE}, #{discountType,mode=IN,jdbcType=INTEGER}, #{discountDesc,mode=IN,jdbcType=VARCHAR}, #{discount,mode=IN,jdbcType=DOUBLE}, #{firstPicUrl,mode=IN,jdbcType=VARCHAR}, #{removeStatus,mode=IN,jdbcType=INTEGER}, #{discountTemp,mode=IN,jdbcType=DOUBLE}, #{id,mode=OUT,jdbcType=INTEGER})")
    @Options(statementType= StatementType.CALLABLE)
    int saveReturnId(Product product);

    @Update("UPDATE `product` SET `name`=#{name,jdbcType=VARCHAR}, `money`=#{money,jdbcType=DOUBLE}, `discount_type`=#{discountType,jdbcType=INTEGER}, `discount_desc`=#{discountDesc,jdbcType=VARCHAR}, `discount`=#{discount,jdbcType=DOUBLE}, `first_pic_url`=#{firstPicUrl,jdbcType=VARCHAR}, `remove_status`=#{removeStatus,jdbcType=INTEGER}, `update_time`=NOW(), `discount_temp`=#{discountTemp,jdbcType=DOUBLE} WHERE (`id`=#{id,jdbcType=INTEGER})")
    long updateById(Product product);
}
