package com.scorpio.frame.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.goodev.picker.IntentAction;


/**
 * Created by lonaever on 14-7-22.
 */
public class PickImageUtil {
    private static final int TAKE_PICTURE = 100;
    private static final int CROP_PICTURE = 300;
    private static final int CHOOSE_PICTURE = 500;
    private static final int CHOOSE_MULTI_PICTURE = 700;
    private static final String TEM_IMAGE_NAME = "tem_pick_image.jpg";
    private static Uri imageUri;
    private static int aspectX = 0, aspectY = 0, scale = 0;
    private static Activity context;
    private static int mode;//0:单选模式 1:多选模式

    private static void init() {
        String tem_image_path = "file://" + FileUtil.appSavePathFile(TEM_IMAGE_NAME);
        LogUtil.Debug("image uri=" + tem_image_path);
        imageUri = Uri.parse(tem_image_path);
    }

    public static void open(Activity context) {
        init();
        PickImageUtil.context = context;
        aspectX = 0;
        aspectY = 0;
        scale = 0;
        mode = 0;
        showDlg();
    }

    public static void openCrop(Activity context, int aspectX, int aspectY, int scale) {
        init();
        PickImageUtil.context = context;
        PickImageUtil.aspectX = aspectX;
        PickImageUtil.aspectY = aspectY;
        PickImageUtil.scale = scale;
        mode = 0;
        showDlg();
    }

    public static void openMulti(Activity context) {
        init();
        PickImageUtil.context = context;
        aspectX = 0;
        aspectY = 0;
        scale = 0;
        mode = 1;
        showDlg();
    }

    private static void showDlg() {
        String[] choices = {"拍照", "相册"};
        final ListAdapter adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, choices);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选取照片");
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: //相机
                            {
                                String status = Environment
                                        .getExternalStorageState();
                                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                    startCamera();
                                } else {
                                    Toast.makeText(context, "没有SD卡",
                                            Toast.LENGTH_LONG).show();
                                }
                                break;

                            }
                            case 1: //相册
                            {
                                if (mode == 0) {
                                    startPhoto();
                                } else {
                                    startMultiPhoto();
                                }
                                break;
                            }
                        }
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.create().show();
    }

    private static void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(intent, TAKE_PICTURE);
    }

    public static void startCamera(Activity mContext) {
        init();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mContext.startActivityForResult(intent, TAKE_PICTURE);
    }

    private static void startPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        if (aspectX > 0 && aspectY > 0) {
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("outputX", aspectX * scale);
            intent.putExtra("outputY", aspectY * scale);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", false); // no face detection
        }
        context.startActivityForResult(intent, CHOOSE_PICTURE);
    }

    public  static void startPhoto(Activity context) {
        init();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        if (aspectX > 0 && aspectY > 0) {
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("outputX", aspectX * scale);
            intent.putExtra("outputY", aspectY * scale);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", false); // no face detection
        }
        context.startActivityForResult(intent, CHOOSE_PICTURE);
    }

    private static void cropImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", aspectX * scale);
        intent.putExtra("outputY", aspectY * scale);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, CROP_PICTURE);
    }

    private static void startMultiPhoto() {
        Intent intent = new Intent();
        intent.setAction(IntentAction.ACTION_MULTIPLE_PICK);
        context.startActivityForResult(intent, CHOOSE_MULTI_PICTURE);
    }

    public static void startMultiPhoto(Activity context) {
        Intent intent = new Intent();
        intent.setAction(IntentAction.ACTION_MULTIPLE_PICK);
        context.startActivityForResult(intent, CHOOSE_MULTI_PICTURE);
    }

    private static Uri onCamera(Intent data) {
        if (aspectX > 0 && aspectY > 0) {
            cropImage();
            return null;
        }
        return imageUri;
    }

    private static Uri onPhoto(Intent data) {
        if (aspectX > 0 && aspectY > 0) {
            return imageUri;//经过crop后的Uri
        } else {
            return data.getData();//从相册选择出来的原始图片Uri
        }
    }

    private static Uri onCrop(Intent data) {
        return imageUri;
    }

    private static Uri[] onPhotoMulti(Intent data) {
        Parcelable[] parcelableUris = data.getParcelableArrayExtra(IntentAction.EXTRA_DATA);
        if (parcelableUris==null) {
            return null;
        }
        Uri[] uris = new Uri[parcelableUris.length];
        //Java doesn't allow array casting, this is a little hack
        System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
        return uris;
    }

    public static Uri handleResult(int requestCode, int resultCode, Intent data) {
        LogUtil.Debug("requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
        Uri uri = null;
        if (resultCode != Activity.RESULT_OK) {
            return null;
        } else {
            switch (requestCode) {
                case TAKE_PICTURE:
                    uri = onCamera(data);
                    break;
                case CHOOSE_PICTURE:
                    uri = onPhoto(data);
                    break;
                case CROP_PICTURE:
                    uri = onCrop(data);
                    break;
            }
        }
        return uri;
    }

    /**
     * 当使用多选模式的时候
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public static Uri[] handleMultiResult(int requestCode, int resultCode, Intent data) {
        LogUtil.Debug("requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
        Uri uris[] = null;
        if (resultCode != Activity.RESULT_OK) {
            return null;
        } else {
            switch (requestCode) {
                case TAKE_PICTURE:
                    Uri uri = onCamera(data);
                    uris=new Uri[]{uri};
                    break;
                case CHOOSE_MULTI_PICTURE:
                    uris=onPhotoMulti(data);
                    break;
            }
        }
        return uris;
    }

}
