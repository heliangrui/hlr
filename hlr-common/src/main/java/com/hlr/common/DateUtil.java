package com.hlr.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil
 * Description:
 * date: 2023/10/19 11:08
 *
 * @author hlr
 */
public class DateUtil {
    private static final String[] wtb = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private DateUtil() {
    }

    public static int fmtDateTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (int) (date.getTime() / 1000L);
    }

    public static boolean isFistMonthOfThisYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(2) == 0;
    }

    public static int getNextMonthZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, 1);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getToDayZeroTime() {
        Calendar c = Calendar.getInstance();
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static Date getAgoDayTime(int day) {
        Calendar c = Calendar.getInstance();
        c.add(5, day);
        return c.getTime();
    }

    public static String fmtLongTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dtTime = new Date();
        dtTime.setTime(time * 1000L);
        return sdf.format(dtTime);
    }

    public static String fmtLongTimeMsec(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dtTime = new Date();
        dtTime.setTime(time);
        return sdf.format(dtTime);
    }

    public static String fmtLongTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dtTime = new Date();
        dtTime.setTime(time * 1000L);
        return sdf.format(dtTime);
    }

    public static String fmtLongTimeMsec(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date dtTime = new Date();
        dtTime.setTime(time);
        return sdf.format(dtTime);
    }

    public static int getNowIntTime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static int fmtStrTime(String dt, String pattern) {
        new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date date;
        try {
            date = sdf.parse(dt);
        } catch (ParseException var5) {
            return 0;
        }

        return date.getTime() > 2147483647000L ? Integer.MAX_VALUE : (int) (date.getTime() / 1000L);
    }

    public static String changeFmtStr(String dt, String currentPattern, String targetPattern) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(currentPattern);

        try {
            Date date = sdf1.parse(dt);
            SimpleDateFormat sdf2 = new SimpleDateFormat(targetPattern);
            return sdf2.format(date);
        } catch (ParseException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(1);
    }

    public static int getOldYear(long oldLongTime) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(oldLongTime * 1000L);
        return now.get(1);
    }

    public static int getCurrentMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(2) + 1;
    }

    public static int getCurrentDay() {
        Calendar now = Calendar.getInstance();
        return now.get(5);
    }

    public static int getCurrentHour() {
        Calendar now = Calendar.getInstance();
        return now.get(11);
    }

    public static int getCurrentYear2(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(1);
    }

    public static int getCurrentMonth2(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(2) + 1;
    }

    public static int getCurrentMonth3(int datetime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long) datetime * 1000L);
        return c.get(2) + 1;
    }

    public static int getCurrentDay2(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(5);
    }

    public static int getCurrentDay3(int datetime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long) datetime * 1000L);
        return c.get(5);
    }

    public static int getCurrentHour2(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(11);
    }

    public static int getCurrentHour3(int datetime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long) datetime * 1000L);
        return c.get(11);
    }

    public static String SecToHour(long second) {
        long min = second / 60L;
        long sec = second % 60L;
        long hour = 0L;
        if (min >= 60L) {
            hour = min / 60L;
            min -= hour * 60L;
        }

        StringBuffer sb = new StringBuffer();
        if (hour != 0L) {
            sb.append(hour);
            sb.append("h");
        }

        if (min != 0L) {
            sb.append(min);
            sb.append("m");
        }

        sb.append(sec);
        sb.append("s");
        return sb.toString();
    }

    public static String getTodayString() {
        long now = System.currentTimeMillis() / 1000L;
        return fmtLongTime(now, "yyyy-MM-dd");
    }

    public static String getTodayString2() {
        long now = System.currentTimeMillis() / 1000L;
        return fmtLongTime(now, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getOneMonthAgoString() {
        long now = System.currentTimeMillis() / 1000L - 2592000L;
        return fmtLongTime(now, "yyyy-MM-dd");
    }

    public static int getDate(int time) {
        return (int) DateStr2LongTime(fmtLongTime((long) time, "yyyy-M-dd"));
    }

    public static String fmtLongTime(long time, int digit, int style) {
        String dtStyle = "";
        switch (style) {
            case 1:
                dtStyle = "yyyy-M-dd HH:mm:ss";
                break;
            case 2:
                dtStyle = "yyyy-M-dd";
                break;
            case 3:
                dtStyle = "HH:mm:ss";
                break;
            case 4:
                dtStyle = "yy-M-dd";
                break;
            case 5:
                dtStyle = "M-dd HH";
                break;
            case 6:
                dtStyle = "yyyy/M/dd";
                break;
            case 7:
                dtStyle = "yyyy/M/dd HH:mm:ss";
                break;
            case 8:
                dtStyle = "yyyy/M/dd HH:mm";
                break;
            case 9:
                dtStyle = "yyyyMM";
                break;
            default:
                dtStyle = "yyyy/M/dd HH:mm:ss";
        }

        if (digit == 10) {
            time *= 1000L;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dtStyle);
        Date dtTime = new Date();
        dtTime.setTime(time);
        return sdf.format(dtTime);
    }

    public static long getTodayLongTime() {
        String dt = getTodayString();
        return DateStr2LongTime(dt);
    }

    public static int DateStr3LongTime(String dt, int unit) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        try {
            date = sdf.parse(dt);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return (date.getHours() * 60 + date.getMinutes()) / (unit / 60);
    }

    public static long DateStr2LongTime(String dt) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

        try {
            date = sdf.parse(dt);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date.getTime() / 1000L;
    }

    public static int getWeekOfMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(time * 1000L);
        calendar.setTime(date);
        return calendar.get(4);
    }

    public static int getMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(time * 1000L);
        calendar.setTime(date);
        return calendar.get(2);
    }

    public static int getHour(long time) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(time * 1000L);
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getMonthDayCount(int datetime) {
        return getCurrentDay2(getLastDayOfThisMonth(datetime));
    }

    public static long getLongTimeByYearMonthWeekOfMonth(int year, int month, int weekOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.set(4, weekOfMonth);
        calendar.set(7, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis() / 1000L;
    }

    public static Date getFirstDayOfThisMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static int getFirstDayOfThisMonth(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTime().getTime() / 1000L);
    }

    public static int getFirstTimeOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2, calendar.get(2) + month);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTimeInMillis() / 1000L);
    }

    public static int getFirstDayOfThisYear(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.set(2, 0);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTime().getTime() / 1000L);
    }

    public static Date getLastDayOfThisMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static Date getLastDayOfThisMonth(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    public static int getLastDayOfThisMonth2(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTime().getTime() / 1000L);
    }

    public static int getLastDayLastTimeOfThisMonth(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return (int) (calendar.getTime().getTime() / 1000L);
    }

    public static int getLastDayOfThisYear(int datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) datetime * 1000L);
        calendar.set(2, 11);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTime().getTime() / 1000L);
    }

    public static long getFirstDayOfThisMonthByLogic(int year, int month) {
        long firstday = 0L;
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDayOfThisMonth(year, month));
        int f = cal.get(7);
        if (f > 2) {
            firstday = getFirstDayOfThisMonth(year, month).getTime() + (long) ((7 - f + 1) * 86400000);
        } else {
            firstday = getFirstDayOfThisMonth(year, month).getTime() - (long) ((f - 1) * 86400000);
        }

        return firstday;
    }

    public static long getLastDayOfThisMonthByLogic(int year, int month) {
        long lastday = 0L;
        Calendar cal = Calendar.getInstance();
        cal.setTime(getLastDayOfThisMonth(year, month));
        int l = cal.get(7);
        if (l > 1) {
            lastday = getLastDayOfThisMonth(year, month).getTime() + (long) ((7 - l + 1) * 86400000);
        } else {
            lastday = getLastDayOfThisMonth(year, month).getTime() - 86400000L;
        }

        return lastday;
    }

    public static int getHourZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getTodayZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getFirstMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(2, 0);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getLastMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(2, 11);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNearMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNearMonth(Date date, int monthCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1 * monthCount);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNearYearMonth(Date date, int monthCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1 * monthCount);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return Integer.valueOf(fmtLongTime(c.getTime().getTime() / 1000L, "yyyyMM"));
    }

    public static int getXMonthAgo(Date date, int monthCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1 * monthCount);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, 1);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNextStartTime(int hour) {
        Calendar c = Calendar.getInstance();
        if (getCurrentHour() > hour) {
            c.add(5, 1);
        }

        c.set(11, hour);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getMonthZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getLastMonthZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static long getHourZeroTime(int time) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date((long) time * 1000L));
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime().getTime();
    }

    public static boolean isFistDayOfThisMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(5) == 1;
    }

    public static String GetCurrentDateTimeToString(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date cday = new Date();
        return sdf.format(cday);
    }

    public static String getTimeStrByLongTime(long time_misec) {
        StringBuffer bu = new StringBuffer(20);
        long leav = time_misec % 1000L;
        if (leav != 0L) {
            bu.insert(0, leav + "ms");
        }

        long child = time_misec / 1000L;
        if (child == 0L) {
            if (bu.length() == 0) {
                bu.append("0ms");
            }

            return bu.toString();
        } else {
            leav = child % 60L;
            if (leav != 0L) {
                bu.insert(0, leav + "s");
            }

            child /= 60L;
            if (child == 0L) {
                return bu.toString();
            } else {
                leav = child % 60L;
                if (leav != 0L) {
                    bu.insert(0, leav + "m");
                }

                child /= 60L;
                if (child == 0L) {
                    return bu.toString();
                } else {
                    leav = child % 24L;
                    if (leav != 0L) {
                        bu.insert(0, leav + "h");
                    }

                    child /= 24L;
                    if (child == 0L) {
                        return bu.toString();
                    } else {
                        leav = child % 30L;
                        if (leav != 0L) {
                            bu.insert(0, leav + "d");
                        }

                        child /= 30L;
                        if (child == 0L) {
                            return bu.toString();
                        } else {
                            leav = child % 12L;
                            if (leav != 0L) {
                                bu.insert(0, leav + "m");
                            }

                            child /= 12L;
                            if (child == 0L) {
                                return bu.toString();
                            } else {
                                bu.insert(0, child + "y");
                                return bu.toString();
                            }
                        }
                    }
                }
            }
        }
    }

    public static Date getNextMonthTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, 1);
        return c.getTime();
    }

    public static Date addMinuteTime(Date date, int mins) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(12, mins);
        return c.getTime();
    }

    public static Date addHourTime(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(11, hours);
        return c.getTime();
    }

    public static Date addDayTime(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(6, days);
        return c.getTime();
    }

    public static Date longToDate(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time * 1000L);
        return c.getTime();
    }

    public static String formatElapsedTime(long time) {
        String str = "";
        long time_misec = (long) getNowIntTime() - time;
        long leav = time_misec % 1000L;
        if (leav != 0L) {
            str = leav + "毫秒";
        }

        long child = time_misec / 1000L;
        if (child == 0L) {
            return str;
        } else {
            leav = child % 60L;
            if (leav != 0L) {
                str = leav + "秒";
            }

            child /= 60L;
            if (child == 0L) {
                return str;
            } else {
                leav = child % 60L;
                if (leav != 0L) {
                    str = leav + "分钟";
                }

                child /= 60L;
                if (child == 0L) {
                    return str;
                } else {
                    leav = child % 24L;
                    if (leav != 0L) {
                        str = leav + "小时";
                    }

                    child /= 24L;
                    if (child == 0L) {
                        return str;
                    } else {
                        leav = child % 30L;
                        if (leav != 0L) {
                            str = leav + "天";
                        }

                        child /= 30L;
                        if (child == 0L) {
                            return str;
                        } else {
                            leav = child % 12L;
                            if (leav != 0L) {
                                str = leav + "月";
                            }

                            child /= 12L;
                            if (child == 0L) {
                                return str;
                            } else {
                                str = child + "年";
                                return str;
                            }
                        }
                    }
                }
            }
        }
    }

    public static int getHourTime(int time, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date((long) time * 1000L));
        c.set(11, hour);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getTodayFirstTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) (calendar.getTimeInMillis() / 1000L);
    }

    public static int getTodayEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int toDate(String dateString) {
        String[] part = dateString.split(" ");
        String year = part[5];
        String month = "01";

        for (int i = 0; i < wtb.length; ++i) {
            if (wtb[i].equals(part[1])) {
                int m = i + 1;
                if (m < 10) {
                    month = "0" + m;
                } else {
                    month = "" + m;
                }
                break;
            }
        }

        String day = part[2];
        String sfm = part[3];
        String date = year + "-" + month + "-" + day + " " + sfm;
        return fmtStrTime(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static int addMonthTime(int time, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date((long) time * 1000L));
        c.add(2, month);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static String getCurrentMonthStr() {
        return (new SimpleDateFormat("yyyyMM")).format(new Date());
    }

    public static String getCurrentMonthStr(int time) {
        return (new SimpleDateFormat("yyyyMM")).format(new Date((long) time * 1000L));
    }

    public static String getCurrentDayStr() {
        return (new SimpleDateFormat("yyyyMMdd")).format(new Date());
    }

    public static String getCurrentDayStr(int time) {
        return (new SimpleDateFormat("yyyyMMdd")).format(new Date((long) time * 1000L));
    }

    public static int getMonthBetween(int startTime, int endTime) {
        Calendar s = Calendar.getInstance();
        s.setTimeInMillis((long) startTime * 1000L);
        Calendar e = Calendar.getInstance();
        e.setTimeInMillis((long) endTime * 1000L);
        int month = e.get(2) - s.get(2);
        int yearMonth = 12 * (e.get(1) - s.get(1));
        return yearMonth + month;
    }

    public static int getWeekStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day_of_week = cal.get(7) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }

        cal.add(5, -day_of_week + 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static int getWeekEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day_of_week = cal.get(7) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }

        cal.add(5, -day_of_week + 7);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static boolean isMonthSupported(String month, int monthCount) {
        int targetmonth = Integer.parseInt(month);
        int thismonth = Integer.parseInt(fmtLongTime((long) getNowIntTime(), "yyyyMM"));
        int xmonthago = Integer.parseInt(fmtLongTime((long) getXMonthAgo(new Date(), monthCount), "yyyyMM"));
        return targetmonth >= xmonthago && targetmonth <= thismonth;
    }

    public static int getYearZeroTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(2, 0);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNextYearZeroTime(Date date, int addYear) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(1, c.get(1) + addYear);
        c.set(2, 0);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getLastYearZeroTime(Date date, int reduceYear) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(1, c.get(1) - reduceYear);
        c.set(2, 0);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getNextMonthZeroTime(Date date, int addMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(2, c.get(2) + addMonth);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }

    public static int getLastMonthZeroTime(Date date, int reduceMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(2, c.get(2) - reduceMonth);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return (int) (c.getTime().getTime() / 1000L);
    }
}
