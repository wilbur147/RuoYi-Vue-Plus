package com.ruoyi.coupon.vo;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 影票视图对象
 *
 * @author ruoyi
 * @date 2021-05-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("影票视图对象")
public class MovieVo {

	/** 内容影票主键 */
	@ApiModelProperty("内容影票主键")
	private Long id;

	/** 图像 */
	@Excel(name = "图像")
	@ApiModelProperty("图像")
	private String img;
	/** 电影名称 */
	@Excel(name = "电影名称")
	@ApiModelProperty("电影名称")
	private String nm;

	/** 英文电影名称 */
	@Excel(name = "英文电影名称")
	@ApiModelProperty("英文电影名称")
	private String enm;

	/** 影幕类型 */
	@Excel(name = "影幕类型")
	@ApiModelProperty("影幕类型")
	private String showMark;

	@Excel(name = "观众评分")
	@ApiModelProperty("观众评分")
	private String sc;

	@Excel(name = "主演")
	@ApiModelProperty("主演")
	private String leadingRole;

	@Excel(name = "描述")
	@ApiModelProperty("描述")
	private String description;

	@Excel(name = "国家")
	@ApiModelProperty("国家")
	private String country;

	@Excel(name = "时长")
	@ApiModelProperty("时长")
	private Integer duration;

	@Excel(name = "影片分类")
	@ApiModelProperty("影片分类")
	private String type;

	@Excel(name = "上映时间")
	@ApiModelProperty("上映时间")
	private String openDay;

	@Excel(name = "热门1，预售2")
	@ApiModelProperty("热门，预售")
	private Integer showType;


}
