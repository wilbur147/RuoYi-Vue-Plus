package com.ruoyi.coupon.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import com.ruoyi.coupon.utils.BeanToHttpUrl;
import com.ruoyi.coupon.vo.MallClassVo;
import com.ruoyi.coupon.vo.MallVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:17
 **/
@Service
public class BuyTogetherServiceImpl implements IBuyTogetherService {

	private final String API_URL = "http://api-gw.haojingke.com";
	private final String clientId = "7ab7663101354c5bba76aed3fd2ea310";
	private final String clientSecret = "950208c4ea788f527271a274cf04526ddbf58b7d";
	private String accessToken = "your accessToken";
	PopClient client = new PopHttpClient(clientId, clientSecret);

	@Override
	public AjaxResult buyTogetherList(MallQueryBo bo) {
		this.setSort(bo);
		JSONObject params = JSONUtil.createObj()
			.putOnce("isunion", "1")
			.putOnce("page", bo.getPage())
			.putOnce("page_size", bo.getPageSize())
			.putOnce("sort_type", bo.getSortType())
			.putOnce("opt_id", bo.getCatId())
			.putOnce("with_coupon", bo.getWithCoupon())
			.putOnce("keyword", bo.getSearchStr())
			.putOnce("pid", bo.getPid())
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/pdd/goodslist" + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
			JSONArray jsonArray = JSONUtil.parseArray(resObj.get("goods_list"));
			MallVo mallVo;
			JSONArray goodsList = JSONUtil.createArray();
			for (JSONObject data : jsonArray.jsonIter()) {
				// 原价
				String price = data.get("min_normal_price").toString();
				// 拼购价格
				String pricePg = data.get("min_group_price").toString();
				String discount = data.get("coupon_discount").toString();
				// 计算券后价
				BigDecimal priceAfter = new BigDecimal(pricePg)
					.subtract(new BigDecimal(discount));
				mallVo = new MallVo()
					.setGoodsId(data.get("goods_sign").toString())
					.setGoodsName(data.get("goods_name").toString())
					.setImg(data.get("goods_thumbnail_url").toString())
					.setPrice(pricePg)
					.setDiscount(discount)
					.setPriceAfter(priceAfter.toString())
					.setSales(data.get("sales_tip").toString());
				goodsList.add(mallVo);
			}
			return AjaxResult.success("获取成功", JSONUtil.createObj()
				.putOnce("searchId", resObj.get("search_id"))
				.putOnce("total", resObj.get("total_count"))
				.putIfAbsent("goodsList", goodsList));
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult mallChannelList(MallQueryBo bo) {
		this.setSort(bo);
		this.setChannelType(bo);
		PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
		if (0 != Integer.parseInt(bo.getChannelType())) {
			List<Integer> activityTags = new ArrayList<Integer>();
			activityTags.add(Integer.parseInt(bo.getChannelType()));
			request.setActivityTags(activityTags);
		}
		request.setPage(bo.getPage());
		request.setPageSize(bo.getPageSize());
		if (ObjectUtil.isNotEmpty(bo.getRangeItem())) {
			// 大额优惠券
			List<PddDdkGoodsSearchRequest.RangeListItem> rangeList = new ArrayList<PddDdkGoodsSearchRequest.RangeListItem>();
			rangeList.add(bo.getRangeItem());
			request.setRangeList(rangeList);
		}

		request.setSortType(StrUtil.isNotBlank(bo.getSortType()) ? Integer.parseInt(bo.getSortType()) : null);
		if (StrUtil.isNotBlank(bo.getWithCoupon())) {
			request.setWithCoupon("1".equals(bo.getWithCoupon()) ? true : false);
		}
		PddDdkGoodsSearchResponse response = null;
		try {
			response = client.syncInvoke(request);
			JSONObject obj = JSONUtil.parseObj(JsonUtil.transferToJson(response));
			if (ObjectUtil.isNotEmpty(obj.get("goods_search_response"))) {
				JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("goods_search_response")));
				JSONArray jsonArray = JSONUtil.parseArray(resObj.get("goods_list"));
				MallVo mallVo;
				JSONArray goodsList = JSONUtil.createArray();
				for (JSONObject data : jsonArray.jsonIter()) {
					// 原价
					String price = data.get("min_normal_price").toString();
					// 拼购价格
					String pricePg = data.get("min_group_price").toString();
					String discount = data.get("coupon_discount").toString();
					// 计算券后价
					BigDecimal priceAfter = new BigDecimal(pricePg)
						.subtract(new BigDecimal(discount));
					mallVo = new MallVo()
						.setGoodsId(data.get("goods_sign").toString())
						.setGoodsName(data.get("goods_name").toString())
						.setImg(data.get("goods_thumbnail_url").toString())
						.setPrice(pricePg)
						.setDiscount(discount)
						.setPriceAfter(priceAfter.toString())
						.setSales(data.get("sales_tip").toString());
					goodsList.add(mallVo);
				}
				return AjaxResult.success("获取成功", JSONUtil.createObj()
					.putOnce("searchId", resObj.get("search_id"))
					.putOnce("total", resObj.get("total_count"))
					.putIfAbsent("goodsList", goodsList));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error();
	}

	/**
	 * 自定义排序规则
	 *
	 * @param bo
	 */
	private void setSort(MallQueryBo bo) {
		if (Validator.isNotEmpty(bo.getSortType())) {
			switch (Integer.parseInt(bo.getSortType())) {
				// 综合排序
				case 0:

					break;
				// 销量排序
				case 1:
					bo.setSortType("6");
					break;
				// 最新排序
				case 2:
					bo.setSortType("11");
					break;
				// 价格升序排序
				case 3:
					bo.setSortType("9");
					break;
				// 价格降序排序
				case 4:
					bo.setSortType("10");
					break;
				default:
			}

			// 是否过滤无券
			if (StrUtil.isNotBlank(bo.getWithCoupon())) {
				if ("1".equals(bo.getWithCoupon())) {
					// 过滤

				} else {
					// 不过滤

				}
			}
		}
	}

	/**
	 * 自定义排序规则
	 *
	 * @param bo
	 */
	private void setChannelType(MallQueryBo bo) {
		if (Validator.isNotEmpty(bo.getChannelType())) {
			switch (Integer.parseInt(bo.getChannelType())) {
				// 不操作
				case 0:

					break;
				// 9.9商品新人特惠
				case 1:
					PddDdkGoodsSearchRequest.RangeListItem specialOffer = new PddDdkGoodsSearchRequest.RangeListItem();
					specialOffer.setRangeFrom(0L);
					specialOffer.setRangeId(1);
					specialOffer.setRangeTo(1000L);
					bo.setRangeItem(specialOffer);
					bo.setChannelType("0");
					break;
				// 精选爆品
				case 2:
					PddDdkGoodsSearchRequest.RangeListItem hotBuy = new PddDdkGoodsSearchRequest.RangeListItem();
					hotBuy.setRangeFrom(50000L);
					hotBuy.setRangeId(5);
					bo.setRangeItem(hotBuy);
					bo.setChannelType("0");
					break;
				// 今日热卖
				case 3:
					PddDdkGoodsSearchRequest.RangeListItem todayHotBuy = new PddDdkGoodsSearchRequest.RangeListItem();
					todayHotBuy.setRangeFrom(10000L);
					todayHotBuy.setRangeId(5);
					bo.setRangeItem(todayHotBuy);
					bo.setChannelType("0");
					break;
				// 百亿补贴
				case 4:
					PddDdkGoodsSearchRequest.RangeListItem subsidy = new PddDdkGoodsSearchRequest.RangeListItem();
					subsidy.setRangeFrom(1000L);
					subsidy.setRangeId(3);
					subsidy.setRangeTo(9999L);
					bo.setRangeItem(subsidy);
					bo.setChannelType("0");
					break;
				// 品牌高佣
				case 5:
					PddDdkGoodsSearchRequest.RangeListItem highCommission = new PddDdkGoodsSearchRequest.RangeListItem();
					highCommission.setRangeFrom(10L);
					highCommission.setRangeId(2);
					highCommission.setRangeTo(999L);
					bo.setRangeItem(highCommission);
					bo.setChannelType("0");
					break;
				// 秒杀
				case 6:
					PddDdkGoodsSearchRequest.RangeListItem spike = new PddDdkGoodsSearchRequest.RangeListItem();
					spike.setRangeFrom(0L);
					spike.setRangeId(1);
					spike.setRangeTo(100L);
					bo.setRangeItem(spike);
					bo.setChannelType("0");
					break;
				// 大额优惠券
				case 7:
					PddDdkGoodsSearchRequest.RangeListItem bigCoupon = new PddDdkGoodsSearchRequest.RangeListItem();
					bigCoupon.setRangeFrom(1500L);
					bigCoupon.setRangeId(3);
					bigCoupon.setRangeTo(9999L);
					bo.setRangeItem(bigCoupon);
					bo.setChannelType("0");
					bo.setWithCoupon("1");
					break;
				default:
			}
		}
	}

	@Override
	public AjaxResult buyTogetherDetail(MallQueryBo bo) {
		JSONObject params = JSONUtil.createObj()
			.putOnce("isunion", "1")
			.putOnce("goods_sign", bo.getGoodsId())
			.putOnce("search_id", bo.getSearchId())
			.putOnce("pid", bo.getPid())
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/pdd/goodsdetail" + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject data = JSONUtil.parseObj(obj.get("data"));
			// 原价
			String price = data.get("min_normal_price").toString();
			// 拼购价格
			String pricePg = data.get("min_group_price").toString();
			String discount = data.get("coupon_discount").toString();
			// 计算券后价
			BigDecimal priceAfter = new BigDecimal(pricePg)
				.subtract(new BigDecimal(discount));
			// 得到图集
			MallVo mallVo = new MallVo()
				.setGoodsId(data.get("goods_sign").toString())
				.setGoodsName(data.get("goods_name").toString())
				.setGoodsDesc(data.get("goods_desc").toString())
				.setImg(data.get("goods_image_url").toString())
				.setImges(data.get("goods_gallery_urls"))
				.setPrice(pricePg)
				.setDiscount(discount)
				.setPriceAfter(priceAfter.toString())
				.setSales(data.get("sales_tip").toString())
				.setCouponStartTime(Long.parseLong(data.get("coupon_start_time").toString()))
				.setCouponEndTime(Long.parseLong(data.get("coupon_end_time").toString()))
				.setShopName(data.get("mall_name").toString())
				.setAvgDesc(data.get("desc_txt").toString())
				.setAvgLgst(data.get("lgst_txt").toString())
				.setAvgServ(data.get("serv_txt").toString());
			return AjaxResult.success(mallVo);
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult buyTogetherTurnChain(MallQueryBo bo) {
		JSONObject params = JSONUtil.createObj()
			.putOnce("goods_sign", bo.getGoodsId())
			.putOnce("search_id", bo.getSearchId())
			.putOnce("pid", bo.getPid())
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/pdd/getunionurl" + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject data = JSONUtil.parseObj(obj.get("data"));
			JSONObject allData = JSONUtil.parseObj(data.get("alldata"));
			return AjaxResult.success(new MallVo().setWeChatInfo(allData.get("we_app_info")));
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult buyTogetherClass(MallQueryBo bo) {
		JSONObject params = JSONUtil.createObj()
			.putOnce("parent_opt_id", 0)
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/pdd/opt" + BeanToHttpUrl.convertToUrl(params));
		String[] optArr = new String[]{"女装", "男装", "水果", "手机", "百货", "母婴", "医药", "美妆", "鞋包", "电器", "运动", "电脑", "内衣", "家纺", "食品", "家具",
			"家装", "车品", "饰品", "玩乐", "淘淘"};
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
			JSONArray optList = JSONUtil.parseArray(resObj.get("goods_opt_list"));
			MallClassVo mallClassVo;
			JSONArray catList = JSONUtil.createArray();
			for (String optName : optArr) {
				for (JSONObject opt : optList.jsonIter()) {
					if (optName.equals(opt.get("opt_name").toString())) {
						mallClassVo = new MallClassVo()
							.setCatId(opt.get("opt_id").toString())
							.setName(opt.get("opt_name").toString());
						catList.add(mallClassVo);
						break;
					}
				}
			}
			return AjaxResult.success(catList);
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult mallSecondIcon(MallQueryBo bo) {
		JSONArray features = JSONUtil.createArray();
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1erfuhqgr17le78q8bo15k611sf1c.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "9.9包邮")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "1")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1erfufep7gp1fff18r18bumiu12.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "火爆热卖")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "2")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1erfuikklq651rp5mov7641nic1h.png")
			.putOnce("toType", 1)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "今日热销")
				.putOnce("jumpType", "pdd")
				.putOnce("page_path", "../shop-list/shop-list")
				.putOnce("channelType", "3")
			)
		);
		features.add(JSONUtil.createObj()
			.putOnce("src", "../../static/shop/o_1erfuj91s5k312akhrb1tt91gei1m.png")
			.putOnce("toType", 2)
			.putOnce("toPath", JSONUtil.createObj()
				.putOnce("title", "百亿补贴")
				.putOnce("app_id", "wxa918198f16869201")
				.putOnce("page_path", "/pages/web/web?specialUrl=1&src=https%3A%2F%2Fmobile.yangkeduo.com%2Fduo_transfer_channel.html%3FresourceType%3D39997%26pid%3D13957782_210518982%26authDuoId%3D200005%26cpsSign%3DCE_210611_13957782_210518982_e238ad1ef824d1380c5be9823e0276be%26duoduo_type%3D2")
			)
		);
		return AjaxResult.success(JSONUtil.createObj()
			.putOnce("features", features));
	}
}
