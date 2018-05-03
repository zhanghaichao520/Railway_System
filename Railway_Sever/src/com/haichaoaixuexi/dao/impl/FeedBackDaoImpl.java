package com.haichaoaixuexi.dao.impl;

import java.sql.ResultSet;

import com.haichaoaixuexi.dao.FeedBackDao;
import com.haichaoaixuexi.entity.Feedback;
import com.haichaoaixuexi.util.DBUtil;

public class FeedBackDaoImpl extends DBUtil implements FeedBackDao {

	@Override
	public boolean save(Feedback fb) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO feedback VALUES (null,?,?)";
		int res= super.executeUpdate(sql, fb.getCONTENT(),fb.getFKR());
		if (res!=0) {
			return true;
		}
		return false;
	}
/*	public static void main(String[] args) {
		Feedback feedback = new Feedback();
		feedback.setCONTENT("Ðè¸Ä½ø");
		feedback.setFKR(3);
		FeedBackDaoImpl fdi = new FeedBackDaoImpl();
		System.out.println(fdi.save(feedback));
	}*/
}
