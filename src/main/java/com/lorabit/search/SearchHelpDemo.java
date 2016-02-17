package com.lorabit.search;

import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-1-27
 */
public class SearchHelpDemo {

    @Autowired
    private LBHttpSolrClient solrWebLBServer;

    @Autowired
    private LBHttpSolrClient solrBlogLBServer;


    public SolrQuery makeSolrQuery(QueryParam queryParam) {
        SolrQuery query = new SolrQuery();

        // Query
        String keyWord = queryParam.getKeyWords();
        if (keyWord.equals("*")) { //有时候*会很慢
            keyWord = "*:*";
        }
        query.setQuery(keyWord);

        // filterQuery
        List<String> fq = queryParam.getFilter();
        if (fq != null)
            query.setFilterQueries(fq.toArray(new String[fq.size()]));
//         pageNo pageSize
        int page = queryParam.getPage() == null ? 0 : queryParam.getPage();
        int pageSize = queryParam.getPageSize() == null ? 10 : queryParam.getPageSize();
        if (page == 0) {
            page = 1;
        }
        query.setStart((page - 1) * pageSize);
        if (query.getStart() < 0)
            query.setStart(0);
        query.setRows(pageSize);

        // sort
        if (queryParam.getSort() != null && !queryParam.getSort().equals("")) {
            String[] sorts = StringUtils.split(queryParam.getSort(), "|");
            String[] sortbys = StringUtils.split(queryParam.getSortBy(), "|");
            for (int i = 0; i < Math.min(sorts.length, sortbys.length); i++) {
                SolrQuery.ORDER order = sortbys[i].trim().equals("asc") ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                query.addSort(sorts[i], order);
//                query.addSortField(sorts[i], order);
            }
        }
        return query;
    }


    public void test() throws IOException {
        QueryParam qp = new QueryParam();
//        qp.setKeyWords("title:*dota*");
        qp.setKeyWords("content_encoding:ISO-8859-1");
        SolrQuery solrQuery = makeSolrQuery(qp);
        solrQuery.setFields("id,content_encoding");
        // execute query
        SolrDocumentList docs = null;
        try {
//            solrQuery.set(UpdateParams.COLLECTION, "blog");
            System.out.println(solrWebLBServer.getHttpClient().getConnectionManager());
            docs = solrBlogLBServer.query(solrQuery).getResults();
//            docs = solrWebLBServer.query(solrQuery).getResults();
        } catch (SolrServerException | HttpSolrServer.RemoteSolrException e) {
            e.printStackTrace();
        }

        // get result
        List<Map<String, Object>> result = Lists.newArrayList();
        for (SolrDocument doc : docs) {
            System.out.println(doc);
            result.add(doc);
        }
        System.out.println(docs.getNumFound());
    }
}
