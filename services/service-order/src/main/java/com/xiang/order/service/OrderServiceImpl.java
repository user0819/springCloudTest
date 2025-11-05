package com.xiang.order.service;

import com.xiang.order.bean.Order;
import com.xiang.order.feign.ProductFeignClient;
import com.xiang.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;

    @Autowired //一定导入 spring-cloud-starter-loadbalancer
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;

    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductFromRemote(productId);
//        Product product = getProductFromRemoteWithLoadBalanceAnnotation(productId);
        Product product = productFeignClient.getProductById(productId);
        Order order = new Order();
        order.setId(1L);


        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNickName("zhangsan");
        order.setAddress("xiang");
        //远程查询商品列表
        order.setProductList(List.of(product));

        return order;
    }

    // 进阶3：基于注解的负载均衡
    private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId) {
        String url = "http://service-product/product/" + productId;
        //2、给远程发送请求； service-product 会被动态替换
        return restTemplate.getForObject(url, Product.class);
    }


    // 进阶2：完成负载均衡发送请求
    private Product getProductFromRemoteWithLoadBalance(Long productId) {
        //1、获取到商品服务所在的所有机器IP+port
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        //远程URL
        String url = "http://" + choose.getHost() + ":" + choose.getPort() + "/product/" + productId;
        log.info("LoadBalance远程请求：{}", url);
        //2、给远程发送请求
        return restTemplate.getForObject(url, Product.class);
    }

    private Product getProductFromRemote(Long productId) {
        //1、获取到商品服务所在的所有机器IP+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");

        ServiceInstance instance = instances.get(0);
        //远程URL
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;
        log.info("getProductFromRemote远程请求：{}", url);
        //2、给远程发送请求
        return restTemplate.getForObject(url, Product.class);
    }


}
