package io.github.silencelwy.smsapi.tool;


import com.alibaba.fastjson.TypeReference;
import io.github.silencelwy.smsapi.client.AccessKeyUtils;
import io.github.silencelwy.smsapi.client.SmsSendClient;
import io.github.silencelwy.smsapi.client.SmsStringUtils;
import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import io.github.silencelwy.smsapi.vo.MessageSendResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SmsSendTool {
    private static final String MESSAGE_SEND = "message/send";
    private static final String MESSAGE_SEND_BATCH = "message/sendBatch";

    private String domain;
    private String apiKey;
    private String accessKey;

    public SmsSendTool(String domain, String apiKey, String accessKey) {
        this.domain = domain;
        this.apiKey = apiKey;
        this.accessKey = accessKey;
    }

    /**
     * 短信发送。无参短信模板内容发送，即所有手机号接收到的短信内容一样
     *
     * @param templateCode
     * @param phones       手机号。手机号最大个数限制1000
     * @return
     */
    public SmsResponse<MessageSendResVo> send(String templateCode, Set<String> phones, String upExtendCode) {
        return sendSingleton(templateCode, null, phones, upExtendCode);
    }

    /**
     * 短信发送。带参短信模板内容发送，即所有手机号接收到的短信内容一样
     *
     * @param templateCode
     * @param params       参数变量值，一般是模板变量。如果是带动态签名，再增加一个变量。
     * @param phones       手机号。手机号最大个数限制1000
     * @return
     */
    public SmsResponse<MessageSendResVo> send(String templateCode, LinkedList<String> params, Set<String> phones, String upExtendCode) {
        return sendSingleton(templateCode, params, phones, upExtendCode);
    }


    /**
     * 所有号码收到内容一致的短信发送。
     *
     * @param templateCode 模板ID
     * @param params       模板参数
     * @param phoneSet     手机号
     * @return HttpResponse
     * @throws IOException
     */
    private SmsResponse<MessageSendResVo> sendSingleton(String templateCode, LinkedList<String> params, Set<String> phoneSet, String upExtendCode) {
        if (SmsStringUtils.isBlank(templateCode)) {
            return SmsResponse.error(40004,"模板id不能为空");
        }
        if (phoneSet == null || phoneSet.size() == 0) {
            return SmsResponse.error(40004,"手机号码数量不能为空");
        }
        if (!SmsStringUtils.isBlank(upExtendCode)) {
            if (!SmsStringUtils.isNumeric(upExtendCode)) {
                return SmsResponse.error(40004,"扩展号必须为数字");
            }
        }
        Set<String> phones = phoneSet.stream().filter(phone ->
                SmsStringUtils.checkPhone(phone)).collect(Collectors.toSet());
        if (phones == null || phones.size() == 0) {
            return SmsResponse.error(40004,"手机号码格式错误，没有可用的手机号");
        }
        if (phones.size() > 1000) {
            return SmsResponse.error(40004,"手机号码数量一次性不能超过1000");
        }

        String phonesStr = phones.stream().collect(Collectors.toSet()).stream().collect(Collectors.joining(","));
        Map<String, Object> bodyParams = new HashMap<>(4);
        bodyParams.put("phones", phonesStr);
        //模板变量内容
        if (params != null && params.size() > 0) {
            bodyParams.put("templateParam", JSON.toJSONString(params));
        }
        bodyParams.put("templateCode", templateCode);
        if (!SmsStringUtils.isBlank(upExtendCode)) {
            bodyParams.put("upExtendCode", upExtendCode);
        }

        Map<String, Object> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_SEND;
        request.setUrl(url);
        ApiResponse post = SmsSendClient.post(request);
        int status = post.getStatus();
        if (status != 200) {
            return SmsResponse.error(status, "网络请求异常，域名或者请求地址不正确");
        }
        String body = new String(post.getBody());
        SmsResponse<MessageSendResVo> smsResponse = JSON.parseObject(body, new TypeReference<SmsResponse<MessageSendResVo>>() {
        });
        return smsResponse;
    }

    /**
     * 所有号码收到内容不一致
     *
     * @param templateCode    模板ID
     * @param phonesAndParams 模板参数
     * @return HttpResponse
     * @throws IOException
     */
    public SmsResponse<MessageSendResVo> sendBatch(String templateCode, Map<String, LinkedList<String>> phonesAndParams, String upExtendCode) {
        if (SmsStringUtils.isBlank(templateCode)) {
            return SmsResponse.error(40004,"模板id不能为空");
        }
        if (phonesAndParams == null || phonesAndParams.size() == 0) {
            return SmsResponse.error(40004,"phonesAndParams参数不能为空");
        }
        if (!SmsStringUtils.isBlank(upExtendCode) && !SmsStringUtils.isNumeric(upExtendCode)) {
            return SmsResponse.error(40004,"扩展号必须为数字");
        }

        String phonesJSON = JSON.toJSONString(phonesAndParams.keySet());
        Map<String, Object> bodyParams = new HashMap<>(4);
        bodyParams.put("phonesJson", phonesJSON);
        //模板变量内容
        bodyParams.put("templateParamJson", JSON.toJSONString(phonesAndParams.values()));
        bodyParams.put("templateCode", templateCode);
        if (!SmsStringUtils.isBlank(upExtendCode) ) {
            bodyParams.put("upExtendCode", upExtendCode);
        }

        Map<String, Object> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_SEND_BATCH;
        request.setUrl(url);
        ApiResponse post = SmsSendClient.post(request);
        int status = post.getStatus();
        if (status != 200) {
            return SmsResponse.error(status, "网络请求异常，域名或者请求地址不正确");
        }
        String body = new String(post.getBody());
        SmsResponse<MessageSendResVo> smsResponse = JSON.parseObject(body, new TypeReference<SmsResponse<MessageSendResVo>>() {
        });
        return smsResponse;
    }
}
