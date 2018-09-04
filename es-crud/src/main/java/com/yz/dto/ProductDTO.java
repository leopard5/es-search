package com.yz.dto;

import com.yz.entity.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yazhong.qi
 * @date 2018/7/2.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDTO extends Product {

    private String highlighted;
}
