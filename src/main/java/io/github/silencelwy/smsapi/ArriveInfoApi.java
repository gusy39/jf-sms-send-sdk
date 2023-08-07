package io.github.silencelwy.smsapi;

import io.github.silencelwy.smsapi.client.DomainEnum;
import io.github.silencelwy.smsapi.tool.ArriveInfoTool;
import io.github.silencelwy.smsapi.vo.ArriveInfoResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ArriveInfoApi {

    private static volatile ArriveInfoTool arriveInfoTool;

    private static final String splitStr = "_";

    private volatile static ConcurrentHashMap<String, ArriveInfoApi> mapApi = new ConcurrentHashMap<>();

    private ArriveInfoApi(String domainEnumUrl, String apiKey, String accessKey) {
        arriveInfoTool = new ArriveInfoTool(domainEnumUrl, apiKey, accessKey);
    }

    public static ArriveInfoApi getInstance(String apiKey, String accessKey) {
        if (!mapApi.containsKey(getKey(apiKey,DomainEnum.DEFAULT.getUrl()))) {
            synchronized (SendSmsApi.class) {
                if (!mapApi.containsKey(getKey(apiKey,DomainEnum.DEFAULT.getUrl()))) {
                    mapApi.put(getKey(apiKey,DomainEnum.DEFAULT.getUrl()), new ArriveInfoApi(DomainEnum.DEFAULT.getUrl(), apiKey, accessKey));
                }
            }
        }
        return mapApi.get(getKey(apiKey,DomainEnum.DEFAULT.getUrl()));
    }

    public static ArriveInfoApi getInstance(String domainUrl, String apiKey, String accessKey) {
        if (domainUrl == null || domainUrl.trim().length() == 0) {
            domainUrl = DomainEnum.DEFAULT.getUrl();
        }
        if (!mapApi.containsKey(getKey(apiKey,domainUrl))) {
            synchronized (SendSmsApi.class) {
                if (!mapApi.containsKey(getKey(apiKey,domainUrl))) {
                    mapApi.put(getKey(apiKey,domainUrl), new ArriveInfoApi(domainUrl, apiKey, accessKey));
                }
            }
        }
        return mapApi.get(getKey(apiKey,domainUrl));
    }

    private static String getKey(String apiKey, String domain){
        return apiKey + splitStr + domain;
    }

    public SmsResponse<List<ArriveInfoResVo>> getArriveInfo(Set<String> msgIdSet) {
        return arriveInfoTool.getArriveInfo(msgIdSet);
    }
}
