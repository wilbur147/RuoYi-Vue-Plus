package com.ruoyi.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IVipshopService;
import com.ruoyi.coupon.utils.BeanToHttpUrl;
import com.ruoyi.coupon.vo.MallClassVo;
import com.ruoyi.coupon.vo.MallVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:17
 **/
@Service
public class VipshopServiceImpl implements IVipshopService {

	private final String API_URL = "http://api-gw.haojingke.com";

	@Override
	public AjaxResult wphList(MallQueryBo bo) {
		this.setSort(bo);
		this.setCateRe(bo);
		JSONObject params = JSONUtil.createObj()
			.putOnce("pageindex", bo.getPage())
			.putOnce("pagesize", bo.getPageSize())
			.putOnce("fieldName", bo.getSortType())
			.putOnce("order", bo.getSort())
			.putOnce("jxCode", bo.getCatIdRe())
			.putOnce("channelType", bo.getChannelType())
			.putOnce("sourceType", bo.getSourceType())
			.putOnce("keyword", bo.getSearchStr())
			.putOnce("apikey", bo.getApikey());
		String apiUrl = "goodslist";
		if (StrUtil.isNotBlank(bo.getSearchStr())) {
			apiUrl = "goodsquery";
		}
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/vip/" + apiUrl + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
			JSONArray jsonArray = JSONUtil.parseArray(resObj.get("goodsInfoList"));
			MallVo mallVo;
			String discount = null;
			JSONArray goodsList = JSONUtil.createArray();
			for (JSONObject data : jsonArray.jsonIter()) {
				discount = null;
				if (ObjectUtil.isNotEmpty(data.get("couponInfo"))) {
					JSONObject couponInfo = JSONUtil.parseObj(data.get("couponInfo"));
					if (ObjectUtil.isNotEmpty(couponInfo.get("buy"))) {
						discount = new BigDecimal(couponInfo.get("buy").toString()).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString();
					}
				}
				mallVo = new MallVo()
					.setGoodsId(data.get("goodsId").toString())
					.setGoodsName(data.get("goodsName").toString())
					.setImg(data.get("goodsThumbUrl").toString())
					.setPrice(new BigDecimal(data.get("marketPrice").toString()).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString())
					.setDiscount(discount)
					.setPriceAfter(new BigDecimal(data.get("vipPrice").toString()).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
				goodsList.add(mallVo);
			}
			return AjaxResult.success("获取成功", JSONUtil.createObj()
				.putOnce("total", resObj.get("total"))
				.putIfAbsent("goodsList", goodsList));
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult mallChannelList(MallQueryBo bo) {
		return AjaxResult.success();
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
					bo.setSortType(null);
					break;
				// 销量排序
				case 1:
					bo.setSortType(null);
					bo.setChannelType("1");
					bo.setSourceType("0");
					break;
				// 最新排序
				case 2:
					bo.setSortType("COMM_RATIO");
					break;
				// 价格升序排序
				case 3:
					bo.setSortType("PRICE");
					bo.setSort("0");
					break;
				// 价格降序排序
				case 4:
					bo.setSortType("PRICE");
					bo.setSort("1");
					break;
				default:
			}
		}
	}

	/**
	 * 自定义分类规则
	 *
	 * @param bo
	 */
	private void setCateRe(MallQueryBo bo) {
		if (Validator.isNotEmpty(bo.getCatId())) {
			if (0 != bo.getCatId()) {
				bo.setSourceType("1");
			}
			// 	精选组货码：当请求源类型为组货时必传
			// 女装精选 7hfpy0m4,男装精选 wj7evz2j,
			// 美妆精选 vd0wbfdx,数码电子 dpot8m5u,精选-首饰 szkl4kj7,鞋包精选 byh9331t,
			// 母婴精选 gkf52p8p,居家精选 cnrzcs22,运动户外精选 indvf44e,家用电器 uggxpyh5
			switch (bo.getCatId().intValue()) {
				// 推荐
				case 0:
					bo.setCatIdRe(null);
					break;
				case 1:
					bo.setCatIdRe("7hfpy0m4");
					break;
				case 2:
					bo.setCatIdRe("wj7evz2j");
					break;
				case 3:
					bo.setCatIdRe("vd0wbfdx");
					break;
				case 4:
					bo.setCatIdRe("dpot8m5u");
					break;
				case 5:
					bo.setCatIdRe("szkl4kj7");
					break;
				case 6:
					bo.setCatIdRe("byh9331t");
					break;
				case 7:
					bo.setCatIdRe("gkf52p8p");
					break;
				case 8:
					bo.setCatIdRe("cnrzcs22");
					break;
				case 9:
					bo.setCatIdRe("indvf44e");
					break;
				case 10:
					bo.setCatIdRe("uggxpyh5");
					break;
				default:
			}
		}
	}

	@Override
	public AjaxResult wphDetail(MallQueryBo bo) {
		JSONObject params = JSONUtil.createObj()
			.putOnce("goods_id", bo.getGoodsId())
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/vip/goodsdetail" + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONArray datas = JSONUtil.parseArray(obj.get("data"));
			JSONObject data = datas.getJSONObject(0);
			// 得到优惠券信息
			JSONObject couponInfo = JSONUtil.parseObj(data.get("couponInfo"));
			String discount = null;
			Long startTime = null, endTime = null;
			if (ObjectUtil.isNotEmpty(couponInfo)) {
				// 是最优的优惠券
				discount = new BigDecimal(couponInfo.get("buy").toString()).multiply(new BigDecimal(100)).toString();
				startTime = Long.parseLong(couponInfo.get("useBeginTime").toString());
				endTime = Long.parseLong(couponInfo.get("useEndTime").toString());
			}
			// 得到店铺信息
			String avgDesc = "低", avgServ = "低";
			JSONObject storeInfo = JSONUtil.parseObj(data.get("storeInfo"));
			JSONObject commentsInfo = JSONUtil.parseObj(data.get("commentsInfo"));
			if (ObjectUtil.isNotEmpty(commentsInfo) &&
				ObjectUtil.isNotEmpty(commentsInfo.get("goodCommentsShare"))) {
				double commentsShare = Double.parseDouble(commentsInfo.get("goodCommentsShare").toString());
				if (commentsShare > 40 && commentsShare < 70) {
					avgDesc = "中";
				} else if (commentsShare >= 70) {
					avgDesc = "高";
				}
			}
			JSONObject storeServiceCapability = JSONUtil.parseObj(data.get("storeServiceCapability"));
			if (ObjectUtil.isNotEmpty(storeServiceCapability) &&
				ObjectUtil.isNotEmpty(storeServiceCapability.get("storeScore"))) {
				double storeScore = Double.parseDouble(storeServiceCapability.get("storeScore").toString());
				if (storeScore > 40 && storeScore < 70) {
					avgServ = "中";
				} else if (storeScore >= 70) {
					avgServ = "高";
				}
			}
			// 描述
			String desc = null;
			if (ObjectUtil.isNotEmpty(data.get("goodsDesc"))) {
				desc = data.get("goodsDesc").toString();
			}
			MallVo mallVo = new MallVo()
				.setGoodsId(data.get("goodsId").toString())
				.setGoodsName(data.get("goodsName").toString())
				.setGoodsDesc(desc)
				.setImg(data.get("goodsMainPicture").toString())
				.setImges(data.get("goodsDetailPictures"))
				.setPrice(new BigDecimal(data.get("marketPrice").toString()).multiply(new BigDecimal(100)).toString())
				.setDiscount(discount)
				.setPriceAfter(new BigDecimal(data.get("vipPrice").toString()).multiply(new BigDecimal(100)).toString())
				.setCouponStartTime(startTime)
				.setCouponEndTime(endTime)
				.setShopName(storeInfo.get("storeName").toString())
				.setAvgDesc(avgDesc)
				.setAvgLgst("高")
				.setAvgServ(avgServ);
			return AjaxResult.success(mallVo);
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult wphTurnChain(MallQueryBo bo) {
		JSONObject params = JSONUtil.createObj()
			.putOnce("goods_id", bo.getGoodsId())
			.putOnce("type", 1)
			.putOnce("chanTag", bo.getPid())
			.putOnce("apikey", bo.getApikey());
		String result = HttpUtil.get(API_URL + "/index.php/v1/api/vip/getunionurl" + BeanToHttpUrl.convertToUrl(params));
		JSONObject obj = JSONUtil.parseObj(result);
		if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject data = JSONUtil.parseObj(obj.get("data"));
			JSONArray infoList = JSONUtil.parseArray(data.get("urlInfoList"));
			if (CollUtil.isNotEmpty(infoList)) {
				JSONObject info = infoList.getJSONObject(0);
				if (ObjectUtil.isNotEmpty(info)) {
					String url = info.get("vipWxUrl").toString();
					String appId = info.get("appId").toString();
					return AjaxResult.success(new MallVo().setWeChatInfo(JSONUtil.createObj().putOnce("app_id", appId)
						.putOnce("page_path", url)));
				}
			}
		}
		return AjaxResult.error();
	}

	@Override
	public AjaxResult wphClass(MallQueryBo bo) {
		// 	精选组货码：当请求源类型为组货时必传
		// 女装精选 1,男装精选 2,
		// 美妆精选 3,数码电子 4,精选-首饰 5,鞋包精选 6,
		// 母婴精选 7,居家精选 8,运动户外精选 9,家用电器 10
		String[] optArr = new String[]{"女装", "男装", "美妆", "数码电子", "首饰", "鞋包", "母婴", "居家", "运动户外", "家用电器"};
		JSONArray catList = JSONUtil.createArray();
		for (int i = 0; i < 10; i++) {
			catList.add(new MallClassVo()
				.setCatId(String.valueOf(i + 1))
				.setName(optArr[i]));
		}
		return AjaxResult.success(catList);
	}

	@Override
	public AjaxResult mallSecondIcon(MallQueryBo bo) {
		return AjaxResult.success();
	}
}
