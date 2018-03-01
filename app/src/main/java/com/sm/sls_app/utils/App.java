package com.sm.sls_app.utils;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * 用来存放所有Activity
 *
 * @author SLS003
 */
public class App extends Application {
    public static ArrayList<Activity> activityS = new ArrayList<Activity>();
    public static ArrayList<Activity> activityS1 = new ArrayList<Activity>();

    // 在整個應用程序的進程第一次被創建的時候執行
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initCrash() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);

    }

    private static App instance;

    public static App getInstance() {
        if (instance == null)
            instance = new App();/////todo
        return instance;
    }

    public void exitApp() {
        System.exit(0);
    }


}
