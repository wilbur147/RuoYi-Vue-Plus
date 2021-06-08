package com.ruoyi.content.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 内容对象 ry_content
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ry_content")
public class Content extends BaseEntity {

private static final long serialVersionUID=1L;


    /** 内容主键ID */
    @TableId(value = "content_id")
    private Long contentId;

    /** 分类 */
    private Long categroyId;

    /** 专题 */
    private Long conTopicId;

    /** 标题名称 */
    private String contentName;

    /** 图标 */
    private String icon;

    /** banner图 */
    private String banner;

    /** 关键字 修改时可以更新为null空*/
    @TableField(value = "keywords", updateStrategy = FieldStrategy.IGNORED)
    private String keywords;

    /** 状态 */
    private Integer status;

    /** 显示顺序 */
    private Long orderNum;

    /** 是否推荐 */
    private String recommend;

    /** 内容 */
    private String content;

    /** 扩展字段 */
    private String expand;

}
