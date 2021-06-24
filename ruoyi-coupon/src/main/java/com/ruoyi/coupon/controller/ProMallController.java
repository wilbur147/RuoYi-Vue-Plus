package com.ruoyi.coupon.controller;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
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
	private final String JD_PID = "30018";
	private final String WPH_PID = "8df750b7fed3ca716d4e8f56e4e11b8b";
	private final String API_KEY = "607d259324479dcf";
	@Autowired
	private IBuyTogetherService togetherService;
	@Autowired
	private IJingDongService jingDongService;
	@Autowired
	private IVipshopService vipshopService;
	@Autowired
	private RedisCache redisCache;

	/**
	 * 商城商品列表
	 */
	@ApiOperation("查询商城首页")
	@GetMapping("/mallIndex")
	public AjaxResult mallIndex() {
		// toType：0 不作任何动作  1 跳转商品列表  2 跳转其它小程序
		JSONObject adOne,adSecond = null;
		JSONArray features,bigAdImges = null;
		String adOneKey = "coupon:shop:index:adOne";
		String featuresKey = "coupon:shop:index:features";
		String adSecondKey = "coupon:shop:index:adSecond";
		String bigAdImgesKey = "coupon:shop:index:bigAdImges";
		try {
			// 商城首页第一层广告图
			adOne = JSONUtil.parseObj(redisCache.getCacheObject(adOneKey).toString());
			// 商城首页多栏功能列表
			features = JSONUtil.parseArray(redisCache.getCacheObject(featuresKey).toString());
			// 商城首页第二层广告图
			adSecond = JSONUtil.parseObj(redisCache.getCacheObject(adSecondKey).toString());
			//	广告大图三张
			bigAdImges = JSONUtil.parseArray(redisCache.getCacheObject(bigAdImgesKey).toString());
			JSONObject result = JSONUtil.createObj()
				.putOnce("adOne", adOne)
				.putOnce("features", features)
				.putOnce("adSecond", adSecond)
				.putOnce("bigAdImges", bigAdImges);
			FileWriter writer = new FileWriter("shop-index-icon.json");
			writer.write(JSONUtil.toJsonStr(result));
			return AjaxResult.success(result);
		}catch (Exception e){
			JSONObject data = JSONUtil.parseObj(ResourceUtil.readUtf8Str("shop-index-icon.json"));
			adOne = data.getJSONObject("adOne");
			features = data.getJSONArray("features");
			adSecond = data.getJSONObject("adSecond");
			bigAdImges = data.getJSONArray("bigAdImges");
			redisCache.setCacheObject(adOneKey,adOne);
			redisCache.setCacheObject(featuresKey,features);
			redisCache.setCacheObject(adSecondKey,adSecond);
			redisCache.setCacheObject(bigAdImgesKey,bigAdImges);
		}
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
//		String[] keyWords = new String[]{"口罩医用", "洁柔", "休闲时尚T恤", "居家必备洗衣机", "撸串聚会绝配啤酒", "仙女必入超显白口红",
//			"智能空调", "夏季必备小电扇", "办公家具必买清单"};
		String classFilePath = "shop-search-keywords.json";
		String keyWordsKey = "coupon:shop:search:keywords";
		try {
			JSONArray objects = JSONUtil.parseArray(redisCache.getCacheObject(keyWordsKey).toString());
			JSONObject result = JSONUtil.createObj()
				.putOnce("keyWords", objects);
			FileWriter writer = new FileWriter(classFilePath);
			writer.write(JSONUtil.toJsonStr(result));
			return AjaxResult.success(objects);
		}catch (Exception e){
			JSONObject data = JSONUtil.parseObj(ResourceUtil.readUtf8Str(classFilePath));
			JSONArray keyWords = data.getJSONArray("keyWords");
			redisCache.setCacheObject(keyWordsKey, keyWords);
			return AjaxResult.success("获取关键词成功", keyWords);
		}
	}


}
