package com.ruoyi.coupon.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:16
 **/
public interface IVipshopService {

	/**
	 * 唯品会类目商品列表
	 *
	 * @param bo 参数
	 * @return
	 */
	AjaxResult wphList(MallQueryBo bo);

	/**
	 * 京东类目运营频道数据
	 *
	 * @param bo 参数
	 * @return
	 */
	AjaxResult mallChannelList(MallQueryBo bo);

	/**
	 * 唯品会商品详情
	 *
	 * @return
	 */
	AjaxResult wphDetail(MallQueryBo bo);

	/**
	 * 唯品会商品转链跳转
	 *
	 * @return
	 */
	AjaxResult wphTurnChain(MallQueryBo bo);

	/**
	 * 唯品会商品分类
	 *
	 * @return
	 */
	AjaxResult wphClass(MallQueryBo bo);

	/**
	 * 唯品会二级商城图标
	 *
	 * @return
	 */
	AjaxResult mallSecondIcon(MallQueryBo bo);
}
