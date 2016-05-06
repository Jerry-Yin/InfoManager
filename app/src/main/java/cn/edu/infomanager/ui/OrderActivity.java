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
import cn.edu.infomanager.adapter.OrdersAdapter;
import cn.edu.infomanager.module.Orders;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.OrdersService;
import cn.edu.infomanager.util.UserService;

public class OrderActivity extends Activity implements OnItemClickListener {
	private static final String TAG = "OrderActivity";
	
	private User user_info;
	private int user_id;
	private String name;
	private ListView listView;
	private List<Orders> infos;
	private OrdersService oService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		user_id = intent.getExtras().getInt("id",-1);
		name = intent.getExtras().getString("name");
		if(user_id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.ac_orders);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(user_id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_contacts);
	}
	@Override
	protected void onResume() {
		super.onResume();
		oService = new OrdersService(this);
		infos = oService.getAllOrders(user_info.getId());
		OrdersAdapter adapter = new OrdersAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	public void btAdd(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShowOrdersActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(this, ShowOrdersActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("user_id", user_info.getId());
		bundle.putInt("id", infos.get(position).getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}


	public void toChartView(View view){
		if(user_id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}else {
			Intent intent = new Intent(this, OrderChartViewActivity.class);
			intent.putExtra("user_id", user_id);
			intent.putExtra("name", name);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(oService!=null)
			oService.closeDB();
	}
}
