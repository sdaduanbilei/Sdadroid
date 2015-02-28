package com.scorpio.frame.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class FileUtil {
	public static String SaveFolder = "e_zhebao";
	
	public static void init(String saveFolder) {
		if(saveFolder!=null) {
			SaveFolder=saveFolder;
		}
		String path=appSavePath();
		if(path!=null) {
			File f=new File(path);
			if(!f.exists()) {
				f.mkdirs();
			}
			LogUtil.Log("init savepath ok:"+path);
		}else {
			LogUtil.Debug("init savepath fail,no sdcard");
		}

	}
	
	//获取程序的数据路径
	public static String appDataPath(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}

	//返回app通用的存储路径
	public static String appSavePath() {
		String path=null;
		if(isSdOk()) {
			path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+SaveFolder+"/";
		}
		return path;
	}
	
	//返回app在通用存储路径下的文件路径
	public static String appSavePathFile(String filename) {
		String path=appSavePath();
		if(path!=null) {
			path+=filename;
		}
		return path;
	}

	// 判断sd卡是否存在
	public static boolean isSdOk() {
		boolean sdCardExist = Environment.MEDIA_MOUNTED
				.equals(Environment.getExternalStorageState());
		return sdCardExist;
	}
	
	//判断文件是否存折
	public static boolean isFileExistInApp(String filename) {
		String path=appSavePathFile(filename);
		File f=new File(path);
		return f.exists();
	}

	//保存文件到app存储路径下
	public static String saveDataToFile(String filename, byte[] buffer) {
		String path = appSavePathFile(filename);
		if(path==null) {
			return null;
		}
		File save = new File(path);
		boolean isok = false;
		try {
			OutputStream output = new FileOutputStream(save);
			output.write(buffer);
			output.flush();
			output.close();
			isok = true;
		} catch (Exception e) {
			LogUtil.Debug(e.getLocalizedMessage());
		}
		if (isok) {
			return path;
		}
		return null;
	}
	
	/**
	 * 生成随机的jpg文件名
	 * @return
	 */
	public static String randomPath() {
		Date date = new Date();  
        SimpleDateFormat dateFormat = new SimpleDateFormat(  
                "'img'_yyyyMMddHHmmss"); 
        String filename=dateFormat.format(date) + ".jpg";
        return filename;  
	}
	
//	public static void autoClearSaveFolder(Context context) {
//		String dayKey=DateUtil.getCurrentDateTime().format("YYYYMMDD");
//		SharedPreferences sp = context.getSharedPreferences("savefolderflag", 0);
//		Editor e = sp.edit();
//		boolean haveclear=sp.getBoolean(dayKey, false);
//		if(!haveclear) {
//			clearSaveFolder();
//			e.clear();
//			LogUtil.Debug("auto clear save folder");
//		}
//		e.putBoolean(dayKey, true);
//		e.commit();
//	}
	
	/**
	 * 清除缓存
	 */
	public static void clearSaveFolder() {
		File folder=new File(appSavePath());
		File[] list=folder.listFiles();
		for(int i=0;i<list.length;i++) {
			if(list[i].isFile()) {
				list[i].delete();
			}
		}
	}
	
	/**
	 * 缓存大小
	 * @return
	 */
	public static String size() {
		File folder=new File(appSavePath());
		File[] list=folder.listFiles();
		long size=0;
		for(int i=0;i<list.length;i++) {
			FileInputStream fis = null;
            try {
				fis = new FileInputStream(list[i]);
				size += fis.available();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (float)(size/1024/1000.0)+"M";
	}

    /**
     * 只能转换如：
     * file://
     * content://
     * 这样的为文件的绝对路径
     * @param uri
     * @return
     */
    public static String getFilePathFromUri(Context context,Uri uri) {
        String path=uri.getPath();
        if (path.startsWith("file://")) {
            return path.substring(7);
        }else if (path.startsWith("content://")) {
            return getFilePathFromContentUri(context,uri);
        }
        return path;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathFromContentUri(Context context,Uri uri) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
//	    也可用下面的方法拿到cursor
//	    Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * Gets the content:// URI  from the given corresponding path to a file
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
