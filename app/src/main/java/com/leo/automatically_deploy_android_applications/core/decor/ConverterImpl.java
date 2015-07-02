package com.leo.automatically_deploy_android_applications.core.decor;

import android.view.View;

import com.leo.automatically_deploy_android_applications.widgets.AButton;
import com.leo.automatically_deploy_android_applications.widgets.ACardView;
import com.leo.automatically_deploy_android_applications.widgets.AImageView;
import com.leo.automatically_deploy_android_applications.widgets.ALinearLayout;
import com.leo.automatically_deploy_android_applications.widgets.ARecyclerView;
import com.leo.automatically_deploy_android_applications.widgets.ARelativeLayout;
import com.leo.automatically_deploy_android_applications.widgets.ATextView;
import com.leo.automatically_deploy_android_applications.widgets.AViewPager;

import java.util.Map;

/**
 * Created by littleming on 15/7/2.
 */
public class ConverterImpl extends Converter{

    View converterView(View view, String attrs){
        if(view instanceof ARecyclerView){

        }else if(view instanceof ACardView){

        }else if(view instanceof AViewPager){

        }else if(view instanceof ALinearLayout){

        }else if(view instanceof ARelativeLayout){

        }

        return view;
    }

    @Override
    View toList(View view, String contentXml) {


        return view;
    }

    @Override
    View toCardView(View view, String attrs) {
        return null;
    }

    @Override
    View toViewPager(View view, String attrs) {
        return null;
    }

    @Override
    View toLayout(View view, Map<String, String> attrs) {
        if(view instanceof ALinearLayout){

        }

        return view;
    }

    @Override
    View toLinearLayout(View view, String attrs) {
        return null;
    }

    @Override
    View toRelativeLayout(View view, String attrs) {
        return null;
    }

    @Override
    View toWidget(View view, Map<String, String> attrs) {
        if(view instanceof ATextView){

        }else if(view instanceof AImageView){

        }else if(view instanceof AButton){

        }

        return view;
    }

    @Override
    View toButton(View view, String attrs) {
        return null;
    }

    @Override
    View toImageView(View view, String attrs) {
        return null;
    }

    @Override
    View toImageButton(View view, String attrs) {
        return null;
    }
}
