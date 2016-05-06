/**
 * 
 */
package cn.edu.infomanager.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.Memorandum;
import cn.edu.infomanager.util.DatabaseHelper;

/**
 * @date 2012-5-2
 * @time 下午04:39:22
 */
public class MenorandumAdapter extends BaseAdapter {

	private static final String TAG = "MenorandumAdapter";

	private Context mContext;
	private List<Memorandum> infos;
	private LayoutInflater inflater;
	private DateFormat timeFormat = DateFormat.getDateTimeInstance();
	
	public MenorandumAdapter(Context context, List<Memorandum> infos) {
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
	public View getView(final int position, View arg1, ViewGroup arg2) {

		View itemv = inflater.inflate(R.layout.lv_meme_item, null);
		TextView tv_summary = (TextView) itemv.findViewById(R.id.item_summary);
		TextView tv_discreption = (TextView) itemv.findViewById(R.id.item_discreption);
		TextView tv_date = (TextView) itemv.findViewById(R.id.item_date);
		TextView tv_category = (TextView) itemv.findViewById(R.id.item_categray);
		CheckBox cb_remind = (CheckBox) itemv.findViewById(R.id.checkbox_remind);
		cb_remind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
				ContentValues values = new ContentValues();
				Memorandum memorandum = infos.get(position);
				// TODO: 4/28/16 更新数据库 
				if (isChecked) {
					memorandum.setIsRemind(1);
					values.put("is_remind", 1);
					db.update("memorandum", values, "id = ?", new String[]{String.valueOf(memorandum.getId())});

					Log.d(TAG, "info1 = " + memorandum.getId() + ", " + memorandum.getUserid() + ", " + memorandum.isRemind());
				} else {
					memorandum.setIsRemind(0);
					values.put("is_remind", 0);
					db.update("memorandum", values, "id = ?", new String[]{String.valueOf(memorandum.getId())});
					Log.d(TAG, "info1 = " + memorandum.getId() + ", " + memorandum.getUserid() + ", " + memorandum.isRemind());
				}
			}
		});

		Memorandum info = infos.get(position);
		tv_summary.setText(info.getSummary());
		tv_discreption.setText(info.getDescription());
		long datel = Long.parseLong(info.getDate());
		tv_date.setText(timeFormat.format(new Date(datel)));
		Log.d(TAG, "info = "+ info.getId()+", "+info.getUserid()+", "+info.isRemind());
		if (info.isRemind() == 1){
			cb_remind.setChecked(true);
		}else if (info.isRemind() == 0){
			cb_remind.setChecked(false);
		}

		String c = "普通";
		switch (info.getCategory()) {
		case 0:
			c="困难";
			break;
		case 1:
			c="普通";
			break;
		case 2:
			c="简单";
			break;
		default:
			break;
		}
		tv_category.setText(c);
		return itemv;
	}

}
