package com.haichaoaixuexi.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.haichaoaixuexi.dao.EquipmentDao;
import com.haichaoaixuexi.entity.Eq_issue_kind;
import com.haichaoaixuexi.entity.Equipment;
import com.haichaoaixuexi.util.BeanUtil;
import com.haichaoaixuexi.util.DBUtil;

public class EquipmentDaoImpl extends DBUtil implements EquipmentDao{

	@Override
	public List<Equipment> getEquimentByGroup(int GROUP_ID) {
		// TODO Auto-generated method stub
		List<Equipment> equipments = new ArrayList<Equipment>();
		Equipment equipment = null;
		String sql = "SELECT * FROM eq_op_equipment,group_form,eq_status "+
					 "WHERE eq_op_equipment.SBZT = eq_status.EQ_STATUS_ID "+
					 "AND eq_op_equipment.SSCJ = group_form.GROUP_ID "+
					 "AND group_form.GROUP_ID = ?";
		ResultSet resultSet = super.executeQuery(sql, GROUP_ID);
		try {
			while (resultSet.next()) {
				try {
					equipment = (Equipment) BeanUtil.autoBean(Equipment.class, resultSet);
					equipments.add(equipment);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage().toString());
				}
			}
			return equipments;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return equipments;
	}
/* 	public static void main(String[] args) {
		EquipmentDaoImpl equipmentDaoImpl = new EquipmentDaoImpl();
		Equipment equipment = equipmentDaoImpl.getEquimentById(2);
		
		List<Eq_issue_kind> issues = equipmentDaoImpl.getAllEqIssues();
		System.out.println(new Gson().toJson(issues));
	}
*/
	@Override
	public Equipment getEquimentById(int EQUIP_ID) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		// TODO Auto-generated method stub
		Equipment equipment = null;
		String sql = "SELECT * FROM eq_op_equipment,group_form,eq_status "+
				     "WHERE eq_op_equipment.SBZT = eq_status.EQ_STATUS_ID "+
				     "AND eq_op_equipment.SSCJ = group_form.GROUP_ID "+
				     "AND EQUIP_ID = ?";
		ResultSet resultSet = super.executeQuery(sql, EQUIP_ID);
		try {
			if (resultSet.next()) {
				equipment = (Equipment) BeanUtil.autoBean(Equipment.class, resultSet);
			
			}
			return equipment;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return equipment;
	}

	@Override
	public List<Eq_issue_kind> getAllEqIssues() {
		// TODO Auto-generated method stub
		List<Eq_issue_kind> issues = new ArrayList<Eq_issue_kind>();
		Eq_issue_kind issue = null;
		String sql = "SELECT * FROM eq_issue_kind ";
		ResultSet resultSet = super.executeQuery(sql);
		try {
			while (resultSet.next()) {
				try {
					issue = (Eq_issue_kind) BeanUtil.autoBean(Eq_issue_kind.class, resultSet);
					issues.add(issue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return issues;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return issues;
	}
	@Override
	public boolean updateEqStatus(int SBBH, int SBZT) {
		String sql = "UPDATE eq_op_equipment SET SBZT=? WHERE EQUIP_ID=?";
		int res = super.executeUpdate(sql, SBZT,SBBH);
		if (res!=0) {
			return true;
		}
		return false;
	}
}
