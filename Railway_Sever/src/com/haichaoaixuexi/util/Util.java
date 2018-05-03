package com.haichaoaixuexi.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Util {

	public static String getCurrentDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateNowStr = sdf.format(d);
		return dateNowStr;
	}
	public static String getImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
		//String imgFile = "d:\\111.jpg";// 待处理的图片  
		InputStream in = null;  
		byte[] data = null;  
		// 读取图片字节数组  
		try {  
		in = new FileInputStream(imgFile);  
		data = new byte[in.available()];  
		in.read(data);  
		in.close();  
		} catch (IOException e) {  
		e.printStackTrace();  
		}  
		// 对字节数组Base64编码  
		BASE64Encoder encoder = new BASE64Encoder();  
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串  
		}  
		  
		/** 
		* 将字符串转为图片 
		* @param imgStr 
		* @return 
		*/  
		public static boolean generateImage(String imgStr,String imgFile)throws Exception {// 对字节数组字符串进行Base64解码并生成图片  
		if (imgStr == null) // 图像数据为空  
		return false;  
		BASE64Decoder decoder = new BASE64Decoder();  
		try {  
		// Base64解码  
		byte[] b = decoder.decodeBuffer(imgStr);  
		for (int i = 0; i < b.length; ++i) {  
		if (b[i] < 0) {// 调整异常数据  
		b[i] += 256;  
		}  
		}  
		// 生成jpeg图片  
		String imgFilePath = imgFile;// 新生成的图片  
		OutputStream out = new FileOutputStream(imgFilePath);  
		out.write(b);  
		out.flush();  
		out.close();  
		return true;  
		} catch (Exception e) {  
		throw e;  
		}  
		}  
/*		public static void main(String[] args) {
			String string = getImageStr("D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\1522744940114.png");
			System.out.println(string);
			try {
				generateImage(string, "D:\\1.png");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
}
