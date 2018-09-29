package com.yz.search.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yazhong
 */
@Data
public class MaxwellDTO implements Serializable {
    private static final long serialVersionUID = -305830135340199886L;

    private String database;
    private String table;
    private String type;
    private Long ts;
    private Long xid;
    private Boolean commit;
    private String data;
    private String old;
}
