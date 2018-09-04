package com.yz.controller;

import com.yz.dto.ProductDTO;
import com.yz.entity.Product;
import com.yz.utils.BeanUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 复杂的操作使用 elasticsearchTemplate
 * 查询具体组合可以查看 # QueryBuilders 类
 *
 * @author yazhong.qi
 * @date 2018/7/2.
 */
@RestController
@RequestMapping("/temp/product")
public class ProductTemplateController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ProductTemplateController(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }


    /**
     * 查询所有
     */
    @GetMapping
    public Object findAll( ) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery()).build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Object findPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(
                        PageRequest.of(page - 1, size, Sort.Direction.ASC, "price"))  //页码是从0 开始
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 单独的排序
     */
    @GetMapping("/sort")
    public Object findSort( ) {
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("price").order(SortOrder.ASC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(fieldSortBuilder)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Object findById(@PathVariable String id) {
        GetQuery getQuery = new GetQuery();
        getQuery.setId(id);
        return elasticsearchTemplate.queryForObject(getQuery, Product.class);
    }

    /**
     * 根据名称查询
     * 模糊查询
     * <p>
     * {
     * id: "2",
     * name: "牙",
     * desc: "string",
     * price: 0,
     * producer: "string",
     * tags: [
     * "string"
     * ]
     * },
     * {
     * id: "1",
     * name: "牙膏",
     * desc: "string",
     * price: 0,
     * producer: "string",
     * tags: [
     * "string"
     * ]
     * },
     * {
     * id: "3",
     * name: "膏",
     * desc: "string",
     * price: 0,
     * producer: "string",
     * tags: [
     * "string"
     * ]
     * }
     * <p>
     * 查询牙膏,会查询出来所有
     */
    @GetMapping("/find-by-name")
    public Object findByName(@RequestParam String name) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", name)).build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }


    /**
     * 单字段包含所有输入
     * 这里只会查询出 包含整个name字段的,也就是牙膏只会查询出上面的黑人牙膏
     */
    @GetMapping("/contain")
    public Object contain(@RequestParam String name) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("name", name)
                                .operator(Operator.AND)    //默认使用的是or
                                .minimumShouldMatch("100%")        //匹配率
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * 指定查询的字段
     */
    @GetMapping("/source-filter")
    public Object sourceFilter( ) {

        //只显示名字和价格
        String[] include = {"name", "price"};

        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(include, null);   //两个参数分别是要显示的和不显示的

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSourceFilter(fetchSourceFilter)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * bool查询
     */
    @GetMapping("/bool")
    public Object bool( ) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(
                        boolQuery()
                                .must(matchQuery("price", 40))
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }


    /**
     * filter查询
     */
    @GetMapping("/filter")
    public Object filter(@RequestParam String filed, @RequestParam String filedName,
                         @RequestParam(required = false, defaultValue = "price") String rangeName,
                         @RequestParam(required = false, defaultValue = "0") String range) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(
                        boolQuery()
                                .must(matchQuery(filed, filedName))
                                .filter(
                                        rangeQuery(rangeName)
                                                .gte(range)
                                )
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }


    /**
     * phrase查询
     */
    @GetMapping("/phrase")
    public Object phrase(@RequestParam String filed, @RequestParam String filedName) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchPhraseQuery(filed, filedName)
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }

    /**
     * highlight查询
     */
    @GetMapping("/highlight")
    public Object highlight(@RequestParam String filed, @RequestParam String filedName) {

        //高亮显示的词必须是查询的词
        final List<HighlightBuilder.Field> fields = new HighlightBuilder().field(filed).fields();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchPhraseQuery(filed, filedName)
                )
                .withHighlightFields(fields.toArray(new HighlightBuilder.Field[0]))
                .build();

        //如果不想使用分页的写法
        return elasticsearchTemplate.query(searchQuery, (ResultsExtractor<Object>) response -> {
            List<ProductDTO> chunk = new ArrayList<>();
            for (SearchHit searchHit : response.getHits()) {
                //没有数据
                if (response.getHits().getHits().length <= 0) {
                    return null;
                }
                //hit转换成bean
                ProductDTO productDTO = BeanUtils.mapToBean(searchHit.getSourceAsMap(), new ProductDTO());
                productDTO.setHighlighted(searchHit.getHighlightFields().get("name").fragments()[0].toString());
                chunk.add(productDTO);
            }
            return chunk;
        });

//        return elasticsearchTemplate.queryForPage(searchQuery, Product.class, new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<ProductDTO> chunk = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return null;
//                    }
//                    //hit转换成bean
//                    ProductDTO productDTO = BeanUtils.mapToBean(searchHit.getSourceAsMap(), new ProductDTO());
//                    productDTO.setHighlighted(searchHit.getHighlightFields().get("name").fragments()[0].toString());
//                    chunk.add(productDTO);
//                }
//                if (chunk.size() > 0) {
//                    return new AggregatedPageImpl<>((List<T>) chunk);
//                }
//                return null;
//            }
//        });
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        elasticsearchTemplate.delete(Product.class, id);
    }


    /**
     * 根据指定字段删除
     * 不是完全匹配,类似模糊匹配
     */
    @DeleteMapping("/delete-by-name/{name}")
    public void deleteByName(@PathVariable String name) {
        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setQuery(termQuery("name", name));    //类似模糊删除
        elasticsearchTemplate.delete(deleteQuery, Product.class);
    }

    /**
     * 更新
     */
    @PutMapping
    public void update(@RequestBody Product product) {
        IndexRequest indexRequest = new IndexRequest();
        Map<String, Object> map = BeanUtils.beanToMap(product);
        indexRequest.source(map, XContentType.JSON);    //更新整个实体
//        indexRequest.source("name",product.getName());  //更新指定的字段
        UpdateQuery updateQuery = new UpdateQueryBuilder().withId(product.getId())
                .withClass(Product.class).withIndexRequest(indexRequest).build();
        elasticsearchTemplate.update(updateQuery);
    }


    /**
     * 批量更新
     */
    @PutMapping("/batch")
    public void updateBatch(@RequestBody List<Product> products) {
        List<UpdateQuery> queries = new ArrayList<>();

        for (Product product : products) {
            IndexRequest indexRequest = new IndexRequest();
            Map<String, Object> map = BeanUtils.beanToMap(product);
            indexRequest.source(map, XContentType.JSON);    //更新整个实体
            UpdateQuery updateQuery = new UpdateQueryBuilder().withId(product.getId())
                    .withClass(Product.class).withIndexRequest(indexRequest).build();

            queries.add(updateQuery);
        }
        elasticsearchTemplate.bulkUpdate(queries);

    }
}
