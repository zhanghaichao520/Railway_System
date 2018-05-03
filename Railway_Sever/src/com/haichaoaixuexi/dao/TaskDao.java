package com.haichaoaixuexi.dao;

import java.util.List;

import com.haichaoaixuexi.entity.Task;

public interface TaskDao {
	public List<Task> getCheckTaskByUser(int USER_ID);
	public boolean updateTaskUser(int tid,int uid);
		
}
