mkdir -p ~/fisco/Data-Export
git clone https://gitee.com/WeBankBlockchain/Data-Export.git "$HOME/fisco/Data-Export"
sed -i 's/groupId=1/groupId=1,2/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/cryptoTypeConfig=0/cryptoTypeConfig=1/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/test?auto/dataexport0?auto/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/db0.user=/db0.user=root/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/db0.password=/db0.password=123456/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/#system.db1.dbUrl=jdbc:mysql:\/\/\[ip\]:\[port\]\/\[db\]/system.db1.dbUrl=jdbc:mysql:\/\/127.0.0.1:3306\/dataexport1/g' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/#system.db1.user=/system.db1.user=root/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/#system.db1.password=/system.db1.password=123456/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/db.sharding=false/db.sharding=true/' ~/fisco/Data-Export/tools/config/application.properties
sed -i 's/PerDatasource=0/PerDatasource=2/' ~/fisco/Data-Export/tools/config/application.properties
mkdir -p ~/fisco/Data-Export/tools/config/gm
cp ~/fisco/nodes/127.0.0.1/sdk/gm/* ~/fisco/Data-Export/tools/config/gm/
rm ~/fisco/Data-Export/tools/config/solidity/HelloWorld.sol
mkdir -p ~/fisco/Data-Export/tools/config/solidity/abi/ 
mkdir -p ~/fisco/Data-Export/tools/config/solidity/bin/sm 
cp ~/fisco/console/contracts/solidity/DetailCRUD.sol ~/fisco/Data-Export/tools/config/solidity/
cp ~/fisco/console/contracts/solidity/RecordCRUD.sol ~/fisco/Data-Export/tools/config/solidity/
cp ~/fisco/console/contracts/sdk/abi/sm/DetailCRUD.abi ~/fisco/Data-Export/tools/config/solidity/abi/
cp ~/fisco/console/contracts/sdk/abi/sm/RecordCRUD.abi ~/fisco/Data-Export/tools/config/solidity/abi/
cp ~/fisco/console/contracts/sdk/bin/sm/DetailCRUD.bin ~/fisco/Data-Export/tools/config/solidity/bin/sm/
cp ~/fisco/console/contracts/sdk/bin/sm/RecordCRUD.bin ~/fisco/Data-Export/tools/config/solidity/bin/sm/
