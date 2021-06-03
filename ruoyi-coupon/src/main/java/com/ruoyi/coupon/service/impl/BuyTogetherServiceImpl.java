package com.ruoyi.coupon.service.impl;

import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopAccessTokenClient;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkOauthGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkOauthGoodsRecommendGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkOauthGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkOauthGoodsRecommendGetResponse;
import com.pdd.pop.sdk.http.token.AccessTokenResponse;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.coupon.bo.MallQueryBo;
import com.ruoyi.coupon.service.IBuyTogetherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wilbur
 * @Description
 * @create 2021-06-03 10:17
 **/
@Service
public class BuyTogetherServiceImpl implements IBuyTogetherService {

    private PopClient client = null;


    private final String API_KEY = "607d259324479dcf";
    private final String clientId = "7ab7663101354c5bba76aed3fd2ea310";
    private final String clientSecret = "950208c4ea788f527271a274cf04526ddbf58b7d";
    private final String PID = "13957782_210518982";
    private String accessToken = "your accessToken";

    private final String PINDUODUO_API_ACCESS_TOKEN = "pinduoduo:api:accessToken";
    @Autowired
    private RedisCache redisCache;

    private void getCode(){
        String url="https://jinbao.pinduoduo.com/open.html";
        //client_id
        url+="?client_id="+clientId;
        //授权类型为CODE
        url+="&response_type=code";
        //授权回调地址
        url+="&redirect_uri=http://www.pinduoduo.com";
    }

    private void genAccessToken(){
        PopAccessTokenClient accessTokenClient = new PopAccessTokenClient(clientId,clientSecret);
        // 生成AccessToken
        try {
            AccessTokenResponse response = accessTokenClient.generate("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public AjaxResult buyTogetherList(String listId, Integer offset, Integer limit) {
        client = new PopHttpClient(clientId, clientSecret);

        PddDdkOauthGoodsRecommendGetRequest request = new PddDdkOauthGoodsRecommendGetRequest();
        List<Integer> activityTags = new ArrayList<>();
        activityTags.add(0);
        request.setActivityTags(activityTags);
        request.setCatId(0L);
        request.setChannelType(5);
//        request.setCustomParameters("str");
        request.setForceAuthDuoId(false);
//        List<String> goodsSignList = new ArrayList<>();
//        goodsSignList.add("str");
//        request.setGoodsSignList(goodsSignList);
        request.setLimit(limit);
        request.setListId(listId);
        request.setOffset(offset);
//        request.setPid("str");
        PddDdkOauthGoodsRecommendGetResponse response = null;
        try {
            response = client.syncInvoke(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JsonUtil.transferToJson(response));
        return AjaxResult.success(JsonUtil.transferToJson(response));
    }

    @Override
    public AjaxResult buyTogetherMoreList(MallQueryBo bo) {

        return AjaxResult.success();
    }

    @Override
    public AjaxResult buyTogetherDetail(MallQueryBo bo) {
        client = new PopHttpClient(clientId, clientSecret);

        PddDdkOauthGoodsDetailRequest request = new PddDdkOauthGoodsDetailRequest();
//        request.setCustomParameters("str");
        request.setGoodsSign(bo.getGoodsSign());
//        request.setPid("str");
        request.setSearchId(bo.getSearchId());
//        request.setZsDuoId(0L);
        PddDdkOauthGoodsDetailResponse response = null;
        try {
            response = client.syncInvoke(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(JsonUtil.transferToJson(response));
    }

    @Override
    public AjaxResult buyTogetherTurnChain(MallQueryBo bo) {
        client = new PopHttpClient(clientId, clientSecret);

        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
//        request.setCashGiftId(0L);
//        request.setCashGiftName("str");
//        request.setCustomParameters("str");
        request.setGenerateAuthorityUrl(false);
        request.setGenerateMallCollectCoupon(false);
        request.setGenerateQqApp(false);
        request.setGenerateSchemaUrl(false);
        request.setGenerateShortUrl(false);
        request.setGenerateWeApp(true);
        List<String> goodsSignList = new ArrayList<>();
        goodsSignList.add(bo.getGoodsSign());
        request.setGoodsSignList(goodsSignList);
        request.setMultiGroup(false);
        request.setPId(PID);
        request.setSearchId(bo.getSearchId());
        PddDdkGoodsPromotionUrlGenerateResponse response = null;
        try {
            response = client.syncInvoke(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(JsonUtil.transferToJson(response));
    }
}
