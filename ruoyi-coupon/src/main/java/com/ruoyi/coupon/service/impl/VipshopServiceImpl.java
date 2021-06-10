package com.ruoyi.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
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
        JSONObject params = JSONUtil.createObj()
                .putOnce("pageindex", bo.getPage())
                .putOnce("pagesize", bo.getPageSize())
                .putOnce("fieldName", bo.getSortType())
                .putOnce("order", bo.getSort())
                .putOnce("keyword", bo.getSearchStr())
                .putOnce("apikey", bo.getApikey());
        String apiUrl = "goodslist";
        if (StrUtil.isNotBlank(bo.getSearchStr())){
			apiUrl = "goodsquery";
		}
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/vip/"+apiUrl+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
            JSONArray jsonArray = JSONUtil.parseArray(resObj.get("goodsInfoList"));
            MallVo mallVo;
			String discount = null;
            JSONArray goodsList = JSONUtil.createArray();
            for (JSONObject data : jsonArray.jsonIter()) {
				discount = null;
				if (ObjectUtil.isNotEmpty(data.get("couponInfo"))){
					JSONObject couponInfo = JSONUtil.parseObj(data.get("couponInfo"));
					if (ObjectUtil.isNotEmpty(couponInfo.get("buy"))){
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
            return AjaxResult.success("获取成功",JSONUtil.createObj()
				.putOnce("total", resObj.get("total"))
                    .putIfAbsent("goodsList",goodsList));
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult wphDetail(MallQueryBo bo) {
        JSONObject params = JSONUtil.createObj()
                .putOnce("goods_id", bo.getGoodsId())
                .putOnce("apikey", bo.getApikey());
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/vip/goodsdetail"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONArray datas = JSONUtil.parseArray(obj.get("data"));
			JSONObject data = datas.getJSONObject(0);
			// 得到优惠券信息
			JSONObject couponInfo = JSONUtil.parseObj(data.get("couponInfo"));
			String discount = null;
			Long startTime = null,endTime = null;
			if (ObjectUtil.isNotEmpty(couponInfo)){
				// 是最优的优惠券
				discount = new BigDecimal(couponInfo.get("buy").toString()).multiply(new BigDecimal(100)).toString();
				startTime = Long.parseLong(couponInfo.get("useBeginTime").toString());
				endTime = Long.parseLong(couponInfo.get("useEndTime").toString());
			}
			// 得到店铺信息
			String avgDesc = "低",avgServ = "低";
			JSONObject storeInfo = JSONUtil.parseObj(data.get("storeInfo"));
			JSONObject commentsInfo = JSONUtil.parseObj(data.get("commentsInfo"));
			if (ObjectUtil.isNotEmpty(commentsInfo) &&
				ObjectUtil.isNotEmpty(commentsInfo.get("goodCommentsShare"))){
				double commentsShare = Double.parseDouble(commentsInfo.get("goodCommentsShare").toString());
				if (commentsShare > 40 && commentsShare < 70){
					avgDesc = "中";
				}else if(commentsShare >= 70){
					avgDesc = "高";
				}
			}
			JSONObject storeServiceCapability = JSONUtil.parseObj(data.get("storeServiceCapability"));
			if (ObjectUtil.isNotEmpty(storeServiceCapability) &&
				ObjectUtil.isNotEmpty(storeServiceCapability.get("storeScore"))){
				double storeScore = Double.parseDouble(storeServiceCapability.get("storeScore").toString());
				if (storeScore > 40 && storeScore < 70){
					avgServ = "中";
				}else if(storeScore >= 70){
					avgServ = "高";
				}
			}
			// 描述
			String desc = null;
			if (ObjectUtil.isNotEmpty(data.get("goodsDesc"))){
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
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/vip/getunionurl"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
			JSONObject data = JSONUtil.parseObj(obj.get("data"));
			JSONArray infoList = JSONUtil.parseArray(data.get("urlInfoList"));
			if (CollUtil.isNotEmpty(infoList)){
				JSONObject info = infoList.getJSONObject(0);
				if (ObjectUtil.isNotEmpty(info)){
					String url = info.get("vipWxUrl").toString();
					String appId = info.get("appId").toString();
					return AjaxResult.success(new MallVo().setWeChatInfo(JSONUtil.createObj().putOnce("app_id",appId)
						.putOnce("page_path",url)));
				}
			}
        }
        return AjaxResult.error();
    }
}
