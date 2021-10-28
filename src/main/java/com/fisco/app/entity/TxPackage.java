package com.fisco.app.entity;


public class TxPackage {
    private final String indexName;
    private final String srcData;
    private final String hash;
    private final String uuid;
    public TxPackage(String indexName, byte[]srcData, String hash, String uuid){
        this.indexName=indexName.toLowerCase();
        this.srcData= new String(srcData);
        this.hash=hash;
        this.uuid=uuid;
    }
    public TxPackage(String indexName, String srcData, String hash, String uuid){
        this.indexName=indexName.toLowerCase();
        this.srcData= srcData;
        this.hash=hash;
        this.uuid=uuid;
    }
    public String getIndexName() {
        return indexName;
    }

    public String getSrcData() {
        return srcData;
    }

    public String getHash() {
        return hash;
    }

    public String getUuid() {
        return uuid;
    }
}
