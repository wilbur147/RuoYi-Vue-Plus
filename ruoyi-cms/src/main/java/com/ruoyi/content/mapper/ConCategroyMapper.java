package com.ruoyi.content.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.content.domain.ConCategroy;
import com.ruoyi.content.vo.ConCategroyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper接口
 *
 * @author ruoyi
 * @date 2021-05-12
 */
public interface ConCategroyMapper extends BaseMapper<ConCategroy> {

    List<ConCategroyVo> selectAll(@Param("categroyName")String categroyName,
                                  @Param("categroyId")Long categroyId);



}
