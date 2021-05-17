package com.ruoyi.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.blog.domain.Blog;

import java.util.List;

/**
 * 博客系统Service接口
 *
 * @author ruoyi
 * @date 2021-04-13
 */
public interface IBlogService extends IService<Blog> {

    /**
     * 查询列表
     */
    List<Blog> queryList(Blog ryBlog);
}
