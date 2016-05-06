package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.Orders;
import cn.edu.infomanager.util.OrdersService;

public class ShowOrdersActivity extends Activity {
    private static final String TAG = "ShowOrdersActivity";

    private EditText et_use;
    private EditText et_remark;
    private EditText et_date;
    private EditText et_money;
    private RadioGroup et_in_out;

    private Button bt_save_update;
    private Button bt_orders_del;

    private OrdersService oService;
    private Boolean isUpdate = false;
    private int id;
    private int userid;
    private DateFormat timeFormat = DateFormat.getDateTimeInstance();
    private final String[] Usages = new String[]{"学习", "生活", "投资", "娱乐", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_orders);
        findView();

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id", -1);
        userid = intent.getExtras().getInt("user_id", -1);
        oService = new OrdersService(this);

        if (id < 0) {
            bt_orders_del.setText("舍弃");
        } else {
            bt_save_update.setText("保存修改");
            isUpdate = true;
            initUpdateView(id);
        }
    }

    public void bt_SaveUpdate(View view) {
        Orders info = getInfo();
        if (info == null) {
            return;
        }
        if (isUpdate) {
            info.setUser_id(userid);
            info.setId(id);
            oService.updateOrders(info);
            Toast.makeText(this, "保存修改", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            info.setUser_id(userid);
            oService.insertOrders(info);
            Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void bt_Del(View view) {
        if (!isUpdate) {
            this.finish();
            return;
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("删除 ")
                .setMessage("将删除当前收支信息")
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oService.delOrders(id);

                        Toast.makeText(ShowOrdersActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
                        ShowOrdersActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void findView() {
        et_use = (EditText) findViewById(R.id.orders_use);
        et_remark = (EditText) findViewById(R.id.orders_remark);
        et_money = (EditText) findViewById(R.id.orders_money);
        et_date = (EditText) findViewById(R.id.orders_date);
        et_in_out = (RadioGroup) findViewById(R.id.orders_in_out);
        RadioButton w = (RadioButton) findViewById(R.id.out);
        w.setChecked(true);

        et_date.setHint(timeFormat.format(new Date(System.currentTimeMillis())));
        bt_save_update = (Button) findViewById(R.id.save_update);
        bt_orders_del = (Button) findViewById(R.id.orders_del);
    }

    public void setTextUsage(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("用处")
                .setItems(Usages, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_use.setText(Usages[which]);
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    private void initUpdateView(int id) {
        Orders order = oService.getOrdersByID(id);
        et_use.setText(order.getUse());
        et_remark.setText(order.getRemark());
        et_money.setText(order.getMoney());
        et_date.setText(order.getDate());

        if ("收入".equals(order.getIn_out())) {
            RadioButton n = (RadioButton) findViewById(R.id.in);
            n.setChecked(true);
        } else {
            RadioButton w = (RadioButton) findViewById(R.id.out);
            w.setChecked(true);
        }
    }

    private Orders getInfo() {
        if (et_use.getText().toString() != null && "".equals(et_use.getText().toString())) {
            Toast.makeText(this, "用处不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        String sexstr = ((RadioButton) findViewById(et_in_out.getCheckedRadioButtonId())).getText().toString();
        String date = et_date.getText().toString();
        if ("".equals(date))
            date = timeFormat.format(new Date(System.currentTimeMillis()));

        return new Orders(-1, date,
                et_money.getText().toString(),
                sexstr,
                et_use.getText().toString(),
                et_remark.getText().toString(),
                -1);
    }

    Dialog dialog = null;
    private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    public void selectDate(View view){
        dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                        dateAndTime.set(Calendar.YEAR, year);
                        dateAndTime.set(Calendar.MONTH, month);
                        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        et_date.setText(timeFormat.format(dateAndTime.getTime()));
//                        if(upInfo!=null)
//                            upInfo.setDate((dateAndTime.getTime().getTime())+"");
                    }
                },
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    public void selectTime(View view){
        dialog=new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        dateAndTime.set(Calendar.MINUTE, minute);

                        et_date.setText(timeFormat.format(dateAndTime.getTime()));
//                        if(upInfo!=null)
//                            upInfo.setDate((dateAndTime.getTime().getTime())+"");
                    }
                },
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE),
                true
        );
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (oService != null)
            oService.closeDB();
    }
}
