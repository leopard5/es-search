package com.yz.controller;

import com.yz.entity.Product;
import com.yz.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用productRepository 做基本的增删改查
 *
 * @author yazhong.qi
 * @date 2018/7/2.
 */
@RestController
@RequestMapping("/base/product")
public class ProductBaseController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductBaseController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public void add(@RequestBody Product product) {
        productRepository.save(product);
    }

    @GetMapping
    public Object findAll( ) {
        return productRepository.findAll();      //根据自己的需求然后再做数据处理
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable String id) {
        return productRepository.findById(id);
    }

    @GetMapping("/find-by-name/{name}")
    public Object findByName(@PathVariable String name) {
        return productRepository.findByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        productRepository.deleteById(id);
    }


    @DeleteMapping("/delete-by-name/{name}")
    public void deleteByName(@PathVariable String name) {
        productRepository.deleteByName(name);
    }
}
