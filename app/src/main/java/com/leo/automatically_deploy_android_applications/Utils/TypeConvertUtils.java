package com.leo.automatically_deploy_android_applications.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by littleming on 15/7/9.
 */
public class TypeConvertUtils {
    final static int BUFFER_SIZE = 4096;

    public static InputStream StringTOInputStream(String in) throws Exception{

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
        return is;
    }

    public static String InputStreamTOString(InputStream in,String encoding) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"ISO-8859-1");
    }

}
