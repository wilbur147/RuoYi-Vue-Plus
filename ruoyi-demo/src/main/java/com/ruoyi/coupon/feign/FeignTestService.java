package com.ruoyi.coupon.feign;

import com.ruoyi.coupon.feign.fallback.FeignTestFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign测试service
 *
 * @author Lion Li
 */
@FeignClient(name = "baidu",url = "http://www.baidu.com",fallback = FeignTestFallback.class)
public interface FeignTestService {

    @GetMapping("/s")
    String search(@RequestParam("wd") String wd);
}
