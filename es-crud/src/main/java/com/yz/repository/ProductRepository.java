package com.yz.repository;

import com.yz.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * @author yazhong.qi
 * @date 2018/7/2.
 */
public interface ProductRepository extends ElasticsearchCrudRepository<Product, String> {

    void deleteByName(String name);

    List<Product> findByName(String name);
}
