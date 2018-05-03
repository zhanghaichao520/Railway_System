package com.haichaoaixuexi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	private static final String URL="jdbc:mysql://localhost:3306/railwaysystem?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	private static final String USER="root";
	private static final String PASSWORD="123456";
	//建立连接
	public Connection getConn() {
		// 建立连接
		Connection conn =null;
		try {
			// 1、加载驱动类
			
				Class.forName("com.mysql.jdbc.Driver");
				// 2、建立连接
				conn = DriverManager.getConnection(URL,USER,PASSWORD);
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//增删改
	public int executeUpdate(String sql,Object...params){
		int num=0;
		//建立连接
		Connection conn=  getConn();
		PreparedStatement pstmt=null;
		try {
			//创建PreparedStatement对象
			pstmt= conn.prepareStatement(sql);
			//给占位符赋值
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(i+1, params[i]);
				}
			}			
			//执行增删改操作
			num=pstmt.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				//关闭接口对象
				if(pstmt!=null){
					pstmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return num;
	}	
	
	
	//查询
	public ResultSet executeQuery(String sql,Object...params){
		//建立连接
		Connection conn= getConn();
		ResultSet rst=null;
		try {
			//创建preparedStatemet对象
			PreparedStatement pstmt= conn.prepareStatement(sql);
			//给占位符赋值
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(i+1, params[i]);
				}
			}	
			//执行查询
			rst= pstmt.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rst;
	}	
}
