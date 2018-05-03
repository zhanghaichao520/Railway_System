package com.haichaoaixuexi.railway_system_android.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Task {
	
    private int TASK_ID;

    private int SBBH;

    private int TASK_USER;

    private String ABC;

    private String TASK_PROCESS;

    private String TIME;

    @Generated(hash = 1430544979)
    public Task(int TASK_ID, int SBBH, int TASK_USER, String ABC,
            String TASK_PROCESS, String TIME) {
        this.TASK_ID = TASK_ID;
        this.SBBH = SBBH;
        this.TASK_USER = TASK_USER;
        this.ABC = ABC;
        this.TASK_PROCESS = TASK_PROCESS;
        this.TIME = TIME;
    }
    @Generated(hash = 733837707)
    public Task() {
    }

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
    public String getTIME(){

        return this.TIME;
    }
    public void setTIME(String TIME){

        this.TIME = TIME;
    }
}
