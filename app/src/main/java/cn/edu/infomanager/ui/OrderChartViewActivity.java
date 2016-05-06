package cn.edu.infomanager.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.infomanager.R;
import cn.edu.infomanager.constant.InfManagerConst;
import cn.edu.infomanager.module.ChartData;
import cn.edu.infomanager.module.ChartSumData;
import cn.edu.infomanager.module.Orders;
import cn.edu.infomanager.util.OrdersService;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by JerryYin on 4/24/16.
 */
public class OrderChartViewActivity extends Activity {


    private static final String TAG = "OrderChartViewActivity";
    private LineChartView mLineChartView;   //折线图
    private LineChartData mLineChartData = new LineChartData(); //数据
    private List<AxisValue> mAxisValuesX = new ArrayList<>();    //X坐标值
    private List<AxisValue> mAxisValuesY = new ArrayList<>();    //X坐标值
    private List<PointValue> mPointValues = new ArrayList<PointValue>();  //数据--点
    private List<Line> mLines = new ArrayList<Line>();   //

    private PieChartView mPieChartView; //扇形图
    private PieChartData mPieChartData;    //数据
    private List<SliceValue> mSliceValues = new ArrayList<>();

    private OrdersService mOrdersService;
    private int user_id;
    private List<Orders> mOrderLists = new ArrayList<>();
    private List<ChartData> mDataLists = new ArrayList<>();    //整合前的list
    private List<ChartSumData> mSumDataLists = new ArrayList<>();    //整合后的 String-用处  float-金额


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_chart_view);

        //设置数据
        initDatas();
        //设置坐标
        setupAxis();
        initViews();

    }

    private void initDatas() {
        user_id = getIntent().getIntExtra("user_id", 0);
        mOrdersService = new OrdersService(this);
        mOrderLists = mOrdersService.getAllOrders(user_id);
        if (mOrderLists.size() >= 0) {
            parseLists(mOrderLists);
            mSumDataLists = sumDataList(mDataLists);
            Log.d(TAG, "list.size = " + mSumDataLists.size());

//            addPointsData();
            //In most cased you can call data model methods in builder-pattern-like manner.
//            Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.blue1)).setCubic(true);
//            mLines.add(line);
//            mLineChartData.setLines(mLines);

            addPieDatas();
            mPieChartData = new PieChartData(mSliceValues);
            mPieChartData.setHasLabels(true);   // 是否显示标签
//        mPieChartData.setCenterCircleScale(10);
//        mPieChartData.setCenterCircleColor(getResources().getColor(R.color.white));
//        mPieChartData.setCenterText1("zhong");
        }
    }

    private void initViews() {
//        mLineChartView = (LineChartView) findViewById(R.id.line_chart_view);
        mPieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
//        mLineChartView.setInteractive(true);    //可滑动
//        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
//        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mPieChartView.setInteractive(true);

//        mLineChartData.setAxisXBottom(Axis axisX);
//        ColumnChartData.setStacked(boolean isStacked);
//        Line.setStrokeWidth(int strokeWidthDp);
//        mLineChartView.setLineChartData(mLineChartData);
        mPieChartView.setPieChartData(mPieChartData);
    }

    /**
     * 解析当前数据
     *
     * @param orderLists
     */
    private void parseLists(List<Orders> orderLists) {
        for (int i = 0; i < orderLists.size(); i++) {
            Log.d(TAG, "OrderList = " + orderLists.get(i).getIn_out());
            String in_out = orderLists.get(i).getIn_out();
            if (in_out.equals("支出")) {
                String use = orderLists.get(i).getUse();
                Float money = Float.valueOf(orderLists.get(i).getMoney());
                String date = orderLists.get(i).getDate();
                ChartData map = new ChartData(use, money, date);
                mDataLists.add(map);
            }
        }
    }

    /**
     * 整合数据
     *
     * @param lists
     */
    private List<ChartSumData> sumDataList(List<ChartData> lists) {
        List<ChartSumData> dataList = new ArrayList<>();
        float sumStudy = 0;
        float sumLife = 0;
        float sumInvest = 0;
        float sumFun = 0;
        float sumOthers = 0;
        float sumMoney = 0;

        for (ChartData data : lists) {
            String usage = data.usage;
            switch (usage) {
                case InfManagerConst.STUDY:
                    sumStudy += data.money;
                    break;

                case InfManagerConst.LIFE:
                    sumLife += data.money;
                    break;

                case InfManagerConst.INVEST:
                    sumInvest += data.money;
                    break;

                case InfManagerConst.FUN:
                    sumFun += data.money;
                    break;

                case InfManagerConst.OTHERS:
                    sumOthers += data.money;
                    break;
            }
        }
        sumMoney = sumStudy + sumInvest + sumFun + sumLife + sumOthers;
        sumStudy = getPercent(sumStudy, sumMoney);
        sumLife = getPercent(sumLife, sumMoney);
        sumInvest = getPercent(sumInvest, sumMoney);
        sumFun = getPercent(sumFun, sumMoney);
        sumOthers = getPercent(sumOthers, sumMoney);

        dataList.add(new ChartSumData(InfManagerConst.STUDY, sumStudy, R.color.blue1));
        dataList.add(new ChartSumData(InfManagerConst.LIFE, sumLife, R.color.green1));
        dataList.add(new ChartSumData(InfManagerConst.INVEST, sumInvest, R.color.yellow1));
        dataList.add(new ChartSumData(InfManagerConst.FUN, sumFun, R.color.colorAccent1));
        dataList.add(new ChartSumData(InfManagerConst.OTHERS, sumOthers, R.color.lighter_gray));
        return dataList;
    }

    /**
     * 添加饼状图数据
     */
    private void addPieDatas() {
        for (ChartSumData sumData : mSumDataLists) {
            if (sumData.money > 0) {
                mSliceValues.add(addSliceValue(sumData.money, getResources().getColor(sumData.color), sumData.usage));
            }
        }
    }

    private void addPointsData() {
        cacuDate(mDataLists);
        mPointValues.add(new PointValue(0, 2));
        mPointValues.add(new PointValue(1, 4));
        mPointValues.add(new PointValue(2, 7));
        mPointValues.add(new PointValue(3, 4));
        mPointValues.add(new PointValue(6, 8));
        mPointValues.add(new PointValue(8, 4));
        mPointValues.add(new PointValue(10, 6));
        mPointValues.add(new PointValue(12, 10));
    }

    /**
     * 设置坐标轴
     */
    private void setupAxis() {
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setHasLines(true);
        axisX.setAutoGenerated(true);
        axisX.setInside(true);
        axisX.setTextColor(getResources().getColor(R.color.darker_gray));
        axisX.setName("采集时间");
        axisX.setMaxLabelChars(10);

        mAxisValuesX.add(new AxisValue(0));
        mAxisValuesX.add(new AxisValue(2));
        mAxisValuesX.add(new AxisValue(4));
        mAxisValuesX.add(new AxisValue(6));
        mAxisValuesX.add(new AxisValue(8));
        mAxisValuesX.add(new AxisValue(10));

        axisX.setValues(mAxisValuesX);
        mLineChartData.setAxisXBottom(axisX);

        Axis axisY = new Axis();  //Y轴
        axisY.setHasTiltedLabels(true);
        axisY.setTextColor(getResources().getColor(R.color.darker_gray));
        axisY.setName("金额");
        axisY.setMaxLabelChars(10); //默认是3，只能看最后三个数字
        axisY.setInside(true);
        mAxisValuesY.add(new AxisValue(0));
        mAxisValuesY.add(new AxisValue(2));
        mAxisValuesY.add(new AxisValue(4));
        mAxisValuesY.add(new AxisValue(6));
        mAxisValuesY.add(new AxisValue(8));
        mAxisValuesY.add(new AxisValue(10));
        axisX.setValues(mAxisValuesY);
        mLineChartData.setAxisYLeft(axisY);
    }

    /**
     * 扇形图－数据
     *
     * @param value
     * @param color
     * @param label
     * @return
     */
    private SliceValue addSliceValue(float value, int color, String label) {
        SliceValue sliceValue = new SliceValue(value, color);
        sliceValue.setLabel(value + "% " + label);
        return sliceValue;
    }

    /**
     * 计算百分比
     *
     * @param money
     * @param sum
     * @return
     */
    private float getPercent(float money, float sum) {
        return money / sum * 100;
    }

    private void cacuDate(List<ChartData> list) {
        float sumMoney = 0;

        for (ChartData data : list) {
            Log.d(TAG, "date = " + data.date);    //2016年4月24日 23:41:13
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
            Date date = null;
            try {
                date = dateFormat.parse(data.date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "date1 = " + date);    // Sun Apr 24 23:41:13 GMT+08:00 2016
            Log.d(TAG, "date1.y.m.d = " + date.getYear() + ". " + date.getMonth() + ". " + date.getDay());

            DateFormat format = DateFormat.getDateInstance();//日期格式，精确到日
            String d = format.format(date);
            Log.d(TAG, "d = " + d);   //2016年4月24日
        }


    }
}
