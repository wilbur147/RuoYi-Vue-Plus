package com.ruoyi.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.blog.domain.BlogTag;

import java.util.List;

/**
 * 博客标签Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface IBlogTagService extends IService<BlogTag> {

    /**
     * 查询列表
     */
    List<BlogTag> queryList(BlogTag blogTag);

    /**
     * 置顶操作
     * @param blogTag
     */
    boolean clickTop(BlogTag blogTag);
}
