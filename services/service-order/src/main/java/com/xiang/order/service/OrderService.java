package com.xiang.order.service;


import com.xiang.order.bean.Order;

public interface OrderService {

    Order createOrder(Long productId, Long userId);
}
