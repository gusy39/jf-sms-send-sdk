package io.github.silencelwy.smsapi.client;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

import java.util.*;

public final class AccessKeyUtils {


    public static HmacAlgorithm defaultHmacAlgorithms = HmacAlgorithm.HmacMD5;

    public static Map<String, String> getHeaders(String apiKey, String accessKey, Map<String, String> bodyParams){
        //排序
        SortedMap<String, String> sortedMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        sortedMap.put("x-api-key",apiKey);
        sortedMap.put("x-sign-method", defaultHmacAlgorithms.getValue());
        sortedMap.put("x-nonce", getRandomNickname(10));
        sortedMap.put("x-timestamp", String.valueOf(System.currentTimeMillis()));

        sortedMap.putAll(bodyParams);
        sortedMap.put("x-sign", getSignature(accessKey, sortedMap, defaultHmacAlgorithms));
        return sortedMap;
    }


    /**
     * 签名生成
     * @param secret
     * @param sortedMap
     * @param hmacAlgorithm
     * @return
     */
    private static String getSignature(String secret, SortedMap<String, String> sortedMap, HmacAlgorithm hmacAlgorithm) {
        //  将参数拼接为字符串
        //  e.g. "key1=value1&key2=value2"
        StringBuffer plainText = new StringBuffer();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            plainText.append(entry.getKey() + "=" + entry.getValue());
            plainText.append("&");
        }
        if(plainText != null && plainText.length()>0) {
            plainText.deleteCharAt(plainText.length() - 1);
        }
        HMac mac = new HMac(hmacAlgorithm, secret.getBytes());
        return mac.digestHex(plainText.toString());
//        return HmacUtils.hmac(secret,plainText.toString(),hmacAlgorithms);
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
