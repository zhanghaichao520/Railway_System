package com.haichaoaixuexi.railway_system_android.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
@SmartTable(name="设备故障信息表")
public class Eq_issue_kind {
    @SmartColumn(id =1,name = "序号")
    private int EQ_ISSUE_KIND;
    @SmartColumn(id =2,name = "故障类型")
    private String EQ_ISSUE;

    @Generated(hash = 2046266711)
    public Eq_issue_kind(int EQ_ISSUE_KIND, String EQ_ISSUE) {
        this.EQ_ISSUE_KIND = EQ_ISSUE_KIND;
        this.EQ_ISSUE = EQ_ISSUE;
    }
    @Generated(hash = 1244084838)
    public Eq_issue_kind() {
    }

    public int getEQ_ISSUE_KIND(){

        return this.EQ_ISSUE_KIND;
    }
    public void setEQ_ISSUE_KIND(int EQ_ISSUE_KIND){

        this.EQ_ISSUE_KIND = EQ_ISSUE_KIND;
    }
    public String getEQ_ISSUE(){

        return this.EQ_ISSUE;
    }
    public void setEQ_ISSUE(String EQ_ISSUE){

        this.EQ_ISSUE = EQ_ISSUE;
    }
}
