package com.yz.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * 练习题
 * 需求1: 对名称中包含 牙膏 的商品，计算每个tag下的商品数量
 * 需求2：先分组，再算每组的平均值，计算每个tag下的商品的平均价格
 * 需求3: 计算每个tag下的商品的平均价格，并且按照平均价格降序排序
 * 需求4: 按照指定的价格范围区间进行分组，然后在每组内再按照tag进行分组，最后再计算每组的平均价格
 *
 * @author yazhong.qi
 * @date 2018/7/2.
 */
@RestController
@RequestMapping("/practice")
public class PracticeController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public PracticeController(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @GetMapping("/four")
    public Object four() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchAllQuery()
                )
                .addAggregation(
                        AggregationBuilders.range("range_by_price")
                                .field("price")
                                .addRange(0, 10)
                                .addRange(10, 20)
                                .addRange(20, 30)
                                .addRange(30, 100)
                                .subAggregation(
                                        AggregationBuilders.terms("group_by_tags")
                                                .field("tags")
                                                .order(Terms.Order.aggregation("price", false))
                                                .subAggregation(
                                                        AggregationBuilders.avg("price").field("price"))
                                )
                )
                .build();
        return elasticsearchTemplate.query(searchQuery, SearchResponse::toString);
    }


    @GetMapping("/three")
    public Object three() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchAllQuery()
                )
                .addAggregation(
                        AggregationBuilders.terms("group_by_tags")
                                .field("tags")
                                .order(Terms.Order.aggregation("price", false))
                                .subAggregation(
                                        AggregationBuilders.avg("price").field("price"))
                )
                .build();
        return elasticsearchTemplate.query(searchQuery, SearchResponse::toString);
    }


    @GetMapping("/two")
    public Object two() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchAllQuery()
                )
                .addAggregation(
                        AggregationBuilders.terms("group_by_tags")
                                .field("tags")
                                .subAggregation(
                                        AggregationBuilders.avg("price").field("price"))
                )
                .build();
        //这里直接返回es原封结果到前端,实际根据需求取值
        return elasticsearchTemplate.query(searchQuery, SearchResponse::toString);

    }


    @GetMapping("/one")
    public Object one(@RequestParam String name) {
        Map<String, Long> map = new HashMap<>();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("name", name)
                )
                .addAggregation(
                        AggregationBuilders.terms("all_tags")
                                .field("tags")
                )
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);

        Aggregation aggregation = aggregations.asMap().get("all_tags");
        if (aggregation == null) {
            return null;
        }

        StringTerms modelTerms = (StringTerms) aggregation;

        for (Terms.Bucket actionTypeBucket : modelTerms.getBuckets()) {
            //actionTypeBucket.getKey().toString()聚合字段的相应名称,actionTypeBucket.getDocCount()相应聚合结果
            map.put(actionTypeBucket.getKey().toString(), actionTypeBucket.getDocCount());
        }
        return map;

    }


}
