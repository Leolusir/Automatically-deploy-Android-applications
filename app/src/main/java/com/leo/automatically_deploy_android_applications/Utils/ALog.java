package com.leo.automatically_deploy_android_applications.Utils;

import android.util.Log;

/**
 * Created by littleming on 15/7/3.
 */
public class ALog {
    private static final boolean canLog = true;

    public static void i(String arg1, String arg2){
        if(canLog)
            Log.i(arg1, arg2);
    }

}
