package com.scorpio.frame.util;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class AsynImageUtil {
    public static String imgBaseUrl;

    public static void init(Context context, String url) {
        imgBaseUrl = url;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static void display(ImageView iv, String url) {
        //ImageLoader.getInstance().displayImage(url, iv);
        display(iv, url, 0);
    }


    //----universal-image-loader

    /**
     * @param iv
     * @param url              String imageUri = "http://site.com/image.png"; // from Web
     *                         String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     *                         String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     *                         String imageUri = "assets://image.png"; // from assets
     *                         String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param imageHolderResId
     */
    public static void display(ImageView iv, String url, int imageHolderResId) {
        Builder builder = new Builder()
                .cacheInMemory().cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        if (imageHolderResId > 0) {
            builder.showStubImage(imageHolderResId);
        }
        DisplayImageOptions options = builder.build();
        if (!StringUtil.isEmpty(url)) {
            String imgurl = url;
            if (imgBaseUrl != null) {
                if (!url.startsWith("http://") && !url.startsWith("assets://")) {
                    imgurl = imgBaseUrl + url;
                }
            }
            ImageLoader.getInstance().displayImage(imgurl, iv, options);
        }
    }

    public static void display(ImageView iv, String url, int imageHolderResId, ImageLoadingListener listener) {
        Builder builder = new Builder()
                .cacheInMemory().cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        if (imageHolderResId > 0) {
            builder.showStubImage(imageHolderResId);
        }
        DisplayImageOptions options = builder.build();
        if (!StringUtil.isEmpty(url)) {
            String imgurl = url;
            if (imgBaseUrl != null) {
                if (!url.startsWith("http://")) {
                    imgurl = imgBaseUrl + url;
                }
            }
            ImageLoader.getInstance().displayImage(imgurl, iv, options, listener);
        }
    }


    public static void stopLoad() {
        ImageLoader.getInstance().stop();
    }

    // --Picasso--
    public static void displaySmallImageFromUrl(Context context, ImageView iv,
                                                String url) {
        displayImageFromUrl(context, iv, url, 100, 100, 0);
    }

    public static void displayImageFromUrl(Context context, ImageView iv,
                                           String url) {
        displayImageFromUrl(context, iv, url, 0, 0, 0);
    }

    public static void displaySmallImageFromPath(Context context, ImageView iv,
                                                 String path) {
        displayImageFromPath(context, iv, path, 100, 100, 0);
    }

    public static void displayImageFromPath(Context context, ImageView iv,
                                            String path) {
        displayImageFromPath(context, iv, path, 0, 0, 0);
    }

    public static void displaySmallImageFromUri(Context context, ImageView iv,
                                                Uri uri) {
        displayImageFromUri(context, iv, uri, 100, 100, 0);
    }

    public static void displayImageFromUri(Context context, ImageView iv,
                                           Uri uri) {
        displayImageFromUri(context, iv, uri, 0, 0, 0);
    }


    public static void displayImageFromUrl(Context context, ImageView iv,
                                           String url, int width, int height, int placeholder) {
        if (StringUtil.isEmpty(url)) {
            LogUtil.Debug("displayImageFromUrl url is null");
            return;
        }
        String imgurl = url;
        if (imgBaseUrl != null) {
            if (!url.startsWith("http://")) {
                imgurl = imgBaseUrl + url;
            }
        }
        RequestCreator rc = Picasso.with(context).load(imgurl);
        if (width > 0 && height > 0) {
            rc.resize(width, height).centerCrop();
        }
        if (placeholder > 0) {
            rc.placeholder(placeholder);
        }
        rc.into(iv);
    }

    public static void displayImageFromPath(Context context, ImageView iv,
                                            String path, int width, int height, int placeholder) {
        if (StringUtil.isEmpty(path)) {
            LogUtil.Debug("displayImageFromPath url is null");
            return;
        }
        File imagefile = new File(path);
        RequestCreator rc = Picasso.with(context).load(imagefile);
        if (width > 0 && height > 0) {
            rc.resize(width, height).centerCrop();
        }
        if (placeholder > 0) {
            rc.placeholder(placeholder);
        }
        rc.into(iv);
    }

    public static void displayImageFromUri(Context context, ImageView iv, Uri uri, int width, int height, int placeholder) {
        RequestCreator rc = Picasso.with(context).load(uri);
        if (width > 0 && height > 0) {
            rc.resize(width, height).centerCrop();
        }
        if (placeholder > 0) {
            rc.placeholder(placeholder);
        }
        rc.into(iv);
    }


    //    备用
    public static void init(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 2M
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void displayImage(ImageView imageView, String url) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true)
//                .cacheOnDisk(true)
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)
//                .considerExifParams(true)
//                .displayer(new FadeInBitmapDisplayer(300))
//                .build();
        ImageLoader.getInstance().displayImage(url, imageView,makeDisplayIamage(0));
    }

    private static DisplayImageOptions makeDisplayIamage(int i) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory().cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2);
        if (i>0){
            builder.showStubImage(i);
        }
        return builder.build();
    }

}
