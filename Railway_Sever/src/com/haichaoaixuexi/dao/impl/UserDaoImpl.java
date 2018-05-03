package com.haichaoaixuexi.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.UserDao;
import com.haichaoaixuexi.entity.Users;
import com.haichaoaixuexi.util.BeanUtil;
import com.haichaoaixuexi.util.DBUtil;

public class UserDaoImpl extends DBUtil implements UserDao {

	@Override
	public Users checkLogin(String uname,String upwd) {
		// TODO Auto-generated method stub
		Users user = null;
		String sql = "SELECT * FROM users,role,group_form WHERE USER_NUM = ? AND USER_PWD = ? AND users.ROLE_ID = role.ROLE_ID AND users.GROUP_ID = group_form.GROUP_ID";
		ResultSet resultSet = super.executeQuery(sql, uname,upwd);
		try {
			if (resultSet.next()) {
				try {
					user = (Users) BeanUtil.autoBean(Users.class, resultSet);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
/*	public static void main(String[] args) {
		UserDaoImpl test = new UserDaoImpl();
		Users u = new Users();
		u.setUSER_NAME("hl");
		u.setUSER_PWD("123");
		u.setUSER_ID(3);
		u.setUSER_TEL("12645");
		u.setUSER_ADDR("asdf");
		Gson gson = new Gson();
		System.out.println(test.checkLogin("199509", "81dc9bdb52d04dc20036dbd8313ed055").getUSER_SEX());
	}*/

	@Override
	public Users updateUser(Users users) {
		// TODO Auto-generated method stub
		String sql = "update users set USER_PWD = ?,USER_TEL = ?,USER_ADDR = ? where USER_ID = ?";
		int res = super.executeUpdate(sql, users.getUSER_PWD(),users.getUSER_TEL(),users.getUSER_ADDR(),users.getUSER_ID());
		Users user = null;
		if (res>0) {
			sql = "SELECT * FROM users,role,group_form WHERE users.USER_ID = ? AND users.ROLE_ID = role.ROLE_ID AND users.GROUP_ID = group_form.GROUP_ID";
			ResultSet resultSet = super.executeQuery(sql, users.getUSER_ID());
			try {
				if (resultSet.next()) {
					try {
						user = (Users) BeanUtil.autoBean(Users.class, resultSet);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return user;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}

	@Override
	public Users getUserByPhone(String uphone) {
		// TODO Auto-generated method stub
		Users user = null;
		String sql = "SELECT * FROM users,role,group_form WHERE USER_TEL = ? AND users.ROLE_ID = role.ROLE_ID AND users.GROUP_ID = group_form.GROUP_ID";
		ResultSet resultSet = super.executeQuery(sql, uphone);
		try {
			if (resultSet.next()) {
				try {
					user = (Users) BeanUtil.autoBean(Users.class, resultSet);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<Users> getUsersByGroup(int gid) {
		// TODO Auto-generated method stub
		List<Users> list = new ArrayList<>();
		Users user = null;
		String sql = "SELECT * FROM users,role WHERE users.ROLE_ID = role.ROLE_ID AND users.GROUP_ID = ?";
		ResultSet resultSet = super.executeQuery(sql, gid);
		try {
			while (resultSet.next()) {
				try {
					user = (Users) BeanUtil.autoBean(Users.class, resultSet);
					list.add(user);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
