package com.ruoyi.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.blog.domain.Blog;
import com.ruoyi.blog.mapper.BlogMapper;
import com.ruoyi.blog.service.IBlogService;
import com.ruoyi.blog.service.IBlogSortService;
import com.ruoyi.blog.service.IBlogTagService;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 博客系统Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    private IBlogSortService blogSortService;
    @Autowired
    private IBlogTagService blogTagService;

    @Override
    @SetFilePath
    public TableDataInfo<Blog> queryPageList(Blog blog) {
        LambdaQueryWrapper<Blog> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blog.getTitle())){
            lqw.like(Blog::getTitle ,blog.getTitle());
        }
        if (StringUtils.isNotBlank(blog.getBlogTagId())){
            lqw.in(Blog::getBlogTagId ,blog.getBlogTagId());
        }
        if (StringUtils.isNotBlank(blog.getBlogSortId())){
            lqw.eq(Blog::getBlogSortId ,blog.getBlogSortId());
        }
        if (blog.getStatus() != null){
            lqw.eq(Blog::getStatus ,blog.getStatus());
        }
        if (StringUtils.isNotBlank(blog.getIsOriginal())){
            lqw.eq(Blog::getIsOriginal ,blog.getIsOriginal());
        }
        if (StringUtils.isNotBlank(blog.getIsPublish())){
            lqw.eq(Blog::getIsPublish ,blog.getIsPublish());
        }
        if (StringUtils.isNotBlank(blog.getOpenComment())){
            lqw.eq(Blog::getOpenComment ,blog.getOpenComment());
        }
        if (blog.getType() != null){
            lqw.eq(Blog::getType ,blog.getType());
        }
        lqw.select(Blog.class,i-> !i.getProperty().equals("content")&&!i.getProperty().equals("summary"));
        lqw.orderByDesc(Blog::getSort);
        Page<Blog> page = this.page(PageUtils.buildPage(), lqw);
        page.getRecords().forEach(this::setAttachData);
        return PageUtils.buildDataInfo(page);
    }

    @Override
    @SetFilePath
    public List<Blog> queryList(Blog blog) {
        LambdaQueryWrapper<Blog> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blog.getTitle())){
            lqw.like(Blog::getTitle ,blog.getTitle());
        }
        if (StringUtils.isNotBlank(blog.getBlogTagId())){
            lqw.in(Blog::getBlogTagId ,blog.getBlogTagId());
        }
        if (StringUtils.isNotBlank(blog.getBlogSortId())){
            lqw.eq(Blog::getBlogSortId ,blog.getBlogSortId());
        }
        if (blog.getStatus() != null){
            lqw.eq(Blog::getStatus ,blog.getStatus());
        }
        if (StringUtils.isNotBlank(blog.getIsOriginal())){
            lqw.eq(Blog::getIsOriginal ,blog.getIsOriginal());
        }
        if (StringUtils.isNotBlank(blog.getIsPublish())){
            lqw.eq(Blog::getIsPublish ,blog.getIsPublish());
        }
        if (StringUtils.isNotBlank(blog.getOpenComment())){
            lqw.eq(Blog::getOpenComment ,blog.getOpenComment());
        }
        if (blog.getType() != null){
            lqw.eq(Blog::getType ,blog.getType());
        }
        lqw.select(Blog.class,i-> !i.getProperty().equals("content")&&!i.getProperty().equals("summary"));
        lqw.orderByDesc(Blog::getSort);
        List<Blog> list = this.list(lqw);
        list.forEach(this::setAttachData);
        return list;
    }

    private void setAttachData(Blog blog){
        // 查询分类
        if (StrUtil.isNotEmpty(blog.getBlogSortId())) {
            blog.setBlogSort(blogSortService.getById(blog.getBlogSortId()));
        }
        // 查询标签
        if (StrUtil.isNotEmpty(blog.getBlogTagId())) {
            blog.setBlogTags(blogTagService.listByIds(Arrays.asList(blog.getBlogTagId().split(","))));
        }
    }
}
