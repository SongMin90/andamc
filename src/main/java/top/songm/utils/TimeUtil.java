package top.songm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author songm
 * @datetime 2019/2/13 18:48
 */
public class TimeUtil {

    /**
     * 根据格式fmr格式化日期为字符串
     * @param date
     * @param fmt
     * @return
     */
    public static String formatTime(Date date, String fmt) {
        return new SimpleDateFormat(fmt).format(date);
    }
}
