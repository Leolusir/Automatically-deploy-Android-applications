package com.leo.automatically_deploy_android_applications.core.decor;

import android.view.View;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by littleming on 15/7/2.
 */
public abstract class Converter {

    /*列表布局*/
    abstract View toList(View view, XmlPullParser parser);

    /*卡片布局*/
    abstract View toCardView(View view, XmlPullParser parser);

    /*滑动布局*/
    abstract View toViewPager(View view, XmlPullParser parser);

    /*普通布局*/
    abstract View toLayout(View view, XmlPullParser parser);

    /*线性布局*/
    abstract View toLinearLayout(View view, XmlPullParser parser);

    /*相对布局*/
    abstract View toRelativeLayout(View view, XmlPullParser parser);

    /*控件转化*/
    abstract View toWidget(View view, XmlPullParser parser);

    /*按钮*/
    abstract View toButton(View view, XmlPullParser parser);

    /*文本*/
    abstract View toTextView(View view, XmlPullParser parser);

    /*图片*/
    abstract View toImageView(View view, XmlPullParser parser);

    /*图片按钮*/
    abstract View toImageButton(View view, XmlPullParser parser);
}
