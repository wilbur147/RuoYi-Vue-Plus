package com.ruoyi.content.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;



/**
 * 内容添加对象 ry_content
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@ApiModel("内容添加对象")
public class ContentAddBo {

    /** 分类 */
    @ApiModelProperty("分类")
    private Long categroyId;
    /** 专题 */
    @ApiModelProperty("专题")
    private Long conTopicId;
    /** 标题名称 */
    @ApiModelProperty("标题名称")
    private String contentName;
    /** 图标 */
    @ApiModelProperty("图标")
    private String icon;
    /** banner图 */
    @ApiModelProperty("banner图")
    private String banner;
    /** 关键字 */
    @ApiModelProperty("关键字")
    private String keywords;
    /** 状态 */
    @ApiModelProperty("状态")
    private Integer status;
    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    private Long orderNum;
    /** 是否推荐 */
    @ApiModelProperty("是否推荐")
    private String recommend;
    /** 内容 */
    @ApiModelProperty("内容")
    private String content;
    /** 扩展字段 */
    @ApiModelProperty("扩展字段")
    private String expand;
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
