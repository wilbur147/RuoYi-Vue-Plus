package com.ruoyi.coupon.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.coupon.bo.MovieQueryBo;
import com.ruoyi.coupon.vo.MovieVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 影票 Controller
 *
 * @author ruoyi
 * @date 2021-05-25
 */
@Slf4j
@Api(value = "影票控制器", tags = {"影票内容管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/program/movie")
public class ProMovieController extends BaseController {
    private final String MOVIE_DATA_KEY = "movie:data:detail";
    private final String MAOYAN_API_COOKIE_DATA = "maoyan:api:cookie";
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询猫眼API热门影票列表
     */
    @ApiOperation("查询影票内容列表")
    @GetMapping("/movieList")
    public JSONObject movieList(){
        HttpResponse response = HttpRequest.get("https://m.maoyan.com/ajax/movieOnInfoList")
                .execute();
        StringBuilder cookieStr = new StringBuilder();
        for (HttpCookie cookie : response.getCookies()) {
            cookieStr.append(cookie.getName() + "=" + cookie.getValue()).append("; ");
        }
        redisCache.setCacheObject(MAOYAN_API_COOKIE_DATA,cookieStr.toString());
        return JSONUtil.parseObj(response.body());
    }

    /**
     * 查询猫眼API即将上映
     */
    @ApiOperation("查询猫眼API即将上映")
    @GetMapping("/mostExpected")
    public JSONObject mostExpected(@RequestParam("limit") String limit,
                                   @RequestParam("offset") String offset,
                                   @RequestParam(value = "token",required = false) String token){
        String cookie = redisCache.getCacheObject(MAOYAN_API_COOKIE_DATA).toString();
        HttpResponse response = HttpRequest.get("https://m.maoyan.com/ajax/mostExpected?limit=" + limit + "&offset=" + offset + "&token=" + token)
                .header(Header.COOKIE, cookie)
                .header(Header.USER_AGENT, "PostmanRuntime/7.26.8")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.CONNECTION, "keep-alive")
                .execute();

        return JSONUtil.parseObj(response.body());
    }

    /**
     * 查询猫眼API即将上映 ---- 最受期待
     */
    @ApiOperation("查询猫眼API即将上映--最受期待")
    @GetMapping("/comingList")
    public JSONObject comingList(@RequestParam("limit") String limit,
                                   @RequestParam("optimus_uuid") String optimusUuid,
                                   @RequestParam("optimus_risk_level") String optimusRiskLevel,
                                   @RequestParam("optimus_code") String optimusCode,
                                   @RequestParam(value = "token",required = false) String token){
        String cookie = redisCache.getCacheObject(MAOYAN_API_COOKIE_DATA).toString();
        HttpResponse response = HttpRequest.get("https://m.maoyan.com/ajax/comingList?limit="+limit+"&optimus_uuid="+optimusUuid+
                "&optimus_risk_level="+optimusRiskLevel+"&optimus_code="+optimusCode+"&token="+token)
                .header(Header.COOKIE, cookie)
                .header(Header.USER_AGENT, "PostmanRuntime/7.26.8")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.CONNECTION, "keep-alive")
                .execute();

        return JSONUtil.parseObj(response.body());
    }

    /**
     * 查询猫眼API影票列表----加载更多
     */
    @ApiOperation("查询猫眼API影票列表----加载更多")
    @GetMapping("/moreComingList")
    public JSONObject moreComingList(@RequestParam("movieIds") String movieIds){
        String cookie = redisCache.getCacheObject(MAOYAN_API_COOKIE_DATA).toString();
        String result = HttpRequest.get("https://m.maoyan.com/ajax/moreComingList?token=&movieIds="+movieIds)
                .header(Header.COOKIE, cookie)
                .header(Header.USER_AGENT, "PostmanRuntime/7.26.8")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.CONNECTION, "keep-alive")
                .execute().body();
        return JSONUtil.parseObj(result);
    }

    /**
     * 查询猫眼API影票详情
     */
    @ApiOperation("查询猫眼API影票详情")
    @GetMapping("/detailMovie")
    public JSONObject detailMovie(@RequestParam("movieId") String movieId){
        String cookie = redisCache.getCacheObject(MAOYAN_API_COOKIE_DATA).toString();
        String result = HttpRequest.get("https://m.maoyan.com/ajax/detailmovie?movieId="+movieId)
                .header(Header.COOKIE, cookie)
                .header(Header.USER_AGENT, "PostmanRuntime/7.26.8")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.CONNECTION, "keep-alive")
                .execute().body();
        return JSONUtil.parseObj(result);
    }

    /**
     * 查询猫眼API观众评论
     */
    @ApiOperation("查询猫眼API观众评论")
    @GetMapping("/commentsMovie")
    public JSONObject commentsMovie(@RequestParam("movieId") String movieId,
                                    @RequestParam("offset") String offset){
        String cookie = redisCache.getCacheObject(MAOYAN_API_COOKIE_DATA).toString();
        String result = HttpRequest.get("https://m.maoyan.com/mmdb/comments/movie/"+movieId+".json?_v_=yes&offset="+offset)
                .header(Header.COOKIE, cookie)
                .header(Header.USER_AGENT, "PostmanRuntime/7.26.8")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.CONNECTION, "keep-alive")
                .execute().body();
        return JSONUtil.parseObj(result);
    }

    /**
     * 获取影票CPS链接
     */
    @ApiOperation("获取影票CPS链接")
    @GetMapping("/movieBuy")
    public AjaxResult movieBuy(){
        return AjaxResult.success("获取影票链接成功","https://dwz.cn/D9fEVtaK");
    }


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
