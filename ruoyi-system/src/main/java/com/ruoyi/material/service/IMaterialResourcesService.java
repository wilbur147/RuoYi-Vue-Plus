package com.ruoyi.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.material.domain.MaterialResources;

import java.util.List;

/**
 * 素材资源Service接口
 *
 * @author ruoyi
 * @date 2021-03-24
 */
public interface IMaterialResourcesService extends IService<MaterialResources> {

    /**
     * 查询列表
     */
    TableDataInfo<MaterialResources> queryPageList(MaterialResources materialResources);

    /**
     * 查询列表
     */
    List<MaterialResources> queryList(MaterialResources materialResources);
}
