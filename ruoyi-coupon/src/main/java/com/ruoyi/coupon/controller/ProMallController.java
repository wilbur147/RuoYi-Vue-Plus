package com.ruoyi.coupon.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import com.ruoyi.coupon.service.IJingDongService;
import com.ruoyi.coupon.service.IVipshopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/program/mall")
public class ProMallController extends BaseController {
	private final String PDD_PID = "13957782_210518982";
	private final String JD_PID = "3003723200";
	private final String WPH_PID = "8df750b7fed3ca716d4e8f56e4e11b8b";
	private final String API_KEY = "607d259324479dcf";
	@Autowired
	private IBuyTogetherService togetherService;
	@Autowired
	private IJingDongService jingDongService;
	@Autowired
	private IVipshopService vipshopService;

	/**
	 * 商城商品列表
	 */
	@ApiOperation("查询商城首页")
	@GetMapping("/mallIndex")
	public AjaxResult mallIndex() {
		// toType：0 不作任何动作  1 跳转商品列表  2 跳转其它小程序

		// 商城首页第一层广告图
		JSONObject adOne = JSONUtil.createObj();
		adOne.putOnce("src", "../../static/shop/o_1f73id996g9mfok1bc4tsg51312.png")
			.putOnce("item", JSONUtil.createObj()
				.putOnce("toType", 0));
		// 商城首页多栏功能列表
		JSONArray features = JSONUtil.createArray();
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f55hj3cc84e17vh4gq1em71ahn1c.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "拼多多")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-second-page/shop-second-page")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f55hjjre1snkf5e77s18us1hde1h.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "京东")
				.putOnce("jumpType", "jd")
				.putOnce("page_path", "../shop-second-page/shop-second-page")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f55hjtp535q4b2br6cni18do1m.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "唯品会")
				.putOnce("jumpType", "wph")
				.putOnce("page_path", "../shop-second-page/shop-second-page")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f55hk4bciso1fbl93h6ka17v1r.png")
			.putOnce("toType", 2)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("app_id", "wxa918198f16869201")
				.putOnce("page_path", "/pages/web/web?specialUrl=1&src=https%3A%2F%2Fmobile.yangkeduo.com%2Fduo_transfer_channel.html%3FresourceType%3D39997%26pid%3D13957782_210518982%26authDuoId%3D200005%26cpsSign%3DCE_210611_13957782_210518982_e238ad1ef824d1380c5be9823e0276be%26duoduo_type%3D2")
			)
		);
		// 商城首页第二层广告图
		JSONObject adSecond = JSONUtil.createObj();
		adSecond.putOnce("src", "../../static/shop/o_1f5il8ed71bs913pta30ttakp612.gif")
			.putOnce("item", JSONUtil.createObj()
				.putOnce("toType", 2).putOnce("toPath", JSONUtil.createObj()
					.putOnce("app_id", "wxa918198f16869201")
					.putOnce("page_path", "/pages/web/web?specialUrl=1&src=https%3A%2F%2Fmobile.yangkeduo.com%2Fduo_transfer_channel.html%3FresourceType%3D39997%26pid%3D13957782_210518982%26authDuoId%3D200005%26cpsSign%3DCE_210611_13957782_210518982_e238ad1ef824d1380c5be9823e0276be%26duoduo_type%3D2")));

		// 广告大图三张
		JSONArray bigAdImges = JSONUtil.createArray();
		bigAdImges.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f2j4g85brqh8cl1jg916n2ung17.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "新人特惠")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "1")
			)
		);
		bigAdImges.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f2j4gkvf1uuu14f01c9uids81n1c.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "今日爆款")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "3")
			)
		);
		bigAdImges.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1f2j4h3upn5b158ludf16bhqmp1h.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "大额优惠")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "7")
			)
		);
		return AjaxResult.success(JSONUtil.createObj()
			.putOnce("adOne", adOne)
			.putOnce("features", features)
			.putOnce("adSecond", adSecond)
			.putOnce("bigAdImges", bigAdImges));
	}

	/**
	 * 商城商品列表
	 */
	@ApiOperation("查询二级商城图标")
	@GetMapping("/mallSecondIcon")
	public AjaxResult mallSecondIcon(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		switch (bo.getCpType()) {
			case "pdd":
				return togetherService.mallSecondIcon(bo);
			case "jd":
				return jingDongService.mallSecondIcon(bo);
			case "wph":
				return vipshopService.mallSecondIcon(bo);
			default:
		}
		return AjaxResult.error("查询错误");
	}


	/**
	 * 商品分类
	 */
	@ApiOperation("商品分类")
	@GetMapping("/mallClass")
	public AjaxResult mallClass(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		// 赋值自己的key
		bo.setApikey(API_KEY);
		switch (bo.getCpType()) {
			case "pdd":
				bo.setPid(PDD_PID);
				return togetherService.buyTogetherClass(bo);
			case "jd":
				return jingDongService.jdClass(bo);
			case "wph":
				return vipshopService.wphClass(bo);
			default:
		}
		return AjaxResult.error("查询错误");
	}

	/**
	 * 商城商品列表
	 */
	@ApiOperation("查询商城内容列表")
	@GetMapping("/mallList")
	public Object mallList(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		// 赋值自己的key和推广PID
		bo.setApikey(API_KEY);
		switch (bo.getCpType()) {
			case "pdd":
				bo.setPid(PDD_PID);
				return togetherService.buyTogetherList(bo);
			case "jd":
				return jingDongService.jdList(bo);
			case "wph":
				return vipshopService.wphList(bo);
			default:
		}
		return AjaxResult.error("查询错误");
	}

	/**
	 * 商城商品列表
	 */
	@ApiOperation("查询商城频道内容列表")
	@GetMapping("/mallChannelList")
	public Object mallChannelList(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		// 赋值自己的key和推广PID
		bo.setApikey(API_KEY);
		switch (bo.getCpType()) {
			case "pdd":
				bo.setPid(PDD_PID);
				return togetherService.mallChannelList(bo);
			case "jd":
				return jingDongService.mallChannelList(bo);
			case "wph":
				return vipshopService.mallChannelList(bo);
			default:
		}
		return AjaxResult.error("查询错误");
	}


	/**
	 * 商城商品详情
	 */
	@ApiOperation("查询商城内容列表")
	@GetMapping("/mallDetail")
	public AjaxResult mallDetail(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		// 赋值自己的key和推广PID
		bo.setApikey(API_KEY);
		switch (bo.getCpType()) {
			case "pdd":
				bo.setPid(PDD_PID);
				return togetherService.buyTogetherDetail(bo);
			case "jd":
				return jingDongService.jdDetail(bo);
			case "wph":
				return vipshopService.wphDetail(bo);
			default:
		}
		return AjaxResult.error("查询错误");
	}

	/**
	 * 商城商品推广
	 */
	@ApiOperation("商城商品推广")
	@GetMapping("/mallTurnChain")
	public AjaxResult mallTurnChain(MallQueryBo bo) {
		if (StrUtil.isBlank(bo.getCpType())) {
			return AjaxResult.error("参数错误");
		}
		// 赋值自己的key和推广PID
		bo.setApikey(API_KEY);
		switch (bo.getCpType()) {
			case "pdd":
				bo.setPid(PDD_PID);
				return togetherService.buyTogetherTurnChain(bo);
			case "jd":
				bo.setPid(JD_PID);
				return jingDongService.jdTurnChain(bo);
			case "wph":
				bo.setPid(WPH_PID);
				return vipshopService.wphTurnChain(bo);
			default:
		}
		return AjaxResult.error("推广错误");
	}


	/**
	 * 商品搜索热词推送
	 */
	@ApiOperation("商品搜索热词推送")
	@GetMapping("/mallHotKeywords")
	public AjaxResult mallHotKeywords() {
		String[] keyWords = new String[]{"粽子", "休闲时尚T恤", "居家必备洗衣机", "撸串聚会绝配啤酒", "仙女必入超显白口红",
			"智能空调", "夏季必备小电扇", "办公家具必买清单"};
		return AjaxResult.success("获取关键词成功", Arrays.asList(keyWords));
	}


}
