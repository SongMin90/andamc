package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import top.songm.model.request.User;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 15:00
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM `user` WHERE openid = #{openid,jdbcType=VARCHAR} AND remove_status = 0 LIMIT 1")
    List<User> findByOpenid(@Param("openid") String openid);

    @Insert("INSERT INTO `user` (`openid`, `remove_status`, `update_time`, `create_time`) VALUES (#{openid,jdbcType=VARCHAR}, '0', NOW(), NOW())")
    long registerUser(@Param("openid") String openid);

    @Update("UPDATE `user` SET `nick_name`=#{nickName,jdbcType=VARCHAR}, `gender`=#{gender,jdbcType=INTEGER}, `avatar_url`=#{avatarUrl,jdbcType=VARCHAR}, " +
            "`province`=#{province,jdbcType=VARCHAR}, `city`=#{city,jdbcType=VARCHAR}, `country`=#{country,jdbcType=VARCHAR}, `language`=#{language,jdbcType=VARCHAR}, " +
            "`phone`=#{phone,jdbcType=VARCHAR}, `update_time`=NOW() WHERE (`openid`=#{openid,jdbcType=VARCHAR})")
    long updateUserByOpenid(User user);
}
