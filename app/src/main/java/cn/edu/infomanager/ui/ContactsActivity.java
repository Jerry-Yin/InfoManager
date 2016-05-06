package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.edu.infomanager.R;
import cn.edu.infomanager.adapter.ContactsAdapter;
import cn.edu.infomanager.module.Contacts;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.ContactsService;
import cn.edu.infomanager.util.UserService;

public class ContactsActivity extends Activity implements OnItemClickListener, AdapterView.OnItemLongClickListener {

	private static final String TAG = "ContactsActivity";
	
	private User user_info;
	private ContactsService cService;
	private ListView listView;
	private List<Contacts> infos;
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
		setContentView(R.layout.ac_contacts);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_contacts);
	}
	@Override
	protected void onResume() {
		super.onResume();
		cService = new ContactsService(this);
		infos = cService.getAllContacts(user_info.getId());
		ContactsAdapter adapter = new ContactsAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	/**
	 * 添加联系人
	 * @param view
	 */
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowContactsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cService!=null)
			cService.closeDB();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowContactsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		bundle.putInt("id", infos.get(position).getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	// 长按拨号
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		new AlertDialog.Builder(this)
				.setPositiveButton("拨号", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:"+infos.get(position).getTel()));
						startActivity(intent);
					}
				})
				.create().show();
		return true;
	}

	//打开系统联系人
	public void openSystemContacts(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW,android.provider.ContactsContract.Contacts.CONTENT_URI);
		startActivity(intent);
	}
}
