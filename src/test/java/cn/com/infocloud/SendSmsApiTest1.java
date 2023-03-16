package cn.com.infocloud;

import cn.com.infocloud.client.DomainEnum;
import cn.com.infocloud.vo.ApiResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import java.util.*;

public class SendSmsApiTest1 {
    /**
     * 没有变量的模板发送，即模板内容固定
     * 如模板号：7671218232469217792
     * 模板内容：你有一个事务需要处理。
     */
    @Test
    public void testNoVar() {
//        String apiKey = "f96d8488c789fc7341e9875d4c631b7";
        String apiKey = "你自己的apiKey";
//        String accessKey = "2cfd72a0d9ad4c257d802a34364781b88ea09471120a1d23686584b958d227b";
        String accessKey = "你自己的accessKey";
        SendSmsApi sendSmsApi = SendSmsApi.getInstance(DomainEnum.DEFAULT.getUrl(), apiKey, accessKey);
        //模板号
        String templateCode = "833337332607283200";
        //手机号码，最多不超过1000位
        Set<String> phoneSet = new HashSet<>();
        phoneSet.add("18116545110");
        phoneSet.add("18216545111");
        try {
            ApiResponse send = sendSmsApi.send(templateCode, phoneSet);
            //如果有扩展号需求，替换为以下方法，末尾输入你自己的扩展号,扩展号为数字
//            ApiResponse send = sendSmsApi.send(templateCode, phoneSet,"40");
            System.out.println(JSON.toJSONString(send));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 普通带变量的模板发送，模板内容有变量
     * 如模板号：7671218232469217791
     * 模板内容：你有一个事务编号为{事务编号,30}需要处理。
     */
    @Test
    public void testHasVar() {
//        String apiKey = "f96d8488c789fc7341e9875d4c631b7";
        String apiKey = "你自己的apiKey";
//        String accessKey = "2cfd72a0d9ad4c257d802a34364781b88ea09471120a1d23686584b958d227b";
        String accessKey = "你自己的accessKey";
        SendSmsApi sendSmsApi = SendSmsApi.getInstance(DomainEnum.DEFAULT.getUrl(), apiKey, accessKey);
        //模板号
        String templateCode = "833337635977097216";
        //变量参数集合，按照顺序传入
        LinkedList<String> paramList = new LinkedList<>();
        //模板内容：你有一个事务编号为{事务编号,30}需要处理。
        //这里输入变量内容，编号
        paramList.add("1xhsf1282abhx5");

        //手机号码，最多不超过1000位
        Set<String> phoneSet = new HashSet<>();
        phoneSet.add("18116545110");
        phoneSet.add("18216545111");
        try {
            ApiResponse send = sendSmsApi.sendHasVar(templateCode, paramList, phoneSet);
            //如果有扩展号需求，替换为以下方法，末尾输入你自己的扩展号,扩展号为数字
            //ApiResponse send = sendSmsApi.sendHasVar(templateCode, paramList, phoneSet, "xx");
            System.out.println(JSON.toJSONString(send));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 普通带变量的模板发送，模板内容有变量，每个手机号收到的内容不一样
     * 如模板号：7671218232469217791
     * 模板内容：你本次收入${收入,10}元。
     * <p>
     * // 18116545110收到 【企业签名】你本次收入300.09元。
     * // 18767892205收到 【企业签名】你本次收入222元。
     */
    @Test
    public void testBatch() {
//        String apiKey = "f96d8488c789fc7341e9875d4c631b7";
        String apiKey = "你自己的apiKey";
//        String accessKey = "2cfd72a0d9ad4c257d802a34364781b88ea09471120a1d23686584b958d227b";
        String accessKey = "你自己的accessKey";
        SendSmsApi sendSmsApi = SendSmsApi.getInstance(DomainEnum.DEFAULT.getUrl(), apiKey, accessKey);
        //模板号
        String templateCode = "833337635977097216";
        //多个手机号，收到的内容不一致
        try {
            Map<String, LinkedList<String>> phonesAndParams = new HashMap<>();
            // 18116545110收到 【企业签名】你本次收入300.09元。
            String phone1 = "18116545110";
            LinkedList<String> paramList1 = new LinkedList<>();
            paramList1.add("300.09");
            phonesAndParams.put(phone1, paramList1);
            // 18767892205收到 【企业签名】你本次收入222元。
            String phone2 = "18767892205";
            LinkedList<String> paramList2 = new LinkedList<>();
            paramList2.add("222");
            phonesAndParams.put(phone2, paramList2);

            ApiResponse send = sendSmsApi.sendPhoneToContent(templateCode, phonesAndParams);
            //如果有扩展号需求，替换为以下方法，末尾输入你自己的扩展号,扩展号为数字
            //ApiResponse send = sendSmsApi.sendPhoneToContent(templateCode, phonesAndParams,"xx");
            System.out.println(JSON.toJSONString(send));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
