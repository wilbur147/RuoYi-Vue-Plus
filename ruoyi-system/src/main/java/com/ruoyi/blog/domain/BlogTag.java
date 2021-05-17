package com.ruoyi.blog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 博客标签对象 ry_blog_tag
 * 
 * @author ruoyi
 * @date 2021-04-13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ry_blog_tag")
public class BlogTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "blog_tag_id")
    private Long blogTagId;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String tagName;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;
}
