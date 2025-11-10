package com.xiang.product.controller;


import com.xiang.product.bean.Product;
import com.xiang.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    //查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId) {

        System.out.println("hello ....");
//                int i = 10/0;
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
        return productService.getProductById(productId);
    }
}
