package com.paic.dataplatform.es.Util;

import java.util.Date;

/**
 * Created by czx on 4/1/19.
 */
public class DateUtils {

    private static String DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss.SSS";


    /**
     * 格式化日期
     *
     * @param dateStr 字符型日期
     * @return 返回日期
     */
    public static Date parseDate(String dateStr)
    {
        java.util.Date date = null;
        try
        {
            java.text.DateFormat df = new java.text.SimpleDateFormat(DATETIME_FORMATE);
            date = (java.util.Date)df.parse(dateStr);
        }
        catch(java.text.ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 格式化输出日期
     *
     * @param date 日期
     * @return 返回字符型日期
     */
    public static String formatDate(java.util.Date date) {
        String result = "";
        try {
            if (date != null) {
                java.text.DateFormat df = new java.text.SimpleDateFormat(DATETIME_FORMATE);
                result = df.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public static String  getCurrentDateTime(){

       return formatDate(new Date());
    }


}
