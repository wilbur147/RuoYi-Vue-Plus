package com.ruoyi.coupon.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:16
 **/
public interface IJingDongService {

    /**
     * 京东类目商品列表
     * @param bo 参数
     * @return
     */
    AjaxResult jdList(MallQueryBo bo);

    /**
     * 京东商品详情
     * @return
     */
    AjaxResult jdDetail(MallQueryBo bo);

    /**
     * 京东商品转链跳转
     * @return
     */
    AjaxResult jdTurnChain(MallQueryBo bo);


}
