package com.haichaoaixuexi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.Eq_checkDao;
import com.haichaoaixuexi.dao.EquipmentDao;
import com.haichaoaixuexi.dao.impl.Eq_checkDaoImpl;
import com.haichaoaixuexi.dao.impl.EquipmentDaoImpl;
import com.haichaoaixuexi.entity.Eq_check;
import com.haichaoaixuexi.util.Util;

/**
 * Servlet implementation class getEq_checkForAndroid
 */
@WebServlet("/GetEq_checkForAndroid.do")
public class GetEq_checkForAndroid extends HttpServlet {
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

		PrintWriter out = null;
		out = response.getWriter();
		Eq_checkDao ecd = new Eq_checkDaoImpl();
		Eq_check ec = null;
		Gson gson =new Gson();
		/**
		 * 获取参数
		 */
		String action = request.getParameter("action");
		
		if (action != null && action.equals("getOne")) {
			int SBBH = Integer.parseInt(request.getParameter("SBBH"));
			ec = ecd.getOneEcBySBBH(SBBH);
			if (ec!=null) {
				ec.setImgString(Util.getImageStr("D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\"+ec.getGZTP()));
				out.println(gson.toJson(ec));
			}else {
				out.println("failed");
			}
		}else if (action != null && action.equals("update")) {
			String YSR = request.getParameter("YSR");
			String YSSJ = request.getParameter("YSSJ");
			String YSJG = request.getParameter("YSJG");
			String ID = request.getParameter("ID");
			String SBBH = request.getParameter("SBBH");
			ec = new Eq_check();
			ec.setYSR(Integer.parseInt(YSR));
			ec.setYSSJ(Timestamp.valueOf(YSSJ));
			ec.setYSJG(YSJG);
			ec.setEQ_CHECK_ID(Integer.parseInt(ID));
			ec.setSBBH(Integer.parseInt(SBBH));
			if (ecd.updateEc(ec) && ec != null) {
				out.println("success");
			} else {
				out.println("database error");
			}
		}else if (action != null && action.equals("getAllBySBBH")) {
			int SBBH = Integer.parseInt(request.getParameter("SBBH"));
			List<Eq_check> ecs = new ArrayList<>();
			ecs = ecd.getAllEcBySBBH(SBBH);
			if (ecs!=null&&ecs.size()!=0) {
				for (Eq_check eq_check : ecs) {
					if (eq_check.getYSR()==0) {
						ecs.remove(eq_check);
					}
					eq_check.setImgString(Util.getImageStr("D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\"+eq_check.getGZTP()));
				}
				out.println(gson.toJson(ecs));
			}else {
				out.println("sever failed");
			}
		}else if (action != null && action.equals("getAllByUSER")) {
			int USER_ID = Integer.parseInt(request.getParameter("USER_ID"));
			List<Eq_check> ecs = new ArrayList<>();
			ecs = ecd.getAllEcByUSER(USER_ID);
			if (ecs!=null&&ecs.size()!=0) {
				for (Eq_check eq_check : ecs) {
					if (eq_check.getYSR()==0) {
						ecs.remove(eq_check);
					}
					eq_check.setImgString(Util.getImageStr("D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\"+eq_check.getGZTP()));
				}
				out.println(gson.toJson(ecs));
			}else {
				out.println("sever failed");
			}
		}else if (action != null && action.equals("getAllByYSSJ")) {
			String YSSJ = request.getParameter("YSSJ");
			int gid = Integer.parseInt(request.getParameter("gid"));
			List<Eq_check> ecs = new ArrayList<>();
			ecs = ecd.getAllEcByYSSJ(YSSJ,gid);
			if (ecs!=null&&ecs.size()!=0) {
				for (Eq_check eq_check : ecs) {
					if (eq_check.getYSR()==0) {
						ecs.remove(eq_check);
					}
					eq_check.setImgString(Util.getImageStr("D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\"+eq_check.getGZTP()));
				}
				out.println(gson.toJson(ecs));
			}else {
				out.println("sever failed");
			}
		}

	}

}
