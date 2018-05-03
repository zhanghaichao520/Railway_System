package com.haichaoaixuexi.entity;

import java.sql.Timestamp;

public class Task {
	
    private int TASK_ID;
    private int SBBH;
    private int TASK_USER;
    private String ABC;
    private String TASK_PROCESS;
    private Timestamp TIME;
    public int getTASK_ID(){
        return this.TASK_ID;
    }
    public void setTASK_ID(int TASK_ID){
        this.TASK_ID = TASK_ID;
    }
    public int getSBBH(){
        return this.SBBH;
    }
    public void setSBBH(int SBBH){
        this.SBBH = SBBH;
    }
    public int getTASK_USER(){
        return this.TASK_USER;
    }
    public void setTASK_USER(int TASK_USER){
        this.TASK_USER = TASK_USER;
    }
    public String getABC(){
        return this.ABC;
    }
    public void setABC(String ABC){
        this.ABC = ABC;
    }
    public String getTASK_PROCESS(){
        return this.TASK_PROCESS;
    }
    public void setTASK_PROCESS(String TASK_PROCESS){
        this.TASK_PROCESS = TASK_PROCESS;
    }
    public Timestamp getTIME(){
        return this.TIME;
    }
    public void setTIME(Timestamp TIME){
        this.TIME = TIME;
    }
	@Override
	public String toString() {
		return "Task [TASK_ID=" + TASK_ID + ", SBBH=" + SBBH + ", TASK_USER=" + TASK_USER + ", ABC=" + ABC
				+ ", TASK_PROCESS=" + TASK_PROCESS + ", TIME=" + TIME + "]";
	}
    
}
