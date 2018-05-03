package com.haichaoaixuexi.railway_system_android.utils;

/**
 * Created by haichao on 2018/3/9.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * 拍照，相册图片选取以及图片剪辑的工具类
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUtil {
    public static final int NONE = 0;
    public static final String IMAGE_UNSPECIFIED = "image/*";//任意图片类型
    public static final int PHOTOGRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final int PICTURE_HEIGHT = 128;
    public static final int PICTURE_WIDTH = 128;
    public static String imageName;

    /**
     * 从系统相册中选取照片上传
     * @param activity
     */
    public static void selectPictureFromAlbum(Activity activity){
        // 调用系统的相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        // 调用剪切功能
        activity.startActivityForResult(intent, PHOTOZOOM);
    }

    /**
     * 从系统相册中选取照片上传
     * @param fragment
     */
    public static void selectPictureFromAlbum(Fragment fragment){
        // 调用系统的相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        // 调用剪切功能
        fragment.startActivityForResult(intent, PHOTOZOOM);
    }

    /**
     * 拍照
     * @param activity
     */
    public static void photograph(Activity activity){
        imageName = File.separator + getStringToday() + ".jpg";

        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String status = Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    Environment.getExternalStorageDirectory(), imageName)));
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    activity.getFilesDir(), imageName)));
        }
        activity.startActivityForResult(intent, PHOTOGRAPH);
    }

    /**
     * 拍照
     * @param fragment
     */
    public static void photograph(Fragment fragment){
        imageName = "/" + getStringToday() + ".jpg";

        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String status = Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    Environment.getExternalStorageDirectory(), imageName)));
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    fragment.getActivity().getFilesDir(), imageName)));
        }
        fragment.startActivityForResult(intent, PHOTOGRAPH);
    }

    /**
     * 图片裁剪
     * @param activity
     * @param uri
     * @param height
     * @param width
     */
    public static void startPhotoZoom(Activity activity,Uri uri,int height,int width) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        intent.putExtra("return-data", true);//如果设为true则返回bitmap
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 图片裁剪
     * @param activity
     * @param uri       原图的地址
     * @param height    指定的剪辑图片的高
     * @param width     指定的剪辑图片的宽
     * @param destUri   剪辑后的图片存放地址
     */
    public static void startPhotoZoom(Activity activity,Uri uri,int height,int width,Uri destUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("noFaceDetection", true); //关闭人脸检测
        intent.putExtra("return-data", false);//如果设为true则返回bitmap

        intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri);//输出文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 图片裁剪
     * @param fragment
     * @param uri
     * @param height
     * @param width
     */
    public static void startPhotoZoom(Fragment fragment,Uri uri,int height,int width) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", height);
        intent.putExtra("outputY", width);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 获取当前系统时间并格式化
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 制作图片的路径地址
     * @param context
     * @return
     */
    public static String getPath(Context context){
        String path = null;
        File file = null;
        long tag = System.currentTimeMillis();
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            //SDCard是否可用
            path = Environment.getExternalStorageDirectory() + File.separator +"myimages/";
            file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            path = Environment.getExternalStorageDirectory() + File.separator +"myimages/"+ tag + ".png";
        }else{
            path = context.getFilesDir() + File.separator +"myimages/";
            file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            path = context.getFilesDir() + File.separator +"myimages/"+ tag + ".png";
        }
        return path;
    }

    /**
     * 按比例获取bitmap
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    /**
     * 获取原图bitmap
     * @param path
     * @return
     */
    public static Bitmap convertToBitmap2(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        return  BitmapFactory.decodeFile(path, opts);
    }
}