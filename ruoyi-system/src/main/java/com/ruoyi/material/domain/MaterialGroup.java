package com.ruoyi.material.domain;

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
 * 素材分组对象 material_group
 * 
 * @author ruoyi
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("material_group")
public class MaterialGroup  extends BaseEntity
{

private static final long serialVersionUID=1L;


    /** 分组ID */
    @TableId(value = "material_group_id")
    private Integer materialGroupId;

    /** 分组名称 */
    @Excel(name = "分组名称")
    private String groupName;

    /** 父级ID */
    private Integer parentId;

    /** 是否有效 */
    private String isVoid;
}
