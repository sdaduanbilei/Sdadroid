package com.scorpio.frame.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

public class ToolsUtil {
    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     */
    public static void msgbox(Context context, String msg) {
        new AlertDialog.Builder((context)).setTitle("提示").setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }

    /**
     * 无按钮的提示对话框，点击外部消失
     *
     * @param context
     * @param msg
     */
    public static void msgtip(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 获取当前系统的版本号
     *
     * @param context
     * @return 0：失败 其他：成功的版本号
     */
    public static int currentVersionCode(Context context, String appPackage) {
        int ver = 0;
        try {
            ver = context.getPackageManager().getPackageInfo(appPackage, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return ver;
    }

    public static String currentVersion(Context context, String appPackage) {
        String ver = null;
        try {
            ver = context.getPackageManager().getPackageInfo(appPackage, 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ver;
    }

    /**
     * 获取手机的IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String deviceId = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        return deviceId;
    }


    public static void setTranslucentStatus(Activity context, boolean on) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * Base64加密
     *
     * @param xml
     * @return
     */
    @SuppressLint("NewApi")
    public static String encodeBase64(String xml) {
        return Base64.encodeToString(xml.getBytes(), Base64.DEFAULT);
    }

    /**
     * Base64解密
     *
     * @param rexml
     * @return
     */
    @SuppressLint("NewApi")
    public static String decodeBase64(String rexml) {
        byte b[] = Base64.decode(rexml, Base64.DEFAULT);
        String reString = new String(b);
        return reString;
    }

    /**
     * 发送短信
     *
     * @param context
     * @param to
     * @param content
     */
    public static void sendSms(Context context, String to, String content) {
        Uri uri = Uri.parse("smsto:" + to);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", content);
        context.startActivity(it);
    }

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void tel(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(it);
    }

    /**
     * 判断null和""的情况
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            if (str.length() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 针对于json对于null字符串的处理
     *
     * @param str
     * @return
     */
    public static String emptyString(String str) {
        if (str == null) {
            return "";
        }
        if (str.endsWith("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 通过utf8 encode
     *
     * @param str
     * @return
     */
    public static String encodeUrlString(String str) {
        String s = str;
        try {
            s = URLEncoder.encode(str, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000'; // 采用十六进制,相当于十进制的12288

            } else if (c[i] < '\177') { // 采用八进制,相当于十进制的127
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);

        return returnString;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 根据包名 和类名 打开应用程序
     *
     * @param pkg
     * @param cls
     */
    public static void startapp(Context context, String pkg, String cls) {
        LogUtil.Debug("PKG====" + pkg);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName componentName = new ComponentName(pkg, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
            try {
                context.startActivity(intent);
            } catch (Exception e1) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    /**
     * 获取时间 年月 周
     *
     * @return
     */
    public static String getWeekStr(int day) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK) + day);
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "周" + mWay;
    }


    /**
     * 检测网络是否有效
     *
     * @param context
     * @return ture 有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取手机状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * toast 显示提示信息
     *
     * @param context
     * @param s
     */
    public static void toast(Context context, String s) {
        if (!ToolsUtil.isEmpty(s)){
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        LogUtil.Debug("Toast===" + s);}
    }


    /**
     * 判断是否是中国移动
     * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
     * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
     *
     * @param context
     * @return
     */
    public static boolean isChianMM(Context context) {
        boolean isChina = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                isChina = true;
            }
        }else{
            isChina = false ;
        }
        return isChina;
    }

}
