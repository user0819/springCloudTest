package com.xiang.product.service.impl;

import com.xiang.product.bean.Product;
import com.xiang.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("99"));
        product.setProductName("苹果-"+productId);
        product.setNum(2);

//        try {
//            TimeUnit.SECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return product;
    }
}
