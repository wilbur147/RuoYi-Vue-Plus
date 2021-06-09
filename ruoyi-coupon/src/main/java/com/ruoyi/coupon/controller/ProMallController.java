package com.ruoyi.coupon.controller;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import com.ruoyi.coupon.service.IJingDongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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
    private final String PDD_PID = "13957782_210518982";
    private final String JD_PID = "3003723200";
    private final String API_KEY = "607d259324479dcf";

    @Autowired
    private IBuyTogetherService togetherService;
    @Autowired
    private IJingDongService jingDongService;

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
        switch (bo.getCpType()){
            case "pdd":
				bo.setPid(PDD_PID);
                return togetherService.buyTogetherList(bo);
			case "jd":
                return jingDongService.jdList(bo);
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
        switch (bo.getCpType()){
            case "pdd":
				bo.setPid(PDD_PID);
                return togetherService.buyTogetherDetail(bo);
			case "jd":
				return jingDongService.jdDetail(bo);
            default:
        }
        return AjaxResult.error("查询错误");
    }

    /**
     * 商城商品推广
     */
    @ApiOperation("商城商品推广")
    @GetMapping("/mallTurnChain")
    public AjaxResult mallTurnChain(MallQueryBo bo){
        if (StrUtil.isBlank(bo.getCpType())){
            return AjaxResult.error("参数错误");
        }
        // 赋值自己的key和推广PID
        bo.setApikey(API_KEY);
        switch (bo.getCpType()){
            case "pdd":
				bo.setPid(PDD_PID);
                return togetherService.buyTogetherTurnChain(bo);
			case "jd":
				bo.setPid(JD_PID);
				return jingDongService.jdTurnChain(bo);
            default:
        }
        return AjaxResult.error("推广错误");
    }


    /**
     * 商品搜索热词推送
     */
    @ApiOperation("商品搜索热词推送")
    @GetMapping("/mallHotKeywords")
    public AjaxResult mallHotKeywords(){
        String [] keyWords = new String[]{"粽子","休闲时尚T恤","居家必备洗衣机","撸串聚会绝配啤酒","仙女必入超显白口红",
                "智能空调","夏季必备小电扇","办公家具必买清单"};
        return AjaxResult.success("获取关键词成功", Arrays.asList(keyWords));
    }
}
