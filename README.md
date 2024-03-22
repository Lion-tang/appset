# appset
如果网络较慢，可以参考[gitee](https://gitee.com/lyontang/appset)仓库

#### 介绍
区块链相关资源文件

#### 软件架构
软件架构说明


#### 安装教程

1.  安装环境，
```bash
安装openssl curl并创建文件夹
sudo apt install -y openssl curl 
mkdir -p ~/fisco 
cd ~/fisco
```

安装jdk1.8

```bash
cd ~

从Oracle官网(https://www.oracle.com/java/technologies/downloads/#java8)选择Java 8或以上的版本下载，例如下载jdk-8uX-linux-x64.tar.gz

解压jdk
tar -zxvf jdk-8uX-linux-x64.tar.gz

配置Java环境,编辑~/.bashrc文件
vim ~/.bashrc

打开以后将下面三句输入到文件里面并保存退出
export JAVA_HOME=~/jdk1.8.X  #这是一个文件目录，非文件，X替换为具体下载版本
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

生效profile
source ~/.bashrc

查询Java版本，出现的版本是自己下载的版本，则安装成功。
java -version
```


下载MYSQL

```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
执行mysql systemctl status mysql命令，若显示如下则安装成功
【mysql  active】
如果显示 access denied,需要如下解决方法，不要用 sudo mysql_secure_installation，该脚本已经弃用
sudo 然后改变密码：
sudo mysql
ALTER USER 'root'@'localhost'
IDENTIFIED WITH mysql_native_password BY 'your_new_password';
如果出现“Your Password Does Not Satisfy the Current Policy Requirements” Error
SET GLOBAL validate_password_length = 1;
再次运行
IDENTIFIED WITH mysql_native_password BY 'your_new_password';

FLUSH PRIVILEGES;
确认一下
SELECT user, pluginFROM mysql.user
应该显示
user    |    plugin
root    |    mysql_native_password

```

创建dataexport0、dataexport1,appsetlogin数据库用于后续数据导出服务使用,appsetlogin导入appsetlogin.sql文件

为了调试方便，可以设置所有机器可以连接root用户，**注意生产环境不要这么做**
```bash
sudo vim /etc/mysql/my.cnf

[mysqld]
bind-address = 0.0.0.0

sudo systemctl restart mysql

sudo ufw allow 3306/tcp

mysql -u root -p

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'your_root_password';
FLUSH PRIVILEGES;

# 如果遇到密码不能设置，是MYSQL8对密码复杂度的限制，可以降低密码要求
SET GLOBAL validate_password_policy=LOW;
ALTER USER 'root'@'%' IDENTIFIED BY '123456';
```

python3.6(Ubuntu)

```bash
// 添加仓库，回车继续
sudo add-apt-repository ppa:deadsnakes/ppa
// 安装python 3.6
sudo apt-get install -y python3.6
sudo apt-get install -y python3-pip
```

PyMySQL下载(Ubuntu)

```bash
sudo apt-get install -y python3-pip
sudo pip3 install PyMySQL
如果不支持Pip可以使用脚本安装
git clone https://github.com/PyMySQL/PyMySQL
cd PyMySQL/
python3 setup.py install
```

2. 运行 `build_chain_and_console.sh`，建立区块链和编译智能合约控制台
```
bash build_chain_and_console.sh
```

3. 运行`build_middleware.sh`，启动中间件，监控和审计区块链

```bash
bash build_middleware.sh

####################################
# 中间件管理命令
cd ~/fisco/webase-deploy/
# 部署并启动所有服务
python3 deploy.py installAll  
# 关闭所有服务
Python3 deploy.py stopAll 
# 启动所有服务
Python3 deploy.py startAll 
```

浏览器输入ip:5000/#/home
初始账户admin,密码Abcd1234,第一次进入会要求修改密码

若想每次登录管理中间件免去登录
可以在`webase-deploy/webase-node-mgr/conf/application.yml`中配置`constant.isUseSecurity`为`false`即可禁用`WeBASE-Node-Manager`的登录鉴权，默认以管理员身份登录Webase后台

在Webase`合约管理`模块上传ERC的4个合约后，选择`v0.6.10-gm` 编译器。WeBASE点击编译、部署ERC20（部署会获得一个合约地址）

拿到`合约地址`，在Webase`私钥管理`模块新增用户(新增后获得一个`用户公钥地址`)

复制`用户公钥地址`和`合约地址`分别到 `application.yml` 的 `address:account` 和 `adress:contract`

4. 运行`build_Data-Export.sh` 区块链数据同步工具
首先通过脚本构建该工具，并配置

```bash
bash build_Data-Export.sh
```

如果发现数据无法同步，这是版本问题，请联系361092772@qq.com，我会提供其他版本协助你

运行程序

```bash
chmod +x start.sh
bash start.sh
# 构建完毕后以后可以直接启动jar
nohup java -jar XXX.jar >temp.out &
```

5. 溯源后台服务（假设后台服务在~/fisco/appset-0.0.1-SNAPSHOT.jar）

配置证书（默认读取/conf/gm下国密证书

```bash
mkdir –p ~/fisco/conf/gm 
cp ~/fisco/nodes/172.168.160.18/sdk/gm/*  ~/fisco/conf/gm  # cp [source] [target] source替换成项目gm实际路径
```

修改部署配置文件 `application.yml` `applicationContext.xml`
```bash
修改application.xml中peers标签的ip地址
修改application.yml中datasources下数据库的ip、密码等，再修改address下webase的ip
```

6. 启动系统
```bash
cd ~/fisco
java -Xbootclasspath/a:./conf -jar appset-0.0.1-SNAPSHOT.jar
```

如遇问题可以联系361092772@qq.com，作者看到会尽力帮助解决