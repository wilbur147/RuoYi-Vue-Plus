package com.ruoyi.coupon.controller;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城购物 Controller
 * 
 * @author ruoyi
 * @date 2021-05-25
 */
@Slf4j
@Api(value = "商城控制器", tags = {"商城内容管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/program/mall")
public class ProMallController extends BaseController {
    private final String PID = "13957782_210518982";
    private final String API_KEY = "607d259324479dcf";

    @Autowired
    private IBuyTogetherService togetherService;

    /**
     * 商城商品列表
     */
    @ApiOperation("查询商城内容列表")
    @GetMapping("/mallList")
    public Object mallList(MallQueryBo bo){
        if (StrUtil.isBlank(bo.getCpType())){
            return AjaxResult.error("参数错误");
        }
        // 赋值自己的key和推广PID
        bo.setApikey(API_KEY);
        bo.setPid(PID);

        switch (bo.getCpType()){
            case "pdd":
                return togetherService.buyTogetherList(bo);

            default:
        }
        return AjaxResult.error("查询错误");
    }

    /**
     * 商城商品详情
     */
    @ApiOperation("查询商城内容列表")
    @GetMapping("/mallDetail")
    public AjaxResult mallDetail(MallQueryBo bo){
        if (StrUtil.isBlank(bo.getCpType())){
            return AjaxResult.error("参数错误");
        }

        // 赋值自己的key和推广PID
        bo.setApikey(API_KEY);
        bo.setPid(PID);

        switch (bo.getCpType()){
            case "pdd":
                return togetherService.buyTogetherDetail(bo);

            default:
        }
        return AjaxResult.error("查询错误");
    }

    /**
     * 商城商品推广
     */
    @ApiOperation("查询商城内容列表")
    @GetMapping("/mallTurnChain")
    public AjaxResult mallTurnChain(MallQueryBo bo){
        if (StrUtil.isBlank(bo.getCpType())){
            return AjaxResult.error("参数错误");
        }

        // 赋值自己的key和推广PID
        bo.setApikey(API_KEY);
        bo.setPid(PID);

        switch (bo.getCpType()){
            case "pdd":
                return togetherService.buyTogetherTurnChain(bo);

            default:
        }
        return AjaxResult.error("推广错误");
    }
}
