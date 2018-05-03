package com.haichaoaixuexi.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.Eq_checkDao;
import com.haichaoaixuexi.dao.impl.Eq_checkDaoImpl;
import com.haichaoaixuexi.entity.Eq_check;

/**
 * Servlet implementation class Eq_checkForAndroid
 */
@WebServlet("/Eq_checkForAndroid.do")
public class Eq_checkForAndroid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		String picpath = "";
		String file = request.getSession().getServletContext().getRealPath("upload");
		System.out.println("file:" + file);
		// 创建文件工厂对象
		DiskFileItemFactory df = new DiskFileItemFactory();
		// 设置缓存大小
		df.setSizeThreshold(4096);
		// 创建上传处理对象
		ServletFileUpload su = new ServletFileUpload(df);
		try {
			// 得到所有的表单对象
			List<FileItem> list = su.parseRequest(request);
			System.out.println("表单个数：" + list.size());

			for (FileItem fileItem : list) {
				if (fileItem.isFormField()) {
					// 处理普通表单字段
					System.out.println(fileItem.getFieldName() + fileItem.getString("utf-8"));
					if ("bean".equals(fileItem.getFieldName())) {
						Gson gson = new Gson();
                        ec = gson.fromJson(fileItem.getString("utf-8"), Eq_check.class);
					}
				} else {
					// 处理非普通表单字段
					String picname = fileItem.getName();
					// 截取字符串,获取最后一个\的位置
					int i = file.lastIndexOf("\\");
					// upload\name.jpg
					picpath = file.substring(i + 1, file.length()) + "\\" + picname;
					System.out.println("picname:" + picname);
					//写入文件
					fileItem.write(new File(file, picname));
					ec.setGZTP(picname);
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			out.println("filewritefailed");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.println("unknownfailed");
			e.printStackTrace();
		}
		if (ec!=null) {
			ec.setYSR(0);
			if (ecd.save(ec)) {
				out.println("success");
			}else {
				out.println("database error");
			}
		}
		
	}

}
