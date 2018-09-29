package com.yz.search.repository;

import com.yz.search.model.Retrying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RetryingRepository extends ElasticsearchRepository<Retrying, String> {

    List<Retrying> findByBatchNo(String batchNo);

    Page<Retrying> findByErrorCode(String errorCode, Pageable pageable);

}
