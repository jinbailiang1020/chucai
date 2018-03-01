package com.sm.sls_app.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.sm.sls_app.ui.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    //文件夹目录
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/yilianti/crash_log/";
    //文件名
    private static final String FILE_NAME = "crash";
    //文件名后缀
    private static final String FILE_NAME_SUFFIX = ".txt";
    //上下文
    private Context mContext;
    private static CrashHandler myCrashHandler ;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private CrashHandler() {
    }
    public static synchronized CrashHandler getInstance(){
        if(myCrashHandler!=null){
            return myCrashHandler;
        }else {
            myCrashHandler  = new CrashHandler();
            return myCrashHandler;
        }
    }
    public void init(Context context){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mContext = context;
    }
    public void uncaughtException(final Thread thread, final Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //     干掉当前的程序
//         android.os.Process.killProcess(android.os.Process.myPid());
//         //退出程序
            Intent intent = new Intent(App.getInstance().getApplicationContext(), MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    App.getInstance().getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            //退出程序
            AlarmManager mgr = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent); // 1秒钟后重启应用
            App.getInstance().exitApp();
        }
    }



    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
//        System.out.println("程序挂掉了 ");
        dumpExceptionToSDCard(ex);
//        System.out.println("信息发送了 ");
        return true;
    }

    /**
     * 导出异常信息到SD卡
     *
     *
     *
     * @param ex
     */
    private void dumpExceptionToSDCard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        //创建文件夹
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取当前时间
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            //输出流操作
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出手机信息和异常信息
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            pw.println("发生异常时间：" + time);
            pw.println("应用版本：" + pi.versionName);
            pw.println("应用版本号：" + pi.versionCode);
            pw.println("android版本号：" + Build.VERSION.RELEASE);
            pw.println("android版本号API：" + Build.VERSION.SDK_INT);
            pw.println("手机制造商:" + Build.MANUFACTURER);
            pw.println("手机型号：" + Build.MODEL);
            ex.printStackTrace(pw);
            //关闭输出流
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传异常信息到服务器
     *
     * @param ex
     */
    private void uploadExceptionToServer(Throwable ex) {
    /*    Error error = new Error(ex.getMessage());
        error.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {

            }
        });*/
    }
}