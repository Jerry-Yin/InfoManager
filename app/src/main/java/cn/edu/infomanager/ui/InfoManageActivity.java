/**
 * 
 */
package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.BlogService;
import cn.edu.infomanager.util.ContactsService;
import cn.edu.infomanager.util.MemorandumService;
import cn.edu.infomanager.util.OrdersService;
import cn.edu.infomanager.util.UserService;
import cn.edu.infomanager.util.WorkplanService;

/**
 * @date 2012-5-2
 * @time 下午04:39:22
 */
public class InfoManageActivity extends Activity {
	private static final String TAG = "InfoManageActivity";
	
	private User user_info;
	private UserService userService;
	private View v_chageview;
	
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
		setContentView(R.layout.ac_info_manager);
		
		userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		
		ImageView iv_poto = (ImageView) findViewById(R.id.info_foto);
		TextView tv_name = (TextView) findViewById(R.id.info_name);
		byte[] data = user_info.getPoto();
		if(data!=null && data.length>0){
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			iv_poto.setImageBitmap(bitmap);
		}
		tv_name.setText(user_info.getUsername());
	}
	private Dialog dialog;
	/**
	 * 修改密码
	 * author 
	 */
	public void changePass(View view){
		v_chageview = getLayoutInflater().inflate(R.layout.change_password, null);
		dialog = new AlertDialog.Builder(this)
			.setTitle("修改密码                                   ")
			.setView(v_chageview)
			.show();
	}
	public void changePassword(View view){
		EditText et_oldpass = (EditText) v_chageview.findViewById(R.id.change_oldpass);
		EditText et_newpass = (EditText) v_chageview.findViewById(R.id.change_newpass);
		EditText et_newpass2 = (EditText) v_chageview.findViewById(R.id.change_newpass2);
		
		String oldpass = et_oldpass.getText().toString();
		String newpass = et_newpass.getText().toString()+"";
		String newpass2 = et_newpass2.getText().toString()+"";
		
		
		if(oldpass==null || "".equals(oldpass)){
			Toast.makeText(this, "输入旧密码", Toast.LENGTH_SHORT).show();
			return;
		}
		if("".equals(newpass)||"".equals(newpass2)){
			Toast.makeText(this, "密码不能设置为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!newpass.equals(newpass2)){
			Toast.makeText(this, "两次设置的密码不同", Toast.LENGTH_SHORT).show();
			return;
		}
		user_info.setPassword(newpass);
		userService.changePass(user_info);
		
		Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
		dialog.dismiss();
	}
	
	/**
	 * 注销
	 * author 
	 */
	public void delUser(View view){
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("注销用户 ")
		.setMessage("将删除当前用户的所有信息")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				userService.closeDB();
				MemorandumService mservice = new MemorandumService(InfoManageActivity.this);
				mservice.delInfoByUserID(user_info.getId()+"");
				mservice.closeDB();
				
				ContactsService cService = new ContactsService(InfoManageActivity.this);
				cService.delContactsByUserID(user_info.getId());
				cService.closeDB();
				
				OrdersService oService = new OrdersService(InfoManageActivity.this);
				oService.delOrdersByUserID(user_info.getId());
				oService.closeDB();
				
				BlogService bService = new BlogService(InfoManageActivity.this);
				bService.delBlogByUserID(user_info.getId());
				bService.closeDB();
				
				WorkplanService wService = new WorkplanService(InfoManageActivity.this);
				wService.delWorkPlanByUserID(user_info.getId());
				wService.closeDB();
				
				userService.delUser(user_info);
				
				Toast.makeText(InfoManageActivity.this, "注销用户成功", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getApplicationContext(), LoginActivity.class));
				InfoManageActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.show();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(userService!=null)
			userService.closeDB();
	}
	
	
	
}
