package com.sk.signet.onm.common.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyConfig {
    private static Logger log = LoggerFactory.getLogger(PropertyConfig.class);

    private static ResourceBundle rs;
    public static String DEFAULT_PROPERTIES = "properties/ecsc";

    static {
        try {

            String profileValue = System.getProperty("spring.profiles.active");
            if (profileValue == null) {
                profileValue = System.getenv("spring.profiles.active");
            }
            rs = ResourceBundle.getBundle(DEFAULT_PROPERTIES);
        } catch (MissingResourceException mre) {
            log.info(mre.getMessage());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    public static String getString(String name) {
        String value = null;

        try {
            value = rs.getString(name);
        } catch (MissingResourceException mre) {
            log.info(mre.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return value;
    }

    /**
     * 복수 Property 값 가져오기
     * 
     * @param key
     * @return values - String[]
     */
    public static String[] getStringArray(String key) {
        String[] values = null;

        try {
            values = rs.getString(key).split(";");
        } catch (MissingResourceException mre) {
            log.info(mre.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return values;
    }

    /**
     * 복수 Property 값 가져오기
     * 
     * @param key
     * @return values - int[]
     */
    @SuppressWarnings("null")
    public static int[] getIntArray(String key) {
        int[] ivalues = null;
        String[] values = null;

        try {
            values = rs.getString(key).split(";");

            for (int i = 0, n = values.length; i < n; i++) {
                ivalues[i] = Integer.parseInt(values[i]);
            }
        } catch (MissingResourceException mre) {
            log.info(mre.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return ivalues;
    }

    public static int getInt(String name) {
        String value = null;

        try {
            value = rs.getString(name);
        } catch (MissingResourceException mre) {
            log.info(mre.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return Integer.parseInt(value);
    }

    /***
     * 프로퍼티 내용 가져오는 메소드
     * 
     * @param str
     * @param name
     * @return
     */
    public static String getPropertiesCon(String str, String name) {
        rs = ResourceBundle.getBundle(str);
        String result = rs.getString(name);
        return result;
    }

}