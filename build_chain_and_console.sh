curl  -#Lo ~/fisco/build_chain.sh  https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/FISCO-BCOS/releases/v2.8.0/build_chain.sh

cat >> ~/fisco/ipconf << EOF
127.0.0.1:4 agencyA 1,2
EOF

bash ~/fisco/build_chain.sh -f ~/fisco/ipconf -p 30300,20200,8545 -g -G 

bash ~/fisco/nodes/127.0.0.1/start_all.sh

curl -#Lo ~/fisco/download_console.sh https://gitee.com/FISCO-BCOS/console/raw/master-2.0/tools/download_console.sh

bash download_console.sh

mkdir ~/fisco/console/conf/gm/

cp ~/fisco/nodes/127.0.0.1/sdk/gm/* ~/fisco/console/conf/gm/

cp -n ~/fisco/console/conf/config-example.toml  ~/fisco/console/conf/config.toml

wget -P  ~/fisco/console/contracts/solidity https://github.com/Lion-tang/appset/blob/master/src/main/resources/contract/RecordCRUD.sol

wget -P ~/fisco/console/contracts/solidity https://github.com/Lion-tang/appset/blob/master/src/main/resources/contract/DetailCRUD.sol

