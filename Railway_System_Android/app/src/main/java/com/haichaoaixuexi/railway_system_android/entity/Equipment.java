package com.haichaoaixuexi.railway_system_android.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
@SmartTable(name = "设备信息表")
public class Equipment {
    @SmartColumn(id = 1, name = "序号")
    private int EQUIP_ID;
    @SmartColumn(id = 5, name = "所属区域")
    private String SSQY;
    @SmartColumn(id = 2, name = "设备名称")
    private String SBMC;
    @SmartColumn(id = 6, name = "设备规格")
    private String SBGG;
    @SmartColumn(id = 3, name = "制造厂家")
    private String ZZCJ;
    @SmartColumn(id = 4, name = "制造日期")
    private String ZZRQ;

    private int SBZT;

    private int SSCJ;

    private String GZLX;
    @SmartColumn(id = 7, name = "检修周期（天）")
    private int XXZQ;

    private int GROUP_ID;

    private int USER_ID;

    private String GROUP_NAME;

    private int EQ_STATUS_ID;
    @SmartColumn(id = 8, name = "设备状态")
    private String CONTENT;


    private String CQLX;

    private String SYRQ;

    private String WXCC;

    private String CCBH;

    private String LYFS;

    private String BZ;

    private String SBLX;

    private String CJDH;

    @Generated(hash = 52658053)
    public Equipment(int EQUIP_ID, String SSQY, String SBMC, String SBGG,
            String ZZCJ, String ZZRQ, int SBZT, int SSCJ, String GZLX, int XXZQ,
            int GROUP_ID, int USER_ID, String GROUP_NAME, int EQ_STATUS_ID,
            String CONTENT, String CQLX, String SYRQ, String WXCC, String CCBH,
            String LYFS, String BZ, String SBLX, String CJDH) {
        this.EQUIP_ID = EQUIP_ID;
        this.SSQY = SSQY;
        this.SBMC = SBMC;
        this.SBGG = SBGG;
        this.ZZCJ = ZZCJ;
        this.ZZRQ = ZZRQ;
        this.SBZT = SBZT;
        this.SSCJ = SSCJ;
        this.GZLX = GZLX;
        this.XXZQ = XXZQ;
        this.GROUP_ID = GROUP_ID;
        this.USER_ID = USER_ID;
        this.GROUP_NAME = GROUP_NAME;
        this.EQ_STATUS_ID = EQ_STATUS_ID;
        this.CONTENT = CONTENT;
        this.CQLX = CQLX;
        this.SYRQ = SYRQ;
        this.WXCC = WXCC;
        this.CCBH = CCBH;
        this.LYFS = LYFS;
        this.BZ = BZ;
        this.SBLX = SBLX;
        this.CJDH = CJDH;
    }

    @Generated(hash = 748305627)
    public Equipment() {
    }

    public int getEQUIP_ID() {

        return this.EQUIP_ID;
    }

    public void setEQUIP_ID(int EQUIP_ID) {

        this.EQUIP_ID = EQUIP_ID;
    }

    public String getSSQY() {

        return this.SSQY;
    }

    public void setSSQY(String SSQY) {

        this.SSQY = SSQY;
    }

    public String getSBMC() {

        return this.SBMC;
    }

    public void setSBMC(String SBMC) {

        this.SBMC = SBMC;
    }

    public String getSBGG() {

        return this.SBGG;
    }

    public void setSBGG(String SBGG) {

        this.SBGG = SBGG;
    }

    public String getZZCJ() {

        return this.ZZCJ;
    }

    public void setZZCJ(String ZZCJ) {

        this.ZZCJ = ZZCJ;
    }

    public String getZZRQ() {

        return this.ZZRQ;
    }

    public void setZZRQ(String ZZRQ) {

        this.ZZRQ = ZZRQ;
    }

    public int getSBZT() {

        return this.SBZT;
    }

    public void setSBZT(int SBZT) {

        this.SBZT = SBZT;
    }

    public int getSSCJ() {

        return this.SSCJ;
    }

    public void setSSCJ(int SSCJ) {

        this.SSCJ = SSCJ;
    }

    public String getGZLX() {

        return this.GZLX;
    }

    public void setGZLX(String GZLX) {

        this.GZLX = GZLX;
    }

    public int getXXZQ() {

        return this.XXZQ;
    }

    public void setXXZQ(int XXZQ) {

        this.XXZQ = XXZQ;
    }

    public String getCQLX() {

        return this.CQLX;
    }

    public void setCQLX(String CQLX) {

        this.CQLX = CQLX;
    }

    public String getSYRQ() {

        return this.SYRQ;
    }

    public void setSYRQ(String SYRQ) {

        this.SYRQ = SYRQ;
    }

    public String getWXCC() {

        return this.WXCC;
    }

    public void setWXCC(String WXCC) {

        this.WXCC = WXCC;
    }

    public String getCCBH() {

        return this.CCBH;
    }

    public void setCCBH(String CCBH) {

        this.CCBH = CCBH;
    }

    public String getLYFS() {

        return this.LYFS;
    }

    public void setLYFS(String LYFS) {

        this.LYFS = LYFS;
    }

    public String getBZ() {

        return this.BZ;
    }

    public void setBZ(String BZ) {

        this.BZ = BZ;
    }

    public String getSBLX() {

        return this.SBLX;
    }

    public void setSBLX(String SBLX) {

        this.SBLX = SBLX;
    }

    public String getCJDH() {

        return this.CJDH;
    }

    public void setCJDH(String CJDH) {

        this.CJDH = CJDH;
    }

    public int getGROUP_ID() {

        return this.GROUP_ID;
    }

    public void setGROUP_ID(int GROUP_ID) {

        this.GROUP_ID = GROUP_ID;
    }

    public int getUSER_ID() {

        return this.USER_ID;
    }

    public void setUSER_ID(int USER_ID) {

        this.USER_ID = USER_ID;
    }

    public String getGROUP_NAME() {

        return this.GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {

        this.GROUP_NAME = GROUP_NAME;
    }

    public int getEQ_STATUS_ID() {

        return this.EQ_STATUS_ID;
    }

    public void setEQ_STATUS_ID(int EQ_STATUS_ID) {

        this.EQ_STATUS_ID = EQ_STATUS_ID;
    }

    public String getCONTENT() {

        return this.CONTENT;
    }

    public void setCONTENT(String CONTENT) {

        this.CONTENT = CONTENT;
    }
}