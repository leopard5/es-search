package com.yz.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

/**
 * @author yazhong.qi
 * @date 2018/7/2.
 */
@Data
@Document(indexName = "goods", type = "product")   //要加,不然报空指针异常
public class Product implements Serializable {

    @Id
    private String id;

    private String name;

    private String desc;

    private int price;

    private String producer;

    /**
     * 聚合这些操作用单独的数据结构(fielddata)缓存到内存里了，需要单独开启
     * 要么这里设置
     * 要着通过rest配置
     * PUT goods/_mapping/product
     * {
     *   "properties": {
     *     "tags": {
     *       "type":     "text",
     *       "fielddata": true
     *     }
     *   }
     * }
     */
    @Field(fielddata = true, type = FieldType.Text)
    private List<String> tags;

}
