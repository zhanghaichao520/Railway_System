package com.haichaoaixuexi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.UserDao;
import com.haichaoaixuexi.dao.impl.UserDaoImpl;
import com.haichaoaixuexi.entity.Users;

/**
 * Servlet implementation class UpdateUserInfo
 */
@WebServlet("/UpdateUser.do")
public class UpdateUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

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
		ServletInputStream is = request.getInputStream();
        StringBuilder sb = new StringBuilder();
        int len = 0;
        byte[] buf = new byte[1024];
        while((len=is.read(buf))!=-1){
            sb.append(new String(buf,0,len,"utf-8"));
        }

        System.out.println(sb.toString());
		String userString = sb.toString();
		Gson gson = new Gson();
		PrintWriter out = null;
		/**
		 * 返回值
		 */
		try {
			out = response.getWriter();
			/**
			 * 业务逻辑
			 */
			UserDao udi = new UserDaoImpl();
			Users u = udi.updateUser(gson.fromJson(userString, Users.class));
			if (u!=null) {
				out.println(gson.toJson(u));
			}else {
				out.print("failed");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
