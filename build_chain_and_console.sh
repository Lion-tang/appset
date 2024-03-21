set -e
set -x

curl  -#Lo ~/fisco/build_chain.sh  https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/FISCO-BCOS/releases/v2.8.0/build_chain.sh

cat >> ~/fisco/ipconf << EOF
127.0.0.1:4 agencyA 1,2
EOF

bash ~/fisco/build_chain.sh -f ~/fisco/ipconf -p 30300,20200,8545 -g -G 

bash ~/fisco/nodes/127.0.0.1/start_all.sh

curl -#Lo ~/fisco/download_console.sh https://github.com/FISCO-BCOS/console/releases/download/v2.9.2/download_console.sh

bash download_console.sh

mkdir -p ~/fisco/console/conf/gm/

cp ~/fisco/nodes/127.0.0.1/sdk/gm/* ~/fisco/console/conf/gm/

cp -n ~/fisco/console/conf/config-example.toml  ~/fisco/console/conf/config.toml

wget -P  ~/fisco/console/contracts/solidity https://gitee.com/lyontang/appset/raw/master/plainSol/RecordCRUD.sol

wget -P ~/fisco/console/contracts/solidity https://gitee.com/lyontang/appset/raw/master/plainSol/DetailCRUD.sol

cd ~/fisco/console

bash sol2java.sh -p org.hust.app.contract

cd ~/fisco/