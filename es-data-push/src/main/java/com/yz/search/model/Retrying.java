package com.yz.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author yazhong
 */
@Document(indexName = "retrying")
@Data
public class Retrying implements Serializable {
    private static final long serialVersionUID = -4944584792916928120L;

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