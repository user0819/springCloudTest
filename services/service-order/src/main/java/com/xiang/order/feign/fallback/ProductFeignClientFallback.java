package com.xiang.order.feign.fallback;

import com.xiang.order.feign.ProductFeignClient;
import com.xiang.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public Product getProductById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(new BigDecimal("0"));
        product.setProductName("未知商品");
        product.setNum(0);

        log.error("进入ProductFeignClientFallback");

        return product;
    }
}
