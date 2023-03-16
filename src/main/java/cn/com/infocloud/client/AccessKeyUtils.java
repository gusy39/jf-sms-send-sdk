package cn.com.infocloud.client;

import java.util.*;
import java.util.regex.Pattern;

public final class AccessKeyUtils {


    public static Map<String, Object> getHeaders(String apiKey,String accessKey,Map<String, Object> bodyParams){
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-api-key",apiKey);
        headers.put("x-sign-method", HmacAlgorithms.HMAC_SHA_224.getName());
        headers.put("x-nonce", getRandomNickname(10));
        headers.put("x-timestamp", String.valueOf(System.currentTimeMillis()));
        //排序
        SortedMap<String, Object> sortedMap = new TreeMap<>(bodyParams);
        headers.forEach((k, v) -> sortedMap.put(k, v));
        headers.put("x-sign", getSignature(accessKey, sortedMap, HmacAlgorithms.HMAC_SHA_224));
        return headers;
    }


    /**
     * 签名生成
     * @param secret
     * @param sortedMap
     * @param hmacAlgorithms
     * @return
     */
    private static String getSignature(String secret, SortedMap<String, Object> sortedMap, HmacAlgorithms hmacAlgorithms) {
        //  将参数拼接为字符串
        //  e.g. "key1=value1&key2=value2"
        StringBuffer plainText = new StringBuffer();
        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            plainText.append(entry.getKey() + "=" + entry.getValue());
            plainText.append("&");
        }
        if(plainText != null && plainText.length()>0) {
            plainText.deleteCharAt(plainText.length() - 1);
        }
        return HmacUtils.hmac(secret,plainText.toString(),hmacAlgorithms);
    }

    /**
     * 随机数生成
     * @param length
     * @return
     */
    private static String getRandomNickname(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
