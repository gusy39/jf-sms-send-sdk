package cn.infocloud.cc.client;

import java.util.regex.Pattern;

public final class SmsStringUtils {

    private static Pattern phonePattern = Pattern.compile("^1\\d{10}$");

    /**
     *
     * @param cs
     * @return
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * 判定是否是手机号码
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (phone !=null && phone.length()>0
                && phonePattern.matcher(phone).matches()) {
            return true;
        }
        return false;
    }
    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

}
