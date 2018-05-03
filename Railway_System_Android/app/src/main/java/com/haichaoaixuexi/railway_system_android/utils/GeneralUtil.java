package com.haichaoaixuexi.railway_system_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haichaoaixuexi on 2017/12/24.
 */

public class GeneralUtil {
    public static void toast(Context context,String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    /**
     * 界面转换
     *
     * @param context
     * @param des
     */
    public static void startActivity(Context context, Class des) {
        Intent intent = new Intent();
        intent.setClass(context, des);
        context.startActivity(intent);
        // 关闭源Activity
        ((Activity) context).finish();
    }
    /**
     * 手机号合法性判断
     * @param telNum
     * @return
     */
    public static boolean isMobiPhoneNum(String telNum){
        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    public static Uri getImageStreamFromExternal(String imageName) {
        File externalPubPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );

        File picPath = new File(externalPubPath, imageName);
        Uri uri = null;
        if(picPath.exists()) {
            uri = Uri.fromFile(picPath);
        }

        return uri;
    }
}
