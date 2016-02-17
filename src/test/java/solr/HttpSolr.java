package solr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorabit.search.Docu;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author lorabit
 * @since 16-1-27
 */
public class HttpSolr {

    private static final String URL = "http://localhost:8983/solr/web/";
    private static HttpSolrServer server = new HttpSolrServer(URL);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // Setting the XML response parser is only required for cross
        // version compatibility and only when one side is 1.4.1 or
        // earlier and the other side is 3.1 or later.
//        server.setParser(new XMLResponseParser()); // binary parser is used by default
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used
        // after consulting javadocs to know whether they are truly required.
        server.setSoTimeout(1000);  // socket read timeout
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        server.setAllowCompression(true);
    }

    static int timeDiff = TimeZone.getDefault().getRawOffset()
            - TimeZone.getTimeZone("GMT").getRawOffset();//本地时区和目标时区差

    public static void add() throws IOException, SolrServerException, ParseException {
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", 2);
        doc1.addField("username", "张天豪");
        doc1.addField("title", "我是一个小菜鸡");
        doc1.addField("content", "dota远古大神就是我");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        server.commit();
    }

    public static void update() throws IOException, SolrServerException {
        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", 2);
        doc1.addField("username", "张天豪ruobi");
        doc1.addField("title", "我是一个小菜鸡a");
        doc1.addField("content", "dota远古大神就是我a");
        req.add(doc1);
        UpdateResponse rsp = req.process(server);
        System.out.println(rsp);
    }


    public static void  query() throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("username:43*");
        QueryResponse resp = server.query(query);
        System.out.println(resp.getResults());
        System.out.println(resp.getResults().get(0).get("id").getClass());
        List<Docu> doc = mapper.readValue(
                mapper.writeValueAsString(resp.getResults()), new TypeReference<List<Docu>>() {
                });

        System.out.println(doc);

    }

    public static void facetQuery() throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("dota")
                .setFacet(true)
                .setFacetLimit(8)
                .addFacetField("title");
        QueryResponse resp = server.query(query);
        System.out.println(resp.getResults());
        System.out.println(resp.getFacetFields());
    }

    public static void highlightQuery() throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("title:*大神* OR content:*dota*");
//        query.setParam("hl.fl", "title");
        query.addHighlightField("title");// 高亮字段
        query.setHighlightSnippets(1);//结果分片数，默认为1
        query.setHighlight(true).setHighlightSimplePre("<em>").setHighlightSimplePost("</em>");
//      query.setHighlightFragsize(1000);//每个分片的最大长度，默认为100
        System.out.println(query);
        QueryResponse resp = server.query(query);
        System.out.println(resp.getHighlighting());
        System.out.println(resp.getResults());
    }

    @Test
    public  void LoadBalanceServer() throws IOException, SolrServerException {
        LBHttpSolrClient lbserver = new LBHttpSolrClient("http://localhost:8983/solr/web_shard1_replica1/",
                "http://localhost:8983/solr/web_shard1_replica2/",
                "http://localhost:8983/solr/web_shard2_replica1/",
                "http://localhost:8983/solr/web_shard2_replica2/");
        Docu docToAdd = new Docu(5, new String[]{"爸dsfdsf爸d级对线"}, new String[]{"dota is better " +
            "than lol"},
                new String[]{"ti1 ti2 ti3 ti4 ti5.."}, new Long[]{new Date().getTime()});
        lbserver.addBean(docToAdd);
        lbserver.commit();
    }

}
