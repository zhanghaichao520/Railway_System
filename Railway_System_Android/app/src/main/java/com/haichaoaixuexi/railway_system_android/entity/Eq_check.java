package com.haichaoaixuexi.railway_system_android.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Eq_check{
    @Id(autoincrement = true)
    private Long id;

    private int EQ_CHECK_ID;

    private String JCLX;

    private int SBBH;

    private int GZLX;

    private String GZMS;

    private int BXR;

    private String BXSJ;
    private int YSR;

    private String YSSJ;

    private String GZTP;

    private String ABC;

    private String imgString;

    private String BXR_NAME;

    private String YSJG;

    @Generated(hash = 255222568)
    public Eq_check(Long id, int EQ_CHECK_ID, String JCLX, int SBBH, int GZLX,
            String GZMS, int BXR, String BXSJ, int YSR, String YSSJ, String GZTP,
            String ABC, String imgString, String BXR_NAME, String YSJG) {
        this.id = id;
        this.EQ_CHECK_ID = EQ_CHECK_ID;
        this.JCLX = JCLX;
        this.SBBH = SBBH;
        this.GZLX = GZLX;
        this.GZMS = GZMS;
        this.BXR = BXR;
        this.BXSJ = BXSJ;
        this.YSR = YSR;
        this.YSSJ = YSSJ;
        this.GZTP = GZTP;
        this.ABC = ABC;
        this.imgString = imgString;
        this.BXR_NAME = BXR_NAME;
        this.YSJG = YSJG;
    }

    @Generated(hash = 167317681)
    public Eq_check() {
    }

    public String getBXR_NAME() {
        return BXR_NAME;
    }

    public void setBXR_NAME(String BXR_NAME) {
        this.BXR_NAME = BXR_NAME;
    }

    public String getYSJG() {
        return YSJG;
    }
    public void setYSJG(String ySJG) {
        YSJG = ySJG;
    }
    public int getEQ_CHECK_ID(){

        return this.EQ_CHECK_ID;
    }
    public void setEQ_CHECK_ID(int EQ_CHECK_ID){

        this.EQ_CHECK_ID = EQ_CHECK_ID;
    }
    public String getJCLX(){

        return this.JCLX;
    }
    public void setJCLX(String JCLX){
        this.JCLX = JCLX;
    }
    public int getSBBH(){

        return this.SBBH;
    }
    public void setSBBH(int SBBH){

        this.SBBH = SBBH;
    }
    public int getGZLX(){

        return this.GZLX;
    }
    public void setGZLX(int GZLX){

        this.GZLX = GZLX;
    }
    public String getGZMS(){

        return this.GZMS;
    }
    public void setGZMS(String GZMS){

        this.GZMS = GZMS;
    }
    public int getBXR(){

        return this.BXR;
    }
    public void setBXR(int BXR){

        this.BXR = BXR;
    }
    public String getBXSJ(){

        return this.BXSJ;
    }
    public void setBXSJ(String BXSJ){

        this.BXSJ = BXSJ;
    }
    public int getYSR(){

        return this.YSR;
    }
    public void setYSR(int YSR){

        this.YSR = YSR;
    }
    public String getYSSJ(){

        return this.YSSJ;
    }
    public void setYSSJ(String YSSJ){

        this.YSSJ = YSSJ;
    }
    public String getGZTP(){

        return this.GZTP;
    }
    public void setGZTP(String GZTP){

        this.GZTP = GZTP;
    }
    public String getABC(){

        return this.ABC;
    }
    public void setABC(String ABC){

        this.ABC = ABC;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }
}
