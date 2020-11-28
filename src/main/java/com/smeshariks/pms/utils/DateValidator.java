package com.smeshariks.pms.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements Validator{

    private String dateFormat = "dd.MM.yyyy";
    private String timestampStart;
    private String timestampDead;

    private Date dateStart;
    private Date dateDead;

    private Timestamp tsStart;
    private Timestamp tsDead;

    public DateValidator() {}
    public DateValidator(String timestampStart, String timestampDead){
        this.timestampStart = timestampStart;
        this.timestampDead = timestampDead;
    }

    public DateValidator(Timestamp tsStart, Timestamp tsDead){
        this.tsStart = tsStart;
        this.tsDead = tsDead;
    }

    public boolean isValidSingle(String timestamp) {
        boolean res = true;

        DateFormat dateFormat = new SimpleDateFormat(this.dateFormat);

        try {
            Date checkValue = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            res = false;
        }

        return res;
    }

    public Timestamp convertSingle(String timestamp) {
        Timestamp timestamp1 = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
            Date date = dateFormat.parse(timestamp);
            timestamp1 = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp1;
    }

    public boolean isValid() {

        boolean result = true;

        DateFormat dateFormat = new SimpleDateFormat(this.dateFormat);


        try {
            dateStart = dateFormat.parse(timestampStart);
            dateDead = dateFormat.parse(timestampDead);
            if(dateDead.before(dateStart)) {
                result = false;
            }

        } catch (ParseException e) {
            return false;
        }

        /*
        if(tsDead.before(tsStart)) {
            result = false;
        }
        */

        return result;
    }

    public Timestamp[] getTimestamps() {

        Timestamp timestamps[] = new Timestamp[2];
        timestamps[0] = new Timestamp(dateStart.getTime());
        timestamps[1] = new Timestamp(dateDead.getTime());

        return timestamps;
    }

    public Timestamp formatTimestamp(String strTimestamp) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
            Date date = dateFormat.parse(strTimestamp);

            return new Timestamp(date.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
