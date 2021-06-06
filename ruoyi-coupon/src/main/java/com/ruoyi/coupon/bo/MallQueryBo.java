package com.ruoyi.coupon.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 影票分页查询对象
 *
 * @author ruoyi
 * @date 2021-05-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("商城查询对象")
public class MallQueryBo {

	@ApiModelProperty("商品goodsId")
	private String goodsId;
	@ApiModelProperty("搜索关键字")
	private String searchStr;
	@ApiModelProperty("当前页数")
	private Integer page;
	@ApiModelProperty("分页大小")
	private Integer pageSize;
	@ApiModelProperty("商城类别")
	private String cpType;
	@ApiModelProperty("分类标签")
	private Long catId;
	@ApiModelProperty("排序方式")
	private Integer sortType;
	@ApiModelProperty("搜索id")
	private String searchId;
	@ApiModelProperty("是否只返回优惠券的商品")
	private String withCoupon;

	@ApiModelProperty("蚂蚁星球KEY")
	private String apikey;
	@ApiModelProperty("推广位PID")
	private String pid;
}
