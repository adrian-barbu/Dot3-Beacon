package com.dot3digital.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description     Time Util
 */
public class D3TimeUtil {
    /**
     * Get Time Stamp in Miliseconds from 01.01.1970
     * @return
     */
    public static long getTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
