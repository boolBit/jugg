package com.lorabit.search;

import java.util.List;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-1-27
 */
@Data
public class QueryParam {
    private String keyWords;
    private List<String> filter;
    private String sort;
    private String sortBy;
    private Integer page;
    private Integer pageSize;
}

