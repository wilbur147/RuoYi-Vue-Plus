package com.ruoyi.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IJingDongService;
import com.ruoyi.coupon.utils.BeanToHttpUrl;
import com.ruoyi.coupon.vo.MallVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:17
 **/
@Service
public class JingDongServiceImpl implements IJingDongService {

    private final String API_URL = "http://api-gw.haojingke.com";

    @Override
    public AjaxResult jdList(MallQueryBo bo) {
        JSONObject params = JSONUtil.createObj()
                .putOnce("pageindex", bo.getPage())
                .putOnce("pagesize", bo.getPageSize())
                .putOnce("sortname", bo.getSortType())
                .putOnce("sort", bo.getSort())
                .putOnce("iscoupon", bo.getWithCoupon())
                .putOnce("keyword", bo.getSearchStr())
                .putOnce("apikey", bo.getApikey());
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/jd/goodslist"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
            JSONArray jsonArray = JSONUtil.parseArray(resObj.get("data"));
            MallVo mallVo;
            JSONArray goodsList = JSONUtil.createArray();
            for (JSONObject data : jsonArray.jsonIter()) {
                mallVo = new MallVo()
                        .setGoodsId(data.get("goods_id").toString())
                        .setGoodsName(data.get("goods_name").toString())
                        .setImg(data.get("picurl").toString())
                        .setPrice(new BigDecimal(data.get("price").toString()).multiply(new BigDecimal(100)).toString())
                        .setDiscount(new BigDecimal(data.get("discount").toString()).multiply(new BigDecimal(100)).toString())
                        .setPriceAfter(new BigDecimal(data.get("price_after").toString()).multiply(new BigDecimal(100)).toString())
                        .setSales(data.get("sales").toString());
                goodsList.add(mallVo);
            }
            return AjaxResult.success("获取成功",JSONUtil.createObj()
				.putOnce("total", resObj.get("total"))
                    .putIfAbsent("goodsList",goodsList));
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult jdDetail(MallQueryBo bo) {
        JSONObject params = JSONUtil.createObj()
                .putOnce("isunion", "1")
                .putOnce("goods_id", bo.getGoodsId())
                .putOnce("apikey", bo.getApikey());
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/jd/goodsdetail"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            // 得到封面图及图集
			JSONObject imageInfo = JSONUtil.parseObj(data.get("imageInfo"));
			JSONArray imageList = JSONUtil.parseArray(imageInfo.get("imageList"));
			String img = null;
			ArrayList<String> imges = CollUtil.newArrayList();
			int i = 0;
			for (JSONObject imgObj : imageList.jsonIter()) {
				String url = imgObj.get("url").toString();
				if (i == 0){
					img = url;
				}
				imges.add(url);
				i++;
			}
			// 得到描述
			JSONObject documentInfo = JSONUtil.parseObj(data.get("documentInfo"));
			String document = null;
			if (ObjectUtil.isNotEmpty(documentInfo)){
				document = documentInfo.get("document").toString();
			}
			// 得到价格
			JSONObject priceInfo = JSONUtil.parseObj(data.get("priceInfo"));
			// 得到优惠券信息
			JSONObject couponInfo = JSONUtil.parseObj(data.get("couponInfo"));
			String discount = null,couponUrl = null;
			Long startTime = null,endTime = null;
			if (ObjectUtil.isNotEmpty(couponInfo)){
				JSONArray couponList = JSONUtil.parseArray(couponInfo.get("couponList"));
				for (JSONObject coupon : couponList.jsonIter()) {
					if (1 == Integer.parseInt(coupon.get("isBest").toString())){
						// 是最优的优惠券
						discount = new BigDecimal(coupon.get("discount").toString()).multiply(new BigDecimal(100)).toString();
						startTime = Long.parseLong(coupon.get("useStartTime").toString());
						endTime = Long.parseLong(coupon.get("useEndTime").toString());
						couponUrl = coupon.get("link").toString();
						break;
					}
				}
			}
			// 得到店铺信息
			String avgDesc = "低",avgLgst = "低",avgServ = "低";
			JSONObject shopInfo = JSONUtil.parseObj(data.get("shopInfo"));
			if (ObjectUtil.isNotEmpty(shopInfo.get("commentFactorScoreRankGrade"))){
				avgDesc = shopInfo.get("commentFactorScoreRankGrade").toString();
			}
			if (ObjectUtil.isNotEmpty(shopInfo.get("logisticsFactorScoreRankGrade"))){
				avgLgst = shopInfo.get("logisticsFactorScoreRankGrade").toString();
			}
			if (ObjectUtil.isNotEmpty(shopInfo.get("afsFactorScoreRankGrade"))){
				avgServ = shopInfo.get("afsFactorScoreRankGrade").toString();
			}
			MallVo mallVo = new MallVo()
				.setGoodsId(data.get("skuId").toString())
				.setGoodsName(data.get("skuName").toString())
				.setGoodsDesc(document)
				.setImg(img)
				.setImges(imges)
				.setPrice(new BigDecimal(priceInfo.get("price").toString()).multiply(new BigDecimal(100)).toString())
				.setDiscount(discount)
				.setPriceAfter(new BigDecimal(priceInfo.get("lowestCouponPrice").toString()).multiply(new BigDecimal(100)).toString())
				.setSales(data.get("inOrderCount30Days").toString())
				.setCouponStartTime(startTime)
				.setCouponEndTime(endTime)
				.setCouponUrl(couponUrl)
				.setShopName(shopInfo.get("shopName").toString())
				.setAvgDesc(avgDesc)
				.setAvgLgst(avgLgst)
				.setAvgServ(avgServ);
            return AjaxResult.success(mallVo);
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult jdTurnChain(MallQueryBo bo) {
        JSONObject params = JSONUtil.createObj()
                .putOnce("goods_id", bo.getGoodsId())
                .putOnce("type", 1)
                .putOnce("positionid", bo.getPid())
                .putOnce("couponurl", bo.getCouponUrl())
                .putOnce("apikey", bo.getApikey());
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/jd/getunionurl"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			String url = obj.get("data").toString();
			return AjaxResult.success(new MallVo().setWeChatInfo(JSONUtil.createObj().putOnce("app_id","wx91d27dbf599dff74")
				.putOnce("page_path","pages/union/proxy/proxy?spreadUrl="+ URLUtil.encode(url))));
        }
        return AjaxResult.error();
    }
}
