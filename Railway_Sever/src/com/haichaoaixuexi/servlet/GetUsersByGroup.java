package com.haichaoaixuexi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.UserDao;
import com.haichaoaixuexi.dao.impl.UserDaoImpl;
import com.haichaoaixuexi.entity.Users;

/**
 * Servlet implementation class GetUsersByGroup
 */
@WebServlet("/GetUsersByGroup.do")
public class GetUsersByGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
		String gidString = request.getParameter("gid");
		
		PrintWriter out = null;
		out = response.getWriter();
		/**
		 * 返回值
		 */
		try {
			/**
			 * 业务逻辑
			 */
			UserDao udi = new UserDaoImpl();
			List<Users> users = udi.getUsersByGroup(Integer.parseInt(gidString));
			if (users!=null) {
				Gson gson = new Gson();
				out.println(gson.toJson(users));
				System.out.println(gson.toJson(users));
			}else {
				out.print("failed");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
