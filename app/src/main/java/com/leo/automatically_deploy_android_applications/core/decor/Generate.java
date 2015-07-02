package com.leo.automatically_deploy_android_applications.core.decor;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Map<String, String> parserMap = new HashMap<>();
    static final Class<?>[] mConstructorSignature = new Class[] {Context.class, AttributeSet.class};
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();

    public void init(){
        parserMap.put(WidgetKeys.BUTTON_DES_TAG, WidgetKeys.BUTTON_ANALYZE_TAG);
        parserMap.put(WidgetKeys.TEXTVIEW_DES_TAG, WidgetKeys.TEXTVIEW_ANALYZE_TAG);
        parserMap.put(WidgetKeys.LINEARLAYOUT_DES_TAG, WidgetKeys.LINEARLAYOUT_ANALYZE_TAG);
        parserMap.put(WidgetKeys.RELATIVELAYOUT_DES_TAG, WidgetKeys.RELATIVELAYOUT_ANALYZE_TAG);
        parserMap.put(WidgetKeys.LIST_DES_TAG, WidgetKeys.LIST_ANALYZE_TAG);
        parserMap.put(WidgetKeys.BANNER_DES_TAG, WidgetKeys.VIEWPAGER_ANALYZE_TAG);
        parserMap.put(WidgetKeys.VIEWPAGER_DES_TAG, WidgetKeys.VIEWPAGER_ANALYZE_TAG);
    }

    public View generateView(Context context, int resId){
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
        LinearLayout rootView = null;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parserMap.get(parser.getName());
            Log.i("item", name + "");

            Constructor<? extends View> constructor = sConstructorMap.get(name);
            Class<? extends View> clazz = null;
            final Object[] mConstructorArgs = new Object[2];
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;
            String prefix = "com.leo.automatically_deploy_android_applications.widgets.";
            try {
                clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
                constructor.setAccessible(true);
                View view = constructor.newInstance(mConstructorArgs);
                Log.i("view", view.getClass().getSimpleName() + "");
                if(name.contains("Layout")){
                    rootView = (LinearLayout)view;
                    rootView.setOrientation(LinearLayout.VERTICAL);
                    rootView.setBackgroundColor(Color.parseColor(parser.getAttributeValue(0)));
                    Log.i("add", "add");
                }else{
                    TextView tview = (TextView)view;
                    tview.setText(parser.getAttributeValue(0));
                    tview.setTextColor(Color.WHITE);

                    if(rootView != null){
                        Log.i("add", "add");
                        rootView.addView(tview);
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
        return rootView;
    }


}
