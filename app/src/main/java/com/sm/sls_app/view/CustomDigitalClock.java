package com.sm.sls_app.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DigitalClock;

/**
 * 实现倒计时的类
 * 
 * @author SLS003
 */
public class CustomDigitalClock extends DigitalClock {

  Calendar mCalendar;
  private final static String m12 = "h:mm aa";
  private final static String m24 = "k:mm";
  private FormatChangeObserver mFormatChangeObserver;

  private Runnable mTicker;
  private Handler mHandler;
  private long endTime;
  private ClockListener mClockListener;

  // private boolean mTickerStopped = false;

  // private int type = 1;

  @SuppressWarnings("unused")
  private String mFormat;

  public CustomDigitalClock(Context context) {
    super(context);
    initClock(context);
  }

  public CustomDigitalClock(Context context, AttributeSet attrs) {
    super(context, attrs);
    initClock(context);
  }

  public void setMTickStop(boolean b) {
    // mTickerStopped = b;
  }

  // public void setType(int type) {
  // this.type = type;
  // }
  //
  // public int getType() {
  // return type;
  // }

  private void initClock(Context context) {
    if (mCalendar == null) {
      mCalendar = Calendar.getInstance();
    }
    mFormatChangeObserver = new FormatChangeObserver();
    getContext().getContentResolver().registerContentObserver(
        Settings.System.CONTENT_URI, true, mFormatChangeObserver);

    setFormat();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mHandler = new Handler();

    /** requests a tick on the next hard-second boundary */
    mTicker = new Runnable() {
      public void run() {
        // Log.i("x", "11111111-----run");
        // if (mTickerStopped)
        // {
        // // Log.i("x", "------run");
        // return;
        // }
        long currentTime = System.currentTimeMillis();

        if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
          mClockListener.remainFiveMinutes();
        }
        long distanceTime = (endTime - currentTime) / 1000;

        // Log.i("x", "33333-----run  "+distanceTime);

        if (distanceTime == 0) {
          onDetachedFromWindow();
          mClockListener.timeEnd();
        } else if (distanceTime < 0) {
          setText("");
        } else {
          // if (type == 1)
          setText(dealTime(distanceTime) + " 截止");
          // else
          // setText("点击购买下一期");
        }

        // 刷新UI
        invalidate();

        long now = SystemClock.uptimeMillis();

        long next = now + (1000 - now % 1000);

        mHandler.postAtTime(mTicker, next);
      }
    };
    mTicker.run();
  }

  /**
   * long to HH:mm:ss
   * 
   * @param time
   * @return
   */
  public static String longToTime(long time) {
    if (time > 0) {
      Date date = new Date(time);
      SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
      return sd.format(date);
    }
    return null;
  }

  /** deal time string */
  public static String dealTime(long time) {
    long day = time / (24 * 60 * 60);
    long hours = (time % (24 * 60 * 60)) / (60 * 60);
    long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
    long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;

    String dayStr = String.valueOf(day);
    String hoursStr = timeStrFormat(String.valueOf(hours));
    String minutesStr = timeStrFormat(String.valueOf(minutes));
    String secondStr = timeStrFormat(String.valueOf(second));

    if (day >= 1) {
      return dayStr + "天" + hoursStr + "时";
    }
    if (hours >= 1) {
      return hoursStr + "时" + minutesStr + "分";
    }
    if (minutes >= 1) {
      return minutesStr + "分" + secondStr + "秒";
    } else {
      return secondStr + "秒";
    }
  }

  /** format time */
  private static String timeStrFormat(String timeStr) {
    switch (timeStr.length()) {
    case 1:
      timeStr = "0" + timeStr;
      break;
    }
    return timeStr;
  }

  @Override
  protected void onDetachedFromWindow() {
    // mTickerStopped =true;
    super.onDetachedFromWindow();
  }

  /** Clock end time from now on. */
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  /** Pulls 12/24 mode from system settings */
  private boolean get24HourMode() {
    return android.text.format.DateFormat.is24HourFormat(getContext());
  }

  private void setFormat() {
    if (get24HourMode()) {
      mFormat = m24;
    } else {
      mFormat = m12;
    }
  }

  private class FormatChangeObserver extends ContentObserver {
    public FormatChangeObserver() {
      super(new Handler());
    }

    @Override
    public void onChange(boolean selfChange) {
      setFormat();
    }
  }

  public void setClockListener(ClockListener clockListener) {
    this.mClockListener = clockListener;
  }

  public interface ClockListener {
    void timeEnd();

    void remainFiveMinutes();
  }

}