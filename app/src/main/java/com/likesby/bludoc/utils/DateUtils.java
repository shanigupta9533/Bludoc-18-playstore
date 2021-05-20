package com.likesby.bludoc.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    /**
     * Get a diff between two dates
     *
     * @param oldDate the old date
     * @param newDate the new date
     * @return the diff value, in the days
     */
    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // get current date time with Date()
        Date date = new Date();
        // System.out.println(dateFormat.format(date));
        // don't print it, but save it!
        return dateFormat.format(date);
    }
    public static String currentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        // get current date time with Date()
        Date date = new Date();
        // System.out.println(dateFormat.format(date));
        // don't print it, but save it!
        return dateFormat.format(date);
    }

    //1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400
    public static String  printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        String days_ = "Days",hrs_ = "Hrs",mins_="Mins";
        if(elapsedDays==1)
            days_ = "Day";

        if(elapsedHours==1)
            hrs_ = "Hr";

        if(elapsedMinutes==1)
            mins_ = "Min";

        if(elapsedDays!=0 && elapsedHours==0)
            return elapsedDays+" "+days_;//+", "+elapsedMinutes+" "+mins_;
        else if(elapsedDays==0)
            return elapsedHours+" "+hrs_;//+", "+elapsedMinutes+" "+mins_;
        else
        return elapsedDays+" "+days_+" "+elapsedHours+" "+hrs_;//+", "+elapsedMinutes+" "+mins_;
    }

    public static String  formatDateToMMDDYY(String startDate) {
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String outputDateStr = outputFormat.format(startDate);
        return outputDateStr;
    }

    public static String outFormatset (String datestring2) {
        String format = "dd MM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String ret = sdf.format(new Date(datestring2.replaceAll("-", "/")));
        return ret.replaceAll(" ","/");
    }

    public static String outFormatsetMMM (String datestring2) {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String ret = sdf.format(new Date(datestring2.replaceAll("-", "/")));
        return ret.replaceAll(" ","-");
    }

    public static String outFormatsetWithTime (String datestring2) {
        Log.e("outFormats=====", "datestring2 = " + datestring2);
        String date___ = datestring2.substring(0,10);
        String time___ = datestring2.substring(11,16);
        Log.e("outFormats=====", "time___ = " + time___);
        String AM_PM=null;
        int hr___ = Integer.parseInt(time___.substring(0,2));
        int min___ = Integer.parseInt(time___.substring(3,5));
        Log.e("outFormats=====", "hr___ = " + hr___);
        Log.e("outFormats=====", "min___ = " + min___);
        if (hr___ >=0 && hr___ < 12){
            AM_PM = "AM";
        } else {


            AM_PM = "PM";
        }

        if(hr___>12)
        {
            hr___ = hr___-12;
        }


        String format = "dd MM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String ret = sdf.format(new Date(date___.replaceAll("-", "/")));
        String final_date = ret.replaceAll(" ","/");

        if(min___<10)
        return final_date+" "+hr___+":0"+min___+" "+AM_PM;
        else
            return final_date+" "+hr___+":"+min___+" "+AM_PM;
    }

    public static String outFormatsetTIMEonly (String datestring2) {
        String time___ = datestring2.substring(0,5);
        Log.e("outFormats=====", "time___ = " + time___);
        String AM_PM=null;
        int hr___ = Integer.parseInt(time___.substring(0,2));
        int min___ = Integer.parseInt(time___.substring(3,5));
        Log.e("outFormats=====", "hr___ = " + hr___);
        Log.e("outFormats=====", "min___ = " + min___);
        if (hr___ >=0 && hr___ < 12){
            AM_PM = "AM";
        } else {


            AM_PM = "PM";
        }

        if(hr___>12)
        {
            hr___ = hr___-12;
        }



        if(min___<10)
            return hr___+":0"+min___+" "+AM_PM;
        else
            return hr___+":"+min___+" "+AM_PM;
    }
}
