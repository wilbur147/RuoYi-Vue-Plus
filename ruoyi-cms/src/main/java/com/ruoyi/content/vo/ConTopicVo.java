package com.ruoyi.content.vo;

import com.ruoyi.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;



/**
 * 专题视图对象 mall_package
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@ApiModel("专题视图对象")
public class ConTopicVo {
	private static final long serialVersionUID = 1L;

	/** 内容专题主键 */
	@ApiModelProperty("内容专题主键")
	private Long conTopicId;

	/** 类别名称 */
	@Excel(name = "类别名称")
	@ApiModelProperty("类别名称")
	private String topicName;
	/** 类别图标 */
	@Excel(name = "类别图标")
	@ApiModelProperty("类别图标")
	private String icon;
	private String filePath;
	/** 显示顺序 */
	@Excel(name = "显示顺序")
	@ApiModelProperty("显示顺序")
	private Integer orderNum;
	/** 创建时间 */
	@Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("创建时间")
	private Date createTime;

}
