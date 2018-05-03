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
import com.haichaoaixuexi.dao.TaskDao;
import com.haichaoaixuexi.dao.impl.TaskDaoImpl;
import com.haichaoaixuexi.entity.Task;

/**
 * Servlet implementation class TaskDivide
 */
@WebServlet("/TaskDivide.do")
public class TaskDivide extends HttpServlet {
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
		// TODO Auto-generated method stub.
		/**
		 * 设置编码方式
		 */
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/**
		 * 获取参数
		 */
		String action = request.getParameter("action");
		
		PrintWriter out = null;
		out = response.getWriter();
		TaskDao tdi = new TaskDaoImpl();
		if (action != null && action.equals("getTask")) {
			/**
			 * 业务逻辑
			 */
			String USER_ID = request.getParameter("uid");
			List<Task> tasks = tdi.getCheckTaskByUser(Integer.parseInt(USER_ID));
			if (tasks!=null) {
				Gson gson = new Gson();
				out.println(gson.toJson(tasks));
				System.out.println(gson.toJson(tasks));
			}else {
				out.print("failed");
			}
		}
		if (action != null && action.equals("divTask")) {
			/**
			 * 业务逻辑
			 */
			String uidString = request.getParameter("uid");
			String tidString = request.getParameter("tid");
			boolean res = tdi.updateTaskUser(Integer.parseInt(tidString), Integer.parseInt(uidString));
			if (res) {
				out.print("success");
			}else {
				out.print("failed");
			}
		}
	}

}
