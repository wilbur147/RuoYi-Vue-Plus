package com.ruoyi.content.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专题分页查询对象 ry_con_topic
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("专题分页查询对象")
public class ConTopicQueryBo extends BaseEntity {

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


	/** 专题名称 */
	@ApiModelProperty("专题名称")
	private String topicName;

}
