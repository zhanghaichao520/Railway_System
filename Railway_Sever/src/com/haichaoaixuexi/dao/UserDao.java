/**
 * 
 */
package com.haichaoaixuexi.dao;

import java.util.List;

import com.haichaoaixuexi.entity.Users;

public interface UserDao {

	public Users checkLogin(String uname,String upwd);
	public Users getUserByPhone(String uphone);
	public List<Users> getUsersByGroup(int gid);
	public Users updateUser(Users users);
	public int getGroupUserById(int uid);
	public Users getUserById(int uid);
}
