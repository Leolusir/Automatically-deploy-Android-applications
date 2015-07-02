package com.leo.automatically_deploy_android_applications.core.decor;

import android.view.View;

import java.util.Map;

/**
 * Created by littleming on 15/7/2.
 */
public abstract class Converter {

    /*列表布局*/
    abstract View toList(View view, String contentXml);

    /*卡片布局*/
    abstract View toCardView(View view, String attrs);

    /*滑动布局*/
    abstract View toViewPager(View view, String attrs);

    /*普通布局*/
    abstract View toLayout(View view, Map<String, String> attrs);

    /*线性布局*/
    abstract View toLinearLayout(View view, String attrs);

    /*相对布局*/
    abstract View toRelativeLayout(View view, String attrs);

    /*控件转化*/
    abstract View toWidget(View view, Map<String, String> attrs);

    /*按钮*/
    abstract View toButton(View view, String attrs);

    /*图片*/
    abstract View toImageView(View view, String attrs);

    /*图片按钮*/
    abstract View toImageButton(View view, String attrs);
}
