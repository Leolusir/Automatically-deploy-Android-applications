package com.leo.automatically_deploy_android_applications.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by littleming on 15/7/9.
 */
public class StringUtils {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
