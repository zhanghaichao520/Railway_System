package com.haichaoaixuexi.railway_system_android.data;

import com.haichaoaixuexi.railway_system_android.entity.Users;

/**
 * Created by haichaoaixuexi on 2017/12/24.
 */

public class Const {
    final static public String DBNAME = "railway.db";
    final static public String URL = "http://192.168.1.105:8080/Railway_Sever";

    final static public String URL_LOGIN = URL+"/Login.do";
    final static public String URL_GET_USER_BY_PHONE = URL+"/GetUserByPhone.do";
    final static public String URL_GET_USER_BY_GROUP = URL+"/GetUsersByGroup.do";
    final static public String URL_UPDATEUSER = URL+"/UpdateUser.do";
    final static public String URL_GET_EQUIPMENT = URL+"/GetEquiment.do";
    final static public String URL_DATA_SYNC= URL+"/DataSync.do";
    final static public String URL_TASK_DIVIDE= URL+"/TaskDivide.do";
    final static public String URL_FEEDBACK= URL+"/FeedBack.do";
    final static public String URL_EQ_CHECK= URL+"/Eq_checkForAndroid.do";

    final static public long TIMEOUT = 5000;

    static public Users currentuser = null;
    final static public String MESSAGE_LOGHIN_ERROR = "登陆出错";
    final static public String MESSAGE_LOGHIN_FAILED = "用户名|密码错误";
    final static public String MESSAGE_LOGHIN_SUCCESS = "登陆成功";
    final static public String MESSAGE_USER_NO_EXIST = "用户不存在";
    final static public int REQUEST_CODE_SCAN = 0;
    final static public char TYPE_USER = 'u';
    final static public char TYPE_EQUIMENT = 'e';
}
