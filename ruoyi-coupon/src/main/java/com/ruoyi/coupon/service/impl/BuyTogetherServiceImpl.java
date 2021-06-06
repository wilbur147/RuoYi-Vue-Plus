package com.ruoyi.coupon.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import com.ruoyi.coupon.utils.BeanToHttpUrl;
import com.ruoyi.coupon.vo.MallVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:17
 **/
@Service
public class BuyTogetherServiceImpl implements IBuyTogetherService {

    private final String API_URL = "http://api-gw.haojingke.com";

    @Override
    public AjaxResult buyTogetherList(MallQueryBo bo) {
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
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/goodslist"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject resObj = JSONUtil.parseObj(JSONUtil.toJsonStr(obj.get("data")));
            JSONArray jsonArray = JSONUtil.parseArray(resObj.get("goods_list"));
            MallVo mallVo;
            JSONArray goodsList = JSONUtil.createArray();
            for (JSONObject data : jsonArray.jsonIter()) {
                String price = data.get("min_normal_price").toString();
                String discount = data.get("coupon_discount").toString();
                // 计算券后价
                BigDecimal priceAfter = new BigDecimal(price)
                        .subtract(new BigDecimal(discount));
                mallVo = new MallVo()
                        .setGoodsId(data.get("goods_sign").toString())
                        .setGoodsName(data.get("goods_name").toString())
                        .setImg(data.get("goods_thumbnail_url").toString())
                        .setPrice(price)
                        .setDiscount(discount)
                        .setPriceAfter(priceAfter.toString())
                        .setSales(data.get("sales_tip").toString());
                goodsList.add(mallVo);
            }
            return AjaxResult.success("获取成功",JSONUtil.createObj().put("searchId",resObj.get("search_id"))
                    .putIfAbsent("goodsList",goodsList));
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult buyTogetherDetail(MallQueryBo bo) {
        JSONObject params = JSONUtil.createObj()
                .putOnce("isunion", "1")
                .putOnce("goods_sign", bo.getGoodsId())
                .putOnce("search_id", bo.getSearchId())
                .putOnce("pid", bo.getPid())
                .putOnce("apikey", bo.getApikey());
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/goodsdetail"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            String price = data.get("min_normal_price").toString();
            String discount = data.get("coupon_discount").toString();
            // 计算券后价
            BigDecimal priceAfter = new BigDecimal(price)
                    .subtract(new BigDecimal(discount));
            // 得到图集
            String[] urls = data.get("goods_gallery_urls").toString().replace("\"","").split(",");
            MallVo mallVo = new MallVo()
                    .setGoodsId(data.get("goods_sign").toString())
                    .setGoodsName(data.get("goods_name").toString())
                    .setGoodsDesc(data.get("goods_desc").toString())
                    .setImg(data.get("goods_image_url").toString())
                    .setImges(data.get("goods_gallery_urls"))
                    .setPrice(price)
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
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/getunionurl"+BeanToHttpUrl.convertToUrl(params));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            JSONObject allData = JSONUtil.parseObj(data.get("alldata"));
            return AjaxResult.success(new MallVo().setWeChatInfo(allData.get("we_app_info")));
        }
        return AjaxResult.error();
    }
}
