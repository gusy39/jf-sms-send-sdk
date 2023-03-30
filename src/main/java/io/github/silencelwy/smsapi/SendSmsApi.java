package io.github.silencelwy.smsapi;

import io.github.silencelwy.smsapi.client.DomainEnum;
import io.github.silencelwy.smsapi.tool.SmsSendTool;
import io.github.silencelwy.smsapi.vo.MessageSendResVo;
import io.github.silencelwy.smsapi.vo.SmsResponse;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public final class SendSmsApi {

    private volatile static SmsSendTool smsSendTool;

    private volatile static SendSmsApi sendSmsApi;

    private volatile static SendSmsApi sendSmsApiDefault;

    private SendSmsApi(String domainEnumUrl, String apiKey, String accessKey) {
        smsSendTool = new SmsSendTool(domainEnumUrl, apiKey, accessKey);
    }

    public static SendSmsApi getInstance(String apiKey, String accessKey) {
        if (sendSmsApiDefault == null) {
            synchronized (SendSmsApi.class) {
                if (sendSmsApiDefault == null) {
                    sendSmsApiDefault = new SendSmsApi(DomainEnum.DEFAULT.getUrl(), apiKey, accessKey);
                }
            }
        }
        return sendSmsApiDefault;
    }

    public static SendSmsApi getInstance(String domainUrl, String apiKey, String accessKey) {
        if (sendSmsApi == null) {
            synchronized (SendSmsApi.class) {
                if (sendSmsApi == null) {
                    if (domainUrl == null) {
                        domainUrl = DomainEnum.DEFAULT.getUrl();
                    }
                    sendSmsApi = new SendSmsApi(domainUrl, apiKey, accessKey);
                }
            }
        }
        return sendSmsApi;
    }

    /**
     * 短信发送。无参短信模板内容发送，即所有手机号接收到的短信内容一样
     *
     * @param templateCode
     * @param phones       手机号。手机号最大个数限制1000
     * @return
     */
    public SmsResponse<MessageSendResVo> send(String templateCode, Set<String> phones) {
        return smsSendTool.sendSingleton(templateCode, null, phones, null, null);
    }

    public SmsResponse<MessageSendResVo> send(String templateCode, Set<String> phones, String upExtendCode, String bid) {
        return smsSendTool.sendSingleton(templateCode, null, phones, upExtendCode, bid);
    }

    /**
     * 短信发送。不同手机号对应相同的短信模板变量值，即所有手机号接收到的短信内容一样
     *
     * @param templateCode
     * @param params       模板参数变量值
     * @param phones       手机号。手机号最大个数限制1000
     * @return
     */
    public SmsResponse<MessageSendResVo> sendHasVar(String templateCode, LinkedList<String> params, Set<String> phones) {
        //
        if (params == null || params.size() == 0) {
            return SmsResponse.error(40004, "参数不能为空");
        }
        return smsSendTool.sendSingleton(templateCode, params, phones, null, null);
    }

    public SmsResponse<MessageSendResVo> sendHasVar(String templateCode, LinkedList<String> params, Set<String> phones, String upExtendCode, String bid) {
        //手机号先去重，然后再转换
        return smsSendTool.sendSingleton(templateCode, params, phones, upExtendCode, bid);
    }

    /**
     * 短信发送。不同手机号对应不同的短信模板变量值，即所有手机号接收到的短信内容不一样
     *
     * @param templateCode    模板ID
     * @param phonesAndParams 手机号-手机号对应的模板参数。手机号最大个数限制500
     * @return
     */
    public SmsResponse<MessageSendResVo> sendPhoneToContent(String templateCode, Map<String, LinkedList<String>> phonesAndParams) {
        return smsSendTool.sendBatch(templateCode, phonesAndParams, null, null);
    }

    public SmsResponse<MessageSendResVo> sendPhoneToContent(String templateCode, Map<String, LinkedList<String>> phonesAndParams, String upExtendCode, String bid) {
        return smsSendTool.sendBatch(templateCode, phonesAndParams, upExtendCode, bid);
    }
}
