package com.leo.automatically_deploy_android_applications.core.decor;

import android.content.Context;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.Utils.TypeConvertUtils;
import com.leo.automatically_deploy_android_applications.keywords.RootLayoutKeys;
import com.leo.automatically_deploy_android_applications.keywords.WidgetKeys;

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
    static final Class<?>[] ConstructorSignature = new Class[] {Context.class};
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    private boolean canScroll = false;

    public void init(Context context){
        this.context = context;
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

    public View generateView(String xmlString){
        View view = null;

        try {
            view = generateLayout(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public View generateLayout(String content) throws Exception{

        ScrollView scrollView = new ScrollView(context);
        generateItemView(scrollView, content);

        return scrollView;
    }

    public View generateRootView(String des) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(TypeConvertUtils.StringTOInputStream(des), "UTF-8");

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

            break;
        }

        return v;
    }

    void generateItemView(View parent, String des) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(TypeConvertUtils.StringTOInputStream(des), "UTF-8");
        } catch (Exception e) {
            //empty
        }

        try {
            rInflate(parser, parent);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void rInflate(XmlPullParser parser, View parent)
            throws XmlPullParserException, IOException {
        final int depth = parser.getDepth();
        int type;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();
            final View view = generateViewFromTag(parser, parent, parserMap.get(name));
            final ViewGroup viewGroup = (ViewGroup) parent;
            rInflate(parser, view);
            viewGroup.addView(view);
        }
    }

    private View generateViewFromTag(XmlPullParser parser, View parent, String name){
        View v = null;
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;
        final Object[] mConstructorArgs = new Object[1];
        mConstructorArgs[0] = context;

        try {
            clazz = context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
            constructor = clazz.getConstructor(ConstructorSignature);
            sConstructorMap.put(name, constructor);
            constructor.setAccessible(true);
            View view = constructor.newInstance(mConstructorArgs);
            ALog.i("view", view.getClass().getSimpleName() + "");

            v = converter.converterView(view, parser);
            ALog.i("add", "add_item");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
