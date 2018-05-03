package com.haichaoaixuexi.railway_system_android.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class EDocument implements Serializable{
    private int ED_ID;

    private String TITLE;

    private int UID;

    private String CONTENT;

    private Timestamp CJRQ;
    
    private String USER_NAME;

    public int getED_ID(){

        return this.ED_ID;
    }
    public void setED_ID(int ED_ID){

        this.ED_ID = ED_ID;
    }
    public String getTITLE(){

        return this.TITLE;
    }
    public void setTITLE(String TITLE){

        this.TITLE = TITLE;
    }
    public int getUID(){

        return this.UID;
    }
    public void setUID(int UID){

        this.UID = UID;
    }
    public String getCONTENT(){

        return this.CONTENT;
    }
    public void setCONTENT(String CONTENT){

        this.CONTENT = CONTENT;
    }
    public Timestamp getCJRQ(){

        return this.CJRQ;
    }
    public void setCJRQ(Timestamp CJRQ){

        this.CJRQ = CJRQ;
    }
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
    
}
