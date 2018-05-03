package com.haichaoaixuexi.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.haichaoaixuexi.entity.Eq_issue_kind;
import com.haichaoaixuexi.entity.Equipment;

public interface EquipmentDao {
	public List<Equipment> getEquimentByGroup(int GROUP_ID);
	public Equipment getEquimentById(int EQUIP_ID) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException;
	public List<Eq_issue_kind> getAllEqIssues();
	public boolean updateEqStatus(int SBBH,int SBZT);
}
