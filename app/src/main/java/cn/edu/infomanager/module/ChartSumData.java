package cn.edu.infomanager.module;

/**
 * Created by JerryYin on 4/24/16.
 * 整合后的list
 */
public class ChartSumData  {

    public String usage;
    public Float money;
    public int color;


    public ChartSumData(String usage, Float money, int color) {
        this.usage = usage;
        this.money = money;
        this.color = color;
    }

    public ChartSumData(String usage, Float money) {
        this.usage = usage;
        this.money = money;
    }
}
