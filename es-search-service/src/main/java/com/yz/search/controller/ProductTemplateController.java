package com.yz.search.controller;

import com.yz.search.entity.Product;
import com.yz.enums.AnalyzerEnum;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 复杂的操作使用 elasticsearchTemplate
 * 查询具体组合可以查看 # QueryBuilders 类
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

    /**
     * 单字符串模糊查询，默认排序。将从所有字段中查找包含传来的word分词后字符串的数据集
     *
     * @param word
     * @param pageable
     * @return
     */
    @RequestMapping("/singleWord")
    public Object singleTitle(String word, @PageableDefault Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery(word))
                .withPageable(pageable)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 单字符串模糊查询，单字段排序
     *
     * @param word
     * @param pageable
     * @return
     */
    @RequestMapping("/singleWord1")
    public Object singlePost(String word, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery(word))
                .withPageable(pageable)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);

    }


    /**
     * 单字段对某字符串模糊查询
     *
     * @param content
     * @param userId
     * @param pageable
     * @return
     */
    @RequestMapping("/singleMatch")
    public Object singleMatch(String content, Integer userId, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("content", content))
                .withPageable(pageable)
                .build();
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("userId", userId)).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 单字段对某短语进行匹配查询，短语分词的顺序会影响结果
     *
     * @param content
     * @param pageable
     * @return
     */
    @RequestMapping("/singlePhraseMatch")
    public Object singlePhraseMatch(String content, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchPhraseQuery("content", content))
                .withPageable(pageable).build();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchPhraseQuery("content", content)
//                        .slop(2))
//                .withPageable(pageable).build();

        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * term匹配，即不分词匹配，你传来什么值就会拿你传的值去做完全匹配
     *
     * @param userId
     * @param pageable
     * @return
     */
    @RequestMapping("/singleTerm")
    public Object singleTerm(Integer userId, @PageableDefault Pageable pageable) {
        //不对传来的值分词，去找完全匹配的
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("userId", userId)).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * multi_match多个字段匹配某字符串
     * 多字段匹配
     *
     * @param title
     * @param pageable
     * @return
     */
    @RequestMapping("/multiMatch")
    public Object singleUserId(String title, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(title, "title", "content"))
                .withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * @param title
     * @return
     */
    @RequestMapping("/contain")
    public Object contain(String title) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("title", title)
                        .operator(MatchQueryBuilder.DEFAULT_OPERATOR)).build();

        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 合并查询
     * 即boolQuery，可以设置多个条件的查询方式。它的作用是用来组合多个Query，有四种方式来组合，must，mustnot，filter，should。
     * must代表返回的文档必须满足must子句的条件，会参与计算分值；
     * filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
     * should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
     * mustnot代表必须不满足子句的条件。
     * 譬如我想查询title包含“XXX”，且userId=“1”，且weight最好小于5的结果。那么就可以使用boolQuery来组合。
     *
     *
     * 如果某个字段需要匹配多个值，譬如userId为1，2，3任何一个的，类似于mysql中的in，那么可以使用termsQuery("userId", ids).
     * 如果某字段是字符串，我建议空的就设置为null，不要为""空串，貌似某些版本的ES，使用matchQuery空串会不生效。
     * 详细点的看这篇http://blog.csdn.net/dm_vincent/article/details/41743955
     * boolQuery使用场景非常广泛，应该是主要学习的知识之一。
     * Query和Filter的区别
     *
     *
     * 从代码上就能看出来，query和Filter都是QueryBuilder，也就是说在使用时，你把Filter的条件放到withQuery里也行，反过来也行。那么它们两个区别在哪？
     * 查询在Query查询上下文和Filter过滤器上下文中，执行的操作是不一样的：
     * 1、查询：是在使用query进行查询时的执行环境，比如使用search的时候。
     * 在查询上下文中，查询会回答这个问题——“这个文档是否匹配这个查询，它的相关度高么？”
     * ES中索引的数据都会存储一个_score分值，分值越高就代表越匹配。即使lucene使用倒排索引，对于某个搜索的分值计算还是需要一定的时间消耗。
     * 2、过滤器：在使用filter参数时候的执行环境，比如在bool查询中使用Must_not或者filter
     * 在过滤器上下文中，查询会回答这个问题——“这个文档是否匹配？”
     * 它不会去计算任何分值，也不会关心返回的排序问题，因此效率会高一点。
     * 另外，经常使用过滤器，ES会自动的缓存过滤器的内容，这对于查询来说，会提高很多性能。
     *
     * 总而言之：
     * 1 查询上下文：查询操作不仅仅会进行查询，还会计算分值，用于确定相关度；
     * 2 过滤器上下文：查询操作仅判断是否满足查询条件，不会计算得分，查询的结果可以被缓存。
     * 所以，根据实际的需求是否需要获取得分，考虑性能因素，选择不同的查询子句。
     * @param title
     * @param userId
     * @param weight
     * @return
     */
    @RequestMapping("/bool")
    public Object bool(String title, Integer userId, Integer weight) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery()
                        .must(termQuery("userId", userId))
                        .should(rangeQuery("weight").lt(weight))
                        .must(matchQuery("title", title)))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }
}