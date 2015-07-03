package com.leo.automatically_deploy_android_applications.core.decor;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.leo.automatically_deploy_android_applications.keywords.LinearLayoutParamskey;
import com.leo.automatically_deploy_android_applications.keywords.TextViewParamsKey;
import com.leo.automatically_deploy_android_applications.widgets.AButton;
import com.leo.automatically_deploy_android_applications.widgets.ACardView;
import com.leo.automatically_deploy_android_applications.widgets.AImageView;
import com.leo.automatically_deploy_android_applications.widgets.ALinearLayout;
import com.leo.automatically_deploy_android_applications.widgets.ARecyclerView;
import com.leo.automatically_deploy_android_applications.widgets.ARelativeLayout;
import com.leo.automatically_deploy_android_applications.widgets.ATextView;
import com.leo.automatically_deploy_android_applications.widgets.AViewPager;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by littleming on 15/7/2.
 */
public class ConverterImpl extends Converter{
    private Map<String, Integer> linearLayoutParamsMap = new HashMap<>();
    private Map<String, Integer> textViewParamsMap = new HashMap<>();

    void init(){
        linearLayoutParamsMap.put(LinearLayoutParamskey.BACKGROUND_COLOR_DES, LinearLayoutParamskey.BACKGROUND_COLOR);
        linearLayoutParamsMap.put(LinearLayoutParamskey.ORIENTATION_DES, LinearLayoutParamskey.ORIENTATION);

        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_DES, TextViewParamsKey.TEXTVIEW_TEXT);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_SIZE_DES, TextViewParamsKey.TEXTVIEW_TEXT_SIZE);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_COLOR_DES, TextViewParamsKey.TEXTVIEW_TEXT_COLOR);
    }

    View converterView(View view, XmlPullParser parser){
        View v = null;
        if(view instanceof ARecyclerView){
            v = toList(view, parser);
        }else if(view instanceof ACardView){
            v = toCardView(view, parser);
        }else if(view instanceof AViewPager){
            v = toViewPager(view, parser);
        }else if(view instanceof ALinearLayout){
            v = toLinearLayout(view, parser);
        }else if(view instanceof ARelativeLayout){
            v = toRelativeLayout(view, parser);
        }else{
            v = toWidget(view, parser);
        }

        Log.i("convert", v.getClass().getSimpleName() + "");

        return v;
    }

    @Override
    View toList(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toCardView(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toViewPager(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toLayout(View view, XmlPullParser parser) {
        View v = null;
        if(view instanceof ALinearLayout){
            v = toLinearLayout(view, parser);
        }else if(view instanceof ARelativeLayout){
            v = toRelativeLayout(view, parser);
        }

        return v;
    }

    @Override
    View toLinearLayout(View view, XmlPullParser parser) {
        ALinearLayout linearLayout = (ALinearLayout)view;
        if(parser != null){
            for(int i = 0;i < parser.getAttributeCount();i ++){
                renderLinearLayout(linearLayout, linearLayoutParamsMap.get(parser.getAttributeName(i)),
                        parser.getAttributeValue(i));
            }
        }

        return linearLayout;
    }

    void renderLinearLayout(ALinearLayout linearLayout, int arg, String attrsValue){
        if(linearLayout != null && !TextUtils.isEmpty(attrsValue)){
            switch (arg) {
                case LinearLayoutParamskey.BACKGROUND_COLOR:
                    linearLayout.setBackgroundColor(Color.parseColor(attrsValue));
                    break;

                case LinearLayoutParamskey.ORIENTATION:
                    if("0".equals(attrsValue)){
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                    }else{
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    }
            }
        }
    }

    @Override
    View toRelativeLayout(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toWidget(View view, XmlPullParser parser) {
        View v = null;
        if(view instanceof ATextView){
            v = toTextView(view, parser);
        }else if(view instanceof AImageView){
            v = toImageView(view, parser);
        }else if(view instanceof AButton){
            v = toButton(view, parser);
        }

        return v;
    }

    @Override
    View toButton(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toTextView(View view, XmlPullParser parser) {
        ATextView textView = (ATextView)view;
        if(parser != null){
            for(int i = 0;i < parser.getAttributeCount();i ++){
                renderTextView(textView, textViewParamsMap.get(parser.getAttributeName(i)),
                        parser.getAttributeValue(i));
            }
        }

        return textView;
    }

    void renderTextView(ATextView textView, int arg, String attrsValue){
        if(textView != null && !TextUtils.isEmpty(attrsValue)){
            switch (arg) {
                case TextViewParamsKey.TEXTVIEW_TEXT:
                    textView.setText(attrsValue);
                    break;

                case TextViewParamsKey.TEXTVIEW_TEXT_SIZE:
                    textView.setTextSize(Float.parseFloat(attrsValue));
                    break;

                case TextViewParamsKey.TEXTVIEW_TEXT_COLOR:
                    textView.setTextColor(Color.parseColor(attrsValue));
                    break;
            }
        }
    }

    @Override
    View toImageView(View view, XmlPullParser parser) {
        return view;
    }

    @Override
    View toImageButton(View view, XmlPullParser parser) {
        return view;
    }
}
