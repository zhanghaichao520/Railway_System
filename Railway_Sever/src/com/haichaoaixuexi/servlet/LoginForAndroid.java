package com.haichaoaixuexi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.UserDao;
import com.haichaoaixuexi.dao.impl.UserDaoImpl;
import com.haichaoaixuexi.entity.Users;
import com.haichaoaixuexi.util.MD5;

/**
 * Servlet implementation class LoginForAndroid
 */
@WebServlet("/Login.do")
public class LoginForAndroid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/**
		 * 设置编码方式
		 */
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/**
		 * 获取参数
		 */
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		MD5 md5 = new MD5();
		upwd = md5.getMD5ofStr(upwd);
		System.out.println(uname+":"+upwd);
		PrintWriter out = null;
		/**
		 * 返回值
		 */
		//adb -d shell "run-as com.haichaoaixuexi.railway_system_android cat /data/data/com.haichaoaixuexi.railway_system_android/databases/railway.db > /sdcard/railway.db"
		try {
			out = response.getWriter();
			/**
			 * 业务逻辑
			 */
			UserDao udi = new UserDaoImpl();
			Users u = udi.checkLogin(uname, upwd);
			if (u!=null) {
				Gson gson = new Gson();
				out.println(gson.toJson(u));
				System.out.println(gson.toJson(u));
			}else {
				out.print("failed");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
