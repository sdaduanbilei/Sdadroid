package com.scorpio.frame.data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.scorpio.frame.request.DataResponse;
import com.scorpio.frame.util.FileUtil;
import com.scorpio.frame.util.ImageUtil;
import com.scorpio.frame.util.LogUtil;


/**
 * Created by lonaever on 14-7-23.
 */
public class ImageData {
    public int imgId;
    public String imgUrl;
    public Uri imgUri;//可能是content的或者是file的
    public String filepath;//用于上传时候的路径,可以干别的事

    public ImageData() {
    }

    public ImageData(Uri uri) {
        imgUri = uri;
    }

    public ImageData(String url) {
        imgUrl = url;
    }

    public boolean compress(final Context context, final int outWidth, final int outHeight, final String outFileName) {
        String filecompresspath = FileUtil.appSavePathFile(outFileName);
        int ret = ImageUtil.uriCompressToFile(context, imgUri, filecompresspath, outWidth, outHeight);
        LogUtil.Debug("comress ret=" + ret);
        if (ret != 0) {
            filepath=filecompresspath;
//            if (ret == 1) {
//                filepath = filecompresspath;
//            } else if (ret == 2) {
//                filepath = FileUtil.getFilePathFromUri(context,imgUri);
//            }
            return true;
        }
        return false;
    }

    public void compress(final Context context, final int outWidth, final int outHeight, final String outFileName, final DataResponse dr) {
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected void onPostExecute(Object o) {
                if (o == null) {
                    dr.onFail("压缩图片失败!");
                } else {
                    dr.onSucc(null);
                }
            }

            @Override
            protected Object doInBackground(Object... objects) {
                String filecompresspath = FileUtil.appSavePathFile(outFileName);
                int ret = ImageUtil.uriCompressToFile(context, imgUri, filecompresspath, outWidth, outHeight);
                LogUtil.Debug("comress ret=" + ret);
                if (ret != 0) {
                    filepath=filecompresspath;
//                    if (ret == 1) {
//                        filepath = filecompresspath;
//                    } else if (ret == 2) {
//                        filepath = FileUtil.getFilePathFromUri(context,imgUri);
//                    }
                    return "ok";
                }
                return null;
            }
        }.execute("");
    }

}
