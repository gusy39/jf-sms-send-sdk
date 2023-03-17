package cn.com.infocloud.tool;


import com.alibaba.fastjson.JSON;

import java.io.IOException;
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



}
