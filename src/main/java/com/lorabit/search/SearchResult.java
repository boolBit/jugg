package com.lorabit.search;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-1-27
 */
@Data
public class SearchResult {
    private long                      totalRow;
    private int                       totalPage;
    private int                       page;
    private int                       pageSize;
    private String                    type;  // blog or album or people
    private List<Map<String, Object>> result;
    private boolean                   success = false;
    private String                    errorMsg;

    public SearchResult(){

    }

    public SearchResult(String erroMsg){
        this.errorMsg = erroMsg;
    }

    public void init(long totalRow, List result, int page, int pageSize, String type) {
        this.totalRow = totalRow;
        this.totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            this.totalPage += 1;
        }
        this.page = page;
        this.pageSize = pageSize;
        this.type = type;
        this.result = result;
        this.success = true;
    }
    public List<Integer> getIds() {
        List<Integer> ids = Lists.newArrayList();
        if (result == null) {
            return ids;
        }
        List<Map<String, Object>> pageResult = result;
        for (Map<String, Object> r : pageResult) {
            Object id = r.get("id");
            if (id != null) {
                ids.add(Integer.parseInt(id.toString()));
            }
        }
        return ids;
    }

}
