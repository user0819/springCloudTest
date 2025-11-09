package com.xiang.order.controller;


import com.xiang.order.feign.WeatherFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherFeignClient weatherFeignClient;

    /**
     * 获取天气信息
     * @param auth Authorization header 值
     * @param token 访问令牌
     * @param cityId 城市ID
     * @return 天气信息JSON字符串
     */
    @GetMapping("/getWeather")
    public String getWeather(@RequestParam("auth") String auth,
                           @RequestParam("token") String token,
                           @RequestParam("cityId") String cityId) {
        log.info("开始获取天气信息，cityId: {}", cityId);
        try {
            String result = weatherFeignClient.getWeather(auth, token, cityId);
            log.info("天气信息获取成功");
            return result;
        } catch (Exception e) {
            log.error("获取天气信息失败: {}", e.getMessage(), e);
            return "{\"error\": \"获取天气信息失败: " + e.getMessage() + "\"}";
        }
    }

    /**
     * 测试端点 - 使用默认参数测试
     * 注意：需要替换为有效的 auth 和 token
     * @return 天气信息JSON字符串
     */
    @GetMapping("/test")
    public String testWeather() {
        // 这里使用示例参数，实际使用时需要替换为真实的认证信息
        String auth = "APPCODE your_app_code";
        String token = "your_token";
        String cityId = "101010100"; // 北京的城市ID
        
        log.info("测试获取天气信息，cityId: {}", cityId);
        return getWeather(auth, token, cityId);
    }
}