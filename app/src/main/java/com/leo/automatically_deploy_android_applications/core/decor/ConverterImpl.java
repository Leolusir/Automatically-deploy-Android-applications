package com.leo.automatically_deploy_android_applications.core.decor;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.GravityKey;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.OrientationKey;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.ScaleTypekey;
import com.leo.automatically_deploy_android_applications.keywords.widgetkeys.ImageViewParamsKey;
import com.leo.automatically_deploy_android_applications.keywords.widgetkeys.LinearLayoutParamskey;
import com.leo.automatically_deploy_android_applications.keywords.widgetkeys.ScrollViewParamsKey;
import com.leo.automatically_deploy_android_applications.keywords.widgetkeys.TextViewParamsKey;
import com.leo.automatically_deploy_android_applications.widgets.AButton;
import com.leo.automatically_deploy_android_applications.widgets.ACardView;
import com.leo.automatically_deploy_android_applications.widgets.AImageView;
import com.leo.automatically_deploy_android_applications.widgets.ALinearLayout;
import com.leo.automatically_deploy_android_applications.widgets.ARecyclerView;
import com.leo.automatically_deploy_android_applications.widgets.ARelativeLayout;
import com.leo.automatically_deploy_android_applications.widgets.AScrollView;
import com.leo.automatically_deploy_android_applications.widgets.ATextView;
import com.leo.automatically_deploy_android_applications.widgets.AViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by littleming on 15/7/2.
 */
public class ConverterImpl extends Converter{
    private Map<String, Integer> linearLayoutParamsMap = new HashMap<>();
    private Map<String, Integer> textViewParamsMap = new HashMap<>();
    private Map<String, Integer> imageViewParamsMap = new HashMap<>();
    private Map<String, Integer> scrollViewParamsMap = new HashMap<>();

    void init(){
        linearLayoutParamsMap.put(LinearLayoutParamskey.BACKGROUND_COLOR_DES, LinearLayoutParamskey.BACKGROUND_COLOR);
        linearLayoutParamsMap.put(LinearLayoutParamskey.ORIENTATION_DES, LinearLayoutParamskey.ORIENTATION);

        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_DES, TextViewParamsKey.TEXTVIEW_TEXT);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_SIZE_DES, TextViewParamsKey.TEXTVIEW_TEXT_SIZE);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_COLOR_DES, TextViewParamsKey.TEXTVIEW_TEXT_COLOR);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_GRAVITY_DES, TextViewParamsKey.TEXTVIEW_GRAVITY);

        imageViewParamsMap.put(ImageViewParamsKey.IMAGEVIEW_SRC_TAG, ImageViewParamsKey.IMAGEVIEW_SRC);
        imageViewParamsMap.put(ImageViewParamsKey.IMAGEVIEW_SCALETYPE_TAG, ImageViewParamsKey.IMAGEVIEW_SCALETYPE);

        scrollViewParamsMap.put(ScrollViewParamsKey.ORIENTATION_DES, ScrollViewParamsKey.ORIENTATION);
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

        ALog.i("convert", v.getClass().getSimpleName() + "");

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
    View toScrollView(View view, XmlPullParser parser) {
        AScrollView scrollView = (AScrollView)view;
        if(parser != null)
            for(int i = 0;i < parser.getAttributeCount();i ++){
                renderScrollView(scrollView, scrollViewParamsMap.get(parser.getAttributeName(i))
                                , parser.getAttributeValue(i));
            }

        return null;
    }

    void renderScrollView(AScrollView scrollView, int arg, String attrsValue){
        if(scrollView != null && !TextUtils.isEmpty(attrsValue)){
            switch (arg) {

            }
        }
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
                    if(OrientationKey.ORIENTATION_VERTICAL.equals(attrsValue)){
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

                case TextViewParamsKey.TEXTVIEW_GRAVITY:
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                                                , ViewGroup.LayoutParams.WRAP_CONTENT);
                    if(GravityKey.GRAVITY_CENTER.equals(attrsValue)){
                        lp.gravity = Gravity.CENTER;
                    }
                    textView.setLayoutParams(lp);
                    break;
            }
        }
    }

    @Override
    View toImageView(View view, XmlPullParser parser) {
        AImageView imageView = (AImageView)view;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        if(parser != null){
            for(int i = 0;i < parser.getAttributeCount() ; i ++){
                renderImageView(imageView, imageViewParamsMap.get(parser.getAttributeName(i)),
                        parser.getAttributeValue(i));
            }
        }

        return imageView;
    }

    void renderImageView(AImageView imageView, int arg, String attrsValue){
        if(imageView != null && !TextUtils.isEmpty(attrsValue)){
            switch (arg) {
                case ImageViewParamsKey.IMAGEVIEW_SRC:
                    ImageLoader.getInstance().displayImage(attrsValue, imageView);
                    break;

                case ImageViewParamsKey.IMAGEVIEW_SCALETYPE:
                    if(ScaleTypekey.SCALETYPE_FITXY.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    break;
            }
        }
    }

    @Override
    View toImageButton(View view, XmlPullParser parser) {
        return view;
    }
}
