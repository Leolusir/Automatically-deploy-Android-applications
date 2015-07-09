package com.leo.automatically_deploy_android_applications.core.decor;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leo.automatically_deploy_android_applications.Utils.ALog;
import com.leo.automatically_deploy_android_applications.Utils.StringUtils;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.GravityKey;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.OrientationKey;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.ScaleTypekey;
import com.leo.automatically_deploy_android_applications.keywords.paramsvaluekeys.WHKey;
import com.leo.automatically_deploy_android_applications.keywords.widgetkeys.CommonLayoutParamsKey;
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
    private Context context;

    private Map<String, Integer> commonLayoutParamsMap = new HashMap<>();
    private Map<String, Integer> linearLayoutParamsMap = new HashMap<>();
    private Map<String, Integer> textViewParamsMap = new HashMap<>();
    private Map<String, Integer> imageViewParamsMap = new HashMap<>();
    private Map<String, Integer> scrollViewParamsMap = new HashMap<>();


    void init(Context context){
        this.context = context;
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_WIDTH_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_WIDTH);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_HEIGHT_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_HEIGHT);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_LEFT_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_LEFT);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_TOP_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_TOP);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_RIGHT_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_RIGHT);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_BOTTOM_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_MARGIN_BOTTOM);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_LEFT_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_LEFT);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_TOP_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_TOP);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_RIGHT_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_RIGHT);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_BOTTOM_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_PADDING_BOTTOM);
        commonLayoutParamsMap.put(CommonLayoutParamsKey.LAYOUT_PARAMS_GRAVITY_DES, CommonLayoutParamsKey.LAYOUT_PARAMS_GRAVITY);

        linearLayoutParamsMap.put(LinearLayoutParamskey.BACKGROUND_COLOR_DES, LinearLayoutParamskey.BACKGROUND_COLOR);
        linearLayoutParamsMap.put(LinearLayoutParamskey.ORIENTATION_DES, LinearLayoutParamskey.ORIENTATION);

        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_DES, TextViewParamsKey.TEXTVIEW_TEXT);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_SIZE_DES, TextViewParamsKey.TEXTVIEW_TEXT_SIZE);
        textViewParamsMap.put(TextViewParamsKey.TEXTVIEW_TEXT_COLOR_DES, TextViewParamsKey.TEXTVIEW_TEXT_COLOR);

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

        if( v != null) {
            ALog.i("convert", v.getClass().getSimpleName() + "");
            generateLayoutParams(parser, v);
        }

        return v;
    }

    private void generateLayoutParams(XmlPullParser parser, View view){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                                , LinearLayout.LayoutParams.WRAP_CONTENT);
        if (parser.getAttributeCount() > 0) {
            for (int i = 0 ; i < parser.getAttributeCount() ; i++) {
                if (commonLayoutParamsMap.get(parser.getAttributeName(i)) != null) {
                    renderCommonParams(view, commonLayoutParamsMap.get(parser.getAttributeName(i)),
                            parser.getAttributeValue(i), lp);
                }
            }
        }
    }

    private void renderCommonParams(View view, int arg, String attrsValue, LinearLayout.LayoutParams lp){
        switch (arg) {
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_WIDTH :
                if (StringUtils.isNumeric(attrsValue)){
                    lp.width = px2dip(Integer.parseInt(attrsValue), context);
                } else {
                    if(WHKey.MATCH_PARENT.equals(attrsValue)){
                        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    }
                }
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_HEIGHT :
                if (StringUtils.isNumeric(attrsValue)){
                    lp.height = px2dip(Integer.parseInt(attrsValue), context);
                } else {
                    if(WHKey.MATCH_PARENT.equals(attrsValue)){
                        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    }
                }

                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_MARGIN_LEFT :
                lp.leftMargin = px2dip(Integer.parseInt(attrsValue), context);
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_MARGIN_TOP :
                lp.topMargin = px2dip(Integer.parseInt(attrsValue), context);
                ALog.i("mgTop", lp.topMargin + "");
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_MARGIN_RIGHT :
                lp.rightMargin = px2dip(Integer.parseInt(attrsValue), context);
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_MARGIN_BOTTOM :
                lp.bottomMargin = px2dip(Integer.parseInt(attrsValue), context);
                ALog.i("mgBottom", lp.bottomMargin + "");
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_PADDING_LEFT :
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_PADDING_TOP :
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_PADDING_RIGHT :
                break;
            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_PADDING_BOTTOM :
                break;

            case CommonLayoutParamsKey
                    .LAYOUT_PARAMS_GRAVITY:
                if(GravityKey.GRAVITY_CENTER.equals(attrsValue)){
                    lp.gravity = Gravity.CENTER;
                }
                break;
        }

        view.setLayoutParams(lp);
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

        return null;
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
            for(int i = 0;i < parser.getAttributeCount();i ++) {
                if (linearLayoutParamsMap.get(parser.getAttributeName(i)) != null) {
                    renderLinearLayout(linearLayout, linearLayoutParamsMap.get(parser.getAttributeName(i)),
                            parser.getAttributeValue(i));
                } else {
                    ALog.e("keyErr" , "this type is not supported now -- " + parser.getAttributeValue(i));
                }
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
                    if (OrientationKey.ORIENTATION_VERTICAL.equals(attrsValue)){
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                    }else if (OrientationKey.ORIENTATION_HORIZONTAL.equals(attrsValue)){
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
                if(textViewParamsMap.get(parser.getAttributeName(i)) != null) {
                    renderTextView(textView, textViewParamsMap.get(parser.getAttributeName(i)),
                            parser.getAttributeValue(i));
                }  else {
                    ALog.e("keyErr" , "this type is not supported now -- " + parser.getAttributeValue(i));
                }
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
        AImageView imageView = (AImageView)view;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        if(parser != null){
            for(int i = 0;i < parser.getAttributeCount() ; i ++){
                if(imageViewParamsMap.get(parser.getAttributeName(i)) != null) {
                    renderImageView(imageView, imageViewParamsMap.get(parser.getAttributeName(i)),
                            parser.getAttributeValue(i));
                } else {
                    ALog.e("keyErr" , "this type is not supported now -- " + parser.getAttributeValue(i));
                }
            }
        }

        return imageView;
    }

    void renderImageView(final AImageView imageView, int arg, String attrsValue){
        if(imageView != null && !TextUtils.isEmpty(attrsValue)){
            switch (arg) {
                case ImageViewParamsKey.IMAGEVIEW_SRC:
                    ImageLoader.getInstance().displayImage(attrsValue, imageView);
                    break;

                case ImageViewParamsKey.IMAGEVIEW_SCALETYPE:
                    if(ScaleTypekey.SCALETYPE_MATRIX.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);

                    }else if(ScaleTypekey.SCALETYPE_FIT_XY.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    }else if(ScaleTypekey.SCALETYPE_FIT_CENTER.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    }else if(ScaleTypekey.SCALETYPE_FIT_START.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.FIT_START);

                    }else if(ScaleTypekey.SCALETYPE_FIT_END.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.FIT_END);

                    }else if(ScaleTypekey.SCALETYPE_CENTER.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.CENTER);

                    }else if(ScaleTypekey.SCALETYPE_CENTER_CROP.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }else if(ScaleTypekey.SCALETYPE_CENTER_INSIDE.equals(attrsValue)){
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                    break;
            }
        }
    }

    @Override
    View toImageButton(View view, XmlPullParser parser) {
        return view;
    }

    public int px2dip(int paramInt, Context context) {
        float f = getDensity(context);
        return (int) ((paramInt - 0.5D) / f);
    }

    public float getDensity(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().density;
    }
}
