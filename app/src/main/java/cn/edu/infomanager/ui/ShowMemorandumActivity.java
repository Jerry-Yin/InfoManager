package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.Memorandum;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.AlamrReceiver;
import cn.edu.infomanager.util.MemorandumService;
import cn.edu.infomanager.util.NotificationUtil;
import cn.edu.infomanager.util.UserService;

public class ShowMemorandumActivity extends Activity {

	private static final String TAG = "BusinessManager";
	
	private User user_info;
	private int userid;
	private int id;
	private int position ;
	private Boolean isUpdate = false;
	private MemorandumService mService;
	private ListView listView;
	private Memorandum upInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id", -1);
		userid = bundle.getInt("user_id",-1);
		position = bundle.getInt("position", 0);
		setContentView(R.layout.show_memorandum);
		
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(userid);
		userService.closeDB();
		mService = new MemorandumService(this);
		
		tv_date = (TextView) findViewById(R.id.date);
		tv_time = (TextView) findViewById(R.id.time);
		s_category = (Spinner) findViewById(R.id.category);
		ev_summary = (EditText) findViewById(R.id.edit_summary);
		ev_desvription = (EditText) findViewById(R.id.edit_description);
		
		Button bt_mem_del = (Button) findViewById(R.id.memorandum_del);
		Button bt_mem_save = (Button) findViewById(R.id.memorandum_save);
		
		tv_date.setText(timeFormat.format(System.currentTimeMillis()));
		
		if(id<0){
			//新建
			bt_mem_del.setText("舍弃");
		}else{
			//修改
			bt_mem_save.setText("保存修改");
			isUpdate=true;
			initUpdateView(position);
		}

//		NotificationUtil.cancelNotification(this, getIntent().getIntExtra("tag", -1));
		NotificationUtil.cancelNotification(this, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	private String oldDate;
	private void initUpdateView(int id2) {
		//修改item
		upInfo = mService.selectInfoByID(id2);
		oldDate = upInfo.getDate();
		ev_summary.setText(upInfo.getSummary());
		ev_desvription.setText(upInfo.getDescription());
		tv_date.setText(timeFormat.format(new Date(Long.parseLong(upInfo.getDate()))));
		
		switch (upInfo.getCategory()) {
		case 0:
			s_category.setSelection(1);
			break;
		case 1:
			s_category.setSelection(0);
			break;
		case 2:
			s_category.setSelection(2);
			break;
		default:
			break;
		}
		
	}
	TextView tv_date;
	TextView tv_time;
	Spinner s_category;
	EditText ev_summary;
	EditText ev_desvription;
	public void bt_Add_OK(View view){
		String summary = ev_summary.getText().toString();
		String description = ev_desvription.getText().toString();
		String date = dateAndTime.getTime().getTime()+"";
		if(null==summary || "".equals(summary)){
			Toast.makeText(this, "请填写备忘录", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(description)){
			Toast.makeText(this, "请填写备忘录内容", Toast.LENGTH_SHORT).show();
			return;
		}

		int temp = 1;
		if(((String)s_category.getSelectedItem()).equals("普通"))
			temp=1;
		else if(((String)s_category.getSelectedItem()).equals("简单"))
			temp=2;
		else
			temp=0;

		if(isUpdate){
			//点击item进来的
//			date = upInfo.getDate();
			upInfo.setSummary(summary);
			upInfo.setDescription(description);
			upInfo.setCategory(temp);
			mService.updateMemorandum(upInfo);
			
			String teps = oldDate.substring(oldDate.length() - 6);
			int rcodes = Integer.parseInt(teps);
			
			Intent intent1 = new Intent(this,AlamrReceiver.class);
//			PendingIntent pis = PendingIntent.getBroadcast(this, rcodes, intent1, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pis = PendingIntent.getBroadcast(this, rcodes, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager ams = (AlarmManager) this.getSystemService(Activity.ALARM_SERVICE);
			ams.cancel(pis);
			Log.d(TAG, rcodes + "  取消旧的闹钟提醒");
			
			
			String tep = upInfo.getDate().substring(upInfo.getDate().length()-6);
			int rcode = Integer.parseInt(tep);

			Log.d(TAG, "设置闹钟");
			Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);
			intent.putExtra("code", rcode);
			intent.putExtra("id", position);
			Log.d(TAG, "memo = position = " + position);
			intent.putExtra("user_id", userid);
			intent.putExtra("summary", upInfo.getSummary());
			intent.putExtra("remark", upInfo.getDescription());
			intent.putExtra("is_remind", upInfo.isRemind());
//			PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(upInfo.getDate())/**/
						, pi);//设置闹钟   
//			am.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(upInfo.getDate())/**/
//						, (3*1000), pi);//重复设置
			
			this.finish();
			return;
		}
		/* Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);   
		 PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, 0, intent, 0);   
		 AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);   
		 am.cancel(pi);   
		*/
		//新建的
		boolean isInsert = mService.insertMemorandum(new Memorandum(position, temp, summary, ev_desvription.getText().toString()
						, date
						, user_info.getId()));

		Log.d(TAG, "memo = isInsert = " + isInsert);
		Log.d(TAG, "memo = position = " + position);
		Log.d(TAG, "memo = "+ mService.selectInfoByID(position));
		Log.d(TAG, "memo = id = " + mService.selectInfoByID(position).getId());
		
		String tep = date.substring(date.length()-6);
		int rcode = Integer.parseInt(tep);
		
		Log.d(TAG, "设置闹钟");
		Intent intent = new Intent(ShowMemorandumActivity.this,AlamrReceiver.class);
//		intent.putExtra("code", rcode);
		intent.putExtra("code", rcode);
		intent.putExtra("id", position);
		intent.putExtra("user_id", userid);
		intent.putExtra("summary", summary);
		intent.putExtra("remark", ev_desvription.getText().toString());
//		intent.putExtra("is_remind", upInfo.isRemind());
		intent.putExtra("is_remind", 1);
//		PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi = PendingIntent.getBroadcast(ShowMemorandumActivity.this, rcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, Long.parseLong(date)/**/
					, pi);//设置闹钟   
//		am.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(date)/**/
//					, (3*1000), pi);//重复设置
		
		this.finish();
	}
	public void bt_Mem_Del(View view){
		if(!isUpdate){
			this.finish();
			return;
		}
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("删除 ")
		.setMessage("将删除当前信息")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mService.deleteMemorandum(position, userid);
				Toast.makeText(ShowMemorandumActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
				ShowMemorandumActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
	
	
	Dialog dialog = null;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance();
	private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
	public void selectDate(View view){
		dialog = new DatePickerDialog(
				this,                
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
						dateAndTime.set(Calendar.YEAR, year);
						dateAndTime.set(Calendar.MONTH, month);
						dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

						tv_date.setText(timeFormat.format(dateAndTime.getTime()));  
						if(upInfo!=null)
							upInfo.setDate((dateAndTime.getTime().getTime())+"");
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

						tv_date.setText(timeFormat.format(dateAndTime.getTime()));   
						if(upInfo!=null)
							upInfo.setDate((dateAndTime.getTime().getTime())+"");
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
		if(mService!=null)
			mService.closeDB();
	}

	
}
