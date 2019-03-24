package top.songm.mapper;

import org.apache.ibatis.annotations.*;
import top.songm.model.request.Image;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 10:36
 */
@Mapper
public interface ImageMapper {

    @Select("SELECT * FROM `image` WHERE `data_id` = #{dataId,jdbcType=INTEGER} AND `remove_status` = 0 AND `type` = #{type,jdbcType=INTEGER}")
    List<Image> findByDataId(@Param("dataId") int dataId, @Param("type") int type);

    @Update("UPDATE `image` SET `remove_status` = 1, `update_time`= NOW() WHERE `data_id` = #{dataId,jdbcType=INTEGER} AND type = #{type,jdbcType=INTEGER}")
    long removeByDataIdAndType(@Param("dataId") int dataId, @Param("type") int type);

    @Insert("INSERT INTO `andamc`.`image` (`data_id`, `url`, `type`, `remove_status`, `update_time`, `create_time`) VALUES (#{dataId,jdbcType=INTEGER}, #{url,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER}, '0', NOW(), NOW())")
    long save(Image image);
}
