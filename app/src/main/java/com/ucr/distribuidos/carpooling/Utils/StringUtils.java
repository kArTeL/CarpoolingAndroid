package com.ucr.distribuidos.carpooling.Utils;

import android.text.TextUtils;

/**
 * Created by Neil on 11/23/16.
 */

public class StringUtils {
    /**Valid value*/
    public static boolean isEmpty(String value)
    {
        Boolean result = false;
        if(TextUtils.isEmpty(value)){
            result = true;
        }
        else{
            result = stringByTrimmingWhiteSpaces(value).isEmpty();
        }
        return result;
    }

    /**Trim whitespace*/
    public static String stringByTrimmingWhiteSpaces (String value)
    {
        value = value.trim();
        return value;
    }
}
