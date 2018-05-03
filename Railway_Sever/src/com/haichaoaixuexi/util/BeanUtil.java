package com.haichaoaixuexi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSetMetaData;
/**
 * resultSet映射javabean
 * @author haichaoaixuexi
 *
 */
public class BeanUtil {
    /**
     * 
     * @param clazz 传入一个需要封装的类
     * @param rs    需要进行封装的rs
     * @return 一个封装好的VO（object）
     */
    public static Object autoBean(Class<?> clazz,ResultSet rs) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        ArrayList<Method> setMethods = new ArrayList<Method>(); //用来存取set方法的集合
        Object obj = clazz.newInstance(); // 实例化对象
        try {
            Method[] method = clazz.getMethods();//对象中的所有方法
            for(Method m : method){
                if(m.getName().substring(0, 3).contains("set"))//截取前面3个英文字符，看是否为set，如果是暂定为set方法
                    setMethods.add(m);//将set方法加入集合
            }
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            int columCount = rsmd.getColumnCount();    //获取结果中列的个数
            //循环，目的为将每一列的值用正确的set方法
            for(int i = 1;i<=columCount;i++){
                String columName = rsmd.getColumnName(i);//获取列名
                String field = columName.replaceFirst(columName.substring(0, 1),
                        columName.substring(0, 1).toUpperCase());//因为set方法都是这种形式，setXxxx，因此将列名第一个字母大写
                for(Method m1:setMethods){//遍历set方法集合
                    if(m1.getName().equals("set"+field)){//判断，如果通过判断既可知道该用对象的什么set方法
                        Class<?> para[] = m1.getParameterTypes();//获取方法参数类型，判断是否为为setBoolean
                        if(para[0].getName().equals("boolean")){
                            if(rs.getObject(columName).equals(0)){//因为mysql的boolean是以0,1存在，所以做以下判断
                                m1.invoke(obj,false);
                            }else{
                                m1.invoke(obj, true);
                            }
                        }else{//如果不是setBoolean这种，则执行一下
                            //System.out.println(columName+":"+rs.getObject(columName));
                            m1.invoke(obj,rs.getObject(columName));//通过反射调用方法
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;//返回对象
    }
}