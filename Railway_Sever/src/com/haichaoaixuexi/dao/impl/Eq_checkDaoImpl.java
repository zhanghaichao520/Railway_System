package com.haichaoaixuexi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.haichaoaixuexi.dao.Eq_checkDao;
import com.haichaoaixuexi.entity.Eq_check;
import com.haichaoaixuexi.util.DBUtil;

public class Eq_checkDaoImpl extends DBUtil implements Eq_checkDao {

	@Override
	public boolean save(Eq_check ec) {
		// TODO Auto-generated method stub
		Connection conn = super.getConn();
		String sql = "INSERT INTO eq_check VALUES (null,?,?,?,?,?,?,?,?,?,?)";
		String sql1 = "UPDATE eq_op_equipment SET SBZT=? WHERE EQUIP_ID=?";
		String sql2 = "UPDATE task SET TASK_PROCESS=? WHERE SBBH=?";
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, ec.getJCLX());
			ps.setInt(2, ec.getSBBH());
			ps.setInt(3, ec.getGZLX());
			ps.setString(4, ec.getGZMS());
			ps.setInt(5, ec.getBXR());
			ps.setTimestamp(6, ec.getBXSJ());
			ps.setInt(7, ec.getYSR());
			ps.setTimestamp(8, ec.getYSSJ());
			ps.setString(9, ec.getGZTP());
			ps.setString(10, ec.getABC());
			ps.executeUpdate();

			ps = conn.prepareStatement(sql1);
			ps.setInt(1, 1);
			ps.setInt(2, ec.getSBBH());
			ps.executeUpdate();
			
			ps = conn.prepareStatement(sql2);
			ps.setString(1, "待修");
			ps.setInt(2, ec.getSBBH());
			ps.executeUpdate();

			conn.commit();
			ps.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();// 只要有一个sql语句出现错误，则将事务回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

}
