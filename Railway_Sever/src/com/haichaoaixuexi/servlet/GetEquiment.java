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
import com.haichaoaixuexi.dao.EquipmentDao;
import com.haichaoaixuexi.dao.impl.EquipmentDaoImpl;
import com.haichaoaixuexi.entity.Equipment;

/**
 * Servlet implementation class GetEquimentByUser
 */
@WebServlet("/GetEquiment.do")
public class GetEquiment extends HttpServlet {
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
		String action = request.getParameter("action");
		
		PrintWriter out = null;
		out = response.getWriter();
		EquipmentDao equipmentDaoImpl = new EquipmentDaoImpl();
		if (action != null && action.equals("bygid")) {
			/**
			 * 业务逻辑
			 */
			try {
				String gid = request.getParameter("gid");
				List<Equipment> equipments = equipmentDaoImpl.getEquimentByGroup(Integer.parseInt(gid));
				if (equipments!=null) {
					Gson gson = new Gson();
					out.println(gson.toJson(equipments));
					//System.out.println(equipments.size());
				}else {
					out.print("failed");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		if (action != null && action.equals("byid")) {
			/**
			 * 业务逻辑
			 */
			try {
				String id = request.getParameter("id");
				Equipment equipment = equipmentDaoImpl.getEquimentById(Integer.parseInt(id));
				if (equipment!=null) {
					Gson gson = new Gson();
					out.println(gson.toJson(equipment));
					System.out.println(gson.toJson(equipment));
				}else {
					out.print("failed");
				}
			} catch (Exception e) {
				// TODO: handle exception
				out.print("failed_not_allow");
			}
		}
	}

}
