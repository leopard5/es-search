package com.yz.search.handle;

import com.alibaba.fastjson.JSON;
import com.yz.search.constant.Constants;
import com.yz.search.dto.MaxwellDTO;
import com.yz.search.model.Retrying;
import com.yz.search.repository.RetryingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeDataPushES {

    @Autowired
    private RetryingRepository repository;

    public MaxwellDTO pushData2ES(String message) {
        MaxwellDTO data = JSON.parseObject(message, MaxwellDTO.class);
        Retrying retrying = JSON.parseObject(data.getData(), Retrying.class);

        switch (data.getType()) {
            case Constants
                    .MAXWELL_OP_TYPE_INSERT:
            case Constants
                    .MAXWELL_OP_TYPE_UPDATE:
                repository.save(retrying);
                break;
            case Constants
                    .MAXWELL_OP_TYPE_DELETE:
                repository.delete(retrying);
                break;
            default:
        }
        return data;
    }
}
