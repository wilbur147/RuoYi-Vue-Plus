package com.ruoyi.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.blog.domain.BlogSort;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 博客分类Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface IBlogSortService extends IService<BlogSort> {

    /**
     * 查询分页列表
     */
    TableDataInfo<BlogSort> queryPageList(BlogSort blogSort);

    /**
     * 查询列表
     */
    List<BlogSort> queryList(BlogSort blogSort);


    /**
     * 置顶操作
     * @param blogSort
     */
    boolean clickTop(BlogSort blogSort);
}
