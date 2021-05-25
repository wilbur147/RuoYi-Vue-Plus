package com.ruoyi.coupon.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 影票分页查询对象
 *
 * @author ruoyi
 * @date 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("影票分页查询对象")
public class MovieQueryBo extends BaseEntity {

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


	/** 电影名称 */
	@ApiModelProperty("电影名称")
	private String movieName;

	/** 影票：正在热映 1   即将上映 2 */
	@ApiModelProperty("影票：正在热映 1   即将上映 2")
	private Integer showType;

}
