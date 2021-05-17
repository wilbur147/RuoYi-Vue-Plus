package com.ruoyi.blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.blog.domain.BlogTag;
import com.ruoyi.blog.mapper.BlogTagMapper;
import com.ruoyi.blog.service.IBlogTagService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 博客标签Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

    @Override
    public TableDataInfo<BlogTag> queryPageList(BlogTag blogTag) {
        LambdaQueryWrapper<BlogTag> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blogTag.getTagName())){
            lqw.like(BlogTag::getTagName , blogTag.getTagName());
        }
        if (blogTag.getStatus() != null){
            lqw.eq(BlogTag::getStatus , blogTag.getStatus());
        }
        lqw.orderByDesc(BlogTag::getSort);
        return PageUtils.buildDataInfo(this.page(PageUtils.buildPage(),lqw));
    }

    @Override
    public List<BlogTag> queryList(BlogTag blogTag) {
        LambdaQueryWrapper<BlogTag> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blogTag.getTagName())){
            lqw.like(BlogTag::getTagName , blogTag.getTagName());
        }
        if (blogTag.getStatus() != null){
            lqw.eq(BlogTag::getStatus , blogTag.getStatus());
        }
        lqw.orderByDesc(BlogTag::getSort);
        return this.list(lqw);
    }

    @Override
    public boolean clickTop(BlogTag blogTag) {
        // 默认排序值越大，数据就越靠前
        // 1. 找出目前所有数据中排序最大的一条数据
        LambdaQueryWrapper<BlogTag> lqw = Wrappers.lambdaQuery();
        lqw.select(BlogTag::getSort,BlogTag::getBlogTagId).orderByDesc(BlogTag::getSort).last("limit 1");
        BlogTag tag = this.getOne(lqw);
        if (ObjectUtil.isNotEmpty(tag)){
            blogTag.setSort(tag.getSort() + 1);
            return this.updateById(blogTag);
        }
        return false;
    }
}
