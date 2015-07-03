package com.leo.automatically_deploy_android_applications;

import android.app.Application;
import android.content.Context;

import com.leo.automatically_deploy_android_applications.core.decor.Generate;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
        initImageLoader(getApplicationContext());
    }

    void initGenerate(){
        generate = new Generate();
        generate.init();
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    public Generate getGenerate() {
        return generate;
    }
}
