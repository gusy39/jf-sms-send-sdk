package io.github.silencelwy.smsapi.tool;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import io.github.silencelwy.smsapi.client.AccessKeyUtils;
import io.github.silencelwy.smsapi.client.SmsSendClient;
import io.github.silencelwy.smsapi.client.SmsStringUtils;
import io.github.silencelwy.smsapi.request.ApiRequest;
import io.github.silencelwy.smsapi.request.ApiResponse;
import io.github.silencelwy.smsapi.vo.ArriveInfoResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ArriveInfoTool {
    public static final String MESSAGE_ARRIVE_QRY = "sms/arriveRes/qry";

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
        StringBuffer stringBuffer = new StringBuffer();
        for (String taskId:msgIdSet){
            stringBuffer.append(taskId+",");
        }
        String msgIdStr = stringBuffer.toString();
        if (SmsStringUtils.isBlank(msgIdStr)){
            return SmsResponse.error(40004,"请传入正确的任务id");
        }else {
            msgIdStr = msgIdStr.substring(0,msgIdStr.length()-1);
        }
        Map<String, String> bodyParams = new HashMap<>(1);
        bodyParams.put("msgId",msgIdStr);
        Map<String, String> headers = AccessKeyUtils.getHeaders(apiKey, accessKey, bodyParams);
        ApiRequest request = new ApiRequest();
        request.setBodyParams(bodyParams);
        request.setHeaders(headers);
        String url = domain + MESSAGE_ARRIVE_QRY;
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

        SmsResponse<List<ArriveInfoResVo>> smsResponse = JSONUtil.toBean(body, new TypeReference<SmsResponse<List<ArriveInfoResVo>>>(){},true);
        return smsResponse;

    }

}
