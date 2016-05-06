package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.UserService;

public class RegisterActivity extends Activity {
	private static String TAG = "RegisterActivity";
	
	private static final int CAMERA_WITH_DATA = 0;   //拍照
	private static final int PHOTO_PICKED_WITH_DATA = 1;  //gallery
    File mCurrentPhotoFile;
	
	EditText mEtUsername;
	EditText mEt_Password;
	EditText mEt_Password2;
	EditText mEt_Email;
	EditText mEt_Age;
	ImageView mIv_Poto;
	RadioGroup mRg_Sex;
	Button mBt_register;
	
	String mPotoDir = "";
	UserService uService;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_register);
		findViews();
	}
	private void findViews() {
		mEtUsername=(EditText) findViewById(R.id.usernameRegister);		//用户名
		mEt_Password=(EditText) findViewById(R.id.passwordRegister);	//密码 	1
		mEt_Password2=(EditText) findViewById(R.id.passwordRegister2);	//密码 	2
		mEt_Email = (EditText) findViewById(R.id.useremailRegister);	//邮箱
		mEt_Age=(EditText) findViewById(R.id.ageRegister);				//年龄
		mRg_Sex=(RadioGroup) findViewById(R.id.sexRegister);			//性别
		mBt_register=(Button) findViewById(R.id.Register);				//BT  注册
	}
	/**
	 * BT	注册
	 * @param view
	 */
	public void bt_Register(View view){
		String name=mEtUsername.getText().toString().trim();
		String pass=mEt_Password.getText().toString().trim();
		String pass2=mEt_Password2.getText().toString().trim();
		
		if(name==null || "".equals(name)){
			Toast.makeText(this, "请输入用户名称!", Toast.LENGTH_LONG).show();
			return;
		}
		if(name.length()<2 || name.length()>10){
			Toast.makeText(this, "用户名称应为2~10个字符!", Toast.LENGTH_LONG).show();
			return;
		}
		
		uService=new UserService(RegisterActivity.this);
		if(uService.checkUserExist(name)){
			Toast.makeText(this, "用户名已存在!", Toast.LENGTH_LONG).show();
			return;
		}
		if(pass!=null && !"".equals(pass) &&  pass2!=null && !"".equals(pass2)){
			if(!pass.equals(pass2)){
				Toast.makeText(this, "两次设置的密码不一致,请修改!", Toast.LENGTH_LONG).show();
				return;
			}
		}else{
			Toast.makeText(this, "请设置密码!", Toast.LENGTH_LONG).show();
			return;
		}
		
		String emailstr=mEt_Email.getText().toString().trim();
		String agestr=mEt_Age.getText().toString().trim();
			if(agestr==null || "".equals(agestr))
				agestr="0";
		String sexstr=((RadioButton)findViewById(mRg_Sex.getCheckedRadioButtonId())).getText().toString();
		
		Log.d(TAG, name + " *** " + pass + " *** " + emailstr + " *** " + agestr + "_" + sexstr);
		
		User user=new User();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(emailstr);
		user.setAge(Integer.parseInt(agestr));
		user.setSex(sexstr);
		if(photoBitmap!=null){
			user.setPoto(getImageData(photoBitmap));
		}
		uService.register(user);

		Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();

		Intent intent = new Intent();
		intent.setClass(this, BusinessManager.class);
		Bundle bundle = new Bundle();
		bundle.putString("name", user.getUsername());
		bundle.putInt("id", user.getId());
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	/**
	 * IV	设置头像
	 * @param view
	 */
	public void bt_SetPoto(View view){
		mIv_Poto = (ImageView) view;
		new AlertDialog.Builder(this)
		.setTitle("选择相片")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setItems(new String[] { "相机", "图库" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						switch (which) {
								case CAMERA_WITH_DATA:
									getPicFromCapture(); //拍照
									break;
								case PHOTO_PICKED_WITH_DATA: //相册
									getPicFromContent();
								default:
									break;
						}
					}
				}).setNegativeButton("取消", null).show();
	}

	// 拍照
	private void getPicFromCapture() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			User.PHOTO_DIR.mkdir();
			mCurrentPhotoFile = new File(User.PHOTO_DIR, getPhotoFileName()); // 用当前时间给取得的图片命名

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri fromFile = Uri.fromFile(mCurrentPhotoFile);
			String string = fromFile.toString();
			Log.i("gp", string);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);

		} else {
			Toast.makeText(this, "未检测到SD卡!", Toast.LENGTH_LONG);
		}
	}

	// 用当前时间命名图片
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 相册
	private void getPicFromContent() {
		try {
			Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			Toast.makeText(this, "选择头像出错!", Toast.LENGTH_LONG).show();
		}
	}

	// 对图片进行剪裁
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 54);
		intent.putExtra("outputY", 54);
		intent.putExtra("return-data", true);
		return intent;
	}

	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			Toast.makeText(this, "选择头像出错!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序                                  剪裁后的图片跳转到新的界面
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 54);
		intent.putExtra("outputY", 54);
		intent.putExtra("return-data", true);
		return intent;
	}

	private Bitmap photoBitmap;
	/* 选择头像后返回
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data); 

		    if (resultCode != RESULT_OK)  
		        return; 
		    switch (requestCode) {  
	        case PHOTO_PICKED_WITH_DATA: { //相册
	        	Log.d(TAG, "选择头像结束");
	        	Bundle extras = data.getExtras();
	        	Bitmap bitmap = (Bitmap) extras.get("data");
	        	mIv_Poto.setImageBitmap(bitmap);
	        	photoBitmap = bitmap;
	        	/*try {
	        		File file = new File(User.PHOTO_DIR, System.currentTimeMillis()+".png");
	        		mPotoDir = file.getAbsolutePath();
	        		if(!file.exists())
	        			file.createNewFile();
					bitmap.compress(CompressFormat.PNG, 0, new FileOutputStream(file));
				} catch (Exception e) {
					e.printStackTrace();
				}*/
	        	break;  
	        }  
	        case CAMERA_WITH_DATA: {  
	        	Log.d(TAG, "拍照结束");
	        	doCropPhoto(mCurrentPhotoFile);
	            break;  
	        }
	    }  
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(uService!=null){
			uService.closeDB();
		}
	}

	private byte[] getImageData(Bitmap bitmap){
		int size = bitmap.getWidth()*bitmap.getHeight()*4;
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	private Bitmap getIconFromCursor(Cursor c, int iconIndex) {
		byte[] data = c.getBlob(iconIndex);
		try {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		} catch (Exception e) {
			return null;
		}
	}

}
