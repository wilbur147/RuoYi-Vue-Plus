package com.ruoyi.coupon.bo;

import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
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
	@ApiModelProperty("分类标签衍生")
	private String catIdRe;

	/**
	 * 0综合 1销量 2最新 3价格升序 4价格降序
	 */
	@ApiModelProperty("筛选方式")
	private String sortType;
	@ApiModelProperty("排序方式")
	private String sort;
	@ApiModelProperty("搜索id")
	private String searchId;
	/**
	 * 是否过滤无券： 0不过滤 1过滤
	 */
	@ApiModelProperty("是否只返回优惠券的商品")
	private String withCoupon;
	@ApiModelProperty("优惠券链接")
	private String couponUrl;

	@ApiModelProperty("频道类型")
	private String channelType;
	@ApiModelProperty("请求源类型")
	private String sourceType;
	@ApiModelProperty("过滤大小")
	private PddDdkGoodsSearchRequest.RangeListItem rangeItem;

	@ApiModelProperty("蚂蚁星球KEY")
	private String apikey;
	@ApiModelProperty("推广位PID")
	private String pid;
}
