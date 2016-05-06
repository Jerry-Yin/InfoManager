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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.Contacts;
import cn.edu.infomanager.util.ContactsService;

public class ShowContactsActivity extends Activity {
	private static final String TAG="ShowContactsActivity";
	
	private EditText et_name;
	private EditText et_tel;
	private EditText et_address;
	private EditText et_email;
	private EditText et_birthday;
	private EditText et_nation;
	private EditText et_bloodtype;
	private EditText et_nickname;
	private RadioGroup et_sex;
	
	private Button bt_save_update;
	private Button bt_contacts_del;
	
	private ContactsService cService;
	private Boolean isUpdate = false;
	private int id;
	private int userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_contacts);
		findView();
		
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id",-1);
		userid = intent.getExtras().getInt("user_id",-1);
		cService = new ContactsService(this);
		
		if(id<0){
			bt_contacts_del.setText("舍弃");
		}else{
			bt_save_update.setText("保存修改");
			isUpdate=true;
			initUpdateView(id);
		}
	}
	public void bt_SaveUpdate(View view){
		Contacts info = getInfo();
		if(info==null){
			return;
		}
		if(isUpdate){
			info.setUserid(userid);
			info.setId(id);
			cService.updateContacts(info);
			Toast.makeText(this, "保存修改", Toast.LENGTH_SHORT).show();
			finish();
		}else{
			info.setUserid(userid);
			cService.insertContacts(info);
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
		.setMessage("将删除当前联系人的资料")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cService.delContacts(id);
				
				Toast.makeText(ShowContactsActivity.this, "删除联系人成功", Toast.LENGTH_SHORT).show();
				ShowContactsActivity.this.finish();
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
		et_name = (EditText) findViewById(R.id.contacts_name);
		et_tel = (EditText) findViewById(R.id.contacts_tel);
		et_address = (EditText) findViewById(R.id.contacts_address);
		et_email = (EditText) findViewById(R.id.contacts_email);
		et_birthday = (EditText) findViewById(R.id.contacts_birthday);
		et_nation = (EditText) findViewById(R.id.contacts_nation);
		et_bloodtype = (EditText) findViewById(R.id.contacts_bloodtype);
		et_nickname = (EditText) findViewById(R.id.contacts_nickname);
		et_sex = (RadioGroup) findViewById(R.id.contacts_sex);
		
		bt_save_update = (Button) findViewById(R.id.save_update);
		bt_contacts_del = (Button) findViewById(R.id.contacts_del);
	}
	private void initUpdateView(int id){
		Contacts contact = cService.getContactsByID(id);
		et_name.setText(contact.getName());
		et_tel.setText(contact.getTel());
		et_address.setText(contact.getAddress());
		et_email.setText(contact.getEmail());
		et_birthday.setText(contact.getBirthday());
		et_nation.setText(contact.getNation());
		et_bloodtype.setText(contact.getBloodtype());
		et_nickname.setText(contact.getNickname());
		
		if("男".equals(contact.getSex())){
			RadioButton n = (RadioButton) findViewById(R.id.nan);
			n.setChecked(true);
		}else{
			RadioButton w = (RadioButton) findViewById(R.id.woman);
			w.setChecked(true);
		}
		
	}
	
	private Contacts getInfo(){
		if("".equals(et_name.getText().toString())){
			Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		String sexstr=((RadioButton)findViewById(et_sex.getCheckedRadioButtonId())).getText().toString();
		
		return new Contacts(-1, -1, et_name.getText().toString(),
				sexstr, et_tel.getText().toString(),
				et_email.getText().toString(), et_birthday.getText().toString(),
				et_nation.getText().toString(), et_bloodtype.getText().toString(),
				et_address.getText().toString(), et_nickname.getText().toString());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cService!=null)
			cService.closeDB();
	}
}
