package com.leo.automatically_deploy_android_applications.core.decor;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.keywords.WidgetKeys;
import com.leo.automatically_deploy_android_applications.widgets.AScrollView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
    static final Class<?>[] mConstructorSignature = new Class[] {Context.class, AttributeSet.class};
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();

    public void init(){
        converter = new ConverterImpl();
        converter.init();
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
    }

    public View generateView(Context context, int resId){
        this.context = context;
        View view = null;

        XmlResourceParser xmlParser = context.getResources().getLayout(resId);
        AttributeSet attrs = Xml.asAttributeSet(xmlParser);

        try {
            view = myParser(xmlParser, attrs, context);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public View myParser(XmlPullParser parser,final AttributeSet attrs, Context context) throws XmlPullParserException,
            IOException {

        final int depth = parser.getDepth();
        int type;

        //now just support linearlayout
        ViewGroup rootView = null;
        boolean needScroll = false;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String des  = parser.getName();
            final String name = parserMap.get(parser.getName());
            ALog.i("item", name + "");

            Constructor<? extends View> constructor = sConstructorMap.get(name);
            Class<? extends View> clazz = null;
            final Object[] mConstructorArgs = new Object[2];
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            try {
                if("scroll".equals(des)){
                    needScroll = true;
                }else {
                    clazz = context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
                    constructor = clazz.getConstructor(mConstructorSignature);
                    sConstructorMap.put(name, constructor);
                    constructor.setAccessible(true);
                    View view = constructor.newInstance(mConstructorArgs);
                    ALog.i("view", view.getClass().getSimpleName() + "");
                    if (des.contains("root")) {
                        rootView = (ViewGroup) converter.converterView(view, parser);
                        ALog.i("add", "add_root_view");
                    } else {
                        View childView = converter.converterView(view, parser);
                        if (rootView != null) {
                            ALog.i("add", "add_child_view");
                            rootView.addView(childView);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(needScroll){
            AScrollView scrollView = new AScrollView(context);
            scrollView.addView(rootView);
            return scrollView;
        }

        return rootView;
    }


}
