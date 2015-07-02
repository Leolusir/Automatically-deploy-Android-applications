package com.leo.automatically_deploy_android_applications;

import android.app.Application;

import com.leo.automatically_deploy_android_applications.core.decor.Generate;

/**
 * Created by littleming on 15/7/2.
 */
public class App extends Application{
    private static App app;
    private Generate generate;

    public static App getInstance(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initGenerate();
    }

    void initGenerate(){
        generate = new Generate();
        generate.init();
    }

    public Generate getGenerate() {
        return generate;
    }
}
