package com.ruoyi.content.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分类对象 ry_con_categroy
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ry_con_categroy")
public class ConCategroy extends BaseEntity {

private static final long serialVersionUID=1L;


    /** 内容分类ID */
    @TableId(value = "con_categroy_id")
    private Long categroyId;

    /** 父菜单ID */
    private Long parentId;

    /** 类别名称 */
    private String categroyName;

    /** 类别图标 */
    private String icon;

    /** 显示顺序 */
    private Integer orderNum;

    /** 描述 */
    private String description;

}
