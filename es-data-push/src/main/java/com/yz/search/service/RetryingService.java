package com.yz.search.service;

import com.yz.search.model.Retrying;
import com.yz.search.repository.RetryingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RetryingService {

    @Autowired
    private RetryingRepository repository;

    public void save() throws IOException {
        Retrying retrying = new Retrying();
        repository.save(retrying);
    }

    public void findByBatchNo(String batchNo) {
        repository.findByBatchNo(batchNo);
    }
}
