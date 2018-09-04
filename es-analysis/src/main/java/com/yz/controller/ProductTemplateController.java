package com.yz.controller;

import com.yz.entity.Product;
import com.yz.enums.AnalyzerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * 复杂的操作使用 elasticsearchTemplate
 * 查询具体组合可以查看 # QueryBuilders 类
 *
 */
@RestController
@RequestMapping("/temp/product")
public class ProductTemplateController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ProductTemplateController(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @GetMapping
    public Object findAll(@RequestParam String name, @RequestParam Integer code) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("name", name)
                                .analyzer(AnalyzerEnum.getNameByCode(code))   //mapping没有设置的时候这里可以设置分词类型
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }


}
