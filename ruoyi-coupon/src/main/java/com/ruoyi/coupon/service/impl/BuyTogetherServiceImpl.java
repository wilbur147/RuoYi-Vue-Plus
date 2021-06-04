package com.ruoyi.coupon.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import com.ruoyi.coupon.utils.BeanToHttpUrl;
import org.springframework.stereotype.Service;

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
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/goodslist"+BeanToHttpUrl.convertToUrl(bo));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            return AjaxResult.success(data);
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult buyTogetherDetail(MallQueryBo bo) {
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/goodsdetail"+BeanToHttpUrl.convertToUrl(bo));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            return AjaxResult.success(data);
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult buyTogetherTurnChain(MallQueryBo bo) {
        String result= HttpUtil.get(API_URL+"/index.php/v1/api/pdd/getunionurl"+BeanToHttpUrl.convertToUrl(bo));
        JSONObject obj = JSONUtil.parseObj(result);
        if (200 == Integer.parseInt(obj.get("status_code").toString())) {
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            return AjaxResult.success(data);
        }
        return AjaxResult.error();
    }
}
