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
import com.haichaoaixuexi.dao.EDocumentDao;
import com.haichaoaixuexi.dao.EquipmentDao;
import com.haichaoaixuexi.dao.impl.EDocumentDaoImpl;
import com.haichaoaixuexi.dao.impl.EquipmentDaoImpl;
import com.haichaoaixuexi.entity.EDocument;
import com.haichaoaixuexi.entity.Eq_issue_kind;

/**
 * Servlet implementation class EDocumentForAndroid
 */
@WebServlet("/EDocumentForAndroid.do")
public class EDocumentForAndroid extends HttpServlet {
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
		EDocumentDao equipmentDaoImpl = new EDocumentDaoImpl();
		if (action != null && action.equals("getAll")) {
			List<EDocument> documents = equipmentDaoImpl.getAllEDocument();
			if (documents!=null) {
				Gson gson = new Gson();
				out.println(gson.toJson(documents));
				//System.out.println(gson.toJson(documents));
			}else {
				out.print("failed");
			}
		}
	}

}
