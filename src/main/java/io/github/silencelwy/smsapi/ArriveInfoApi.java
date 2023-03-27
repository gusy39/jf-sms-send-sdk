package io.github.silencelwy.smsapi;

import io.github.silencelwy.smsapi.client.DomainEnum;
import io.github.silencelwy.smsapi.tool.ArriveInfoTool;
import io.github.silencelwy.smsapi.vo.ArriveInfoResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;

import java.util.List;
import java.util.Set;

public final class ArriveInfoApi {

    private static volatile ArriveInfoTool arriveInfoTool;

    private ArriveInfoApi(String domainEnumUrl,String apiKey,String accessKey){
        arriveInfoTool = new ArriveInfoTool(domainEnumUrl,apiKey,accessKey);
    }

    public static ArriveInfoApi getInstance(String apiKey,String accessKey){
        return new ArriveInfoApi(DomainEnum.DEFAULT.getUrl(),apiKey,accessKey);
    }
    public static ArriveInfoApi getInstance(String domainUrl,String apiKey,String accessKey){
        if (domainUrl == null){
            domainUrl = DomainEnum.DEFAULT.getUrl();
        }
        return new ArriveInfoApi(domainUrl,apiKey,accessKey);
    }
    public SmsResponse<List<ArriveInfoResVo>> getArriveInfo(Set<String> msgIdSet){
        return arriveInfoTool.getArriveInfo(msgIdSet);
    }
}
