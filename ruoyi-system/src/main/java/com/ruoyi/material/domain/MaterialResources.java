package com.ruoyi.material.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 素材资源对象 material_resources
 * 
 * @author ruoyi
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("material_resources")
public class MaterialResources  extends BaseEntity
{

private static final long serialVersionUID=1L;


    /** 素材ID */
    @TableId(value = "material_resources_id")
    private Integer materialResourcesId;

    /** 分组ID */
    @Excel(name = "分组ID")
    private Integer materialGroupId;

    /** 资源地址 */
    @Excel(name = "资源地址")
    private String fileUniqueId;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String resourceName;

    /** 资源类型 */
    @Excel(name = "资源类型")
    private String resourceType;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 是否有效 */
    private String isVoid;

    /** 文件路径 */
    @TableField(exist = false)
    private String filePath;
}
