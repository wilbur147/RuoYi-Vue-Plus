package com.ruoyi.content.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;



/**
 * 分类添加对象 ry_con_categroy
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@ApiModel("分类添加对象")
public class ConCategroyAddBo {

    /** 父菜单ID */
    @ApiModelProperty("父菜单ID")
    private Long parentId;
    /** 类别名称 */
    @ApiModelProperty("类别名称")
    private String categroyName;
    /** 类别图标 */
    @ApiModelProperty("类别图标")
    private String icon;
    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    private Integer orderNum;
    /** 描述 */
    @ApiModelProperty("描述")
    private String description;
    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;
    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;
    /** 更新时间 */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;
}
