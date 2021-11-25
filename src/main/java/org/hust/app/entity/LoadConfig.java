//package org.hust.app.entity;
//
//
//import org.ini4j.Ini;
//import org.ini4j.Profile;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// @Date 2021/11/20
// @Description 使用Mysql集群代替，暂时不使用Es链下分布式数据库方案
// @author Lyontang
// **/
//
//public class LoadConfig {
//
//    ElasticSearchConfig elasticSearchConfig;
//    ContractConfig contractConfig;
//    public LoadConfig(String configPath) throws IOException {
//        File iniFile=new File(configPath);
//        Ini ini=new Ini(iniFile);
//
//
//        Profile.Section ESSection=ini.get("elasticsearch");
//        String ESHost=ESSection.get("host");
//        int ESPort=Integer.parseInt(ESSection.get("port"));
//        String record_index = ESSection.get("record_index");
//        String detail_index = ESSection.get("detail_index");
//        elasticSearchConfig=new ElasticSearchConfig(ESHost,ESPort,record_index,detail_index);
//
//        Profile.Section contractSection=ini.get("smart_contract");
//        String cNodeIp=contractSection.get("blockchain_node_ip");
//        int cNodePort=Integer.parseInt(contractSection.get("blockchain_node_port"));
//        String cBinPath=contractSection.get("bin_path");
//        String cAbiPath=contractSection.get("abi_path");
//        String cContractName=contractSection.get("contract_name");
//
//        int cGroupID=Integer.parseInt(contractSection.get("group_id"));
//        String cIdentityFilePath=contractSection.get("identity_path");
//        contractConfig=new ContractConfig(cNodeIp,cNodePort,cBinPath,cAbiPath,cContractName,cGroupID,cIdentityFilePath);
//    }
//
//
//    public ElasticSearchConfig getElasticSearchConfig() {
//        return elasticSearchConfig;
//    }
//
//    public ContractConfig getContractConfig() {
//        return contractConfig;
//    }
//
//}
