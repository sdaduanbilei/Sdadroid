package com.scorpio.frame.util;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 基础工具类
 * @author 豪
 *
 */
public class SystemUtils {
	
	/**
	 * 忙时呼叫转移，提示关机
	 */
	public static final String TYPE_CALL_ENABLE_POWEROFF_SERVICE = "tel:**67*13810538911%23";
	
	/**
	 * 忙时呼叫转移，提示停机
	 */
	public static final String TYPE_CALL_ENABLE_STOP_SERVICE = "tel:**67*13701110216%23";
	
	/**
	 * 忙时呼叫转移，提示空号
	 */
	public static final String TYPE_CALL_ENABLE_SERVICE = "tel:**67*13800000000%23";
	
	/**
	 * 解除忙时呼叫转移
	 */
	public static final String TYPE_CALL_DISABLE_SERVICE = "tel:%23%2367%23";

	/**
     * 获取手机IMEI号
     * @param context
     * @return 手机IMEI号
     */
    public static String getImei(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
    }
	
	/**
	 * 获取Android系统版本代号
	 * @return 返回系统版本代号
	 */
	public static int getSDKVersion(){
		return Build.VERSION.SDK_INT;
	}
	
	/**
	 * 
	 * 查询指定电话的联系人姓名
	 *
	 * @param context
	 * @param number
	 * @return
	 */
    public static String getContactNameByNumber(Context context, String number){
    	String name;
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor.moveToFirst()) {
        	name = cursor.getString(0);
        }else{
        	name = "未知";
        }
        cursor.close();
        return name;
    }
    
    /**
     * 设置忙时呼叫转移类型
     * @param context
     * @param uriString
     */
    public static void setCallEnable(Context context, String uriString){
		Intent i = new Intent(Intent.ACTION_CALL);     
		i.setData(Uri.parse(uriString)); 
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
    }
	
//    /**
//     * 收起通知栏
//     * 需添加权限 <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
//     *
//     * @param context
//     */
//     public static void collapseStatusBar(Context context) {
//        try {
//            Object statusBarManager = context.getSystemService("statusbar");
//            Method collapse;
//
//            if (Build.VERSION.SDK_INT <= 16) {
//                collapse = statusBarManager.getClass().getMethod("collapse");
//            } else {
//                collapse = statusBarManager.getClass().getMethod("collapsePanels");
//            }
//            collapse.invoke(statusBarManager);
//        } catch (Exception localException) {
//            localException.printStackTrace();
//        }
//    }
//
	/**
	 * 判断手机中是否安装某应用
	 * @param context
	 * @param packageName 包名
	 * @return
	 */
	public static boolean getAppIsExist(Context context, String packageName){
		List<PackageInfo> list = context.getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : list) {
        	if(packageInfo.packageName.equals(packageName)) return true;
        }
        return false;
	}
	
	/**
	 * 执行root命令
	 * @param command 待执行的命令，如
	 * "chmod 777 " + getPackageCodePath()  获取root权限；
	 * "reboot -p"  关机；
	 * "reboot"  重启；
	 * "pm install "+安装路径  静默安装；
	 * "pm uninstall "+卸载包名  静默卸载；
	 * @return 返回执行结果
	 */
	public static boolean execCmd(String command) {
		Process process = null;
		DataOutputStream os = null;
		int result = 1;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command+"\n");
			os.writeBytes("exit\n");
			os.flush();
			result = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if(process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result != 1 ? true : false;
	}
	
//	/**
//	 * 切换飞行模式
//	 * @param context
//	 * @param airplane 是否飞行
//	 */
//	public static void setAirplane(Context context, boolean isSystem, boolean airplane){
//		setAirplane(context, isSystem, airplane, true);
//	}
//
	/**
	 * 切换飞行模式
	 * @param context
	 * @param airplane 是否飞行
	 */
//	@SuppressWarnings("deprecation")
//	public static void setAirplane(Context context, boolean isSystem, boolean airplane, boolean displayFailed){
//		try{
//			if(getSDKVersion() >= 17){
//				if(!isSystem) SystemUtils.execCmd("pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
//				Settings.Global.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, airplane ? 1 : 0);
//			}else{
//				putSystemInt(context, Settings.System.AIRPLANE_MODE_ON, airplane ? 1 : 0);
//			}
//			context.sendBroadcast(new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED));
//		}catch(Exception e){
//			if(displayFailed) e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 切换飞行后关闭内容
//	 * @param context
//	 * @param str 内容
//	 */
//	public static void setAirplaneModeRadios(Context context, boolean isSystem, String str){
//		setAirplaneModeRadios(context, isSystem, str, true);
//	}
	
//	/**
//	 * 切换飞行后关闭内容
//	 * @param context
//	 * @param str 内容
//	 * @param displayFailed 是否显示失败提示
//	 */
//	@SuppressWarnings("deprecation")
//	public static void setAirplaneModeRadios(Context context, boolean isSystem, String str, boolean displayFailed){
//		try{
//			if(getSDKVersion() >= 17){
//				if(!isSystem) SystemUtils.execCmd("pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
//				Settings.Global.putString(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_RADIOS, str);
//			}else{
//				putSystemString(context, Settings.System.AIRPLANE_MODE_RADIOS, str);
//			}
//		}catch(Exception e){
//			if(displayFailed) Toast.makeText(context, "未获取root权限，飞行模式信息设置失败", Toast.LENGTH_SHORT).show();
//		}
//	}

	/**
	 * 设置音量大小
	 * @param context
	 * @param type 音量类型 1系统 2 铃声 3 音乐 5 通知
	 * @param volume 音量大小
	 */
	public static void setStreamVolume(Context context, int type, int volume){
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audio.setStreamVolume(type, volume, 0);
	}



	/**
	 * 设置手机铃声模式
	 * @param context
	 * type = 2 （铃声）
	 * type = 3 （静音）
	 * type = 4 （铃声震动）
	 * type = 5 （震动）
	 */
//	@SuppressWarnings("deprecation")
//	public static void setRingAndVibrate(Context context, boolean silence, boolean vibrate){
//		int type = (silence ? 1 : 0) + (vibrate ? 4 : 2);
//		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//		if(MeizuUtils.isMeizu()){
//			switch(type){
//			case 2://不静音，不震动
//				if (SystemUtils.getSDKVersion() > 15) {
//					Settings.System.putInt(context.getContentResolver(),"vibrate_when_ringing", 0);
//				}else{
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
//				}
//				audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//				break;
//			case 3://静音，不震动
//				if (SystemUtils.getSDKVersion() > 15) {
//					Settings.System.putInt(context.getContentResolver(),"vibrate_when_ringing", 0);
//				}else{
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
//				}
//				audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//				break;
//			case 4://不静音，震动
//				if (SystemUtils.getSDKVersion() > 15) {
//					Settings.System.putInt(context.getContentResolver(),"vibrate_when_ringing", 1);
//				}else{
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
//				}
//				audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//				break;
//			case 5://静音，震动
//				if (SystemUtils.getSDKVersion() > 15) {
//					Settings.System.putInt(context.getContentResolver(),"vibrate_when_ringing", 1);
//				}else{
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//					audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
//				}
//				audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//				break;
//			}
//		}else{
//			switch(type){
//			case 2://不静音，不震动
//				audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
//				break;
//			case 3://静音，不震动
//				audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
//				break;
//			case 4://不静音，震动
//				audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
//				break;
//			case 5://静音，震动
//				audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//				audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
//				break;
//			}
//		}
//    }
	
	/**
	 * 设置桌面背景
	 * @param context
	 * @param uri 壁纸地址
	 */
	@SuppressWarnings("deprecation")
	public static void setWallpaper(Context context, String uri){
		Bitmap bitmap = BitmapFactory.decodeFile(uri);
		try{
			context.setWallpaper(bitmap);
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 设置wifi
	 * @param context
	 * @param wifi 是否开启wifi
	 */
	public static void setWifi(Context context, boolean wifi){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		try{
			wifiManager.setWifiEnabled(wifi);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置gprs
	 * @param context
	 * @param gprs 是否开启gprs
	 */
	public static void setGPRS(Context context, boolean gprs){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method setMobileDataEnabl;
		try {
			setMobileDataEnabl = cm.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
			setMobileDataEnabl.invoke(cm, gprs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置蓝牙
	 * @param context
	 * @param bluetooth 是否开启蓝牙
	 */
	public static void setBluetooth(Context context, boolean bluetooth){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetooth) bluetoothAdapter.enable();
		else bluetoothAdapter.disable();
	}
	
	/**
	 * 控制系统音乐
	 * @param context
	 * @param type 0 播放、暂停 1 上一曲 2 下一曲
	 */
//	public static void controlMusic(Context context, int type){
//		switch(type){
//		case 0:
//			if(MeizuUtils.getFlymeVersion() < 4){
//				Intent intent = new Intent("com.android.music.musicservicecommand.togglepause");
//				intent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
//				context.startService(intent);
//			}else{
//				Intent intent=new Intent("com.meizu.media.music.player.musicservicecommand.togglepause");
//				intent.setClassName("com.meizu.media.music","com.meizu.media.music.player.PlaybackService");
//				context.startService(intent);
//			}
//			break;
//		case 1:
//			if(MeizuUtils.getFlymeVersion() < 4){
//				Intent intent = new Intent("com.android.music.musicservicecommand.previous");
//				intent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
//				context.startService(intent);
//			}else{
//				Intent intent=new Intent("com.meizu.media.music.player.musicservicecommand.previous");
//				intent.setClassName("com.meizu.media.music","com.meizu.media.music.player.PlaybackService");
//				context.startService(intent);
//			}
//			break;
//		case 2:
//			if(MeizuUtils.getFlymeVersion() < 4){
//				Intent intent = new Intent("com.android.music.musicservicecommand.next");
//				intent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
//				context.startService(intent);
//			}else{
//				Intent intent=new Intent("com.meizu.media.music.player.musicservicecommand.next");
//				intent.setClassName("com.meizu.media.music","com.meizu.media.music.player.PlaybackService");
//				context.startService(intent);
//			}
//			break;
//		}
//	}
	
	/**
	 * 回到系统桌面
	 * @param context
	 */
	public static void goSystemHome(Context context){
		 Intent intent = new Intent("android.intent.action.MAIN");
		 intent.addCategory("android.intent.category.HOME");
		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 context.startActivity(intent);
	}
	
	/**
	 * 打开第三方应用
	 * @param context
	 * @param pack 软件包名
	 */
	public static void openOtherApp(Context context, String pack){
		try{
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(pack);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	/**
//	 * 锁屏
//	 * @param context
//	 */
//	public static void LockScreen(Context context){
//		DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//		ComponentName componentName = new ComponentName(context, LockScreen.class);
//        if(policyManager.isAdminActive(componentName)) {
//        	policyManager.lockNow();
//        }else {
//        	Toast.makeText(context, "您还没有给予软件锁屏权限，请授权后再次使用本功能！", Toast.LENGTH_LONG).show();
//        	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
//        	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, context.getApplicationInfo().name);
//        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	context.startActivity(intent);
//        }}
//
	/**
	 * 亮屏
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public static void LightScreen(Context context){
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);  
    	WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "");
    	wakeLock.acquire(15000);
	}
	
	/**
	 * 号码直拨
	 * @param context
	 * @param number 号码
	 */
	public static void call(Context context, String number){
		Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+number));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 获取Settings.System中的int数据
	 * @param name
	 * @return
	 */
	public static int getSystemInt(Context context, String name){
		try {
			return Settings.System.getInt(context.getContentResolver(), name);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取Settings.System中的string数据
	 * @param name
	 * @return
	 */
	public static String getSystemString(Context context, String name){
		String result = "";
		result = Settings.System.getString(context.getContentResolver(),name);
		if(result != null) return result;
		return "";
	}
	
	/**
	 * 向Settings.System中存储int数据
	 * @param name
	 */
	public static void putSystemInt(Context context, String name, int value){
		Settings.System.putInt(context.getContentResolver(), name, value);
	}
	
	/**
	 * 向Settings.System中存储String数据
	 * @param name
	 */
	public static void putSystemString(Context context, String name, String value){
		Settings.System.putString(context.getContentResolver(), name, value);
	}
}
