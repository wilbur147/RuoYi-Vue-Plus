package com.ruoyi.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 商城分类视图对象
 *
 * @author ruoyi
 * @date 2021-05-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("商城分类视图对象")
public class MallClassVo {

	@ApiModelProperty("分类ID")
	private String catId;

	@ApiModelProperty("分类名称")
	private String name;


}
