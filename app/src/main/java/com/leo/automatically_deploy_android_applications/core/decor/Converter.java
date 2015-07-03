package com.leo.automatically_deploy_android_applications.core.decor;

import android.view.View;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by littleming on 15/7/2.
 */
public abstract class Converter {

    /*list*/
    abstract View toList(View view, XmlPullParser parser);

    /*card*/
    abstract View toCardView(View view, XmlPullParser parser);

    /*viewpager*/
    abstract View toViewPager(View view, XmlPullParser parser);

    /*layout*/
    abstract View toLayout(View view, XmlPullParser parser);

    /*linearLayout*/
    abstract View toLinearLayout(View view, XmlPullParser parser);

    /*relativeLayout*/
    abstract View toRelativeLayout(View view, XmlPullParser parser);

    /*widget*/
    abstract View toWidget(View view, XmlPullParser parser);

    /*button*/
    abstract View toButton(View view, XmlPullParser parser);

    /*textView*/
    abstract View toTextView(View view, XmlPullParser parser);

    /*image*/
    abstract View toImageView(View view, XmlPullParser parser);

    /*imageButton*/
    abstract View toImageButton(View view, XmlPullParser parser);
}
