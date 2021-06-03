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

	@ApiModelProperty("商城类别")
	private String cpType;
	@ApiModelProperty("分页大小")
	private Integer pageSize;
	@ApiModelProperty("当前页数")
	private Integer page;
	@ApiModelProperty("分页标志")
	private String listId;
	@ApiModelProperty("分类标签")
	private Long optId;
	@ApiModelProperty("排序方式")
	private Integer sortType;
	@ApiModelProperty("商品goodsSign")
	private String goodsSign;
	@ApiModelProperty("搜索id")
	private String searchId;
	@ApiModelProperty("是否使用个性化推荐")
	private Boolean useCustomized;
	@ApiModelProperty("是否只返回优惠券的商品")
	private Boolean withCoupon;
}
