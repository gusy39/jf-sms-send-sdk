package io.github.silencelwy.smsapi.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.github.silencelwy.smsapi.client.AccessKeyUtils;
import io.github.silencelwy.smsapi.client.SmsSendClient;
import io.github.silencelwy.smsapi.client.SmsStringUtils;
import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import io.github.silencelwy.smsapi.vo.ArriveInfoResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;

import java.util.*;
import java.util.stream.Collectors;

public class ArriveInfoTool {
    public static final String MESSAGE_ARRIVE_QRY = "sms/arrive/qry";

    private String domain;
    private String apiKey;
    private String accessKey;

    public ArriveInfoTool(String domain, String apiKey, String accessKey) {
        this.domain = domain;
        this.apiKey = apiKey;
        this.accessKey = accessKey;
    }

    public SmsResponse<List<ArriveInfoResVo>> getArriveInfo(Set<String> msgIdSet){
        if (msgIdSet == null || msgIdSet.size() == 0){
            return SmsResponse.error(40004,"任务id集合为空");
        }
        if (msgIdSet.size() > 600){
            return SmsResponse.error(40004,"单次查询任务量不超过600条");
        }
        String msgIdStr = msgIdSet.stream().filter(taskId -> SmsStringUtils.isNumeric(taskId)).collect(Collectors.joining(","));
        if (SmsStringUtils.isBlank(msgIdStr)){
            return SmsResponse.error(40004,"请传入正确的任务id");
        }
        Map<String, Object> bodyParams = new HashMap<>(1);
        bodyParams.put("msgId",msgIdStr);
        Map<String, Object> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_ARRIVE_QRY;
        request.setUrl(url);
        ApiResponse post = SmsSendClient.post(request);
        int status = post.getStatus();
        if (status != 200){
            return SmsResponse.error(status,"网络请求异常，域名或者请求地址不正确");
        }
        String body = new String(post.getBody());
        SmsResponse<List<ArriveInfoResVo>> smsResponse = JSON.parseObject(body, new TypeReference<SmsResponse<List<ArriveInfoResVo>>>(){});
        return smsResponse;

    }

}
