package com.haichaoaixuexi.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.batch.Main;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.Eq_checkDao;
import com.haichaoaixuexi.dao.EquipmentDao;
import com.haichaoaixuexi.dao.UserDao;
import com.haichaoaixuexi.entity.EDocument;
import com.haichaoaixuexi.entity.Eq_check;
import com.haichaoaixuexi.entity.Users;
import com.haichaoaixuexi.util.BeanUtil;
import com.haichaoaixuexi.util.DBUtil;
import com.haichaoaixuexi.util.Util;

import sun.net.www.content.image.gif;

public class Eq_checkDaoImpl extends DBUtil implements Eq_checkDao {
	/**
	 * 设备点巡检结果保存 1.巡检记录保存到数据库 2.更新设备状态 3.更新任务进度 4.更新任务负责人到包机组长
	 */
	@Override
	public boolean save(Eq_check ec) {
		// TODO Auto-generated method stub
		Connection conn = super.getConn();
		String sql = "INSERT INTO eq_check VALUES (null,?,?,?,?,?,?,?,?,?,?,?)";
		String sql1 = "UPDATE eq_op_equipment SET SBZT=? WHERE EQUIP_ID=?";
		String sql2 = "UPDATE task SET TASK_PROCESS=? ,TASK_USER=? WHERE SBBH=?";
		if (ec.getJCLX().equals("巡检")) {
			sql2 = "INSERT INTO task VALUES (NULL,?,?,?,?,?)";
		}
		UserDao udi = new UserDaoImpl();
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
			ps.setString(11, ec.getYSJG());
			ps.executeUpdate();

			ps = conn.prepareStatement(sql1);
			ps.setInt(1, 1);
			ps.setInt(2, ec.getSBBH());
			ps.executeUpdate();

			ps = conn.prepareStatement(sql2);
			if (ec.getJCLX().equals("巡检")) {
				ps.setInt(1, ec.getSBBH());
				ps.setInt(2, udi.getGroupUserById(ec.getBXR()));
				ps.setString(3, ec.getABC());
				ps.setString(4, "待验收");
				ps.setTimestamp(5, ec.getBXSJ());
			} else {
				ps.setString(1, "待验收");
				ps.setInt(2, udi.getGroupUserById(ec.getBXR()));
				ps.setInt(3, ec.getSBBH());
			}
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

	@Override
	public Eq_check getOneEcBySBBH(int SBBH) {
		// TODO Auto-generated method stub
		Eq_check ec = null;
		String sql = "SELECT * FROM eq_check WHERE SBBH = ? ORDER BY BXSJ DESC LIMIT 1";
		ResultSet resultSet = super.executeQuery(sql, SBBH);
		try {
			if (resultSet.next()) {
				try {
					ec = (Eq_check) BeanUtil.autoBean(Eq_check.class, resultSet);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UserDao udi = new UserDaoImpl();
				ec.setBXR_NAME(udi.getUserById(ec.getBXR()).getUSER_NAME());
				return ec;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ec;
	}
	/*
	 * public static void main(String[] args) { Eq_checkDao ecd = new
	 * Eq_checkDaoImpl(); Eq_check eq_check = ecd.getOneEcBySBBH(180307002);
	 * System.out.println(eq_check.getBXR_NAME()); Gson gson =new Gson();
	 * eq_check.setImgString(Util.getImageStr(
	 * "D:\\工作区\\服务器\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Railway_Sever\\upload\\"
	 * +eq_check.getGZTP()));
	 * System.out.println(gson.toJson(eq_check.getSBBH())); try {
	 * Util.generateImage(eq_check.getImgString(), "D:\\1.png"); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * }
	 */

	@Override
	public boolean updateEc(Eq_check ec) {
		// TODO Auto-generated method stub
		Connection conn = super.getConn();
		String sql = " UPDATE eq_check SET YSR = ?,YSSJ=?,YSJG=? WHERE EQ_CHECK_ID=?";
		String sql1 = "UPDATE eq_op_equipment SET SBZT=? WHERE EQUIP_ID=?";
		String sql2 = "DELETE FROM task WHERE SBBH = ?";
		if (ec.getYSJG().equals("不合格")) {
			sql2 = "UPDATE task SET TASK_PROCESS=? ,TASK_USER=? WHERE SBBH=?";
		}
		UserDao udi = new UserDaoImpl();
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ec.getYSR());
			ps.setTimestamp(2, ec.getYSSJ());
			ps.setString(3, ec.getYSJG());
			ps.setInt(4, ec.getEQ_CHECK_ID());
			ps.executeUpdate();

			ps = conn.prepareStatement(sql1);
			if (ec.getYSJG().equals("不合格")) {
				ps.setInt(1, 2);
			}else {
				ps.setInt(1, 0);
			}
			ps.setInt(2, ec.getSBBH());
			ps.executeUpdate();
			
			ps = conn.prepareStatement(sql2);
			if (ec.getYSJG().equals("不合格")) {
				ps.setString(1, "待检");
				ps.setInt(2, udi.getGroupUserById(ec.getYSR()));
				ps.setInt(3, ec.getSBBH());
			}else {
				ps.setInt(1, ec.getSBBH());
			}
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

	@Override
	public List<Eq_check> getAllEcBySBBH(int SBBH) {
		// TODO Auto-generated method stub
		List<Eq_check> ecs = new ArrayList<>();
		Eq_check ec= null;
		String sql ="SELECT * FROM eq_check WHERE SBBH = ?";
		ResultSet resultSet = super.executeQuery(sql,SBBH);
		UserDao udi = new UserDaoImpl();
		try {
			while (resultSet.next()) {
				try {
					ec = (Eq_check) BeanUtil.autoBean(Eq_check.class, resultSet);
					ec.setBXR_NAME(udi.getUserById(ec.getBXR()).getUSER_NAME());
					ecs.add(ec);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
			return ecs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ecs;
	}
	/*public static void main(String[] args) {
		Eq_checkDao ecd = new Eq_checkDaoImpl();
		
		System.out.println(ecd.getAllEcByYSSJ("2017-04-22 17:34").size());
	}*/

	@Override
	public List<Eq_check> getAllEcByUSER(int USER_ID) {
		// TODO Auto-generated method stub
		List<Eq_check> ecs = new ArrayList<>();
		Eq_check ec= null;
		String sql ="SELECT * FROM eq_check WHERE BXR = ? OR YSR = ?";
		ResultSet resultSet = super.executeQuery(sql,USER_ID,USER_ID);
		UserDao udi = new UserDaoImpl();
		try {
			while (resultSet.next()) {
				try {
					ec = (Eq_check) BeanUtil.autoBean(Eq_check.class, resultSet);
					ec.setBXR_NAME(udi.getUserById(ec.getBXR()).getUSER_NAME());
					ecs.add(ec);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
			return ecs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ecs;
	}

	@Override
	public List<Eq_check> getAllEcByYSSJ(String YSSJ,int gid) {
		// TODO Auto-generated method stub
		List<Eq_check> ecs = new ArrayList<>();
		Eq_check ec= null;
		String sql ="SELECT * FROM eq_check WHERE YSSJ>?";
		ResultSet resultSet = super.executeQuery(sql,YSSJ);
		UserDao udi = new UserDaoImpl();
		EquipmentDao edi = new EquipmentDaoImpl();
		try {
			while (resultSet.next()) {
				try {
					ec = (Eq_check) BeanUtil.autoBean(Eq_check.class, resultSet);
					ec.setBXR_NAME(udi.getUserById(ec.getBXR()).getUSER_NAME());
					if (edi.getEquimentById(ec.getSBBH()).getGROUP_ID()==gid) {
						ecs.add(ec);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
			return ecs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ecs;
	}
}
