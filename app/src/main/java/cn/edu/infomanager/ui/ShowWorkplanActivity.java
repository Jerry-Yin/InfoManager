package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.WorkPlan;
import cn.edu.infomanager.util.WorkplanService;

public class ShowWorkplanActivity extends Activity {
	private static final String TAG="ShowWorkplanActivity";
	
	private EditText et_use;
	private EditText et_remark;
	private EditText et_date;
	private EditText et_money;
	
	private Button bt_save_update;
	private Button bt_work_del;
	
	private WorkplanService wService;
	private Boolean isUpdate = false;
	private int id;
	private int userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_workplan);
		findView();
		
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id",-1);
		userid = intent.getExtras().getInt("user_id",-1);
		wService = new WorkplanService(this);
		
		if(id<0){
			bt_work_del.setText("舍弃");
		}else{
			bt_save_update.setText("保存修改");
			isUpdate=true;
			initUpdateView(id);
		}
	}
	public void bt_SaveUpdate(View view){
		WorkPlan info = getInfo();
		if(info==null){
			return;
		}
		if(isUpdate){
			info.setUser_id(userid);
			info.setId(id);
			wService.updateWorkPlan(info);
			Toast.makeText(this, "保存修改", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			info.setUser_id(userid);
			wService.insertWorkPlan(info);
			Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	public void bt_Del(View view){
		if(!isUpdate){
			this.finish();
			return;
		}
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("删除 ")
		.setMessage("将删除当前工作规划")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wService.delWorkPlan(id);
				
				Toast.makeText(ShowWorkplanActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
				ShowWorkplanActivity.this.finish();
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
		
		bt_save_update = (Button) findViewById(R.id.save_update);
		bt_work_del = (Button) findViewById(R.id.work_del);
		
	}
	private void initUpdateView(int id){
		WorkPlan work = wService.getWorkPlanByID(id);
		et_use.setText(work.getSummary());
		et_remark.setText(work.getRemark());
		et_money.setText(work.getDate());
		et_date.setText(work.getTime());
		
	}
	
	private WorkPlan getInfo(){
		if("".equals(et_use.getText().toString())){
			Toast.makeText(this, "工作内容为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		return new WorkPlan(id, et_use.getText().toString(),
					et_remark.getText().toString(),
					et_date.getText().toString(),
					et_money.getText().toString(), userid);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(wService!=null)
			wService.closeDB();
	}
}
