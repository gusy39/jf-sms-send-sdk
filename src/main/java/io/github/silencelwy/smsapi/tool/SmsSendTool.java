package io.github.silencelwy.smsapi.tool;


import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import io.github.silencelwy.smsapi.client.AccessKeyUtils;
import io.github.silencelwy.smsapi.client.SmsSendClient;
import io.github.silencelwy.smsapi.client.SmsStringUtils;
import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import io.github.silencelwy.smsapi.vo.MessageSendResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

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
     * 所有号码收到内容一致的短信发送。
     *
     * @param templateCode 模板ID
     * @param params       模板参数
     * @param phoneSet     手机号
     * @param bid          客户端业务ID，长度不超过64字符
     * @return HttpResponse
     * @throws IOException
     */
    public SmsResponse<MessageSendResVo> sendSingleton(String templateCode, LinkedList<String> params, Set<String> phoneSet, String upExtendCode, String bid) {
        if (SmsStringUtils.isBlank(templateCode)) {
            return SmsResponse.error(40004, "模板id不能为空");
        }
        if (phoneSet == null || phoneSet.size() == 0) {
            return SmsResponse.error(40004, "手机号码数量不能为空");
        }
        if (!SmsStringUtils.isBlank(upExtendCode) && !SmsStringUtils.isNumeric(upExtendCode)) {
            return SmsResponse.error(40004, "扩展号必须为数字");
        }
        if (!SmsStringUtils.isBlank(bid) && bid.length() > 64) {
            return SmsResponse.error(40004, "业务bid长度不能超过64位");
        }
        Set<String> phones = new HashSet<>();

        for (String phone:phoneSet){
            if (SmsStringUtils.checkPhone(phone)){
                phones.add(phone);
            }
        }
        if (phones == null || phones.size() == 0) {
            return SmsResponse.error(40004, "手机号码格式错误，没有可用的手机号");
        }
        if (phones.size() > 1000) {
            return SmsResponse.error(40004, "手机号码数量一次性不能超过1000");
        }


        StringBuffer stringBuffer = new StringBuffer();
        for (String phone:phones){
            stringBuffer.append(phone+",");
        }
        String phoneStr = stringBuffer.toString();
        if (SmsStringUtils.isBlank(phoneStr)){
            return SmsResponse.error(40004,"请传入正确的任务id");
        }else {
            phoneStr = phoneStr.substring(0,phoneStr.length()-1);
        }

        Map<String, String> bodyParams = new HashMap<>(4);
        bodyParams.put("phones", phoneStr);
        //模板变量内容
        if (params != null && params.size() > 0) {
            bodyParams.put("templateParam", JSONUtil.toJsonStr(params));
        }
        bodyParams.put("templateCode", templateCode);
        if (!SmsStringUtils.isBlank(upExtendCode)) {
            bodyParams.put("upExtendCode", upExtendCode);
        }
        if (!SmsStringUtils.isBlank(bid)) {
            bodyParams.put("bid", bid);
        }

        Map<String, String> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_SEND;
        request.setUrl(url);
        ApiResponse post = SmsSendClient.post(request);
        int status = post.getStatus();
        String body = null;
        try {
            body = new String(post.getBody(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (status != 200) {
            return SmsResponse.error(status, "网络请求异常："+url+","+body);
        }
        SmsResponse<MessageSendResVo> smsResponse = JSONUtil.toBean(body, new TypeReference<SmsResponse<MessageSendResVo>>() {
        }, true);
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
    public SmsResponse<MessageSendResVo> sendBatch(String templateCode, Map<String, LinkedList<String>> phonesAndParams, String upExtendCode, String bid) {
        if (SmsStringUtils.isBlank(templateCode)) {
            return SmsResponse.error(40004, "模板id不能为空");
        }
        if (phonesAndParams == null || phonesAndParams.size() == 0) {
            return SmsResponse.error(40004, "phonesAndParams参数不能为空");
        }
        if (!SmsStringUtils.isBlank(upExtendCode) && !SmsStringUtils.isNumeric(upExtendCode)) {
            return SmsResponse.error(40004, "扩展号必须为数字");
        }
        if (!SmsStringUtils.isBlank(bid) && bid.length() > 64) {
            return SmsResponse.error(40004, "业务bid长度不能超过64位");
        }

        String phonesJSON = JSONUtil.toJsonStr(phonesAndParams.keySet());
        Map<String, String> bodyParams = new HashMap<>(4);
        bodyParams.put("phonesJson", phonesJSON);
        //模板变量内容
        bodyParams.put("templateParamJson", JSONUtil.toJsonStr(phonesAndParams.values()));
        bodyParams.put("templateCode", templateCode);
        if (!SmsStringUtils.isBlank(upExtendCode)) {
            bodyParams.put("upExtendCode", upExtendCode);
        }
        if (!SmsStringUtils.isBlank(bid)) {
            bodyParams.put("bid", bid);
        }

        Map<String, String> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_SEND_BATCH;
        request.setUrl(url);
        ApiResponse post = SmsSendClient.post(request);
        int status = post.getStatus();
        if (status != 200) {
            return SmsResponse.error(status, "网络请求异常："+url);
        }
        String body = null;
        try {
            body = new String(post.getBody(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SmsResponse<MessageSendResVo> smsResponse = JSONUtil.toBean(body, new TypeReference<SmsResponse<MessageSendResVo>>() {
        },true);
        return smsResponse;
    }
}
