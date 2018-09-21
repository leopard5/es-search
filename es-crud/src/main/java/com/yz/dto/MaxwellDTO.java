package com.yz.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MaxwellDTO implements Serializable {

    private static final long serialVersionUID = -305830135340199886L;

    private String database;
    private String table;
    private String type;
    private Date ts;
    private Long xid;
    private Boolean commit;
    private String data;
}
