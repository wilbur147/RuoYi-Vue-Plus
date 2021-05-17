package com.ruoyi.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.material.domain.MaterialGroup;

import java.util.List;

/**
 * 素材分组Service接口
 *
 * @author ruoyi
 * @date 2021-03-24
 */
public interface IMaterialGroupService extends IService<MaterialGroup> {

    /**
     * 查询列表
     */
    List<MaterialGroup> queryList(MaterialGroup materialGroup);
}
