/* ---------------------------------------------------------------------
 * @(#)MathUtil.java
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

/**
 * 
 * @since 2005. 5. 7.
 * @version 1.0
 */
public class MathUtil {

    /**
     * 반올림 하기
     * 
     * @param d1    분모
     * @param d2    분자
     * @param digit 소숫점 아래 자릿수
     * @return
     */
    public static double round(int d1, int d2, int digit) {

        return round((double) d1, (double) d2, digit);
    }

    /**
     * 반올림 하기
     * 
     * @param d1    분모
     * @param d2    분자
     * @param digit 소숫점 아래 자릿수
     * @return
     */
    public static double round(double d1, double d2, int digit) {

        int length = Integer.parseInt("1" + StringUtil.fillSpace("0", digit));
        double result = (d1 / d2) * length;

        result = Math.round(result);
        result = result / length;

        return result;
    }

    /**
     * 숫자 문자열을 숫자 값으로 바꾸기.
     */
    public static int parseInt(String srValue) {
        return Integer.parseInt(srValue);
    }

    /**
     * 오브젝트 타입을 숫자 값으로 바꾸기.
     */
    public static int parseInt(Object obj) {
        return Integer.parseInt(String.valueOf(obj));
    }

    /**
     * 숫자 문자열을 숫자 값으로 바꾸기.
     * to convert the value of String Object to Integer,
     * if Exception occured return nReturnValue.
     * 
     * @param srValue      원본 문자
     * @param defaultValue default value
     * @return Parses the string argument as a signed decimal integer.
     */
    public static int parseInt(String srValue, int defaultValue) {
        try {
            return Integer.parseInt(srValue);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 오브젝트 타입을 숫자 값으로 바꾸기.
     */
    public static int parseInt(Object obj, int defaultValue) {
        try {
            return parseInt(obj);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

}
