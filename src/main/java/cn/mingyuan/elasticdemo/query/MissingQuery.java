package cn.mingyuan.elasticdemo.query;

import cn.mingyuan.elasticdemo.base.BaseTransportClient;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.HashMap;
import java.util.Map;

/**
 * 本类演示如何获取缺失字段的文档，详细信息看query方法<br/>
 *
 * 2.x版本的MissingQuery已经在5.x中抛弃了<br/>
 * 在 {@link BoolQueryBuilder#mustNot(QueryBuilder)} 中使用{@link ExistsQueryBuilder} 实现MissingQuery功能
 *
 * @author jiangmingyuan@myhaowai.com
 * @version 2016/11/18 9:49
 * @since jdk1.8
 */
public class MissingQuery extends BaseTransportClient {
    private String indexName = "test1";
    private String typeName = "type1";

    public void buildIndex() {
        Map<String, Object> map = new HashMap();
        map.put("id", 1);
        map.put("name", "lucy2");
        map.put("age", 10);

        IndexResponse resp = client.prepareIndex(indexName, typeName).setSource(map).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        System.out.println(resp);

        map = new HashMap<>();
        map.put("id", "2");
        map.put("name", "lily");

        resp = client.prepareIndex(indexName, typeName).setSource(map).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        System.out.println(resp);
    }

    public void query() {
        SearchRequestBuilder search = client.prepareSearch(indexName).setTypes(typeName);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("age"));//查询哪些文档缺失age字段
        search.setQuery(boolQueryBuilder);
        SearchResponse resp = search.get();
        System.out.println(resp);
    }

    public void deleteDocs() {
        SearchResponse res = client.prepareSearch(indexName).setTypes(typeName).setQuery(QueryBuilders.matchAllQuery()).get();
        SearchHits hits = res.getHits();
        SearchHit[] searchHits = hits.getHits();
        if (searchHits != null && searchHits.length > 0) {
            for (SearchHit h : searchHits) {
                String id = h.id();
                System.out.println("deleting "+id);
                DeleteResponse deleteResponse = client.prepareDelete(this.indexName, this.typeName, id).get();
                if(deleteResponse.getResult()== DocWriteResponse.Result.DELETED){
                    System.out.println("delete success");
                }
            }
        }
    }

    public static void main(String[] args) {
        MissingQuery test = new MissingQuery();
        test.buildIndex();
        test.query();
        test.deleteDocs();
    }
}
