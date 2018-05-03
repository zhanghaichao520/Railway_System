package com.haichaoaixuexi.dao;

import java.util.List;

import com.haichaoaixuexi.entity.Eq_check;

public interface Eq_checkDao {
	public boolean save(Eq_check ec);
	public boolean updateEc(Eq_check ec);
	public Eq_check getOneEcBySBBH(int SBBH);
	public List<Eq_check> getAllEcBySBBH(int SBBH);
}
