package com.ruoyi.blog.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 博客系统对象 ry_blog
 *
 * @author ruoyi
 * @date 2021-04-13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "ry_blog", excludeProperty = "remark")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Blog extends BaseEntity
{

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "blog_id")
    private Integer blogId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 简介 */
    private String summary;

    /** 内容 */
    private String content;

    /** 标签 */
    @Excel(name = "标签")
    private String blogTagId;

    /** 分类 */
    @Excel(name = "分类")
    private String blogSortId;

    /** 点击数 */
    @Excel(name = "点击数")
    private Long clickCount;

    /** 封面图 */
    @Excel(name = "封面图")
    private String fileUniqueId;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 文章出处 */
    @Excel(name = "文章出处")
    private String articlesPart;

    /** 是否原创 */
    @Excel(name = "是否原创")
    private String isOriginal;

    /** 是否发布 */
    @Excel(name = "是否发布")
    private String isPublish;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    /** 是否评论 */
    @Excel(name = "是否评论")
    private String openComment;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 编辑器类型【0 富文本编辑器， 1：Markdown编辑器】 */
    @Excel(name = "编辑器类型")
    private Integer editorType;

    /** 外链【如果是推广，那么将跳转到外链】 */
    private String outsideLink;

    /** 文件路径 */
    @TableField(exist = false)
    private String filePath;

    /** 分类 */
    @TableField(exist = false)
    private BlogSort blogSort;

    /** 标签列表 */
    @TableField(exist = false)
    private List<BlogTag> blogTags;
}
