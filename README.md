# 使用说明

## 引入依赖包
```xml
<dependency>
    <groupId>io.github.silencelwy</groupId>
    <artifactId>jf-sms-send-sdk</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```


## 1：短信发送示例
短信发送一般分为
+ 固定内容短信模板，
+ 带变量的模板的短信发送,
+ 带变量且每个号码发送内容不一样

### 1.1 初始化短信发送api
```java
    //        String apiKey = "f96d8488c789fc7341e9875d4c631b7";
    static String apiKey = "你自己的apiKey";
    //        String accessKey = "2cfd72a0d9ad4c257d802a34364781b88ea09471120a1d23686584b958d227b";
    static String accessKey = "你自己的AccessKey";
    //方式1：默认域名获取示例(推荐)
    private SendSmsApi sendSmsApi = SendSmsApi.getInstance(apiKey, accessKey);
    //方式2：如果是内网环境，无法访问外网，可以让运维开通公网访问ip和端口，以下方式实现初始化
    //private SendSmsApi sendSmsApi= SendSmsApi.getInstance(DomainEnum.IP_PORT.getUrl(), apiKey, accessKey);
    //方式3：如果是内网环境，无法访问外网，如果使用ip域名转发，访问url可以自定义，示例如下
    //private SendSmsApi sendSmsApi= SendSmsApi.getInstance("http://199.12.25.32:25172/", apiKey, accessKey);
```

### 1.1：固定内容短信模板
+ 即模板内容固定，同时每个号码收到的内容也是一致的。

```java
    /**
     * 没有变量的模板发送，即模板内容固定
     * 如模板号：7671218232469217792
     * 模板内容：你有一个事务需要处理。
     */
    @Test
    public void testNoVar() {
        //模板号
//        String templateCode = "833337332607283200";
        String templateCode = "你自己的模板id";
        //手机号码，最多不超过1000位
        Set<String> phoneSet = new HashSet<>();
//        phoneSet.add("1811654xxxx");
//        phoneSet.add("1821654xxxx");
        phoneSet.add("手机号1");
        phoneSet.add("手机号2");

        SmsResponse<MessageSendResVo> send = sendSmsApi.send(templateCode, phoneSet);
        //如果有扩展号或者业务id需求：多传入两个字段 扩展号和自身业务id 两个字段均非必传 如果无须传入，传null即可
        // 扩展号为数字；业务id最长长度为64位
//        SmsResponse<MessageSendResVo> send = sendSmsApi.send(templateCode, phoneSet,"xx","xxxxyyy");
        System.out.println(JSON.toJSONString(send));
        //如果发送有问题，提供该值给平台咨询
        String requestId = send.getRequestId();
        if (200 == send.getCode()){
            System.out.println("提交短信成功");
            MessageSendResVo data = send.getData();
            //每一批短信编号。该值不为空，说明平台已经接收提交的短信任务
            String msgId = data.getMsgId();
            //状态描述
            String desc = data.getDesc();
            //提交失败的手机号，手机号不正确，格式不规范，黑名单等
            String failPhones = data.getFailPhones();
        }
    }
```
### 1.2：带变量的模板的短信发送
+ 模板内容带有变量，通过传入的参数替换其中的变量构成完成短信。
+ 如果是动态签名，需多传入一个签名参数即可。
+ 如果自由发送短信，可以创建一个大变量，即所有模板内容只有一个变量。传入这个变量的参数即为全部短信内容
+ 所有号码收到的短信内容一致的。
```java
    /**
     * 普通带变量的模板发送，模板内容有变量
     * 如模板号：7671218232469217791
     * 模板内容：你有一个事务编号为{事务编号,30}需要处理。
     * 如果存在动态签名，增加一个变量传入即可，
     * 即增加一个变量传入签名
     */
    @Test
    public void testHasVar() {
        //模板号
//        String templateCode = "833337635977097216";
        String templateCode = "你自己的模板id";
        //变量参数集合，按照顺序传入
        LinkedList<String> paramList = new LinkedList<>();
        //模板内容：你有一个事务编号为{事务编号,30}需要处理。
        //如果存在动态签名，即多传入一个变量，如下。一般不需要传入
        //paramList.add("xxx公积金");
        //这里输入变量内容
        paramList.add("1xhsf1282abhx5");

        //手机号码，最多不超过1000位
        Set<String> phoneSet = new HashSet<>();
//        phoneSet.add("1811654xxxx");
//        phoneSet.add("1821654xxxx");
        phoneSet.add("手机号1");
        phoneSet.add("手机号2");

        SmsResponse<MessageSendResVo> send = sendSmsApi.sendHasVar(templateCode, paramList, phoneSet);
        //如果有扩展号或者业务id需求：多传入两个字段 扩展号和自身业务id 两个字段均非必传 如果无须传入，传null即可
        // 扩展号为数字；业务id最长长度为64位
        // SmsResponse<MessageSendResVo> send = sendSmsApi.sendHasVar(templateCode, paramList, phoneSet, "xx","xxxxyyyy");
        System.out.println(JSON.toJSONString(send));
        //如果发送有问题，提供该值给平台咨询
        String requestId = send.getRequestId();
        if (200 == send.getCode()){
            System.out.println("提交短信成功");
            MessageSendResVo data = send.getData();
            //每一批短信编号。该值不为空，说明平台已经接收提交的短信任务
            String msgId = data.getMsgId();
            //状态描述
            String desc = data.getDesc();
            //提交失败的手机号，手机号不正确，格式不规范，黑名单等
            String failPhones = data.getFailPhones();
        }
    }
```
### 1.3：带变量且每个号码发送内容不一样
+ 同上一个内容相似，区别在于每个手机号对应有一个变量内容。即每一个手机号码收到的内容不一致。
```java
    /**
     * 普通带变量的模板发送，模板内容有变量，每个手机号收到的内容不一样
     * 如模板号：7671218232469217791
     * 模板内容：你本次收入${收入,10}元。
     * <p>
     * // 1811654xxxx(手机号1)收到 【企业签名】你本次收入300.09元。
     * // 1876789xxxx(手机号2)收到 【企业签名】你本次收入222元。
     */
    @Test
    public void testBatch() {
        //模板号
//        String templateCode = "833337635977097216";
        String templateCode = "你自己的模板id";
        //多个手机号，收到的内容不一致

        Map<String, LinkedList<String>> phonesAndParams = new HashMap<>();
        // 1811654xxxx收到 【企业签名】你本次收入300.09元。
        String phone1 = "1811654xxxx";
        LinkedList<String> paramList1 = new LinkedList<>();
        //如果存在动态签名，即多传入一个变量，如下。固定签名不需要传入
        //paramList.add("xxx公积金");
        paramList1.add("300.09");
        phonesAndParams.put(phone1, paramList1);
        // 18767892205收到 【企业签名】你本次收入222元。
        String phone2 = "1876789xxxx";
        LinkedList<String> paramList2 = new LinkedList<>();
        //如果存在动态签名，即多传入一个变量，如下。一般不需要传入
        //paramList.add("xxx公积金");
        paramList2.add("222");
        phonesAndParams.put(phone2, paramList2);

        SmsResponse<MessageSendResVo> send = sendSmsApi.sendPhoneToContent(templateCode, phonesAndParams);
        //如果有扩展号或者业务id需求：多传入两个字段 扩展号和自身业务id 两个字段均非必传 如果无须传入，传null即可
        // 扩展号为数字；业务id最长长度为64位
        //SmsResponse<MessageSendResVo> send = sendSmsApi.sendPhoneToContent(templateCode, phonesAndParams,"xx","xxxxyyyy");
        System.out.println(JSON.toJSONString(send));
        //如果发送有问题，提供该值给平台咨询
        String requestId = send.getRequestId();
        if (200 == send.getCode()){
            System.out.println("提交短信成功");
            MessageSendResVo data = send.getData();
            //每一批短信编号。该值不为空，说明平台已经接收提交的短信任务
            String msgId = data.getMsgId();
            //状态描述
            String desc = data.getDesc();
            //提交失败的手机号，手机号不正确，格式不规范，黑名单等
            String failPhones = data.getFailPhones();
        }
    }
  ```
  
### 1.4：短信发送响应内容：
  

| 响应参数说明 |        |  备注   | 描述        |
|--------------|:-------|--------|:------------|
| code         |        |   响应码   | 200表示成功，如果不是200，参考最后返回码说明 |
| message      |        |        | 状态码的描述 |
| requestId    |        |        |问题排查时，提供该字段给短信平台|
| data         |        |        |           |
|              | msgId  |        |每一批短信编号。该值不为空，说明平台已经接收提交的短信任务，由此数据可查询是否到达，参考回执查询接口          |
|              | failPhones |        |  提交失败的手机号  |

## 2：回执报告查询
通过发送接口返回的msgId，查询短信发送情况
### 2.1： 初始化回执查询api
```java
    //        String apiKey = "f96d8488c89b23fc7a1e3475d4c631b7";
    private String apiKey = "你自己的apiKey";
    //        String accessKey = "2cfd72a0d9ad4c234d802a87fa0ca81b88ea09471120a1d23686584b958d227b";
    private String accessKey = "你自己的accessKey";
    //方式1：默认域名获取示例(推荐)
    private ArriveInfoApi arriveInfoApi = ArriveInfoApi.getInstance(apiKey, accessKey);
    //方式2：如果是内网环境，无法访问外网，可以让运维开通公网访问ip和端口，以下方式实现初始化
    //private ArriveInfoApi arriveInfoApi = ArriveInfoApi.getInstance(DomainEnum.IP_PORT.getUrl(), apiKey, accessKey);
    //方式3：如果是内网环境，无法访问外网，如果使用ip域名转发，访问url可以自定义，示例如下
    //private ArriveInfoApi arriveInfoApi = ArriveInfoApi.getInstance("http://199.12.xx.xx:25172/",apiKey, accessKey);
```
### 2.2：回执查询示例
```java
    /**
     * 任务号：836281956443505664，836281566675434496
     * 只支持查询24小时以内记录
     * 查询到达结果，多个查询结果，返回以下示例结构
     */
    @Test
    public void testGetArriveInfo() {
        //任务号，最多不超过600，提交发送成功后返回的msgId
        Set<String> msgIdSet = new HashSet<>();
//        msgIdSet.add("836281956443505664");
//        msgIdSet.add("836281566675434496");
        msgIdSet.add("填入msgId");
        SmsResponse<List<ArriveInfoResVo>> smsResponse = arriveInfoApi.getArriveInfo(msgIdSet);
        System.out.println(JSON.toJSONString(smsResponse));
        if (200 == smsResponse.getCode()){
            System.out.println("查询回执成功");
            List<ArriveInfoResVo> data = smsResponse.getData();
            if (data !=null && data.size()>0){
                data.forEach(arriveInfoResVo -> {
                    //回执结果，DELIVRD表示到达成功，其他结果表示失败
                    String arrive = arriveInfoResVo.getArrive();
                    //手机号
                    String tel = arriveInfoResVo.getTel();
                    //任务号
                    String msgId = arriveInfoResVo.getMsgId();
                    //短信发送时传入的业务id，发送时没有传入该字段，字段返回为空（可无视）
                    //String bid = arriveInfoResVo.getBid();
                });
            }
        }
    }
```
### 2.3：回执结果
| 响应参数说明 |        |  备注   | 描述        |
|--------------|:-------|--------|:------------|
| code         |        |   响应码   | 200表示成功，如果不是200，参考响应码说明 |
| message      |        |        | 状态码的描述 |
| requestId    |        |        |问题排查时，提供该字段给短信平台|
| data         |        |        |           |
|              | msgId  |        |每一批短信编号         |
|              | tel |        |  提交失败的手机号  |
|              | arrive |        |  除DELIVRD外，其他都是未送达  |
|              | bid |        |  短信发送时传入的bid，没有使用到可无视该字段即可  |

## 三：返回码说明
每次调用接口时，可能获得正确或错误的返回码，开发者可以根据返回码信息调试接口，排查错误。
全局返回码说明如下：

|响应码	| 描述 |
|--------------|:-------|
|-1	  |  系统繁忙，请稍后再试或者其他原因|
| 200	|   请求成功|
| 2000	|IP白名单限制|
| 2001	|API授权错误|
| 6000	|访问频繁,请稍后再试|
| 40002|	余额不足|
| 40004|	参数错误,详情见提示|
| 40005|	超出模板参数设置限制|
| 40006|	扩展号不匹配或者被企业其他账号占用|
| 60001|	模板不存在|
| 60002|	批量发送下电话数量和参数数量不一样|
| 60003|	短信变量个数和模板变量个数不一样|
| 60004|	变量值超过模板设置的变量值最大长度|
| 60005|	没有可以发送的内容|
| 60006|	模板参数变量格式错误|
| 60007|	手机号参数格式错误|
| 60008|	手机号数量大于1000|

  
  