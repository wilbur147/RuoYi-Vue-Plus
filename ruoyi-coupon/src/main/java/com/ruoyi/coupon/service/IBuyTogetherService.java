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
     * 拼多多精选商品列表
     * @return
     */
    AjaxResult buyTogetherList(String listId, Integer offset, Integer limit);

    /**
     * 拼多多多类目商品列表
     * @param bo 参数
     * @return
     */
    AjaxResult buyTogetherMoreList(MallQueryBo bo);

    /**
     * 拼多多多商品详情
     * @return
     */
    AjaxResult buyTogetherDetail(MallQueryBo bo);

    /**
     * 拼多多多商品转链跳转
     * @return
     */
    AjaxResult buyTogetherTurnChain(MallQueryBo bo);


}
