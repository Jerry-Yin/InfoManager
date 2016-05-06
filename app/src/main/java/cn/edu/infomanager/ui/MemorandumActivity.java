package cn.edu.infomanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.edu.infomanager.R;
import cn.edu.infomanager.adapter.MenorandumAdapter;
import cn.edu.infomanager.module.Memorandum;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.MemorandumService;
import cn.edu.infomanager.util.UserService;

public class MemorandumActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "BusinessManager";
	
	private User user_info;
	private MemorandumService mService;
	private ListView listView;
	private List<Memorandum> infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		if(id<0){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.ac_memorandum);
		
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_infos);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mService = new MemorandumService(this);
		infos = mService.slectAllInfo(user_info.getId());
		MenorandumAdapter adapter = new MenorandumAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	View iview;
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowMemorandumActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());

		if (infos.size()>0){
			bundle.putInt("position", infos.size());	//传递过去当前的item id
		}
//		bundle.putInt("id", infos.get(position).getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mService!=null)
			mService.closeDB();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowMemorandumActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
//		bundle.putInt("id", infos.get(position).getId());
		bundle.putInt("id", infos.get(position).getId());

		bundle.putInt("position", infos.size()-1-position);	//list 与 ID 是相反的
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
