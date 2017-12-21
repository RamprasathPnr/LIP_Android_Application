package com.omneagate.lip.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by USER1 on 13-06-2016.
 */
public class Helper {


    /**
     * This Method Will Convert The Date Format As Per Flag value
     **/
    public static String appDateFormat(String dateStr, int format) {

        String dateTime = null;
        java.util.Date date;

        if (StringNullCheck(dateStr) && !dateStr.contains("yyyy-mm-dd")) {

            SimpleDateFormat dateFormat = null;
            switch (format) {
                case 1:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
                case 2:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    break;
            }

            try {
                date = dateFormat.parse(dateStr);

                switch (format) {
                    case 1:
                        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        break;
                    case 2:
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        break;
                }


                dateTime = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateTime;
    }

    /**
     * Return True If The String Is Not Empty OtherWise False
     * **/
    public static boolean StringNullCheck(String nullCheckString) {

        if (nullCheckString != null) {
            if (nullCheckString.length() > 0 && !nullCheckString.equals("")
                    && !nullCheckString.equals("null")
                    && !nullCheckString.equals("(null)")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
