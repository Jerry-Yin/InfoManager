package cn.edu.infomanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.UserService;

public class BusinessManager extends Activity {

	private static final String TAG = "BusinessManager";
	
	private User user_info;
	private UserService userService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		String name = intent.getExtras().getString("name");
		if(id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.ac_business_manager);
		
		userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		
		if(user_info==null){
			user_info = userService.getUserInfoByName(name);
		}
		if(user_info==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		
		ImageView iv_poto = (ImageView) findViewById(R.id.poto);
		byte[] data = user_info.getPoto();
		if(data!=null && data.length>0){
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			iv_poto.setImageBitmap(bitmap);
		}
		TextView tv_username = (TextView) findViewById(R.id.username);
		tv_username.setText(user_info.getUsername());
		
		if(userService!=null){
			userService.closeDB();
		}
	}
	
	/**
	 * 备忘录管理
	 * author 
	 */
	public void bt_Memorandum(View view){
		Intent intent = new Intent();
		intent.setClass(this, MemorandumActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 用户信息管理
	 * @param view
	 */
	public void bt_InfoManager(View view){
		Intent intent = new Intent();
		intent.setClass(this, InfoManageActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 通讯录
	 * @param view
	 */
	public void bt_Contacts(View view){
		Intent intent = new Intent();
		intent.setClass(this, ContactsActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 收支信息
	 * @param view
	 */
	public void bt_Orders(View view){
		Intent intent = new Intent();
		intent.setClass(this, OrderActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 日志管理
	 * @param view
	 */
	public void bt_Blog(View view){
		Intent intent = new Intent();
		intent.setClass(this, BlogActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 工作规划
	 * @param view
	 */
	public void bt_Workplan(View view){
		Intent intent = new Intent();
		intent.setClass(this, WorkplanActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void bt_LogOut(View view){
//		startActivity(new Intent(this, LoginActivity.class));
		this.finish();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(userService!=null){
			userService.closeDB();
		}
	}
	
}
