package com.ruoyi.content.program;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.content.bo.MovieQueryBo;
import com.ruoyi.content.vo.MovieVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 电影Controller
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
@Api(value = "影票控制器", tags = {"影票内容管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/program/movie")
public class ProContentController extends BaseController {
    private final String MOVIE_DATA_KEY = "movie:data:detail";
    @Autowired
    private RedisCache redisCache;
    /**
     * 查询内容列表
     */
    @ApiOperation("查询影票内容列表")
    @GetMapping("/query")
    public AjaxResult query(MovieQueryBo bo) {
        Map<String,Object> paramMap = new HashMap<>(16);
        paramMap.put("lat",null);
        paramMap.put("lng",null);
        paramMap.put("mode","qmm");
        paramMap.put("app_key",null);
        paramMap.put("domainName","https://gw.taototo.cn/");
        paramMap.put("token",null);
        paramMap.put("platformUUID","24518126");
        paramMap.put("latitude",null);
        paramMap.put("longitude",null);
        paramMap.put("cityId","40");
        paramMap.put("evnType","h5");
        paramMap.put("userUUID","932c93a890d14d138aef2f1512be75f6");
        paramMap.put("v",null);
        paramMap.put("isCouponPop",null);
        paramMap.put("ci","40");
        paramMap.put("cityCode","500100");
        paramMap.put("page",1);
        paramMap.put("limit",10);
        paramMap.put("showType",bo.getShowType() == null ? 1 : bo.getShowType());
        String result = HttpRequest.post("https://yp-api.taototo.cn/yp-api/movie/film/query")
                //表单内容
                .form(paramMap)
                //超时，毫秒
                .timeout(20000)
                .execute().body();
        JSONObject obj = JSONUtil.parseObj(result);
        // 定义返回内容
        if (Validator.isNotEmpty(obj) && 200 == Integer.parseInt(obj.get("code").toString())){
            List<MovieVo> resArr = new ArrayList<>();
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            JSONArray arr = JSONUtil.parseArray(JSON.toJSON(data.get("list")));
            MovieVo movieVo = null;
            for (Object item : arr) {
                JSONObject jsonObject = JSONUtil.parseObj(item);
                movieVo = new MovieVo()
                        .setId(Long.parseLong(jsonObject.get("showId").toString()))
                        .setImg("https://gw.alicdn.com/tfscom/cdn"+jsonObject.get("poster"))
                        .setLeadingRole(jsonObject.get("leadingRole").toString())
                        .setNm(jsonObject.get("showName").toString())
                        .setSc(jsonObject.get("remark").toString())
                        .setShowMark(jsonObject.get("showMark").toString())
                        .setCountry(jsonObject.get("country").toString())
                        .setDuration(Integer.parseInt(jsonObject.get("duration").toString()))
                        .setType(jsonObject.get("type").toString())
                        .setOpenDay(jsonObject.get("openDay").toString().split(" ")[0])
                        .setShowType(Integer.parseInt(jsonObject.get("showType").toString()));
                resArr.add(movieVo);
                redisCache.setCacheObject(MOVIE_DATA_KEY, movieVo);
            }

            // 过滤内容
            // 1. 搜索
            if (StrUtil.isNotEmpty(bo.getSearchValue()) && CollectionUtil.isNotEmpty(resArr)){
                resArr = resArr.stream().filter(i->i.getNm().indexOf(bo.getSearchValue()) != -1).collect(Collectors.toList());
            }
            return AjaxResult.success(resArr);
        }
        return AjaxResult.error();
    }

    /**
     * 获取影票内容详细信息
     */
    @ApiOperation("获取影票内容详细信息")
    @GetMapping("/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        Map<String,Object> paramMap = new HashMap<>(16);
        paramMap.put("lat",null);
        paramMap.put("lng",null);
        paramMap.put("mode","qmm");
        paramMap.put("app_key",null);
        paramMap.put("domainName","https://gw.taototo.cn/");
        paramMap.put("token",null);
        paramMap.put("platformUUID","24518126");
        paramMap.put("latitude",null);
        paramMap.put("longitude",null);
        paramMap.put("cityId","40");
        paramMap.put("evnType","h5");
        paramMap.put("userUUID","932c93a890d14d138aef2f1512be75f6");
        paramMap.put("v",null);
        paramMap.put("isCouponPop",null);
        paramMap.put("ci","40");
        paramMap.put("showId",id);
        String result = HttpRequest.get("https://yp-api.taototo.cn/yp-api/movie/film/detail")
                //表单内容
                .form(paramMap)
                //超时，毫秒
                .timeout(20000)
                .execute().body();
        JSONObject obj = JSONUtil.parseObj(result);
        // 定义返回内容
        MovieVo movieVo = JSONUtil.toBean(JSONUtil.toJsonStr(redisCache.getCacheObject(MOVIE_DATA_KEY)), MovieVo.class);
        if (Validator.isNotEmpty(obj) && 200 == Integer.parseInt(obj.get("code").toString())){
            JSONObject data = JSONUtil.parseObj(obj.get("data"));
            JSONObject movie = JSONUtil.parseObj(data.get("movie"));
            if (ObjectUtil.isNotEmpty(movie)){
                movieVo.setEnm(movie.get("showNameEn").toString())
                        .setDescription(movie.get("description").toString());

                return AjaxResult.success(movieVo);
            }
        }
        return AjaxResult.error();
    }
}
