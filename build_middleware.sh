set -e
set -x

wget -P ~/fisco https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/WeBASE/releases/download/v1.5.2/webase-deploy.zip

unzip ~/fisco/webase-deploy.zip

sed -i 's/mysql.password=dbPassword/mysql.password=123456/g' ~/fisco/webase-deploy/common.properties
sed -i 's/mysql.user=dbUsername/mysql.user=root/g' ~/fisco/webase-deploy/common.properties
sed -i 's/encrypt.type=0/encrypt.type=1/' ~/fisco/webase-deploy/common.properties
sed -i 's/encrypt.sslType=0/encrypt.sslType=1/' ~/fisco/webase-deploy/common.properties
sed -i 's/fisco.version=2.7.2/fisco.version=2.8.0/' ~/fisco/webase-deploy/common.properties 
sed -i 's/node.counts=nodeCounts/node.counts=4/' ~/fisco/webase-deploy/common.properties
sed -i 's/exist.fisco=no/exist.fisco=yes/' ~/fisco/webase-deploy/common.properties
sed -i "s/fisco.dir=\/data\/app/fisco.dir=\/home\/$USER\/fisco/" ~/fisco/webase-deploy/common.properties

bash ~/fisco/nodes/127.0.0.1/start_all.sh

