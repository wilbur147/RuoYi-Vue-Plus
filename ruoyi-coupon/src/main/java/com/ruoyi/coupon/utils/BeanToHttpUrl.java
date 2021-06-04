package com.ruoyi.coupon.utils;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-04 09:48
 **/
public class BeanToHttpUrl {

    public static String convertToUrl(Object bean){
        if (ObjectUtil.isNotEmpty(bean)){
            HashMap<String,Object> map = JSONUtil.toBean(JSONUtil.toJsonStr(bean), HashMap.class);
            StringBuilder sb  = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (Validator.isNotEmpty(entry.getValue())) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            return sb.toString();
        }
        return null;
    }
}
