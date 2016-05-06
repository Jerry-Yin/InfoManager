package cn.edu.infomanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.WorkPlan;

public class WorkplanAdapter extends BaseAdapter {

	private Context mContext;
	private List<WorkPlan> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance();
	
	public WorkplanAdapter(Context context, List<WorkPlan> infos) {
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		
		View itemv = inflater.inflate(R.layout.lv_order_item, null);
		TextView tv_use = (TextView) itemv.findViewById(R.id.order_use);
		TextView tv_money = (TextView) itemv.findViewById(R.id.order_money);
		TextView tv_date = (TextView) itemv.findViewById(R.id.order_date);
		
		WorkPlan info = infos.get(position);
		tv_use.setText(info.getSummary());
		tv_money.setText(info.getRemark());
		tv_date.setText(info.getDate());
		
		return itemv;
	}

}