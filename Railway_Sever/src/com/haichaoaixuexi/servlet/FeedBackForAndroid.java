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
import com.google.gson.JsonSyntaxException;
import com.haichaoaixuexi.dao.FeedBackDao;
import com.haichaoaixuexi.dao.impl.FeedBackDaoImpl;
import com.haichaoaixuexi.entity.Feedback;

/**
 * Servlet implementation class FeedBack
 */
@WebServlet("/FeedBack.do")
public class FeedBackForAndroid extends HttpServlet {
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
		String fbString = sb.toString();
		PrintWriter out = null;
		out = response.getWriter();
		Gson gson = new Gson();
		try {
			/**
			 * 业务逻辑
			 */
			FeedBackDao fdi = new FeedBackDaoImpl();
			boolean res = fdi.save(gson.fromJson(fbString, Feedback.class));
			if (res) {
				out.print("success");
			}else {
				out.print("failed");
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			out.print("failed");
			e.printStackTrace();
		}
		
	}

}
