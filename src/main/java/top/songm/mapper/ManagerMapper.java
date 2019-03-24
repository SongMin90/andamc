package top.songm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.songm.model.request.Manager;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/16 17:35
 */
@Mapper
public interface ManagerMapper {

    @Select("SELECT * FROM `manager` WHERE id=#{id,jdbcType=INTEGER} LIMIT 1")
    Manager findById(@Param("id") int id);

    @Select("SELECT * FROM `manager` WHERE `phone` = #{phone,jdbcType=VARCHAR} AND `password` = #{password,jdbcType=VARCHAR} AND `remove_status` = 0")
    List<Manager> findByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);
}
