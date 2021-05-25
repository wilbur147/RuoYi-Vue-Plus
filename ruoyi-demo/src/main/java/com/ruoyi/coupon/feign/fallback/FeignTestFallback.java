package com.ruoyi.coupon.feign.fallback;


import com.ruoyi.coupon.feign.FeignTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * feign测试fallback
 *
 * @author Lion Li
 */
@Slf4j
@Component
public class FeignTestFallback implements FeignTestService {

    @Override
    public String search(String wd) {
        log.error("fallback");
        return "报错啦";
    }
}
