package com.ruoyi.content.mapper;
import com.ruoyi.common.core.mybatisplus.core.BaseMapperPlus;
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
public interface ConCategroyMapper extends BaseMapperPlus<ConCategroy> {

    List<ConCategroyVo> selectAll(@Param("categroyName")String categroyName,
                                  @Param("categroyId")Long categroyId);



}
