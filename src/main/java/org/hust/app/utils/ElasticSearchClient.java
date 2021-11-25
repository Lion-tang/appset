//package org.hust.app.utils;
//
//
//import org.elasticsearch.search.sort.SortOrder;
//import org.hust.app.entity.ElasticSearchConfig;
//import org.hust.app.entity.TxPackage;
//import org.apache.http.HttpHost;
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.support.master.AcknowledgedResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.rest.RestStatus;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// @Date 2021/11/20
// @Description 使用Mysql集群代替，暂时不使用Es链下分布式数据库方案
// @author Lyontang
// **/
//
//public class ElasticSearchClient {
//    private final RestHighLevelClient restHighLevelClient;
//    private final ElasticSearchConfig elasticSearchConfig;
//    public ElasticSearchClient(ElasticSearchConfig elasticSearchConfig){
//        this.elasticSearchConfig=elasticSearchConfig;
//        restHighLevelClient=new RestHighLevelClient(RestClient.builder(new HttpHost(elasticSearchConfig.getHost(),elasticSearchConfig.getPort(),"http")));
//    }
////    创建索引（相当于创建mysql中的数据库），暂时不管
//    public void CreateIndex(String indexName) throws IOException {
//        indexName=indexName.toLowerCase();
//        if(ExistIndex(indexName)){
//            return;
//        }
//        CreateIndexRequest createIndexRequest=new CreateIndexRequest(indexName);
//        CreateIndexResponse response=restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//        System.out.println(response.toString());
//    }
////    检查索引是否存在，暂时不管
//    public boolean ExistIndex(String indexName) throws IOException {
//        indexName=indexName.toLowerCase();
//        GetIndexRequest request=new GetIndexRequest(indexName);
//        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
//    }
////    删除索引，暂时不管
//    public boolean DeleteIndex(String indexName) throws IOException {
//        indexName=indexName.toLowerCase();
//        if(!ExistIndex(indexName)){
//            return true;
//        }
//        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest(indexName);
//        AcknowledgedResponse delete=restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//        return delete.isAcknowledged();
//    }
////    插入原始交易数据（就是向es中以键值对的形式插入数据插入数据），返回数据在es中的url
//    public String InsertTxSrcData(TxPackage txPackage) throws IOException {
//        HashMap<String,Object> data=new HashMap<>();
//        data.put("uuid",txPackage.getUuid());
//        data.put("data",txPackage.getSrcData());
//        IndexRequest request=new IndexRequest(txPackage.getIndexName());
//        request.id(txPackage.getHash());
//        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s");
//        request.source(data, XContentType.JSON);
//        IndexResponse index=restHighLevelClient.index(request, RequestOptions.DEFAULT);
//        if(index.status()==RestStatus.CREATED||index.status()==RestStatus.OK){
//            return elasticSearchConfig.getHost()+":"+elasticSearchConfig.getPort()+"/"+txPackage.getIndexName()+"/"+txPackage.getUuid()+"/"+txPackage.getHash();
//        }
//        return null;
//    }
//
////    检查交易数据是否存在
//    public boolean ExistTxData(String indexName,String hash) throws IOException {
//        indexName=indexName.toLowerCase();
//        GetRequest getRequest=new GetRequest(indexName,hash);
//        getRequest.fetchSourceContext(new FetchSourceContext(false));
//        getRequest.storedFields("_none_");
//        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
//    }
////    通过交易交易hash获取获取交易信息
//    public Map<String,Object> GetSrcTxData(String indexName, String hash) throws IOException {
//        indexName=indexName.toLowerCase();
//        GetRequest getRequest=new GetRequest(indexName,hash);
//        GetResponse response=restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
//        return response.getSourceAsMap();
//    }
////    通过若干特定字段获取交易信息
//    public ArrayList<Map<String, Object>> GetSrcTxData(String indexName, Map<String,Object> field) throws IOException {
//        indexName=indexName.toLowerCase();
//        SearchRequest searchRequest=new SearchRequest(indexName);
//        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
//        for (String key : field.keySet()) {
//            Object value = field.get(key);
//            searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
//            searchSourceBuilder.size(20);
//            searchSourceBuilder.sort(key, SortOrder.DESC);
//        }
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse=restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        SearchHit[] hits=searchResponse.getHits().getHits();
//        ArrayList<Map<String,Object>> res = new ArrayList<>();
//        for(SearchHit hit:hits){
//            res.add(hit.getSourceAsMap());
//        }
//        return  res;
//    }
//    public Map<String,Object> GetBlockData(String indexName,String hash) throws IOException {
//        indexName+="_block";
//        return GetSrcTxData(indexName,hash);
//    }
//    public ArrayList<Map<String ,Object>> GetBlockData(String indexName,Map<String,Object> field) throws IOException {
//        indexName+="_block";
//        return GetSrcTxData(indexName,field);
//    }
////    根据交易hash更新交易数据，这个功能应该不需要
//    public String UpdateTxData(TxPackage txPackage) throws IOException {
//        UpdateRequest updateRequest=new UpdateRequest(txPackage.getIndexName(),txPackage.getHash());
//        updateRequest.timeout("1s");
//        HashMap<String,Object> data=new HashMap<>();
//        data.put("uuid",txPackage.getUuid());
//        data.put("data",txPackage.getSrcData());
//        updateRequest.doc(data, XContentType.JSON);
//        UpdateResponse updateResponse=restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//        if(updateResponse.status()==RestStatus.CREATED||updateResponse.status()==RestStatus.OK){
//            return elasticSearchConfig.getHost()+":"+elasticSearchConfig.getPort()+"/"+txPackage.getIndexName()+"/"+txPackage.getUuid()+"/"+txPackage.getHash();
//        }
//        return null;
//    }
////    根据交易hash值删除交易数据，因为交易数据本应该持久化存储，所以感觉这个也不需要
//    public boolean DeleteTxData(String indexName,String hash) throws IOException {
//        indexName=indexName.toLowerCase();
//        DeleteRequest deleteRequest=new DeleteRequest(indexName,hash);
//        deleteRequest.timeout("10s");
//        DeleteResponse deleteResponse=restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
//        return (deleteResponse.status()==RestStatus.OK)||(deleteResponse.status()==RestStatus.NOT_FOUND);
//    }
////    批量插入数据
//    public ArrayList<String> BulkRequest(ArrayList<TxPackage> txs) throws IOException {
//        BulkRequest bulkRequest=new BulkRequest();
//        bulkRequest.timeout("10s");
//        for (TxPackage tx : txs) {
//            Map<String, Object> txData = new HashMap<>();
//            txData.put("uuid", tx.getUuid());
//            txData.put("data", tx.getSrcData());
//            bulkRequest.add(new IndexRequest(tx.getIndexName()).id(tx.getHash()).source(txData, XContentType.JSON));
//        }
//        BulkResponse bulkResponse=restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        ArrayList<String> res=new ArrayList<>();
//        if(bulkResponse.status()==RestStatus.OK){
//            for (TxPackage tx : txs) {
//                res.add(elasticSearchConfig.getHost() + ":" + elasticSearchConfig.getPort() + "/" + tx.getIndexName()+ "/" + tx.getUuid()  + "/" + tx.getHash());
//            }
//        }
//        return res;
//    }
////    记录当前已经转储的区块高度
//    public boolean RecordBlockHeight(String groupID,String contractName,int height) throws IOException {
//        groupID=groupID.toLowerCase();
//        if(!ExistTxData(groupID,contractName)){
//            return insertHeight(groupID,contractName,0);
//        }else{
//            return insertHeight(groupID,contractName,height);
//        }
//    }
////    获取当前智能合约已经转储的高度
//    public int GetBlockHeight(String groupID,String contractName) throws IOException {
//        groupID=groupID.toLowerCase();
//        if(!ExistTxData(groupID,contractName)){
//            return 0;
//        }
//        Map<String,Object> data=GetSrcTxData(groupID,contractName);
//        if(data==null){
//            return 0;
//        }
//        return  Integer.parseInt(data.get("height").toString());
//    }
//    private boolean insertHeight(String groupID,String contractName,int height) throws IOException {
//        HashMap<String, Integer>data=new HashMap<>();
//        data.put("height",height);
//        IndexRequest request=new IndexRequest(groupID);
//        request.id(contractName);
//        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s");
//        request.source(data, XContentType.JSON);
//        IndexResponse index=restHighLevelClient.index(request, RequestOptions.DEFAULT);
//        return index.status()==RestStatus.CREATED||index.status()==RestStatus.OK;
//    }
//    public static String timeStampToFormatDate(long timeStamp) {
//        Date time = new Date(timeStamp);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return simpleDateFormat.format(time);
//    }
//
//
//}
