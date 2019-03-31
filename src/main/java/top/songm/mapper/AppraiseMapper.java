package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import top.songm.model.request.Appraise;
import top.songm.model.request.Product;
import top.songm.model.response.AppraiseRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 10:36
 */
@Mapper
public interface AppraiseMapper {

    @Select("SELECT COUNT(1) FROM `appraise` WHERE `product_id` = #{productId,jdbcType=INTEGER} AND `remove_status` = 0")
    int countByProductId(@Param("productId") int productId);

    @Select("SELECT * FROM `appraise` WHERE `product_id` = #{productId,jdbcType=INTEGER} AND `remove_status` = 0")
    List<Appraise> findByProductId(@Param("productId") int productId);

    @Select("SELECT * FROM `appraise` WHERE `product_id` = #{productId,jdbcType=INTEGER} AND `remove_status` = 0 LIMIT #{position,jdbcType=INTEGER}, #{pageSize,jdbcType=INTEGER}")
    List<AppraiseRow> findByProductIdWithPage(@Param("productId") int productId, @Param("position") int position, @Param("pageSize") int pageSize);

    @Insert("INSERT INTO `appraise` (`openid`, `order_id`, `product_id`, `star`, `content`, `remove_status`, `update_time`, `create_time`) " +
            "VALUES (#{openid,jdbcType=VARCHAR}, #{orderId,jdbcType=INTEGER}, #{productId,jdbcType=INTEGER}, #{star,jdbcType=INTEGER}, #{content,jdbcType=VARCHAR}, '0', NOW(), NOW())")
    long save(Appraise appraise);

    @Select("CALL save_appraise_return_id(#{openid,mode=IN,jdbcType=VARCHAR}, #{orderId,mode=IN,jdbcType=INTEGER}, #{productId,mode=IN,jdbcType=INTEGER}, #{star,mode=IN,jdbcType=INTEGER}, #{content,mode=IN,jdbcType=VARCHAR}, #{id,mode=OUT,jdbcType=INTEGER})")
    @Options(statementType= StatementType.CALLABLE)
    int saveReturnId(Appraise appraise);
}
