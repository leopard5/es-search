package com.yz.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author yazhong
 */
@Document(indexName = "retrying")
@Data
public class Retrying {
    /**
     * 自增主键ID
     */
    @Id
    private String id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 错误码
     */
    private String errorCode;
}