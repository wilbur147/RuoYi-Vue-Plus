package com.ruoyi.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.material.domain.MaterialResources;
import com.ruoyi.material.mapper.MaterialResourcesMapper;
import com.ruoyi.material.service.IMaterialResourcesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材资源Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-24
 */
@Service
public class MaterialResourcesServiceImpl extends ServiceImpl<MaterialResourcesMapper, MaterialResources> implements IMaterialResourcesService {


    @Override
    @SetFilePath
    public TableDataInfo<MaterialResources> queryPageList(MaterialResources materialResources) {
        LambdaQueryWrapper<MaterialResources> lqw = Wrappers.lambdaQuery();
        if (materialResources.getMaterialGroupId() != null){
            lqw.eq(MaterialResources::getMaterialGroupId ,materialResources.getMaterialGroupId());
        }
        if (StringUtils.isNotBlank(materialResources.getResourceName())){
            lqw.like(MaterialResources::getResourceName ,materialResources.getResourceName());
        }
        if (StringUtils.isNotBlank(materialResources.getResourceType())){
            lqw.eq(MaterialResources::getResourceType ,materialResources.getResourceType());
        }
        if (StringUtils.isNotBlank(materialResources.getIsVoid())){
            lqw.eq(MaterialResources::getIsVoid ,materialResources.getIsVoid());
        }
        return PageUtils.buildDataInfo(this.page(PageUtils.buildPage(),lqw));
    }

    @Override
    @SetFilePath
    public List<MaterialResources> queryList(MaterialResources materialResources) {
        LambdaQueryWrapper<MaterialResources> lqw = Wrappers.lambdaQuery();
        if (materialResources.getMaterialGroupId() != null){
            lqw.eq(MaterialResources::getMaterialGroupId ,materialResources.getMaterialGroupId());
        }
        if (StringUtils.isNotBlank(materialResources.getResourceName())){
            lqw.like(MaterialResources::getResourceName ,materialResources.getResourceName());
        }
        if (StringUtils.isNotBlank(materialResources.getResourceType())){
            lqw.eq(MaterialResources::getResourceType ,materialResources.getResourceType());
        }
        if (StringUtils.isNotBlank(materialResources.getIsVoid())){
            lqw.eq(MaterialResources::getIsVoid ,materialResources.getIsVoid());
        }
        return this.list(lqw);
    }

}
