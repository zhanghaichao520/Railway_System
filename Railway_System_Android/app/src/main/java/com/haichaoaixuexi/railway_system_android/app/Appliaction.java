package com.haichaoaixuexi.railway_system_android.app;

import android.app.Application;

import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.greendao.DaoMaster;
import com.haichaoaixuexi.railway_system_android.greendao.DaoSession;
import com.lzy.okgo.OkGo;
import com.mob.MobSDK;
import com.zwy.kutils.KUtilLibs;
import com.zwy.kutils.http.HttpBuild;
import com.zwy.kutils.utils.Log;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.haichaoaixuexi.railway_system_android.data.Const.DBNAME;


/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe:app初始化
 */

public class Appliaction extends Application {
    DaoSession mDaoSession = null;
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        KUtilLibs.init(true, "海超的日志",this,new HttpBuild.Build(null, 10, HttpBuild.CookieType.MemoryCookieStore));
        initGreenDao();
        initOkGo();
    }

    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(Const.TIMEOUT, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(Const.TIMEOUT, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(Const.TIMEOUT, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this).setOkHttpClient(builder.build());
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), DBNAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        Log.d("GreenDao初始化成功");
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
