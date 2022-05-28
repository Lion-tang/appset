首先生产商生产出产品后上传产品，这一步是上传detail的属性，比较全面，会自动分配一个Uid，把这个uid生成二维码溯源码，贴到商品上

中间节点会扫码解析这个二维码，生成追溯商品的的uid，上传节点Record信息

### 文件结构说明

```
├─account
│  └─ecdsa
├─conf
└─src
    ├─main
    │  ├─java
    │  │  └─org
    │  │      └─hust
    │  │          └─app
    │  │              ├─client
    │  │              ├─common
    │  │              ├─config
    │  │              ├─contract
    │  │              ├─controller
    │  │              ├─entity
    │  │              │  └─VO
    │  │              ├─mapper
    │  │              ├─service
    │  │              └─utils
    │  └─resources
    │      ├─contract
    │      ├─static
    │      │  ├─files
    │      │  ├─images
    │      │  ├─layui
    │      │  │  ├─css
    │      │  │  │  └─modules
    │      │  │  │      ├─laydate
    │      │  │  │      │  └─default
    │      │  │  │      └─layer
    │      │  │  │          └─default
    │      │  │  ├─font
    │      │  │  └─lay
    │      │  │      └─modules
    │      │  └─layuiadmin
    │      │      ├─css
    │      │      │  └─font-awesome-4.7.0
    │      │      │      ├─css
    │      │      │      └─fonts
    │      │      ├─json
    │      │      │  ├─console
    │      │      │  ├─content
    │      │      │  ├─forum
    │      │      │  ├─layer
    │      │      │  ├─layim
    │      │      │  ├─mall
    │      │      │  ├─message
    │      │      │  ├─table
    │      │      │  ├─upload
    │      │      │  ├─user
    │      │      │  ├─useradmin
    │      │      │  └─workorder
    │      │      ├─layui
    │      │      │  ├─css
    │      │      │  │  └─modules
    │      │      │  │      ├─laydate
    │      │      │  │      │  └─default
    │      │      │  │      ├─layer
    │      │      │  │      │  └─default
    │      │      │  │      └─layim
    │      │      │  │          ├─html
    │      │      │  │          ├─mobile
    │      │      │  │          ├─skin
    │      │      │  │          └─voice
    │      │      │  ├─font
    │      │      │  ├─images
    │      │      │  │  └─face
    │      │      │  └─lay
    │      │      │      └─modules
    │      │      │          └─mobile
    │      │      ├─lib
    │      │      │  └─extend
    │      │      ├─modules
    │      │      ├─style
    │      │      │  └─res
    │      │      │      └─template
    │      │      └─tpl
    │      │          ├─layim
    │      │          └─system
    │      └─templates
    │          ├─component
    │          │  ├─anim
    │          │  ├─auxiliar
    │          │  ├─badge
    │          │  ├─button
    │          │  ├─carousel
    │          │  ├─code
    │          │  ├─flow
    │          │  ├─form
    │          │  ├─grid
    │          │  ├─laydate
    │          │  ├─layer
    │          │  ├─laypage
    │          │  ├─laytpl
    │          │  ├─nav
    │          │  ├─panel
    │          │  ├─progress
    │          │  ├─rate
    │          │  ├─table
    │          │  ├─tabs
    │          │  ├─timeline
    │          │  ├─upload
    │          │  └─util
    │          ├─home
    │          ├─set
    │          │  ├─system
    │          │  └─user
    │          ├─system
    │          ├─template
    │          │  └─tips
    │          └─user
    └─test
        └─java
            └─com
                └─fisco
                    └─app
```



![image-20220124225443417](C:\Users\Lyon\AppData\Roaming\Typora\typora-user-images\image-20220124225443417.png)

- account区块链账户文件：Dapp合法用户有自己的公钥，交易时需要
- Dapp配置文件：区块链底层签署给，Dapp节点调用sdk的证书、公钥等
- client调用SDK：Dapp刚启动时会使用某些合约，这时会调用合约加载sdk或者部署sdk，一般是加载sdk，加载成功日志弹出`调用合约成功`类似的信息
- common sdk常用功能：每次产生一个sdk调用对象会消耗系统资源，这里采用享元设计模式，可以将一些常用的合约sdk调用对象存储起来，以后调用
- config配置：类似过滤器等程序需要的配置类
- contract fiscoSDK：区块链编译智能合约生产的javasdk源码
- controller 控制器类：负责前端逻辑处理
- entity 实体类：负责后端返回到前端的json数据，决定了返回json的结构
- mapper 持久化类： 查询链下数据库
- service 服务器类：文件hash、上传、下载文件等等功能性源文件
- util 公用功能：系统权限控制、密码验证、账号登陆等公用功能
- contract ：包含智能合约源文件
- static：包含系统的静态资源：包括不限于js、css、jpg、png等
- templates：系统前端页面
- spring配置：`application.yml`包含mybatis的配置、日志框架等，`applicationContext.xml`包含Dapp接入的区块链节点ip、端口、本地的account、conf配置等
- banner.txt：每次系统启动时，终端的徽标，目前是Hust
- conf.ini：后期运维人员可编辑这个文件改变系统的一些自定义配置



### 接口说明

默认`RecordCRUD`智能合约部署到群组1，`DetailCRUD`智能合约部署到群组2

##### CRUDClient.java

类描述： 可以不继承ApplicationRunner，这里是让spring容器启动部署合约

```java
    public void insertDetail(String uid, String attr) 
    /*	功能：为了postman测试， dapp通过有用户账户构造的DetailCRUD发起交易
        输入：唯一商品标识码uid，详细交易的属性attr
        输出：无
        执行：调用智能合约DetailCRUD的insert接口函数
        样例：insertDetail("e8c18fe0-29da-498c-9a95-2ee01f36867e","陈小春,上海港口,法国港口....")
       */

    public void insertRecord(String main_tx_hash, String uid, String locate) 
    /* 	功能：为了postman测试，dapp通过用户账户构造的RecordCRUD发起交易,目前把uid和locate当作常用查询属性，后期可以自定义常用查询属性
        输入：detail交易的hash值 main_tx_hash, 唯一商品标识码uid, 地址locate
        输出：无
        执行：调用智能合约RecordCRUD的insert接口函数
        样例：insertRecord("0x7de71362da4a520cdf8c7f4b7bc4f1650e83b2f75551367b6c70976c3b2405c1","e8c18fe0-29da-498c-9a95-2ee01f36867e","海南省海口市")
        */
    
    public  Tuple3 queryRecord(String uid) throws ContractException
    /* 	功能：为了postman测试，dapp通过queryRecord查询交易
        输入：唯一商品标识码uid
        输出：多组(main_tx_hash,uid,locate)组成的tuple3数据
        执行：调用智能合约RecordCRUD的query接口函数
        样例：queryRecord("0x7de71362da4a520cdf8c7f4b7bc4f1650e83b2f75551367b6c70976c3b2405c1")
        */    
    
    public void run(ApplicationArguments args) throws Exception 
    /*	功能：应用启动加载默认账户，防止每次生成匿名账户和智能合约地址
        输入：系统启动参数
        输出：日志会打印"调用CRUDClient的加载合约地址{}方法"
        执行：默认以0x67611f776c55565e921b00a4a23c65dc4e9f67df账户加载detailCRUD智能合约
        */
    
    public Object getCRUDFromAccount(Integer groupNum, String accountFile, int contractChoice)
    /*	功能：从账户accountFile加载群组groupNum的智能合约,contractChoice=1加载RecordCRUD智能合约，contractChoice=2加载DetailCRUD智能合约
        输入：群组号groupNum,账户accountFile,智能合约选择contractChoice
        输出：智能合约对象Object
        执行：通过特定账户和群组号调用特定合约的加载方法load()
        样例：getCRUDFromAccount(2, "0x67611f776c55565e921b00a4a23c65dc4e9f67df", detailChoice)
        */
```

**CommonClient.java**

类描述：提供一个发布合约的方法，并提供了基本实现方法，可以继承此类实现自己的方法

```java
    public <T> void deploy(String contractName, Class<T> clazz,BcosSDK sdk,Integer groupId) throws ContractException, NoSuchMethodException, InvocationTargetException, IllegalAccessException 
    /*	功能：部署智能合约
        输入：合约名contractName，合约类类名，javasdk类，群组号groupId
        输出：无
        执行：通过合约名、合约类、群组号部署智能合约
        样例：deploy("HelloWorld",Helloworld.class,bcosSDK,1)
        */
    
    public Object getContract(String contractName) 
    /*	功能：获得合约类对象
        输入：合约名contractName
        输出：合约类对象
        执行：通过合约名contractName获得合约类对象，若合约名不存在，返回null
        样例：getContract("HelloWorld")
        */
    
	public Map<String, Object> getContractMap()
    /*	功能：获得合约的享元map
        输入：无
        输出：合约享元map
        执行：获得享元map，减少系统对象生成和垃圾回收，map中一个合约名对应一个合约对象
        样例：getContractMap()
        */
    
	public void setContractMap(Map<String, Object> contractMap)
	/*	功能：设置合约的享元map
		输入：合约享元对象map
		输出：无
		执行：将已存在的享元map设置为目前全局map
		样例：setContractMap(contractMap)
		*/
```

**ErrorPageConfig.java**

类描述：错误信息拦截类，发生400或500类错误拦截，返回较为友好的前端页面

```java
public void registerErrorPages(ErrorPageRegistry registry)
    /*	功能：配置拦截方法
    	输入：无，错误信息发生时，会自动注册到这个方法
    	输出：无
    	执行：将错误拦截，返回Controller中预置的页面
    	样例：用户使用不存在的资源时，返回笑脸，表示资源不存在
    	*/
```

**FiscoConfig.java**

类描述：加载xml配置文件，读取共识节点ip：port等

**GoodsComparator.java**

类描述：比较器，用于测试商品类的比较

**MybatisConfig.java**

类描述：配置`Mybatis`分页的拦截器

**DetailCRUD.java、RecordCRUD.java、TestCRUD.java**

类描述：智能合约编译后的java文件

**CrudController.java**

类描述：可通过postman调用该Controller的方法，进一步调用sdk测试接口

```java
public ResponseData insert(@RequestBody Goods goods)
    /*	功能：调用区块链发送商品信息的RecordCRUD合约上链接口
    	输入：商品goods
    	输出：返回前端的json数据。包含新增成功的提示
    	执行：调用智能合约的insertRecord接口
    	样例：insert(goods)
    */
    
public ResponseData insertDetail(@RequestBody Goods goods)
    /*	功能：调用区块链发送商品信息的DetailCRUD合约上链接口
    	输入：商品goods
    	输出：返回前端的json数据，包含新增成功的提示
    	执行：调用智能合约的insertDetail接口
    	样例：insertDetail(goods)
    	*/
```

**TxController.java**

类描述：交易相关Controller，包含文件上传、查询主链、查询辅链、下载文件等接口

```java
public ResponseData upload(MultipartFile file, @RequestParam("uid") String uid, @RequestParam("desc")String desc, Principal principal)
    /*	功能：将多媒体文件做hash处理，得到多媒体数据上链指纹，同其他数据一同上链
    	输入：多媒体文件，商品唯一标识码uid，描述信息desc，用户登录系统信息principal
    	输出：返回前端json数据，包含执行结果提示
    	执行：如果多媒体文件不存在，返回“请求接口数据异常”json数据；如果多媒体文件为空，返回“文件为空，上传失败”的json数据；如果调用查询链上数据发现有相同多媒体文件数据返回“文件上传失败，文件已经存在”的json数据；如果发生异常，返回“文件上传异常”的json数据
    	样例：upload(file,"e8c18fe0-29da-498c-9a95-2ee01f36867e","描述信息",principal)
    	*/
    	
public ResponseListData queryRecord(@RequestParam("uid") String uid)
    /*	功能：查询主链数据
    	输入：商品唯一标识码uid
    	输出：返回前端json数据，包含执行结果提示
    	执行：如果uid为null或""，返回“请输入查询标识号”的json数据；如果查询发生异常，返回“服务器内部错误”的json数据；如果记录数为null，返回”未找到记录“的json数据。
    	样例：queryRecord("e8c18fe0-29da-498c-9a95-2ee01f36867e")
    	*/

public ResponseListData queryDetail(@RequestParam("txHash") String txHash)
    /*	功能：查询辅链数据
    	输入：交易hash
    	输出：返回前端json数据，包含执行结果提示
    	执行：如果uid为null或""，返回“请输入查询标识号”的json数据；如果查询发生异常，返回“服务器内部错误”的json数据；如果记录数为null，返回”未找到记录“的json数据。
    	样例：queryDetail("e8c18fe0-29da-498c-9a95-2ee01f36867e")
    	*/

public void download(@RequestParam("fileNames") String fileNames, HttpServletResponse response)
    /* 	功能：下载多媒体文件
    	输入：文件上链指纹
    	输出：前端下载文件
    	执行：调用fileService.zipDownload接口
    	样例：download("7c4a8d09ca3762af61e5",response)
    	*/
```

**UserController.java**

类描述：用户登录、注册、删除等相关接口

```java
public ResponseData register(@RequestBody Customer user)
    /* 	功能：注册用户
    	输入：用户信息user
    	输出：返回前端json数据
    	执行：调用userService.register接口
    	样例：register(user)
    	*/
    
public ResponseData deleteUser(@RequestBody List<String> batchUsername)
    /* 	功能：删除用户
    	输入：批量用户名batchUsername
    	输出：返回前端json数据
    	执行：调用userService.deleteUser接口
    	样例：deleteUser(batchUsername)
    	*/
    
public ResponseListData showUserAndAdmin(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit)
    /*	功能：分页显示用户和管理员
    	输入：分页当前页和每页限制数
    	输出：返回前端json数据
    	执行：调用userService.showUserAndAdmin接口
    	样例：showUserAndAdmin(1,10)
    	*/
    
public ResponseListData showUser(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit)
    /*	功能：分页显示用户
    	输入：分页当前页和每页限制数
    	输出：返回前端json数据
    	执行：调用userService.showUser接口
    	样例：showUser(1,10)
    	*/
```

**ViewController.java**

类描述：负责视图的逻辑转换

**TxDetailVO.java、TxRecordVO.java、Customer.java、Goods.java**

类描述：为了适配前端json数据的data结构建立的类

**ResponseData.java、ResponseListData.java**

类描述：包含了data数据外还添加了响应码code、响应消息msg等的完整json数据结构类

**CustomerMapper.java、TxDetailMapper.java、TxRecordMapper.java**

类描述：持久化类，MybatisPlus配置多数据源，从分片的分布式数据库中查询链下RecordCRUD、DetailCRUD合约数据

**FileServiceImpl.java**

类描述：文件服务类，给TxController提供文件相关接口

```java
public String uploadFile(MultipartFile file, String uid, String desc, String mapKey, String addressFile, String locate) throws Exception
    /*	功能：上传多媒体文件
    	输入：多媒体文件file，商品唯一标识码uid，描述信息desc，用户标识mapKey，用户的群组账户，定位locate
    	输出：返回处理结果字符串
    	执行：先对多媒体文件file判断是否为null，若是返回”未上传任何文件“字符串；获取目前系统的静态资源路径，如果路径不存在，就创建出来，把file后缀取出，将file做SHA-1处理，加上后缀形成新文件名shaDigest，如果shaDigest已经存在，直接返回客户端；文件hash值在文件夹不存在，可以存在，则存储文件，最终向Record、Detail智能合约发送交易。
    	样例：showUser(1,10)
    	*/
    
public List<TxDetailVO> queryDetail(String txHash)
    /*	功能：查询Detail链下数据
    	输入：交易Hash
    	输出：Detail对应的java类形成的列表List
    	执行：调用MybatisPlus的查询接口txDetailMapper.selectDetailVO
    	样例：queryDetail("e8c18fe0-29da-498c-9a95-2ee01f36867e")
    	*/
    
public List<TxRecordVO> queryRecord(String uid)
    /*	功能：查询Record链下数据
    	输入：商品唯一标识码uid
    	输出：Record对应的java类形成的列表List
    	执行：调用MybatisPlus的查询接口recordMapper.selectDetailVO
    	样例：queryDetail("e8c18fe0-29da-498c-9a95-2ee01f36867e")
    	*/
    
public void downloadOne(String fileName, HttpServletResponse response) throws IOException
    /*	功能：根据单个文件名下载文件
    	输入：文件名fileName
    	输出：无
    	执行：调用response输出流把文件输出到客户端
    	样例：downloadOne("example.zip")
    	*/
    
public void zipDownload(HttpServletResponse response, List<String> fileList, String zipFileName) throws FileNotFoundException
    /*	功能：zip打包下载多个文件
    	输入：文件名列表fileList，输出的压缩报名zipFileName
    	输出：无
    	执行：zipEntry先打包成zip然后调用downFile输出到压缩包文件到客户端
    	样例：zipDownload(fileList,"out.zip")
    	*/
    
private void downFile(HttpServletResponse response, String zipFileName)
    /*	功能：为zip打包文件下载
    	输入：输出的压缩包名zipFileName
    	输出：无
    	执行：调用response输出流把文件输出到客户端
    	样例：downFile("out.txt")
    	*/
    
 public void deleteFile(String fileString)
    /*	功能：下载完zip后，服务器删除打包文件
    	输入：压缩包名fileString
    	输出：无
    	执行：删除zip文件
    	样例：deleteFile("out.zip")
    	*/
```

**UserServiceImpl.java**

类描述：为`UserController`提供用户相关接口

```java
public Integer register(Customer user)
    /*	功能：注册用户
    	输入：用户信息user
    	输出：注册操作代码
    	执行：将用户密码做Hash处理，同时为用户生成群组账户
    	样例：register(user)
    	*/
    
public Integer deleteUser(List<String> batchUsername)
    /*	功能：在DApp删除用户，但是区块链并没用删除，由于DApp已经删除了用户对应区块链账户的信息，所以在逻辑上该用户就被删除了
    	输入：用户名列表batchUsername
    	输出：删除操作代码
    	执行：条件删除链下用户数据库
    	样例：deleteUser(batchUsername)
    	*/
    
public ResponseListData showUserAndAdmin(Integer num, Integer limit)
    /*	功能：显示普通用户节点和链管理员节点的接口
    	输入：分页第几页，每页控制记录数limit
    	输出：返回前端json数据，包含执行结果提示
    	执行：MybatisPlus分页显示查询结果
    	样例：showUserAndAdmin(1,10)
    	*/
    
public ResponseListData showUser(Integer num, Integer limit)
    /*	功能：仅显示普通用户节点的接口
    	输入：分页第几页，每页控制记录数limit
    	输出：返回前端json数据，包含执行结果提示
    	执行：MybatisPlus分页显示查询结果
    	样例：showUser(1,10)
    	*/
```

**FileUtils.java**

类描述：文件公用方法

```java
public static File getFileFromSystem(String winChildPath,String linuxChildPath) throws FileNotFoundException
	/*	功能：返回工程项目根目录，winChildPath形如/static/img/,linuxChildPath形如/files/
    	输入：win路径，linux路径
    	输出：返回抽象文件File类
    	执行：根据不同操作系统获取资源路径
    	样例：getFileFromSystem("/static/files/" + fileName, "/files/" + fileName)
    	*/

public  static File getFileFromSystem(String winChildDir,String linuxChildDir,String fileName) throws FileNotFoundException 
    //重载 getFileFromSystem(String winChildPath,String linuxChildPath)方法
   
public static String getCurrentTime()
    /*	功能：获取当前时间，格式为yyyy_MM_dd_HH_mm_ss
    	输入：无
    	输出：时间的String表示
    	执行：simpleDateFormat转换
    	样例：getCurrentTime()
    	*/
```

**HashPasswordEncoder.java、MyPasswordEncoder.java**

类描述：用户密码编码

**LoginSuccessHandler.java**

类描述：用户登录成功后的操作，包括默认跳转页面等

**SpringUtils.java**

类描述：Spring生命周期管理

**ShaUtils.java**

类描述：Hash公用类

```java
public static String code(byte[] bytes,String mode)
    /*	功能：把二进制数组转换为十六进制字符串,可根据需要截取SHA-1产生的摘要位数,mode可选MD5或SHA_1
    	输入：二进制字节数组，Hash选择模式
    	输出：二进制数据Hash后的字符串
    	执行：选取Hash后第9位到第28位
    	样例：getCurrentTime()
    	*/
```

**UserDetailsServiceImpl.java**

类描述：实现从数据库取数据验证登录

**MySecurityConfig.java**

类描述：安全访问控制

```java
protected void configure(AuthenticationManagerBuilder auth) throws Exception
    /*	功能：把前端用户输入的密码hash后与数据库密码比对
    	输入：无
    	输出：无
    	执行：调用HashPasswordEncoder()比对
    	样例：configure(auth)
    	*/
    
public void configure(WebSecurity web) throws Exception
    /*	功能：放行能够访问的静态资源
    	输入：无
    	输出：无
    	执行：
    	样例：configure(web)
    	*/
    
protected void configure(HttpSecurity http) throws Exception 
    /*	功能：根据系统不同角色授予不同权限，对于退出的用户清除cookie
    	输入：无
    	输出：无
    	执行：
    	样例：configure(http)
    	*/
```



