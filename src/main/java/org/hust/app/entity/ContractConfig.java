//package org.hust.app.entity;
//
///**
// @Date 2021/11/20
// @Description 使用Mysql集群代替，暂时不使用Es链下分布式数据库方案
// @author Lyontang
// **/
//
//public class ContractConfig {
//    private String nodeIp;
//    private int nodePort;
//    private String binPath;
//    private String abiPath;
//    private String contractName;
//    private int groupID;
//    private String identityFilePath;
//
//    ContractConfig(String contractNodeIp, int contractNodePort, String contactBinPath, String contractAbiPath, String contractNames, int cGroupID, String cIdentityFilePath){
//        nodeIp=contractNodeIp;
//        nodePort=contractNodePort;
//        binPath=contactBinPath;
//        abiPath=contractAbiPath;
//        contractName=contractNames;
//        groupID=cGroupID;
//        identityFilePath=cIdentityFilePath;
//    }
//
//    public String getNodeIp() {
//        return nodeIp;
//    }
//
//    public int getNodePort() {
//        return nodePort;
//    }
//
//    public String getAbiPath() {
//        return abiPath;
//    }
//
//    public String getBinPath() {
//        return binPath;
//    }
//
//    public String getContractName() {
//        return contractName;
//    }
//
//    public int getGroupID() {
//        return groupID;
//    }
//
//    public String getIdentityFilePath() {
//        return identityFilePath;
//    }
//
//    public void setNodeIp(String nodeIp) {
//        this.nodeIp = nodeIp;
//    }
//
//    public void setNodePort(int nodePort) {
//        this.nodePort = nodePort;
//    }
//
//    public void setAbiPath(String abiPath) {
//        this.abiPath = abiPath;
//    }
//
//    public void setBinPath(String binPath) {
//        this.binPath = binPath;
//    }
//
//    public void setContractName(String contractName) {
//        this.contractName = contractName;
//    }
//
//    public void setGroupID(int groupID) {
//        this.groupID = groupID;
//    }
//
//    public void setIdentityFilePath(String identityFilePath) {
//        this.identityFilePath = identityFilePath;
//    }
//}
