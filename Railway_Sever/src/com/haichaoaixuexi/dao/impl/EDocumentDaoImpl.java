package com.haichaoaixuexi.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.EDocumentDao;
import com.haichaoaixuexi.entity.EDocument;
import com.haichaoaixuexi.entity.Equipment;
import com.haichaoaixuexi.util.BeanUtil;
import com.haichaoaixuexi.util.DBUtil;

public class EDocumentDaoImpl extends DBUtil implements EDocumentDao{

	@Override
	public List<EDocument> getAllEDocument() {
		// TODO Auto-generated method stub
		List<EDocument> documents = new ArrayList<>();
		EDocument document= null;
		String sql ="SELECT ED_ID,TITLE,UID,CONTENT,CJRQ,USER_NAME FROM electronic_document,users WHERE electronic_document.UID=users.USER_ID";
		ResultSet resultSet = super.executeQuery(sql);
		try {
			while (resultSet.next()) {
				try {
					document = (EDocument) BeanUtil.autoBean(EDocument.class, resultSet);
					documents.add(document);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
			return documents;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return documents;
	}
	/*public static void main(String[] args) {
		EDocumentDao edi = new EDocumentDaoImpl();
		Gson gson =new Gson();
		System.out.println(gson.toJson(edi.getAllEDocument()));
	}*/
}
