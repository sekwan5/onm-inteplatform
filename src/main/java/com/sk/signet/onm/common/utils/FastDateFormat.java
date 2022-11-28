/* ---------------------------------------------------------------------
 * @(#)FastDateFormat.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p>
 * 이 클래스는 {@link java.text.SimpleDateFormat}의 더 빠르고 스레드에 안전한 버전이다.
 * </p>
 * 
 * <p>
 * 대부분의 포맷 상황에서 <code>SimpleDateFormat</code>을 대체하는데 사용될 수 있다.
 * 특별히 멀티스레드 서버환경에서 유용하며, 참고로
 * <code>SimpleDateFormat</code>은 모든 JDK 버전에서 스레드에 안전하지 않다.
 * 그리고 Sun의 어떠한 bug/RFE 에서도 변경할 예정이 없는 것으로 알려져 있다.
 * </p>
 *
 * <p>
 * 이 클래스는 단지 날짜 포맷만을 위해 사용되나,
 * SimpleDateFormat에 양립하는 모든 패턴을 지원한다.(time zone은 제외 - 아래 참조).
 * </p>
 *
 * <p>
 * JDK 1.4 이후부터 새 패턴이 추가되었다.
 * <code>'Z'</code>는 RFC822 포맷(<code>+0800</code> 또는 <code>-1100</code>)을 표현한다.
 * 이 패턴은 이 클래스를 사용함으로써 모든 JDK 버전에서도 사용이 가능하다.
 * </p>
 *
 * <p>
 * 추가로, <code>'ZZ'</code>패턴은 ISO8601 전체 포맷(<code>+08:00</code> 또는
 * <code>-11:00</code>)
 * 을 표현하는데, 이것은 JDK 1.4에 적합하지 않은 것으로 알려져있다.
 * </p>
 *
 * <table border=1 cellspacing=1 cellpadding=4>
 * <tr bgcolor="#ccccff">
 * <th align=left>패턴
 * <th align=left>날짜/시간 조합
 * <th align=left>표시
 * <th align=left>예제
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td>Text
 * <td><code>AD</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>y</code>
 * <td>Year
 * <td>Year
 * <td><code>1996</code>; <code>96</code>
 * <tr>
 * <td><code>M</code>
 * <td>Month in year
 * <td>Month
 * <td><code>July</code>; <code>Jul</code>; <code>07</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>w</code>
 * <td>Week in year
 * <td>Number
 * <td><code>27</code>
 * <tr>
 * <td><code>W</code>
 * <td>Week in month
 * <td>Number
 * <td><code>2</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>D</code>
 * <td>Day in year
 * <td>Number
 * <td><code>189</code>
 * <tr>
 * <td><code>d</code>
 * <td>Day in month
 * <td>Number
 * <td><code>10</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>F</code>
 * <td>Day of week in month
 * <td>Number
 * <td><code>2</code>
 * <tr>
 * <td><code>E</code>
 * <td>Day in week
 * <td>Text
 * <td><code>Tuesday</code>; <code>Tue</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td>Text
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td>Number
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td>Number
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td>Number
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>h</code>
 * <td>Hour in am/pm (1-12)
 * <td>Number
 * <td><code>12</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td>Number
 * <td><code>30</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>s</code>
 * <td>Second in minute
 * <td>Number
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td>Number
 * <td><code>978</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>z</code>
 * <td>Time zone
 * <td>General time zone
 * <td><code>Pacific Standard Time</code>; <code>PST</code>;
 * <code>GMT-08:00</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td>RFC 822 time zone
 * <td><code>-0800</code>
 * </table>
 * 
 * <h4>예제 패턴</h4>
 * 
 * <table border=1 cellspacing=1 cellpadding=4>
 * <tr bgcolor="#ccccff">
 * <th align=left>날짜/시간 패턴
 * <th align=left>결과
 * <tr>
 * <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code>
 * <td><code>2001.07.04 AD at 12:08:56 PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"EEE, MMM d, ''yy"</code>
 * <td><code>Wed, Jul 4, '01</code>
 * <tr>
 * <td><code>"h:mm a"</code>
 * <td><code>12:08 PM</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"hh 'o''clock' a, zzzz"</code>
 * <td><code>12 o'clock PM, Pacific Daylight Time</code>
 * <tr>
 * <td><code>"K:mm a, z"</code>
 * <td><code>0:08 PM, PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code>
 * <td><code>02001.July.04 AD 12:08 PM</code>
 * <tr>
 * <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
 * <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyMMddHHmmssZ"</code>
 * <td><code>010704120856-0700</code>
 * </table>
 * 
 * @author TeaTrove project
 * @author Brian S O'Neill
 * @author Sean Schofield
 * @author Gary Gregory
 * @author Stephen Colebourne
 * @since 2.0
 * @version $Id: FastDateFormat.java,v 1.1 2009/04/07 10:46:24 since88 Exp $
 */
public class FastDateFormat extends Format {

    // A lot of the speed in this class comes from caching, but some comes
    // from the special int to StringBuffer conversion.
    //
    // The following produces a padded 2 digit number:
    // buffer.append((char)(value / 10 + '0'));
    // buffer.append((char)(value % 10 + '0'));
    //
    // Note that the fastest append to StringBuffer is a single char (used here).
    // Note that Integer.toString() is not called, the conversion is simply
    // taking the value and adding (mathematically) the ASCII value for '0'.
    // So, don't change this code! It works and is very fast.

    private static final long serialVersionUID = -8861029378580349134L;
    /**
     * FULL 로케일에 따른 Date, Time 스타일.
     */
    public static final int FULL = DateFormat.FULL;
    /**
     * LONG 로케일에 따른 Date, Time 스타일.
     */
    public static final int LONG = DateFormat.LONG;
    /**
     * MEDIUM 로케일에 따른 Date, Time 스타일.
     */
    public static final int MEDIUM = DateFormat.MEDIUM;
    /**
     * SHORT 로케일에 따른 Date, Time 스타일.
     */
    public static final int SHORT = DateFormat.SHORT;

    // 내부 클래스에서 사용하는 패키지 영역
    static final double LOG_10 = Math.log(10);

    private static String cDefaultPattern;

    private static Map cInstanceCache = new HashMap(7);
    private static Map cDateInstanceCache = new HashMap(7);
    private static Map cTimeInstanceCache = new HashMap(7);
    private static Map cDateTimeInstanceCache = new HashMap(7);
    private static Map cTimeZoneDisplayCache = new HashMap(7);

    /**
     * 날짜 패턴.
     */
    private final String mPattern;
    /**
     * 타임존.
     */
    private final TimeZone mTimeZone;
    /**
     * Calendar에 우선하는 타임존인지 여부.
     */
    private final boolean mTimeZoneForced;
    /**
     * 로케일.
     */
    private final Locale mLocale;
    /**
     * 기본값에 우선하는 로케일인지 여부.
     */
    private final boolean mLocaleForced;
    /**
     * 파싱 규칙.
     */
    private Rule[] mRules;
    /**
     * 최대 평가치 길이.
     */
    private int mMaxLengthEstimate;

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 기본 패턴과 기본 로케일에 따르는 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              FastDateFormat.getInstance() = "FastDateFormat[yy. M. d. a h:m]"
     * 
     * @return 날짜 포맷터
     */
    public static FastDateFormat getInstance() {
        return getInstance(getDefaultPattern(), null, null);
    }

    /**
     * <p>
     * 지정한 패턴과 기본 로케일에 따르는 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              FastDateFormat.getInstance("yyyy-MM-dd") =
     *              "FastDateFormat[yyyy-MM-dd]"
     *
     * @param pattern {@link java.text.SimpleDateFormat}에 적합한 패턴 문자열
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 패턴이 잘못 지정될 경우 발생.
     */
    public static FastDateFormat getInstance(String pattern) {
        return getInstance(pattern, null, null);
    }

    /**
     * <p>
     * 지정한 패턴과 타임존에 따르는 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              TimeZone zone1 = TimeZone.getTimeZone("GMT+9");
     *              TimeZone zone2 = TimeZone.getTimeZone("America/New_York");
     *
     *              FastDateFormat.getInstance("yyyy-MM-dd", zone1) =
     *              "FastDateFormat[yyyy-MM-dd]"
     *
     * @param pattern  {@link java.text.SimpleDateFormat}에 적합한 패턴 문자열
     * @param timeZone 타임존
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 패턴이 잘못 지정될 경우 발생.
     */
    public static FastDateFormat getInstance(String pattern, TimeZone timeZone) {
        return getInstance(pattern, timeZone, null);
    }

    /**
     * <p>
     * 지정한 패턴과 로케일에 따르는 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              FastDateFormat.getInstance("yyyy-MM-dd", Locale.KOREA) =
     *              "FastDateFormat[yyyy-MM-dd]"
     * 
     * @param pattern {@link java.text.SimpleDateFormat}에 적합한 패턴 문자열
     * @param locale  로케일
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 패턴이 잘못 지정될 경우 발생.
     */
    public static FastDateFormat getInstance(String pattern, Locale locale) {
        return getInstance(pattern, null, locale);
    }

    /**
     * <p>
     * 지정한 패턴과 타임존, 로케일에 따르는 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              TimeZone zone1 = TimeZone.getTimeZone("GMT+9");
     *              TimeZone zone2 = TimeZone.getTimeZone("America/New_York");
     *
     *              FastDateFormat.getInstance("yyyy-MM-dd", zone1, Locale.KOREA) =
     *              "FastDateFormat[yyyy-MM-dd]"
     *
     * @param pattern  {@link java.text.SimpleDateFormat}에 적합한 패턴 문자열
     * @param timeZone 타임존
     * @param locale   로케일
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 패턴이 잘못 지정될 경우 발생.
     */
    public static synchronized FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat emptyFormat = new FastDateFormat(pattern, timeZone, locale);
        FastDateFormat format = (FastDateFormat) cInstanceCache.get(emptyFormat);
        if (format == null) {
            format = emptyFormat;
            format.init(); // convert shell format into usable one
            cInstanceCache.put(format, format); // this is OK!
        }
        return format;
    }

    /**
     * <p>
     * 지정한 스타일과 타임존, 로케일에 따르는 날짜 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              Date date = new Date();
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              FastDateFormat.getDateInstance(#STYLE, zone,
     *              Locale.KOREA).format(date) = RESULT
     * 
     *              // RESULT
     *              DateFormat.FULL = "2005년 2월 4일 금요일"
     *              DateFormat.LONG = "2005년 2월 4일 (금)"
     *              DateFormat.MEDIUM = "2005. 2. 4."
     *              DateFormat.SHORT = "05. 2. 4."
     *
     * @param style    날짜 스타일 : FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 타임존
     * @param locale   로케일
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 로케일이 날짜패턴을 가지고 있지 않을 경우 발생.
     */
    public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
        Object key = new Integer(style);
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale == null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(style, locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateInstanceCache.put(key, format);

            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date pattern for locale: " + locale);
            }
        }
        return format;
    }

    /**
     * <p>
     * 지정한 스타일과 타임존, 로케일에 따르는 시간 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              Date date = new Date();
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              FastDateFormat.getTimeInstance(#STYLE, zone,
     *              Locale.KOREA).format(date) = RESULT
     * 
     *              // RESULT
     *              DateFormat.FULL = "오후 12시 57분 40초 GMT+09:00"
     *              DateFormat.LONG = "오후 12시 57분 40초"
     *              DateFormat.MEDIUM = "오후 12:57:40"
     *              DateFormat.SHORT = "오후 12:57"
     *
     * @param style    날짜 스타일 : FULL, LONG, MEDIUM, or SHORT
     * @param timeZone 타임존
     * @param locale   로케일
     * @return 날짜 포맷터
     * @throws IllegalArgumentException 로케일이 날짜패턴을 가지고 있지 않을 경우 발생.
     */
    public static synchronized FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale) {
        Object key = new Integer(style);
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale != null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cTimeInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getTimeInstance(style, locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cTimeInstanceCache.put(key, format);

            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date pattern for locale: " + locale);
            }
        }
        return format;
    }

    /**
     * <p>
     * 지정한 스타일과 타임존, 로케일에 따르는 날짜와 시간 포맷 인스턴스를 얻는다.
     * </p>
     * 
     * @pi2.example .
     *              Date date = new Date();
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              FastDateFormat.getDateTimeInstance(#STYLE, #STYLE, zone,
     *              Locale.KOREA).format(date) = RESULT
     * 
     *              // RESULT는 getDateInstance(), getTimeInstance() 메소드 예제 참조
     *
     * @param dateStyle 날짜 스타일 : FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle 시간 스타일 : FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  타임존
     * @param locale    로케일
     * @return 날짜/시간 포맷터
     *         {@link #getTimeInstance(int, TimeZone, Locale)}와
     *         {@link #getDateInstance(int, TimeZone, Locale)} 예제 참조
     * @throws IllegalArgumentException 로케일이 날짜/시간 패턴을 가지고 있지 않을 경우 발생.
     */
    public static synchronized FastDateFormat getDateTimeInstance(
            int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {

        Object key = new Pair(new Integer(dateStyle), new Integer(timeStyle));
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale != null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateStyle, timeStyle,
                        locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateTimeInstanceCache.put(key, format);

            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return format;
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 타임존 명을 얻는다. 수행속도를 위해 내부적으로만 캐쉬를 사용한다.
     * </p>
     * 
     * @param tz       쿼리를 위한 타임존
     * @param daylight 오후인지 여부.
     * @param style    <code>TimeZone.LONG</code> 또는 <code>TimeZone.SHORT</code> 사용을
     *                 위한 스타일
     * @param locale   사용되는 로케일
     * @return 타임존에 대한 문자열 명
     */
    static synchronized String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        Object key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = (String) cTimeZoneDisplayCache.get(key);
        if (value == null) {
            // This is a very slow call, so cache the results.
            value = tz.getDisplayName(daylight, style, locale);
            cTimeZoneDisplayCache.put(key, value);
        }
        return value;
    }

    /**
     * <p>
     * 기본 패턴값을 얻는다. 내부적으로만 사용됨.
     * </p>
     * 
     * @return 기본 패턴값
     */
    private static synchronized String getDefaultPattern() {
        if (cDefaultPattern == null) {
            cDefaultPattern = new SimpleDateFormat().toPattern();
        }
        return cDefaultPattern;
    }

    // Constructor
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 새로운 FastDateFormat을 생성한다. 내부적으로만 사용됨.
     * </p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat}에 적합한 패턴 문자열
     * @param timeZone 타임존, <code>null</code>이면 <code>Date</code>와
     *                 <code>Calendar</code> 내부의 값을 이용한다.
     * @param locale   로케일, <code>null</code>이면 시스템 기본값을 이용한다.
     * @throws IllegalArgumentException 패턴이 잘못되었거나 <code>null</code>일 경우 발생.
     */
    protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
        super();
        if (pattern == null) {
            throw new IllegalArgumentException("The pattern must not be null");
        }
        mPattern = pattern;

        mTimeZoneForced = (timeZone != null);
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        mTimeZone = timeZone;

        mLocaleForced = (locale != null);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        mLocale = locale;
    }

    /**
     * <p>
     * 최초 사용을 위한 인스턴스를 초기화한다. 내부적으로만 사용됨.
     * </p>
     */
    protected void init() {
        List rulesList = parsePattern();
        mRules = (Rule[]) rulesList.toArray(new Rule[rulesList.size()]);

        int len = 0;
        for (int i = mRules.length; --i >= 0;) {
            len += mRules[i].estimateLength();
        }

        mMaxLengthEstimate = len;
    }

    // Parse the pattern
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 주어진 패턴 규칙의 목록을 얻는다. 내부적으로만 사용됨.
     * </p>
     * 
     * @return Rule 객체 <code>List</code>
     * @throws IllegalArgumentException 패턴이 잘못되었을 경우 발생.
     */
    protected List parsePattern() {
        DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
        List rules = new ArrayList();

        String[] ERAs = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();

        int length = mPattern.length();
        int[] indexRef = new int[1];

        for (int i = 0; i < length; i++) {
            indexRef[0] = i;
            String token = parseToken(mPattern, indexRef);
            i = indexRef[0];

            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }

            Rule rule;
            char c = token.charAt(0);

            switch (c) {
                case 'G': // era designator (text)
                    rule = new TextField(Calendar.ERA, ERAs);
                    break;
                case 'y': // year (number)
                    if (tokenLen >= 4) {
                        rule = UnpaddedNumberField.INSTANCE_YEAR;
                    } else {
                        rule = TwoDigitYearField.INSTANCE;
                    }
                    break;
                case 'M': // month in year (text and number)
                    if (tokenLen >= 4) {
                        rule = new TextField(Calendar.MONTH, months);
                    } else if (tokenLen == 3) {
                        rule = new TextField(Calendar.MONTH, shortMonths);
                    } else if (tokenLen == 2) {
                        rule = TwoDigitMonthField.INSTANCE;
                    } else {
                        rule = UnpaddedMonthField.INSTANCE;
                    }
                    break;
                case 'd': // day in month (number)
                    rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
                    break;
                case 'h': // hour in am/pm (number, 1..12)
                    rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, tokenLen));
                    break;
                case 'H': // hour in day (number, 0..23)
                    rule = selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen);
                    break;
                case 'm': // minute in hour (number)
                    rule = selectNumberRule(Calendar.MINUTE, tokenLen);
                    break;
                case 's': // second in minute (number)
                    rule = selectNumberRule(Calendar.SECOND, tokenLen);
                    break;
                case 'S': // millisecond (number)
                    rule = selectNumberRule(Calendar.MILLISECOND, tokenLen);
                    break;
                case 'E': // day in week (text)
                    rule = new TextField(Calendar.DAY_OF_WEEK, tokenLen < 4 ? shortWeekdays : weekdays);
                    break;
                case 'D': // day in year (number)
                    rule = selectNumberRule(Calendar.DAY_OF_YEAR, tokenLen);
                    break;
                case 'F': // day of week in month (number)
                    rule = selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, tokenLen);
                    break;
                case 'w': // week in year (number)
                    rule = selectNumberRule(Calendar.WEEK_OF_YEAR, tokenLen);
                    break;
                case 'W': // week in month (number)
                    rule = selectNumberRule(Calendar.WEEK_OF_MONTH, tokenLen);
                    break;
                case 'a': // am/pm marker (text)
                    rule = new TextField(Calendar.AM_PM, AmPmStrings);
                    break;
                case 'k': // hour in day (1..24)
                    rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen));
                    break;
                case 'K': // hour in am/pm (0..11)
                    rule = selectNumberRule(Calendar.HOUR, tokenLen);
                    break;
                case 'z': // time zone (text)
                    if (tokenLen >= 4) {
                        rule = new TimeZoneNameRule(mTimeZone, mTimeZoneForced, mLocale, TimeZone.LONG);
                    } else {
                        rule = new TimeZoneNameRule(mTimeZone, mTimeZoneForced, mLocale, TimeZone.SHORT);
                    }
                    break;
                case 'Z': // time zone (value)
                    if (tokenLen == 1) {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                    } else {
                        rule = TimeZoneNumberRule.INSTANCE_COLON;
                    }
                    break;
                case '\'': // literal text
                    String sub = token.substring(1);
                    if (sub.length() == 1) {
                        rule = new CharacterLiteral(sub.charAt(0));
                    } else {
                        rule = new StringLiteral(sub);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Illegal pattern component: " + token);
            }

            rules.add(rule);
        }

        return rules;
    }

    /**
     * <p>
     * 지정 패턴에 따른 토큰 파싱을 수행한다. 내부적으로만 사용됨.
     * </p>
     * 
     * @param pattern  패턴
     * @param indexRef 인덱스 참조값
     * @return 파싱된 토큰
     */
    protected String parseToken(String pattern, int[] indexRef) {
        StringBuffer buf = new StringBuffer();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // Scan a run of the same character, which indicates a time
            // pattern.
            buf.append(c);

            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek == c) {
                    buf.append(c);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            // This will identify token as text.
            buf.append('\'');

            boolean inLiteral = false;

            for (; i < length; i++) {
                c = pattern.charAt(i);

                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' is treated as escaped '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral &&
                        (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
                    i--;
                    break;
                } else {
                    buf.append(c);
                }
            }
        }

        indexRef[0] = i;
        return buf.toString();
    }

    /**
     * <p>
     * 요구되는 패딩에 따른 적합한 규칙을 얻는다. 내부적으로만 사용됨.
     * </p>
     * 
     * @param field   규칙을 얻기위한 필드 정수값
     * @param padding 요구되는 패딩 정수값 (1 or 2)
     * @return 올바른 패딩에 대한 규칙
     */
    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnpaddedNumberField(field);
            case 2:
                return new TwoDigitNumberField(field);
            default:
                return new PaddedNumberField(field, padding);
        }
    }

    // Format methods
    // -----------------------------------------------------------------------
    /**
     * <p>
     * <code>Date</code>나 <code>Calendar</code> 객체를 포맷한다.
     * </p>
     * 
     * @param obj        대상 객체
     * @param toAppendTo 저장될 버퍼
     * @param pos        필드 위치 - 여기서는 무시됨
     * @return 포맷 버퍼 문자열
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof Date) {
            return format((Date) obj, toAppendTo);
        } else if (obj instanceof Calendar) {
            return format((Calendar) obj, toAppendTo);
        } else {
            throw new IllegalArgumentException("Unknown class: " +
                    (obj == null ? "<null>" : obj.getClass().getName()));
        }
    }

    /**
     * <p>
     * <code>Date</code> 객체를 포맷한다.
     * </p>
     * 
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Locale locale = Locale.KOREA;
     *              Date date = new Date();
     * 
     *              FastDateFormat.getInstance().format(date) = "05. 2. 4. 오전 4:11"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss").format(date) =
     *              "2005-02-04 04:11:25"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss",
     *              zone).format(date) = "2005-02-04 01:11:25"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss",
     *              locale).format(date) = "2005-02-04 04:11:25"
     *
     * @param date 포맷할 대상 객체
     * @return 포맷 문자열
     */
    public String format(Date date) {
        Calendar c = new GregorianCalendar(mTimeZone);
        c.setTime(date);
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /**
     * <p>
     * <code>Calendar</code> 객체를 포맷한다.
     * </p>
     * 
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Locale locale = Locale.KOREA;
     *              Calendar cal = Calendar.getInstance(zone);
     * 
     *              FastDateFormat.getInstance().format(cal) = "05. 2. 4. 오전 4:11"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss").format(cal) =
     *              "2005-02-04 01:11:25"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss",
     *              zone).format(cal) = "2005-02-04 01:11:25"
     *              FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss",
     *              locale).format(cal) = "2005-02-04 01:11:25"
     *
     * @param calendar 포맷할 대상 객체
     * @return 포맷 문자열
     */
    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /**
     * <p>
     * 제공한 <code>StringBuffer</code>에 <code>Date</code>를 포맷한다.
     * </p>
     * 
     * @param date 포맷할 객체
     * @param buf  포맷정보가 저장될 버퍼
     * @return 수행 결과
     */
    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = new GregorianCalendar(mTimeZone);
        c.setTime(date);
        return applyRules(c, buf);
    }

    /**
     * <p>
     * 제공한 <code>StringBuffer</code>에 <code>Calendar</code>를 포맷한다.
     * </p>
     * 
     * @param calendar 포맷할 객체
     * @param buf      포맷정보가 저장될 버퍼
     * @return 수행 결과
     */
    public StringBuffer format(Calendar calendar, StringBuffer buf) {
        if (mTimeZoneForced) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(mTimeZone);
        }
        return applyRules(calendar, buf);
    }

    /**
     * <p>
     * 지정 Calendar에 대해 규칙을 적용해 포맷한다.
     * </p>
     * 
     * @param calendar 포맷할 객체
     * @param buf      포맷정보가 저장될 버퍼
     * @return 수행 결과
     */
    protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
        Rule[] rules = mRules;
        int len = mRules.length;
        for (int i = 0; i < len; i++) {
            rules[i].appendTo(buf, calendar);
        }
        return buf;
    }

    // Parsing
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 현재 지원하지 않음, 항상 null 리턴함.
     * </p>
     * 
     * @param source 파싱할 문자열
     * @param pos    파싱 위치
     * @return 항상 <code>null</code> 반환.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        pos.setIndex(0);
        pos.setErrorIndex(0);
        return null;
    }

    // Accessors
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 현재 설정된 패턴정보를 얻는다.
     * </p>
     * 
     * @return 패턴정보, {@link java.text.SimpleDateFormat} 참조.
     */
    public String getPattern() {
        return mPattern;
    }

    /**
     * <p>
     * 현재 설정된 타임존을 얻는다.
     * </p>
     * 
     * @return the time zone
     */
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    /**
     * <p>
     * calendar에 설정된 타임존이 포맷터의 타임존을 오버라이드 한다면 <code>true</code>.
     * </p>
     * 
     * @return calendar에 오버라이드된 타임존이면 <code>true</code>
     */
    public boolean getTimeZoneOverridesCalendar() {
        return mTimeZoneForced;
    }

    /**
     * <p>
     * 현재 설정된 로케일을 얻는다.
     * </p>
     * 
     * @return 로케일
     */
    public Locale getLocale() {
        return mLocale;
    }

    /**
     * <p>
     * 현재 설정된 포맷에 대한 최대 길이를 얻는다.
     * </p>
     * 
     * @return 포맷 길이의 최대치
     */
    public int getMaxLengthEstimate() {
        return mMaxLengthEstimate;
    }

    // Basics
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 2개의 객체를 비교한다.
     * </p>
     * 
     * @param obj 비교할 객체
     * @return 같다면 <code>true</code>
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FastDateFormat)) {
            return false;
        }
        FastDateFormat other = (FastDateFormat) obj;
        if ((mPattern == other.mPattern || mPattern.equals(other.mPattern)) &&
                (mTimeZone == other.mTimeZone || mTimeZone.equals(other.mTimeZone)) &&
                (mLocale == other.mLocale || mLocale.equals(other.mLocale)) &&
                (mTimeZoneForced == other.mTimeZoneForced) &&
                (mLocaleForced == other.mLocaleForced)) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * 적합한 해쉬코드를 얻는다.
     * </p>
     * 
     * @return 해쉬코드
     */
    @Override
    public int hashCode() {
        int total = 0;
        total += mPattern.hashCode();
        total += mTimeZone.hashCode();
        total += (mTimeZoneForced ? 1 : 0);
        total += mLocale.hashCode();
        total += (mLocaleForced ? 1 : 0);
        return total;
    }

    /**
     * <p>
     * 현 객체의 문자열 버전을 얻는다.
     * </p>
     * 
     * @return 디버깅용 문자열
     */
    @Override
    public String toString() {
        return "FastDateFormat[" + mPattern + "]";
    }

    // Rules
    // -----------------------------------------------------------------------
    /**
     * <p>
     * 규칙을 정의하는 내부 인터페이스.
     * </p>
     */
    private interface Rule {
        int estimateLength();

        void appendTo(StringBuffer buffer, Calendar calendar);
    }

    /**
     * <p>
     * 숫자와 관련된 규칙을 정의하는 내부 인터페이스.
     * </p>
     */
    private interface NumberRule extends Rule {
        void appendTo(StringBuffer buffer, int value);
    }

    /**
     * <p>
     * 고정 단일 캐릭터 출력을 위한 내부 클래스.
     * </p>
     */
    private static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char value) {
            mValue = value;
        }

        public int estimateLength() {
            return 1;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValue);
        }
    }

    /**
     * <p>
     * 고정 단일 문자열 출력을 위한 내부 클래스.
     * </p>
     */
    private static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String value) {
            mValue = value;
        }

        public int estimateLength() {
            return mValue.length();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValue);
        }
    }

    /**
     * <p>
     * 설정된 값 출력을 위한 내부 클래스.
     * </p>
     */
    private static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int field, String[] values) {
            mField = field;
            mValues = values;
        }

        public int estimateLength() {
            int max = 0;
            for (int i = mValues.length; --i >= 0;) {
                int len = mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValues[calendar.get(mField)]);
        }
    }

    /**
     * <p>
     * 채워지지 않은 숫자 출력을 위한 내부 클래스.
     * </p>
     */
    private static class UnpaddedNumberField implements NumberRule {
        static final UnpaddedNumberField INSTANCE_YEAR = new UnpaddedNumberField(Calendar.YEAR);

        private final int mField;

        UnpaddedNumberField(int field) {
            mField = field;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + '0'));
            } else if (value < 100) {
                buffer.append((char) (value / 10 + '0'));
                buffer.append((char) (value % 10 + '0'));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>
     * 채워지지 않은 월(月) 출력을 위한 내부클래스.
     * </p>
     */
    private static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + '0'));
            } else {
                buffer.append((char) (value / 10 + '0'));
                buffer.append((char) (value % 10 + '0'));
            }
        }
    }

    /**
     * <p>
     * 채워진 숫자 출력을 위한 내부 클래스.
     * </p>
     */
    private static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                // Should use UnpaddedNumberField or TwoDigitNumberField.
                throw new IllegalArgumentException();
            }
            mField = field;
            mSize = size;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                for (int i = mSize; --i >= 2;) {
                    buffer.append('0');
                }
                buffer.append((char) (value / 10 + '0'));
                buffer.append((char) (value % 10 + '0'));
            } else {
                int digits;
                if (value < 1000) {
                    digits = 3;
                } else {
                    digits = (int) (Math.log(value) / LOG_10) + 1;
                }
                for (int i = mSize; --i >= digits;) {
                    buffer.append('0');
                }
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>
     * 2자리 아라비아 숫자 출력을 위한 내부 클래스.
     * </p>
     */
    private static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int field) {
            mField = field;
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                buffer.append((char) (value / 10 + '0'));
                buffer.append((char) (value % 10 + '0'));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>
     * 2자리 아라비아 연도 출력을 위한 내부클래스.
     * </p>
     */
    private static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) (value / 10 + '0'));
            buffer.append((char) (value % 10 + '0'));
        }
    }

    /**
     * <p>
     * 2자리 아라비아 월(月) 출력을 위한 내부클래스.
     * </p>
     */
    private static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) (value / 10 + '0'));
            buffer.append((char) (value % 10 + '0'));
        }
    }

    /**
     * <p>
     * 12 시간 필드의 출력을 위한 내부 클래스.
     * </p>
     */
    private static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            mRule = rule;
        }

        public int estimateLength() {
            return mRule.estimateLength();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(Calendar.HOUR);
            if (value == 0) {
                value = calendar.getLeastMaximum(Calendar.HOUR) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        public void appendTo(StringBuffer buffer, int value) {
            mRule.appendTo(buffer, value);
        }
    }

    /**
     * <p>
     * 24 시간 필드 출력을 위한 내부 클래스.
     * </p>
     */
    private static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            mRule = rule;
        }

        public int estimateLength() {
            return mRule.estimateLength();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(Calendar.HOUR_OF_DAY);
            if (value == 0) {
                value = calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        public void appendTo(StringBuffer buffer, int value) {
            mRule.appendTo(buffer, value);
        }
    }

    /**
     * <p>
     * 타임존 이름 출력을 위한 내부클래스.
     * </p>
     */
    private static class TimeZoneNameRule implements Rule {
        private final TimeZone mTimeZone;
        private final boolean mTimeZoneForced;
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;

        TimeZoneNameRule(TimeZone timeZone, boolean timeZoneForced, Locale locale, int style) {
            mTimeZone = timeZone;
            mTimeZoneForced = timeZoneForced;
            mLocale = locale;
            mStyle = style;

            if (timeZoneForced) {
                mStandard = getTimeZoneDisplay(timeZone, false, style, locale);
                mDaylight = getTimeZoneDisplay(timeZone, true, style, locale);
            } else {
                mStandard = null;
                mDaylight = null;
            }
        }

        public int estimateLength() {
            if (mTimeZoneForced) {
                return Math.max(mStandard.length(), mDaylight.length());
            } else if (mStyle == TimeZone.SHORT) {
                return 4;
            } else {
                return 40;
            }
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            if (mTimeZoneForced) {
                if (mTimeZone.useDaylightTime() && calendar.get(Calendar.DST_OFFSET) != 0) {
                    buffer.append(mDaylight);
                } else {
                    buffer.append(mStandard);
                }
            } else {
                TimeZone timeZone = calendar.getTimeZone();
                if (timeZone.useDaylightTime() && calendar.get(Calendar.DST_OFFSET) != 0) {
                    buffer.append(getTimeZoneDisplay(timeZone, true, mStyle, mLocale));
                } else {
                    buffer.append(getTimeZoneDisplay(timeZone, false, mStyle, mLocale));
                }
            }
        }
    }

    /**
     * <p>
     * 숫자 <code>+/-HHMM</code> 또는 <code>+/-HH:MM</code>와 같은 타임존 출력을 위한 내부 클래스.
     * </p>
     */
    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);

        final boolean mColon;

        TimeZoneNumberRule(boolean colon) {
            mColon = colon;
        }

        public int estimateLength() {
            return 5;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);

            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }

            int hours = offset / (60 * 60 * 1000);
            buffer.append((char) (hours / 10 + '0'));
            buffer.append((char) (hours % 10 + '0'));

            if (mColon) {
                buffer.append(':');
            }

            int minutes = offset / (60 * 1000) - 60 * hours;
            buffer.append((char) (minutes / 10 + '0'));
            buffer.append((char) (minutes % 10 + '0'));
        }
    }

    // ----------------------------------------------------------------------
    /**
     * <p>
     * 타임존 이름에 대한 복합키로 사용되는 내부 클래스.
     * </p>
     */
    private static class TimeZoneDisplayKey {
        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;

        TimeZoneDisplayKey(TimeZone timeZone,
                boolean daylight, int style, Locale locale) {
            mTimeZone = timeZone;
            if (daylight) {
                style |= 0x80000000;
            }
            mStyle = style;
            mLocale = locale;
        }

        @Override
        public int hashCode() {
            return mStyle * 31 + mLocale.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
                return mTimeZone.equals(other.mTimeZone) &&
                        mStyle == other.mStyle &&
                        mLocale.equals(other.mLocale);
            }
            return false;
        }
    }

    // ----------------------------------------------------------------------
    /**
     * <p>
     * 복합 객체를 만들기 위한 내부 Helper 클래스.
     * </p>
     *
     * <p>
     * 복합 객체의 해쉬테이블 키를 만들기위한 클래스로서 사용된다.
     * </p>
     */
    private static class Pair {
        private final Object mObj1;
        private final Object mObj2;

        public Pair(Object obj1, Object obj2) {
            mObj1 = obj1;
            mObj2 = obj2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Pair)) {
                return false;
            }

            Pair key = (Pair) obj;

            return (mObj1 == null ? key.mObj1 == null : mObj1.equals(key.mObj1)) &&
                    (mObj2 == null ? key.mObj2 == null : mObj2.equals(key.mObj2));
        }

        @Override
        public int hashCode() {
            return (mObj1 == null ? 0 : mObj1.hashCode()) +
                    (mObj2 == null ? 0 : mObj2.hashCode());
        }

        @Override
        public String toString() {
            return "[" + mObj1 + ':' + mObj2 + ']';
        }
    }

}
