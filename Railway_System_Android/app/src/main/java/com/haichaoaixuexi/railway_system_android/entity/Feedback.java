package com.haichaoaixuexi.railway_system_android.entity;

public class Feedback {
    private int FB_ID;

    private String CONTENT;

    private int FKR;

    public int getFB_ID(){

        return this.FB_ID;
    }
    public void setFB_ID(int FB_ID){

        this.FB_ID = FB_ID;
    }
    public String getCONTENT(){

        return this.CONTENT;
    }
    public void setCONTENT(String CONTENT){

        this.CONTENT = CONTENT;
    }
    public int getFKR(){

        return this.FKR;
    }
    public void setFKR(int FKR){

        this.FKR = FKR;
    }
}
