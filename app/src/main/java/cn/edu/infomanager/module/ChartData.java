package cn.edu.infomanager.module;

/**
 * Created by JerryYin on 4/24/16.
 * 用于绘制 支出图表 时需要用到的数据
 * 整合前
 */
public class ChartData {

    public int id;
    public String usage;
    public Float money;
    public String date;

    public ChartData(String usage, Float money, String date) {
        this.usage = usage;
        this.money = money;
        this.date = date;
    }

    public ChartData(int id, String usage, Float money, String date) {
        this.id = id;
        this.usage = usage;
        this.money = money;
        this.date = date;
    }
}
