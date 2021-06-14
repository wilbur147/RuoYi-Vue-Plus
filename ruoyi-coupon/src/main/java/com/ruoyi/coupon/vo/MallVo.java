package com.ruoyi.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 商品视图对象
 *
 * @author ruoyi
 * @date 2021-05-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("商品视图对象")
public class MallVo {

	@ApiModelProperty("商品ID")
	private String goodsId;

	@ApiModelProperty("商品名称")
	private String goodsName;

	@ApiModelProperty("商品描述")
	private String goodsDesc;

	@ApiModelProperty("封面图像")
	private String img;

	@ApiModelProperty("多图")
	private Object imges;

	@ApiModelProperty("价格")
	private String price;

	@ApiModelProperty("优惠券金额")
	private String discount;

	@ApiModelProperty("券后价格")
	private String priceAfter;

	@ApiModelProperty("销量")
	private String sales;

	@ApiModelProperty("优惠券开始时间")
	private Long couponStartTime;

	@ApiModelProperty("优惠券结束时间")
	private Long couponEndTime;

	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("商品描述分")
	private String avgDesc;

	@ApiModelProperty("商家物流分")
	private String avgLgst;

	@ApiModelProperty("商家服务分")
	private String avgServ;

	@ApiModelProperty("优惠券链接")
	private String couponUrl;

	@ApiModelProperty("推广小程序信息")
	private Object weChatInfo;


}
