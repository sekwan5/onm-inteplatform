/* ---------------------------------------------------------------------
 * @(#)DateUtil.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;

/**
 * <p>
 * {@link java.util.Calendar}와 {@link java.util.Date}를 다루는데
 * 필요한 여러 기능을 지원하는 클래스
 * </p>
 *
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 * @author Stephen Colebourne
 * @author Janek Bogucki
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author <a href="mailto:yhlee@handysoft.co.kr">yhlee</a>
 * @since 2.0
 * @version $Id: DateUtil.java,v 1.1 2009/04/07 10:46:24 since88 Exp $
 */
public class DateUtil {

    /**
     * UTC 타임존. (가끔씩 GMT로써 참조된다)
     */
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    /**
     * 표준 초(初)에 대한 밀리세컨드
     */
    public static final int MILLIS_IN_SECOND = 1000;
    /**
     * 표준 분(分)에 대한 밀리세컨드
     */
    public static final int MILLIS_IN_MINUTE = 60 * 1000;
    /**
     * 표준 시(時)에 대한 밀리세컨드
     */
    public static final int MILLIS_IN_HOUR = 60 * 60 * 1000;
    /**
     * 표준 일(日)에 대한 밀리세컨드
     */
    public static final int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    /**
     * 보름치, 어떤 달의 보름 이상인지, 이하인지를 표현하는데 사용한다.
     */
    public final static int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            { Calendar.MILLISECOND },
            { Calendar.SECOND },
            { Calendar.MINUTE },
            { Calendar.HOUR_OF_DAY, Calendar.HOUR },
            { Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM /*
                                                                    * Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK,
                                                                    * Calendar.DAY_OF_WEEK_IN_MONTH
                                                                    */ },
            { Calendar.MONTH, DateUtil.SEMI_MONTH },
            { Calendar.YEAR },
            { Calendar.ERA } };

    /**
     * 지정한 날이 속한 한 주의 범위를 <b>일요일</b> 부터 지정함.
     */
    public final static int RANGE_WEEK_SUNDAY = 1;

    /**
     * 지정한 날이 속한 한 주의 범위를 <b>월요일</b> 부터 지정함.
     */
    public final static int RANGE_WEEK_MONDAY = 2;

    /**
     * 지정한 날이 속한 한 주의 범위를 <b>지정한 날</b> 부터 지정함.
     */
    public final static int RANGE_WEEK_RELATIVE = 3;

    /**
     * 지정한 날이 속한 한 주의 범위를 <b>지정한 날</b> 을 가운데로 해서 주변 날짜를 지정함.
     */
    public final static int RANGE_WEEK_CENTER = 4;

    /**
     * 지정한 날이 속한 한 달의 범위를 <b>일요일</b> 부터 지정함.
     */
    public final static int RANGE_MONTH_SUNDAY = 5;

    /**
     * 지정한 날이 속한 한 달의 범위를 <b>월요일</b> 부터 지정함.
     */
    public final static int RANGE_MONTH_MONDAY = 6;

    /**
     * <p>
     * 사용가능한 모든 메소드가 static이기 때문에 <code>DateUtil</code> 인스탄스는
     * 일반적으로 필요하지 않음.<br>
     * 대신 아래와 같이 사용하면 됨.<br>
     * <code>DateUtil.parse(str)</code>.
     * </p>
     *
     * <p>
     * 참고로 본 생성자는 JavaBean 인스탄스를 위해 public으로 선언됨
     * </p>
     */
    public DateUtil() {
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반올림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              Date date = cal.getTime();
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(date) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.round(date, Calendar.YEAR)) = 2006-01-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.MONTH)) = 2005-09-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.DATE)) = 2005-09-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.round(date, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.round(date, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반올림한 값 (객체는 복제되어 리턴됨.)
     * @throws IllegalArgumentException date가 <code>null</code>일 경우 발생.
     */
    public static Date round(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        GregorianCalendar gval = new GregorianCalendar();
        gval.setTime(date);
        modify(gval, field, true);
        return gval.getTime();
    }

    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반올림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(cal) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.round(cal, Calendar.YEAR)) = 2006-01-01
     *              12:00:00
     *              ff.format(DateUtil.round(cal, Calendar.MONTH)) = 2005-09-01
     *              12:00:00
     *              ff.format(DateUtil.round(cal, Calendar.DATE)) = 2005-09-01
     *              12:00:00
     *              ff.format(DateUtil.round(cal, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.round(cal, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.round(cal, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반올림한 값
     * @throws IllegalArgumentException date가 <code>null</code>일 경우 발생.
     */
    public static Calendar round(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar rounded = (Calendar) date.clone();
        modify(rounded, field, true);
        return rounded;
    }

    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반올림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              Date date = cal.getTime();
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(date) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.round(date, Calendar.YEAR)) = 2006-01-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.MONTH)) = 2005-09-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.DATE)) = 2005-09-01
     *              09:00:00
     *              ff.format(DateUtil.round(date, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.round(date, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.round(date, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜 객체
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반올림한 값 (객체는 복제되어 리턴됨.)
     * @throws IllegalArgumentException date가 <code>null</code>일 경우 발생.
     * @throws ClassCastException       대상 객체가 <code>Date</code>나
     *                                  <code>Calendar</code> 유형이 아닐 경우 발생.
     */
    public static Date round(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (date instanceof Date) {
            return round((Date) date, field);
        } else if (date instanceof Calendar) {
            return round((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not round " + date);
        }
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반내림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              Date date = cal.getTime();
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(date) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.truncate(date, Calendar.YEAR)) = 2005-01-01
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.MONTH)) = 2005-08-01
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.DATE)) = 2005-08-31
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.truncate(date, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜 객체
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반내림한 값 (객체는 복제되어 리턴됨.)
     * @throws IllegalArgumentException date가 <code>null</code>일 경우 발생.
     */
    public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        GregorianCalendar gval = new GregorianCalendar();
        gval.setTime(date);
        modify(gval, field, false);
        return gval.getTime();
    }

    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반내림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(cal) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.truncate(cal, Calendar.YEAR)) = 2005-01-01
     *              12:00:00
     *              ff.format(DateUtil.truncate(cal, Calendar.MONTH)) = 2005-08-01
     *              12:00:00
     *              ff.format(DateUtil.truncate(cal, Calendar.DATE)) = 2005-08-31
     *              12:00:00
     *              ff.format(DateUtil.truncate(cal, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.truncate(cal, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.truncate(cal, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜 객체
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반내림한 값 (객체는 복제되어 리턴됨.)
     * @throws IllegalArgumentException date가 <code>null</code>일 경우 발생.
     */
    public static Calendar truncate(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar truncated = (Calendar) date.clone();
        modify(truncated, field, false);
        return truncated;
    }

    /**
     * <p>
     * 지정한 날짜에 대해 해당 필드를 반내림한다.
     * </p>
     *
     * @pi2.example .
     *              TimeZone zone = TimeZone.getTimeZone("GMT+9");
     *              Calendar cal = Calendar.getInstance(zone);
     *              cal.set(2005, 8, 1, 1, 12, 20);
     *              Date date = cal.getTime();
     *              FastDateFormat ff = FastDateFormat.getInstance("yyyy-MM-dd
     *              hh:mm:ss", zone);
     *
     *              ff.format(date) = 2005-09-01 01:12:20
     *              ff.format(DateUtil.truncate(date, Calendar.YEAR)) = 2005-01-01
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.MONTH)) = 2005-08-01
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.DATE)) = 2005-08-31
     *              09:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.HOUR)) = 2005-09-01
     *              01:00:00
     *              ff.format(DateUtil.truncate(date, Calendar.MINUTE)) = 2005-09-01
     *              01:12:00
     *              ff.format(DateUtil.truncate(date, Calendar.SECOND)) = 2005-09-01
     *              01:12:20
     * 
     * @param date  대상 날짜 객체
     * @param field <code>Calendar</code> 또는 <code>SEMI_MONTH</code> 부터의 필드
     * @return 반내림한 값 (객체는 복제되어 리턴됨.)
     * @throws ClassCastException 대상 객체가 <code>Date</code>나 <code>Calendar</code>
     *                            유형이 아닐 경우 발생.
     */
    public static Date truncate(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (date instanceof Date) {
            return truncate((Date) date, field);
        } else if (date instanceof Calendar) {
            return truncate((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not truncate " + date);
        }
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 날짜 계산 함수. 내부적으로만 사용함.
     * </p>
     * 
     * @param val   calendar 객체
     * @param field 적용 필드
     * @param round 반올림 여부
     */
    private static void modify(Calendar val, int field, boolean round) {
        boolean roundUp = false;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == field) {
                    // This is our field... we stop looping
                    if (round && roundUp) {
                        if (field == DateUtil.SEMI_MONTH) {
                            // This is a special case that's hard to generalize
                            // If the date is 1, we round up to 16, otherwise
                            // we subtract 15 days and add 1 month
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            // We need at add one to this field since the
                            // last number causes us to round up
                            val.add(fields[i][0], 1);
                        }
                    }
                    return;
                }
            }
            // We have various fields that are not easy roundings
            int offset = 0;
            boolean offsetSet = false;
            // These are special types of fields that require different rounding rules
            switch (field) {
                default:
                case DateUtil.SEMI_MONTH:
                    if (fields[i][0] == Calendar.DATE) {
                        // If we're going to drop the DATE field's value,
                        // we want to do this our own way.
                        // We need to subtrace 1 since the date has a minimum of 1
                        offset = val.get(Calendar.DATE) - 1;
                        // If we're above 15 days adjustment, that means we're in the
                        // bottom half of the month and should stay accordingly.
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        // Record whether we're in the top or bottom half of that range
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (fields[i][0] == Calendar.HOUR) {
                        // If we're going to drop the HOUR field's value,
                        // we want to do this our own way.
                        offset = val.get(Calendar.HOUR);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset > 6;
                        offsetSet = true;
                    }
                    break;
            }
            if (!offsetSet) {
                int min = val.getActualMinimum(fields[i][0]);
                int max = val.getActualMaximum(fields[i][0]);
                // Calculate the offset from the minimum allowed value
                offset = val.get(fields[i][0]) - min;
                // Set roundUp if this is more than half way between the minimum and maximum
                roundUp = offset > ((max - min) / 2);
            }
            // We need to remove this field
            val.add(fields[i][0], -offset);
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");

    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * 지정한 범위스타일에 따른 날짜를 열거형<code>Iterator</code>에 담는다.
     * </p>
     *
     * <p>
     * 특정일로부터의 한 주간 범위 등의 달력관련 App에 응용될 수 있다.
     * </p>
     * 
     * @param focus      작업에 적용될 날짜
     * @param rangeStyle 범위 스타일. {@link #iterator(Calendar, int)} 한수를 참조할 것.
     *
     * @return 날짜를 담은 열거형 객체
     * @throws IllegalArgumentException date가 <code>null</code>이거나 rangeStyle이 없을 경우
     *                                  발생.
     */
    public static Iterator iterator(Date focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        GregorianCalendar gval = new GregorianCalendar();
        gval.setTime(focus);
        return iterator(gval, rangeStyle);
    }

    /**
     * <p>
     * 지정한 범위스타일에 따른 날짜를 열거형<code>Iterator</code>에 담는다.
     * </p>
     *
     * <p>
     * 특정일로부터의 한 주간 범위 등의 달력관련 App에 응용될 수 있다.
     * </p>
     * 
     * @param focus      작업에 적용될 날짜
     * @param rangeStyle 범위 스타일. 의미는 각 해당 링크 참조.<br>
     *                   {@link DateUtil#RANGE_MONTH_SUNDAY}. <br>
     *                   {@link DateUtil#RANGE_MONTH_MONDAY}. <br>
     *                   {@link DateUtil#RANGE_WEEK_SUNDAY}. <br>
     *                   {@link DateUtil#RANGE_WEEK_MONDAY}. <br>
     *                   {@link DateUtil#RANGE_WEEK_RELATIVE}. <br>
     *                   {@link DateUtil#RANGE_WEEK_CENTER}
     *
     * @return 날짜를 담은 열거형 객체
     * @throws IllegalArgumentException date가 <code>null</code>이거나 rangeStyle이 없을 경우
     *                                  발생.
     */
    public static Iterator iterator(Calendar focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar start = null;
        Calendar end = null;
        int startCutoff = Calendar.SUNDAY;
        int endCutoff = Calendar.SATURDAY;
        switch (rangeStyle) {
            case RANGE_MONTH_SUNDAY:
            case RANGE_MONTH_MONDAY:
                // Set start to the first of the month
                start = truncate(focus, Calendar.MONTH);
                // Set end to the last of the month
                end = (Calendar) start.clone();
                end.add(Calendar.MONTH, 1);
                end.add(Calendar.DATE, -1);
                // Loop start back to the previous sunday or monday
                if (rangeStyle == RANGE_MONTH_MONDAY) {
                    startCutoff = Calendar.MONDAY;
                    endCutoff = Calendar.SUNDAY;
                }
                break;
            case RANGE_WEEK_SUNDAY:
            case RANGE_WEEK_MONDAY:
            case RANGE_WEEK_RELATIVE:
            case RANGE_WEEK_CENTER:
                // Set start and end to the current date
                start = truncate(focus, Calendar.DATE);
                end = truncate(focus, Calendar.DATE);
                switch (rangeStyle) {
                    default:
                    case RANGE_WEEK_SUNDAY:
                        // already set by default
                        break;
                    case RANGE_WEEK_MONDAY:
                        startCutoff = Calendar.MONDAY;
                        endCutoff = Calendar.SUNDAY;
                        break;
                    case RANGE_WEEK_RELATIVE:
                        startCutoff = focus.get(Calendar.DAY_OF_WEEK);
                        endCutoff = startCutoff - 1;
                        break;
                    case RANGE_WEEK_CENTER:
                        startCutoff = focus.get(Calendar.DAY_OF_WEEK) - 3;
                        endCutoff = focus.get(Calendar.DAY_OF_WEEK) + 3;
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
        }
        if (startCutoff < Calendar.SUNDAY) {
            startCutoff += 7;
        }
        if (startCutoff > Calendar.SATURDAY) {
            startCutoff -= 7;
        }
        if (endCutoff < Calendar.SUNDAY) {
            endCutoff += 7;
        }
        if (endCutoff > Calendar.SATURDAY) {
            endCutoff -= 7;
        }
        while (start.get(Calendar.DAY_OF_WEEK) != startCutoff) {
            start.add(Calendar.DATE, -1);
        }
        while (end.get(Calendar.DAY_OF_WEEK) != endCutoff) {
            end.add(Calendar.DATE, 1);
        }
        return new DateIterator(start, end);
    }

    /**
     * <p>
     * 지정한 범위스타일에 따른 날짜를 열거형<code>Iterator</code>에 담는다.
     * </p>
     *
     * <p>
     * 특정일로부터의 한 주간 범위 등의 달력관련 App에 응용될 수 있다.
     * </p>
     * 
     * @param focus      작업에 적용될 날짜
     * @param rangeStyle 범위 스타일. 의미는 각 해당 링크 참조.
     *
     * @return 날짜를 담은 열거형 객체
     * @throws IllegalArgumentException date가 <code>null</code>이거나 rangeStyle이 없을 경우
     *                                  발생.
     * @throws ClassCastException       대상 객체가 <code>Date</code>나
     *                                  <code>Calendar</code> 유형이 아닐 경우 발생.
     */
    public static Iterator iterator(Object focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (focus instanceof Date) {
            return iterator((Date) focus, rangeStyle);
        } else if (focus instanceof Calendar) {
            return iterator((Calendar) focus, rangeStyle);
        } else {
            throw new ClassCastException("Could not iterate based on " + focus);
        }
    }

    /**
     * <p>
     * 날짜를 담을 열거자 내부 클래스.
     * </p>
     */
    static class DateIterator implements Iterator {
        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar startFinal, Calendar endFinal) {
            super();
            this.endFinal = endFinal;
            spot = startFinal;
            spot.add(Calendar.DATE, -1);
        }

        public boolean hasNext() {
            return spot.before(endFinal);
        }

        public Object next() {
            if (spot.equals(endFinal)) {
                throw new NoSuchElementException();
            }
            spot.add(Calendar.DATE, 1);
            return spot.clone();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 지정한 날짜의 입력된 값 만큼의 경과일 구하기
     *
     * @param offset int(증가일수)
     * @param date   String(날짜)
     * @return String
     */
    public static String addDate(int offset, String date) {
        int z = getDaysFrom21Century(date);
        int n = z + offset;
        return getDateStringFrom21Century(n);
    }

    /**
     * 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다. 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
     *
     * @param s String(날짜)
     * @return int
     */
    public static int getDaysFrom21Century(String s) {

        int d, m, y;
        if (s.length() == 8) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(4, 6));
            d = Integer.parseInt(s.substring(6));
            return getDaysFrom21Century(d, m, y);
        } else if (s.length() == 10) {
            y = Integer.parseInt(s.substring(0, 4));
            m = Integer.parseInt(s.substring(5, 7));
            d = Integer.parseInt(s.substring(8));
            return getDaysFrom21Century(d, m, y);
        } else if (s.length() == 11) {
            d = Integer.parseInt(s.substring(0, 2));
            String strM = s.substring(3, 6).toUpperCase();
            String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
                    "AUG", "SEP", "OCT", "NOV", "DEC" };
            m = 0;
            for (int j = 1; j <= 12; j++) {
                if (strM.equals(monthNames[j - 1])) {
                    m = j;
                    break;
                }
            }
            if (m < 1 || m > 12)
                throw new RuntimeException("Invalid month name: " + strM + " in " + s);
            y = Integer.parseInt(s.substring(7));
            return getDaysFrom21Century(d, m, y);
        } else
            throw new RuntimeException("Invalid date format: " + s);
    }

    /**
     * 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다. 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
     *
     * @param d int 일
     * @param m int 월
     * @param y int 년
     * @return int
     */
    public static int getDaysFrom21Century(int d, int m, int y) {

        if (y >= 2000) {
            int sum = getDaysFromYearFirst(d, m, y);
            for (int j = y - 1; j >= 2000; j--)
                sum += getDaysInYear(j);
            return sum - 1;
        } else if (y > 0 && y < 2000) {
            int sum = getDaysFromYearFirst(d, m, y);
            for (int j = 1999; j >= y; j--)
                sum -= getDaysInYear(y);
            return sum - 1;
        } else
            throw new RuntimeException("Invalid year " + y + " in " + d + "/" + m + "/"
                    + y);
    }

    /**
     * 지정한 년도의 첫날 부터 지정한 월의 지정한 날 까지의 날짜 수를 구한다.
     *
     * @param day   int 일
     * @param month int 월
     * @param year  int 년
     * @return int
     */
    public static int getDaysFromYearFirst(int day, int month, int year) {

        if (month < 1 || month > 12) {
            throw new RuntimeException("Invalid month " + month + " in " + day + "/"
                    + month + "/" + year);
        }

        int max = getDaysInMonth(month, year);

        if (day >= 1 && day <= max) {
            int sum = day;
            for (int j = 1; j < month; j++)
                sum += getDaysInMonth(j, year);
            return sum;
        } else {
            throw new RuntimeException("Invalid date " + day + " in " + day + "/" + month
                    + "/" + year);
        }
    }

    /**
     * 2000년 1월 1일 기준을 n일 경과한 날짜를 구한다.
     *
     * @param elapsed int
     * @return String
     */
    public static String getDateStringFrom21Century(int elapsed) {

        int y = 2000;
        int m = 1;
        int d = 1;

        int n = elapsed + 1;
        int j = 2000;
        if (n > 0) {
            while (n > getDaysInYear(j)) {
                n -= getDaysInYear(j);
                j++;
            }
            y = j;

            int i = 1;
            while (n > getDaysInMonth(i, y)) {
                n -= getDaysInMonth(i, y);
                i++;
            }
            m = i;
            d = n;
        } else if (n < 0) {
            while (n < 0) {
                n += getDaysInYear(j - 1);
                j--;
            }
            y = j;

            int i = 1;
            while (n > getDaysInMonth(i, y)) {
                n -= getDaysInMonth(i, y);
                i++;
            }
            m = i;
            d = n;
        }

        String strY = "" + y;
        String strM = "";
        String strD = "";

        if (m < 10)
            strM = "0" + m;
        else
            strM = "" + m;

        if (d < 10)
            strD = "0" + d;
        else
            strD = "" + d;

        return strY + "/" + strM + "/" + strD;
    }

    /**
     * 지정한 년도의 총 날짜 수를 구한다.
     *
     * @param year int 년도
     * @return int
     */
    public static int getDaysInYear(int year) {

        if (year > 1582) {
            if (year % 400 == 0)
                return 366;
            else if (year % 100 == 0)
                return 365;
            else if (year % 4 == 0)
                return 366;
            else
                return 365;
        } else if (year == 1582) {
            return 355;
        } else if (year > 4) {
            if (year % 4 == 0)
                return 366;
            else
                return 365;
        } else if (year > 0) {
            return 365;
        } else {
            return 0;
        }
    }

    /**
     * 지정한 년도, 지정한 월의 총 날짜 수를 구한다.
     *
     * @param m int
     * @param y int
     * @return int
     */
    public static int getDaysInMonth(int month, int year) {

        if (month < 1 || month > 12) {
            throw new RuntimeException("Invalid month: " + month);
        }

        int[] b = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        if (month != 2 && month >= 1 && month <= 12 && year != 1582) {
            return b[month - 1];
        }

        if (month != 2 && month >= 1 && month <= 12 && year == 1582) {
            if (month != 10) {
                return b[month - 1];
            } else {
                return b[month - 1] - 10;
            }
        }
        if (month != 2) {
            return 0;
        }
        // m == 2 (즉 2월)
        if (year > 1582) {
            if (year % 400 == 0) {
                return 29;
            } else if (year % 100 == 0) {
                return 28;
            } else if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else if (year == 1582) {
            return 28;
        } else if (year > 4) {
            if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else if (year > 0) {
            return 28;
        } else {
            throw new RuntimeException("Invalid year: " + year);
        }
    }

}
