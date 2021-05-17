package com.ruoyi.blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.blog.domain.BlogSort;
import com.ruoyi.blog.mapper.BlogSortMapper;
import com.ruoyi.blog.service.IBlogSortService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 博客分类Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Service
public class BlogSortServiceImpl extends ServiceImpl<BlogSortMapper, BlogSort> implements IBlogSortService {

    @Override
    public List<BlogSort> queryList(BlogSort blogSort) {
        LambdaQueryWrapper<BlogSort> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blogSort.getSortName())){
            lqw.like(BlogSort::getSortName , blogSort.getSortName());
        }
        if (blogSort.getStatus() != null){
            lqw.eq(BlogSort::getStatus , blogSort.getStatus());
        }
        lqw.orderByDesc(BlogSort::getSort);
        return this.list(lqw);
    }

    @Override
    public boolean clickTop(BlogSort blogSort) {
        // 1. 找出目前所有数据中排序最大的一条数据
        LambdaQueryWrapper<BlogSort> lqw = Wrappers.lambdaQuery();
        lqw.select(BlogSort::getSort,BlogSort::getBlogSortId).orderByDesc(BlogSort::getSort).last("limit 1");
        BlogSort tag = this.getOne(lqw);
        if (ObjectUtil.isNotEmpty(tag)){
            blogSort.setSort(tag.getSort() + 1);
            return this.updateById(blogSort);
        }
        return false;
    }
}
