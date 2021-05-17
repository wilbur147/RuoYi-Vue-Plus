package com.ruoyi.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.material.domain.MaterialGroup;
import com.ruoyi.material.mapper.MaterialGroupMapper;
import com.ruoyi.material.service.IMaterialGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材分组Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-24
 */
@Service
public class MaterialGroupServiceImpl extends ServiceImpl<MaterialGroupMapper, MaterialGroup> implements IMaterialGroupService {

    @Override
    public List<MaterialGroup> queryList(MaterialGroup materialGroup) {
        LambdaQueryWrapper<MaterialGroup> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(materialGroup.getGroupName())){
            lqw.like(MaterialGroup::getGroupName ,materialGroup.getGroupName());
        }
        if (StringUtils.isNotBlank(materialGroup.getIsVoid())){
            lqw.eq(MaterialGroup::getIsVoid ,materialGroup.getIsVoid());
        }
        return this.list(lqw);
    }
}
