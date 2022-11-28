/* ---------------------------------------------------------------------
 * @(#)DateFormatUtil.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * 날짜와 시간을 포맷하는데 필요한 유틸리티 클래스.
 * </p>
 *
 * <p>
 * 포맷은 {@link com.hs.pi.commons.date.FastDateFormat} 클래스를 사용한다.
 * </p>
 *
 * ※ 참고<br>
 * 1. TimeZone : 그리니치 표준시간(GMT)를 기준으로 각 지역이 위치한 경도에 따라<br>
 * 시간의 차이가 있다. 이러한 동일한 시간대를 지역을 동일한 timezone을 가진다고 말한다.<br>
 * 2. UTC(Universal Time Coordinated) : 지역 표준시를 말하며, GMT(그리니치 표준시)라고도 한다.<br>
 * 1970년 1월 1일 자정부터 시간이 계산된다.
 *
 * @author Apache Ant - DateUtil
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Stephen Colebourne
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @since 2.0
 * @version $Id: DateFormatUtil.java,v 1.1 2009/04/07 10:46:24 since88 Exp $
 */
public class DateFormatUtil {

    /**
     * ISO8601 포맷. <tt>yyyy-MM-dd'T'HH:mm:ss</tt>.
     */
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * ISO8601 포맷. <tt>yyyy-MM-dd'T'HH:mm:ssZZ</tt>.
     */
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat
            .getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");

    /**
     * ISO8601 포맷. <tt>yyyy-MM-dd</tt>.
     */
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * ISO8601 포맷. <tt>yyyy-MM-ddZZ</tt>.
     */
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");

    /**
     * ISO8601 포맷. <tt>'T'HH:mm:ss</tt>.
     */
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");

    /**
     * ISO8601 포맷. <tt>'T'HH:mm:ssZZ</tt>.
     */
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");

    /**
     * ISO8601 포맷. <tt>HH:mm:ss</tt>.
     */
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");

    /**
     * ISO8601 포맷. <tt>HH:mm:ssZZ</tt>.
     */
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");

    /**
     * SMTP 포맷. <tt>EEE, dd MMM yyyy HH:mm:ss Z</tt>.
     */
    public static final FastDateFormat SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z",
            Locale.US);

    /**
     * ISO8601 포맷. <tt>yyyyMMddHHmmss</tt>.
     */
    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /**
     * ISO8601 포맷. <tt>yyyyMMddHHmmss.SSS</tt>.
     */
    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_MS = FastDateFormat.getInstance("yyyyMMddHHmmss.SSS");

    /**
     * ISO8601 포맷. <tt>yyyyMMdd</tt>.
     */
    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_SHORT = FastDateFormat.getInstance("yyyyMMdd");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_YYYY = FastDateFormat.getInstance("yyyy");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_YY = FastDateFormat.getInstance("yy");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_MM = FastDateFormat.getInstance("MM");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_DD = FastDateFormat.getInstance("dd");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_HH = FastDateFormat.getInstance("HH");

    public static final FastDateFormat ISO_DATETIME_pi2_FORMAT_MI = FastDateFormat.getInstance("mm");

    /**
     * SimpleDateFormat. <tt>yyyyMMddHHmmss</tt>.
     */
    public static final SimpleDateFormat ISO_FULL_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss",
            new Locale("en", "US"));
    /**
     * SimpleDateFormat. <tt>yyyyMMddhhmmss</tt>.
     */
    public static final SimpleDateFormat ISO_SHORT_FORMAT = new SimpleDateFormat("yyyyMMdd", new Locale("en", "US"));

    /**
     * <tt>yyyyMMdd</tt>.
     */
    public static final FastDateFormat HORT_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    /**
     * <tt>yyyyMMdd</tt>.
     */
    public static final FastDateFormat HORT_MS_FORMAT = FastDateFormat.getInstance("HHmm");

    /**
     * KOREAN SHORT 포맷. <tt>yyyy년 MM월 dd일</tt>.
     */
    public static final FastDateFormat KOREAN_SHORT_FORMAT = FastDateFormat.getInstance("yyyy년 MM월 dd일");
    /**
     * KOREAN FULL 포맷. <tt>yyyy년 MM월 dd일</tt>.
     */
    public static final FastDateFormat KOREAN_FULL_FORMAT = FastDateFormat.getInstance("yyyy년 MM월 dd일 a HH시 mm분 ss초");

    /**
     * KOREAN FULL 포맷. <tt>yyyy년 MM월 dd일</tt>.
     */
    public static final FastDateFormat FLEX_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd [HH:mm]");

    public static final FastDateFormat ISO_DATETIME_MS_FORMAT = FastDateFormat.getInstance("yyyyMMdd HH:mm:ss");

    // -----------------------------------------------------------------------
    /**
     * <p>
     * <code>DateFormatUtil</code> 인스탄스는 일반적으로 필요하지 않음.<br>
     *
     * <p>
     * 참고로 본 생성자는 JavaBean 인스탄스를 위해 public으로 선언됨
     * </p>
     */
    public DateFormatUtil() {
    }

    /**
     * <p>
     * 지정한 패턴을 적용해 주어진 시간(밀리초)을 UTC로 포맷한다.
     * </p>
     * 
     * @param millis  포맷대상 시간(밀리초)
     * @param pattern 포맷할 패턴
     * @return 포맷된 날짜
     */
    public static String formatUTC(long millis, String pattern) {
        return format(new Date(millis), pattern, DateUtil.UTC_TIME_ZONE, null);
    }

    /**
     * <p>
     * 지정한 패턴을 적용해 주어진 Date를 UTC로 포맷한다.
     * </p>
     * 
     * @param date    포맷대상 날짜
     * @param pattern 포맷할 패턴
     * @return 포맷된 날짜
     */
    public static String formatUTC(Date date, String pattern) {
        return format(date, pattern, DateUtil.UTC_TIME_ZONE, null);
    }

    /**
     * <p>
     * 지정한 패턴과 로케일을 적용해 주어진 시간(밀리초)을 UTC로 포맷한다.
     * </p>
     * 
     * @param millis  포맷대상 시간(밀리초)
     * @param pattern 포맷할 패턴
     * @param locale  로케일
     * @return 포맷된 날짜
     */
    public static String formatUTC(long millis, String pattern, Locale locale) {
        return format(new Date(millis), pattern, DateUtil.UTC_TIME_ZONE, locale);
    }

    /**
     * <p>
     * 지정한 패턴과 로케일을 적용해 주어진 Date를 UTC로 포맷한다.
     * </p>
     * 
     * @param date    포맷대상 날짜
     * @param pattern 포맷할 패턴
     * @param locale  로케일
     * @return 포맷된 날짜
     */
    public static String formatUTC(Date date, String pattern, Locale locale) {
        return format(date, pattern, DateUtil.UTC_TIME_ZONE, locale);
    }

    /**
     * <p>
     * 지정한 패턴을 적용해 주어진 시간(밀리초)을 포맷한다.
     * </p>
     * 
     * @param millis  포맷대상 시간(밀리초)
     * @param pattern 포맷할 패턴
     * @return 포맷된 날짜
     */
    public static String format(long millis, String pattern) {
        return format(new Date(millis), pattern, null, null);
    }

    /**
     * <p>
     * 지정한 패턴을 적용해 주어진 Date를 포맷한다.
     * </p>
     * 
     * @param date    포맷대상 날짜
     * @param pattern 포맷할 패턴
     * @return 포맷된 날짜
     */
    public static String format(Date date, String pattern) {
        return format(date, pattern, null, null);
    }

    /**
     * <p>
     * 지정한 로케일, 패턴을 적용해 주어진 시간(밀리초)을 포맷한다.
     * </p>
     * 
     * @param millis   포맷대상 시간(밀리초)
     * @param pattern  포맷할 패턴
     * @param timeZone 타임존, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(long millis, String pattern, TimeZone timeZone) {
        return format(new Date(millis), pattern, timeZone, null);
    }

    /**
     * <p>
     * 지정한 타임존, 패턴을 적용해 주어진 Date를 포맷한다.
     * </p>
     * 
     * @param date     포맷대상 날짜
     * @param pattern  포맷할 패턴
     * @param timeZone 타임존, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(Date date, String pattern, TimeZone timeZone) {
        return format(date, pattern, timeZone, null);
    }

    /**
     * <p>
     * 지정한 로케일, 패턴을 적용해 주어진 시간(밀리초)을 포맷한다.
     * </p>
     * 
     * @param millis  포맷대상 시간(밀리초)
     * @param pattern 포맷할 패턴
     * @param locale  로케일, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(long millis, String pattern, Locale locale) {
        return format(new Date(millis), pattern, null, locale);
    }

    /**
     * <p>
     * 지정한 로케일, 패턴을 적용해 주어진 Date를 포맷한다.
     * </p>
     * 
     * @param date    포맷대상 날짜
     * @param pattern 포맷할 패턴
     * @param locale  로케일, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(Date date, String pattern, Locale locale) {
        return format(date, pattern, null, locale);
    }

    /**
     * <p>
     * 지정한 타임존과 로케일, 패턴을 적용해 주어진 시간(밀리초)을 포맷한다.
     * </p>
     * 
     * @param millis   포맷대상 시간(밀리초)
     * @param pattern  포맷할 패턴
     * @param timeZone 타임존, <code>null</code> 가능
     * @param locale   로케일, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(long millis, String pattern, TimeZone timeZone, Locale locale) {
        return format(new Date(millis), pattern, timeZone, locale);
    }

    /**
     * <p>
     * 지정한 타임존과 로케일, 패턴을 적용해 주어진 Date를 포맷한다.
     * </p>
     * 
     * @param date     포맷대상 날짜
     * @param pattern  포맷할 패턴
     * @param timeZone 타임존, <code>null</code> 가능
     * @param locale   로케일, <code>null</code> 가능
     * @return 포맷된 날짜
     */
    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(date);
    }

    /**
     * <p>
     * 오늘 날짜를 yyyyMMddHHmmss 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyyMMddHHmmss)
     */
    public static String getToday() {
        return ISO_DATETIME_pi2_FORMAT.format(new Date());
    }

    public static String getTodayMs() {
        return ISO_DATETIME_MS_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 현재시간을 T'HH:mm:ss 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 일자 (T'HH:mm:ss)
     */
    public static String getTime() {
        return ISO_TIME_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyyMMddHHmmss 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyyMMddHHmmss)
     */
    public static String getTodayFull() {
        return ISO_DATETIME_pi2_FORMAT.format(new Date());
    }

    public static String getTodayFullMs() {
        return ISO_DATETIME_pi2_FORMAT_MS.format(new Date());
    }

    public static String getTodayFullKR() {
        return KOREAN_FULL_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyyMMdd 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyyMMdd)
     */
    public static String getTodayShort() {
        return ISO_DATETIME_pi2_FORMAT_SHORT.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyy 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyy)
     */
    public static String getTodayYyyy() {
        return ISO_DATETIME_pi2_FORMAT_YYYY.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yy 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yy)
     */
    public static String getTodayYy() {
        return ISO_DATETIME_pi2_FORMAT_YY.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 MM 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (MM)
     */
    public static String getTodayMm() {
        return ISO_DATETIME_pi2_FORMAT_MM.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 dd 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (dd)
     */
    public static String getTodayDd() {
        return ISO_DATETIME_pi2_FORMAT_DD.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 HH 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (HH)
     */
    public static String getTodayHh() {
        return ISO_DATETIME_pi2_FORMAT_HH.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 MI 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (MI)
     */
    public static String getTodayMi() {
        return ISO_DATETIME_pi2_FORMAT_MI.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyyMMdd 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyyMMdd)
     */
    public static String getFlexTime() {
        return FLEX_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyyMMdd 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyyMMdd)
     */
    public static String getYmd() {
        return HORT_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 시간 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (HHss)
     */
    public static String getShortTime() {
        return HORT_MS_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 오늘 날짜를 yyyy년 MM월 dd일 형식으로 얻는다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyy년 MM월 dd일 )
     */
    public static String getTodayKr() {
        return KOREAN_SHORT_FORMAT.format(new Date());
    }

    /**
     * <p>
     * 지정된 날짜 문자열(14자리)에 대해 yyyy년 MM월 dd일 a hh시 mm분 ss초 형식으로 전환한다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyy년 MM월 dd일 오전/오후 hh시 mm분 ss초)
     */
    public static String parseDateFull(String str) {

        if (str != null) {

            try {
                if (str.length() == 8) {
                    return KOREAN_FULL_FORMAT.format(ISO_SHORT_FORMAT.parse(str));
                } else if (str.length() == 14) {
                    return KOREAN_FULL_FORMAT.format(ISO_FULL_FORMAT.parse(str));
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return str;
    }

    /**
     * <p>
     * 지정된 날짜 문자열(14자리)에 대해 yyyy년 MM월 dd일 형식으로 전환한다.
     * </p>
     * 
     * @return 포맷된 날짜 (yyyy년 MM월 dd일)
     */
    public static String parseDateShort(String str) {

        if (str != null) {
            try {
                if (str.length() == 8) {
                    return KOREAN_SHORT_FORMAT.format(ISO_SHORT_FORMAT.parse(str));
                } else if (str.length() == 14) {
                    return KOREAN_SHORT_FORMAT.format(ISO_FULL_FORMAT.parse(str));
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return str;
    }

    /**
     * <p>
     * 8자리 또는 14자리 날짜 스트링을 주고 [2002-01-01]의 포맷을 가져옴.
     * </p>
     *
     * @param 14자리로된 숫자로된 날짜대상 값.
     * @return 포맷된 날짜.
     */

    public static String getFormatedDate(String date) {
        return getFormatedDate(date, "");
    }

    /**
     * <p>
     * 8자리 또는 14자리 날짜 스트링을 주고 [2002-01-01]의 포맷을 가져옴.
     * </p>
     *
     * @param 14자리로된 숫자로된 날짜대상 값.
     * @return 포맷된 날짜.
     */
    public static String getFormatedDate(String date, String chr) {

        if (date == null) {
            return " ";
        }
        if (date.length() != 14 && date.length() != 8) {
            return " ";
        }

        StringBuffer returnDate = new StringBuffer(10);
        returnDate.append(date.substring(0, 4));
        returnDate.append(chr);
        returnDate.append(date.substring(4, 6));
        returnDate.append(chr);
        returnDate.append(date.substring(6, 8));

        return returnDate.toString();
    }

    public static String getCurrentDate(String dateFormatText) {
        /**
         *
         * Symbol Meaning Presentation Example
         * ------ ------- ------------ -------
         * G era designator (Text) AD
         * y year (Number) 1996
         * M month in year (Text & Number) July & 07
         * d day in month (Number) 10
         * h hour in am/pm (1~12) (Number) 12
         * H hour in day (0~23) (Number) 0
         * m minute in hour (Number) 30
         * s second in minute (Number) 55
         * S millisecond (Number) 978
         * E day in week (Text) Tuesday
         * D day in year (Number) 189
         * F day of week in month (Number) 2 (2nd Wed in July)
         * w week in year (Number) 27
         * W week in month (Number) 2
         * a am/pm marker (Text) PM
         * k hour in day (1~24) (Number) 24
         * K hour in am/pm (0~11) (Number) 0
         * z time zone (Text) Pacific Standard Time
         * ' escape for text (Delimiter)
         * '' single quote (Literal)
         *
         * ex) getCurrentDate("yyyy/MM/dd hh:mm:ss")
         * 결과] 2004/05/28 11:12:12
         */

        java.util.Date currentDate = java.util.Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(dateFormatText);

        String currentTime = dateFormat.format(currentDate);

        return currentTime;
    }
}
