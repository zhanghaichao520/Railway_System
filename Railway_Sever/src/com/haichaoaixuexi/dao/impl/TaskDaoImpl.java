package com.haichaoaixuexi.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import com.haichaoaixuexi.dao.TaskDao;
import com.haichaoaixuexi.entity.Equipment;
import com.haichaoaixuexi.entity.Task;
import com.haichaoaixuexi.util.BeanUtil;
import com.haichaoaixuexi.util.DBUtil;

public class TaskDaoImpl extends DBUtil implements TaskDao{

	@Override
	public List<Task> getCheckTaskByUser(int USER_ID) {
		// TODO Auto-generated method stub
		List<Task> tasks = new ArrayList<Task>();
		Task task = null;
		String sql = "SELECT * FROM task WHERE TASK_USER = ?";
		ResultSet resultSet = super.executeQuery(sql, USER_ID);
		try {
			while (resultSet.next()) {
				try {
					task = (Task) BeanUtil.autoBean(Task.class, resultSet);
					tasks.add(task);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}
/*	public static void main(String[] args) {
		TaskDaoImpl tdi = new TaskDaoImpl();
		
		System.out.println(tdi.updateTaskUser(1, 4));
	}*/

	@Override
	public boolean updateTaskUser(int tid,int uid) {
		// TODO Auto-generated method stub
		String sql = "UPDATE task SET TASK_USER=? WHERE TASK_ID=?";
		int res = super.executeUpdate(sql, uid,tid);
		if (res!=0) {
			return true;
		}
		return false;
	}
}
