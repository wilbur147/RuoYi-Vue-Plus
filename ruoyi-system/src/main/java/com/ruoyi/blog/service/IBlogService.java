package com.ruoyi.blog.service;

import com.ruoyi.blog.domain.Blog;
import com.ruoyi.common.core.mybatisplus.core.IServicePlus;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 博客系统Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface IBlogService extends IServicePlus<Blog> {

    /**
     * 查询分页列表
     */
    TableDataInfo<Blog> queryPageList(Blog ryBlog);

    /**
     * 查询列表
     */
    List<Blog> queryList(Blog ryBlog);
}
