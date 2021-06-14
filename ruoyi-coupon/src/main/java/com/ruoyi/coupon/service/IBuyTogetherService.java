package com.ruoyi.coupon.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:16
 **/
public interface IBuyTogetherService {

	/**
	 * 拼多多类目商品列表
	 *
	 * @param bo 参数
	 * @return
	 */
	AjaxResult buyTogetherList(MallQueryBo bo);

	/**
	 * 拼多多类目运营频道数据
	 *
	 * @param bo 参数
	 * @return
	 */
	AjaxResult mallChannelList(MallQueryBo bo);

	/**
	 * 拼多多多商品详情
	 *
	 * @return
	 */
	AjaxResult buyTogetherDetail(MallQueryBo bo);

	/**
	 * 拼多多商品转链跳转
	 *
	 * @return
	 */
	AjaxResult buyTogetherTurnChain(MallQueryBo bo);


	/**
	 * 拼多多商品分类
	 *
	 * @return
	 */
	AjaxResult buyTogetherClass(MallQueryBo bo);

	/**
	 * 拼多多二级商城图标
	 *
	 * @return
	 */
	AjaxResult mallSecondIcon(MallQueryBo bo);

}
