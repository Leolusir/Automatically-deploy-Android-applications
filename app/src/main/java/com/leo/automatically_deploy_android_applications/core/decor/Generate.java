package com.leo.automatically_deploy_android_applications.core.decor;

import android.content.Context;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.keywords.RootLayoutKeys;
import com.leo.automatically_deploy_android_applications.keywords.WidgetKeys;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by littleming on 15/7/2.
 */
public class Generate {
    private Context context;
    private ConverterImpl converter;

    private final String prefix = "com.leo.automatically_deploy_android_applications.widgets.";
    private Map<String, String> parserMap = new HashMap<>();
    static final Class<?>[] ConstructorSignature = new Class[] {Context.class};
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    final static int BUFFER_SIZE = 4096;

    private boolean canScroll = false;

    public void init(){
        converter = new ConverterImpl();
        converter.init(context);
        parserMap.put(WidgetKeys.BUTTON_DES_TAG, WidgetKeys.BUTTON_ANALYZE_TAG);
        parserMap.put(WidgetKeys.TEXTVIEW_DES_TAG, WidgetKeys.TEXTVIEW_ANALYZE_TAG);
        parserMap.put(WidgetKeys.IMAGE_DES_TAG, WidgetKeys.IMAGE_ANALYZE_TAG);
        parserMap.put(WidgetKeys.LINEARLAYOUT_DES_TAG, WidgetKeys.LINEARLAYOUT_ANALYZE_TAG);
        parserMap.put(WidgetKeys.ROOT_LINEARLAYOUT_DES_TAG, WidgetKeys.LINEARLAYOUT_ANALYZE_TAG);
        parserMap.put(WidgetKeys.RELATIVELAYOUT_DES_TAG, WidgetKeys.RELATIVELAYOUT_ANALYZE_TAG);
        parserMap.put(WidgetKeys.LIST_DES_TAG, WidgetKeys.LIST_ANALYZE_TAG);
        parserMap.put(WidgetKeys.BANNER_DES_TAG, WidgetKeys.VIEWPAGER_ANALYZE_TAG);
        parserMap.put(WidgetKeys.VIEWPAGER_DES_TAG, WidgetKeys.VIEWPAGER_ANALYZE_TAG);
        parserMap.put(WidgetKeys.SCROLL_VIEW_DES_TAG, WidgetKeys.SCROLL_VIEW_ANALYZE_TAG);

        parserMap.put(RootLayoutKeys.LAYOUT_LINEAR_DES, RootLayoutKeys.LAYOUT_LINEAR);
    }

    public View generateView(Context context){
        this.context = context;
        View view = null;

        try {
            view = Parser(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public View Parser(Context context) throws IOException {
        InputStream is = context.getAssets().open("desfile");
        String xmlString = null;
        try {
            xmlString = InputStreamTOString(is, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALog.i("xml", xmlString + "");
        View view = generateLayout(xmlString);

        if(canScroll){
            ScrollView scrollView = new ScrollView(context);
            scrollView.addView(view);
            return scrollView;
        }

        return view;
    }

    public View generateLayout(String content) {
        content = content.replaceAll(" ", "");
        String[] items = content.split("<Item>");

        //generate root view
        ViewGroup rootView = null;
        try {
            rootView = (ViewGroup)generateRootView(items[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(rootView == null){
            return null;
        }

        //generate widgets
        for (int i = 1 ; i < items.length ; i ++ ) {
            View view = null;
            if (i == items.length - 1){
                String des = items[i].toString();
                String[] xdes = des.split("</Item>");
                des = "<Item> \n" + xdes[0] + "</Item>";
                view = generateItemView(des);
            } else {
                view = generateItemView(items[i]);
            }

            if (view != null)
                rootView.addView(view);
        }

        return rootView;
    }

    public View generateRootView(String des) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(StringTOInputStream(des), "UTF-8");

        View v = null;
        final int depth = parser.getDepth();
        int type;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String desName = parser.getName();
            if(desName.contains("linear"))
                canScroll = true;
            ALog.i("name", desName + "");
            final String name = parserMap.get(parser.getName());

            Constructor<? extends View> constructor = sConstructorMap.get(name);
            Class<? extends View> clazz = null;
            final Object[] mConstructorArgs = new Object[1];
            mConstructorArgs[0] = context;

            clazz = context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
            constructor = clazz.getConstructor(ConstructorSignature);
            sConstructorMap.put(name, constructor);
            constructor.setAccessible(true);
            View view = constructor.newInstance(mConstructorArgs);
            ALog.i("view", view.getClass().getSimpleName() + "");

            v = converter.converterView(view, parser);
            ALog.i("add", "add_root_view");
        }

        return v;
    }

    public View generateItemView(String des) {
        des = "<Item> \n" + des;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(StringTOInputStream(des), "UTF-8");
        } catch (Exception e) {
            return null;
        }

        View v = null;
        final int depth = parser.getDepth();
        int type;

        try {
            while (((type = parser.next()) != XmlPullParser.END_TAG ||
                    parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
                if (type != XmlPullParser.START_TAG) {
                    continue;
                }

                final String desName = parser.getName();
                if("Item".equals(desName)){
                    ALog.i("name", "-------------");
                    ALog.i("name", desName + "");
                }else{
                    ALog.i("name", desName + "");
                    final String name = parserMap.get(parser.getName());

                    Constructor<? extends View> constructor = sConstructorMap.get(name);
                    Class<? extends View> clazz = null;
                    final Object[] mConstructorArgs = new Object[1];
                    mConstructorArgs[0] = context;

                    clazz = context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
                    constructor = clazz.getConstructor(ConstructorSignature);
                    sConstructorMap.put(name, constructor);
                    constructor.setAccessible(true);
                    View view = constructor.newInstance(mConstructorArgs);
                    ALog.i("view", view.getClass().getSimpleName() + "");

                    v = converter.converterView(view, parser);
                    ALog.i("add", "add_item");
                }

            }
        } catch (XmlPullParserException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (IOException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (ClassNotFoundException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (NoSuchMethodException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (InvocationTargetException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (InstantiationException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (IllegalAccessException e) {
            ALog.e("AException", e.toString() + "");
            return null;
        } catch (Exception e) {
            ALog.e("AException", e.toString() + "");
            return null;
        }

        return v;
    }

    public InputStream StringTOInputStream(String in) throws Exception{

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
        return is;
    }

    public String InputStreamTOString(InputStream in,String encoding) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"ISO-8859-1");
    }

}
