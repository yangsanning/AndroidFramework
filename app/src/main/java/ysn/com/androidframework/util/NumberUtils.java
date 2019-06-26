package ysn.com.androidframework.util;

import java.util.regex.Pattern;

/**
 * @Author yangsanning
 * @ClassName NumberUtils
 * @Description 一句话概括作用
 * @Date 2019/6/26
 * @History 2019/6/26 author: description:
 */
public class NumberUtils {

    private static Pattern INT_PATTERN = Pattern.compile("^[-+]?[\\d]*$");
    private static Pattern FLOAT_PATTERN = Pattern.compile("^[-+]?[.\\d]*$");

    public static boolean isInt(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        return INT_PATTERN.matcher(str).matches();
    }

    public static boolean isFloat(String str) {
        return FLOAT_PATTERN.matcher(str).matches();
    }
}
