package com.yz.controller;

import com.yz.enums.AnalyzerEnum;
import com.yz.enums.CharFilterEnum;
import com.yz.enums.TokenFilterEnum;
import com.yz.enums.TokenizerEnum;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分词
 *
 */
@RestController
@RequestMapping("/demo1")
public class Demo1Controller {

    private final ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    public Demo1Controller(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * 分词
     *
     * @param name         分词文本
     * @param analyzerCode 分词类型,具体参考枚举类
     */
    @GetMapping
    public Object getByName(@RequestParam String name,
                            @RequestParam(required = false) Integer analyzerCode,
                            @RequestParam(required = false) Integer tokenizerCode,
                            @RequestParam(required = false) Integer charFilterCode,
                            @RequestParam(required = false) Integer tokenFilterCode) {


        AnalyzeRequestBuilder analyzeRequestBuilder = getAnalyzeRequestBuilder(name);
        if (analyzerCode != null) {
            String analyzer = AnalyzerEnum.getNameByCode(analyzerCode);
            analyzeRequestBuilder.setAnalyzer(analyzer);
        }

        /*
         * 当自带的分词无法满足需求时,可以自定义分词.
         * 通过自定义Character Filters,Tokenizer和Tokenizer Filters来实现
         */
        if (tokenizerCode != null) {
            analyzeRequestBuilder.setTokenizer(TokenizerEnum.getNameByCode(tokenizerCode));
        }

        if (charFilterCode != null) {
            analyzeRequestBuilder.addCharFilter(CharFilterEnum.getNameByCode(charFilterCode));
        }

        if (tokenFilterCode != null) {
            analyzeRequestBuilder.addTokenFilter(TokenFilterEnum.getNameByCode(tokenFilterCode));
        }


        return getResult(analyzeRequestBuilder);
    }


    private List<String> getResult(AnalyzeRequestBuilder analyzeRequestBuilder) {
        List<AnalyzeResponse.AnalyzeToken> tokens = analyzeRequestBuilder.execute().actionGet().getTokens();

        List<String> searchTermList = new ArrayList<>();
        tokens.forEach(ikToken -> searchTermList.add(ikToken.getTerm()));

        return searchTermList;
    }

    private AnalyzeRequestBuilder getAnalyzeRequestBuilder(@RequestParam String name) {
        return new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE, "test", name);
    }
}
