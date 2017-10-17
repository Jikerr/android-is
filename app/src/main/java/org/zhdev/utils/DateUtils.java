package org.zhdev.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public final static String BASELONGDATE_FORMART = "yyyy-MM-dd hh:mm:ss";

    public static String getNowDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(BASELONGDATE_FORMART);
        Date date = new Date();
        return sdf.format(date);
    }
    public static Long getNowDateStamp(){
        return new Date().getTime();
    }
}
