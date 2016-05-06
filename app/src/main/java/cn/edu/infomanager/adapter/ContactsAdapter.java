/**
 * 
 */
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
import cn.edu.infomanager.module.Contacts;

/**
 * @date 2012-5-2
 * @time 下午04:39:22
 */
public class ContactsAdapter extends BaseAdapter {

	private Context mContext;
	private List<Contacts> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance();
	
	public ContactsAdapter(Context context, List<Contacts> infos) {
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
		
		View itemv = inflater.inflate(R.layout.lv_contacts_item, null);
		TextView tv_name = (TextView) itemv.findViewById(R.id.item_name);
		TextView tv_tel = (TextView) itemv.findViewById(R.id.item_tel);
		
		Contacts info = infos.get(position);
		tv_name.setText(info.getName());
		tv_tel.setText(info.getTel());
		
		return itemv;
	}

}
