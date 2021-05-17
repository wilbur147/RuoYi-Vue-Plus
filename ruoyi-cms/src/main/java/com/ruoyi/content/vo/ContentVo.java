package com.ruoyi.content.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 内容视图对象 mall_package
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@ApiModel("内容视图对象")
public class ContentVo {
	private static final long serialVersionUID = 1L;

	/** 内容主键ID */
	@ApiModelProperty("内容主键ID")
	private Long contentId;

	/** 内容主键ID */
	@ApiModelProperty("分类ID")
	private Long categroyId;


	/** 内容主键ID */
	@ApiModelProperty("专题ID")
	private Long conTopicId;

	/** 标题名称 */
	@Excel(name = "标题名称")
	@ApiModelProperty("标题名称")
	private String contentName;
	/** 图标 */
	@Excel(name = "图标")
	@ApiModelProperty("图标")
	private String icon;
	@Excel(name = "图标路径")
	private String iconPath;
	/** banner图 */
	@Excel(name = "banner图")
	@ApiModelProperty("banner图")
	private String banner;
	@Excel(name = "banner图路径")
	private String bannerPath;
	/** 关键字 */
	@Excel(name = "关键字")
	@ApiModelProperty("关键字")
	private String keywords;
	/** 状态 */
	@Excel(name = "状态")
	@ApiModelProperty("状态")
	private Integer status;
	/** 显示顺序 */
	@Excel(name = "显示顺序")
	@ApiModelProperty("显示顺序")
	private Long orderNum;
	/** 是否推荐 */
	@Excel(name = "是否推荐")
	@ApiModelProperty("是否推荐")
	private String recommend;
	/** 创建时间 */
	@Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("创建时间")
	private Date createTime;

}
