package com.smeshariks.pms.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampConverter {

    public static String convert(Timestamp timestamp, boolean withTime) {
        if (withTime)
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(timestamp);
        else
            return new SimpleDateFormat("dd.MM.yyyy").format(timestamp);
    }
}
