package com.haichaoaixuexi.railway_system_android.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haichao on 2018/5/21.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 */
@SmartTable(name = "统计分析表")
public class Eq_analysis {
    @SmartColumn(id =1,name = "设备名称")
    private String SBMC;
    @SmartColumn(id =2,name = "检修次数")
    private int count;
    @SmartColumn(id =3,name = "占比%")
    private int ratio;
    @SmartColumn(id =4,name = "检修合格率%")
    private int passPercent;
    @SmartColumn(id =5,name = "检修周期(天)")
    private int cycle;
    private int SBBH;
    private List<Eq_check> ecs = new ArrayList<>();
    private int passCount;
    public String getSBMC() {
        return SBMC;
    }

    public void setSBMC(String SBMC) {
        this.SBMC = SBMC;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getPassPercent() {
        return passPercent;
    }

    public void setPassPercent(int passPercent) {
        this.passPercent = passPercent;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getSBBH() {
        return SBBH;
    }

    public void setSBBH(int SBBH) {
        this.SBBH = SBBH;
    }

    public List<Eq_check> getEcs() {
        return ecs;
    }

    public void setEcs(List<Eq_check> ecs) {
        this.ecs = ecs;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }
}
