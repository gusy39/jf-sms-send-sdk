package cn.com.infocloud.client;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtils {

    /**
     * HmacSHA256算法,返回的结果始终是32位
     * @param key 加密的键，可以是任何数据
     * @param content 待加密的内容
     * @return 加密后的内容
     * @throws Exception
     */
    public static byte[] hmac(byte[] key,byte[] content, HmacAlgorithms hmacAlgorithms){
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance(hmacAlgorithms.toString());
            hmacSha256.init(new SecretKeySpec(key, 0, key.length, hmacAlgorithms.toString()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] hmacSha256Bytes = hmacSha256.doFinal(content);
        return hmacSha256Bytes;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    public  static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1){
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
    /**
     * sha256_HMAC加密
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String hmac(String secret, String message,HmacAlgorithms hmacAlgorithms){
        String hash = "";
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance(hmacAlgorithms.toString());
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), hmacAlgorithms.toString());
            hmacSha256.init(secret_key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] bytes = hmacSha256.doFinal(message.getBytes());
        hash = byteArrayToHexString(bytes);
        return hash;
    }

}
