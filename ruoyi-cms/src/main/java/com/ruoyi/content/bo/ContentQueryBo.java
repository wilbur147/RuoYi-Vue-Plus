package com.ruoyi.content.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 内容分页查询对象 ry_content
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("内容分页查询对象")
public class ContentQueryBo extends BaseEntity {

	/** 分页大小 */
	@ApiModelProperty("分页大小")
	private Integer pageSize;
	/** 当前页数 */
	@ApiModelProperty("当前页数")
	private Integer pageNum;
	/** 排序列 */
	@ApiModelProperty("排序列")
	private String orderByColumn;
	/** 排序的方向desc或者asc */
	@ApiModelProperty(value = "排序的方向", example = "asc,desc")
	private String isAsc;


	/** 分类 */
	@ApiModelProperty("分类")
	private Long categroyId;
	/** 专题 */
	@ApiModelProperty("专题")
	private Long conTopicId;
	/** 标题名称 */
	@ApiModelProperty("标题名称")
	private String contentName;
	/** 状态 */
	@ApiModelProperty("状态")
	private String status;
	/** 关键字 */
	@ApiModelProperty("关键字")
	private String keywords;

}
