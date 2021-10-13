package com.maxi.mvvm.utils;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 *     author : li
 *     e-mail : xxx@xx
 *     time   : 2020/03/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TimeTools {

    /*
     * 将时间转换为时间戳
     * yyyy-MM-dd HH:mm:ss
     */
    public static String timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return String.valueOf(timeStemp);
    }

    /*
     * 将时间转换为时间戳
     * yyyy-MM-dd HH:mm
     */
    public static String timeToStampX(long trackTime) {
        String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  yyyy-MM-dd
     */
    public static String formateTrackTime(long trackTime) {
        String result = new SimpleDateFormat("yyyy-MM-dd").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  yyyy-MM
     */
    public static String formatTrackTimeYM(long trackTime) {
        String result = new SimpleDateFormat("yyyy-MM").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  yyyy/MM/dd
     */
    public static String formateTrackTime2(long trackTime) {
        String result = new SimpleDateFormat("yyyy/MM/dd").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  1546071846000
     *                  yyyy年MM月dd日
     */
    public static String formateTrackTimeYear(long trackTime) {
        String result = new SimpleDateFormat("yyyy/MM/dd").format(trackTime);
        return result;
    }


    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  1546071846000
     *                  dd日
     */
    public static String formateTrackTimeDate(long trackTime) {
        String result = new SimpleDateFormat("dd日").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  1546071846000
     *                  dd日 HH:mm:ss
     */
    public static String formateTrackTimeSs(long trackTime) {
        String result = new SimpleDateFormat("dd日 HH:mm:ss").format(trackTime);
        return result;
    }

    /**
     * 13位的豪秒级别的时间戳
     *
     * @param trackTime =  1546071846000
     * @param formart   = yyyy年MM月dd日 HH:mm:ss
     */
    public static String formateTrackTime(long trackTime, String formart) {
        String result = new SimpleDateFormat(formart).format(trackTime);
        return result;
    }

    /**
     * 分钟转天 时 分
     *
     * @param trackTime 分钟数
     * @return
     */
    public static String forMinTime(int trackTime) {

        int day = trackTime / (24 * 60);
        int hour = trackTime / 60 - day * 24;
        int min = trackTime - day * 24 * 60 - hour * 60;

        return day + "天" + hour + "小时" + min + "分";
    }


    /**
     * 、时间戳转天 时 分 秒
     *
     * @param ms 时间戳
     * @return
     */
    public static String secondToHMTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        System.out.println(strDay);

        if (day < 0) {
            strDay = "00";
        }

        if (hour < 0) {
            strHour = "00";
        }

        if (minute < 0) {
            strMinute = "00";
        }

        if (second < 0) {
            strSecond = "00";
        }
        return strDay + "天：" + strHour + "时：" + strMinute + "分：" + strSecond + "秒";
    }


    /**
     * 返回文字描述的日期
     *
     * @param times
     * @return
     */
    public static String getTimeFormatText(long times) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年

        Date date = new Date(times);
        if (date == null) {
            return null;
        }


        long diff = new Date().getTime() - date.getTime();
        long r = 0;

        if (date.getYear() != new Date().getYear()) {
            return formateTrackTime(date.getTime(), "yyyy-MM-dd");
        }

        if (diff > day) {
                return formateTrackTime(date.getTime(), "MM-dd");
        }


        if (diff > hour) {
            if (date.getDay() != new Date().getDay()) {
                return formateTrackTime(date.getTime(), "昨天 HH：mm");
            }else {
                return formateTrackTime(date.getTime(), "HH：mm");
            }
        }

        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTransTime(String timeStr) {


        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(timeStr, df);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String transTime = f2.format(date);

        return transTime;
    }
    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //不同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //同一年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
