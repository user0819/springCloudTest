package com.xiang.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.xiang.order.bean.Order;
import com.xiang.order.properties.OrderProperties;
import com.xiang.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    OrderProperties orderProperties;

    @GetMapping("/config")
    public String config() {
        return "order.timeout=" + orderProperties.getTimeout() + "； " +
                "order.auto-confirm=" + orderProperties.getAutoConfirm() + "；" +
                "order.db-url=" + orderProperties.getDbUrl();
    }

    //创建订单
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId) {
        return orderService.createOrderSentinel(productId, userId);
    }


    @GetMapping("/secKill")
    @SentinelResource(value = "secKill-order", fallback = "secKillFallback")
    public Order secKill(@RequestParam(value = "userId") Long userId,
                         @RequestParam(value = "productId", defaultValue = "1000") Long productId) {
        Order order = orderService.createOrder(productId, userId);
        order.setId(Long.MAX_VALUE);
        return order;
    }

    public Order secKillFallback(Long userId, Long productId, Throwable exception) {
        System.out.println("secKillFallback....");
        Order order = new Order();
        order.setId(productId);
        order.setUserId(userId);
        order.setAddress("异常信息：" + exception.getClass());
        return order;
    }


}
