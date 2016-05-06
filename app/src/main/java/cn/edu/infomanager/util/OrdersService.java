package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.infomanager.module.Orders;

public class OrdersService {
	private DatabaseHelper dbHelper;
	public OrdersService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	
	//增
	public Boolean insertOrders(Orders orders){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql ="insert into orders(_date, money, in_out, use, remark, user_id) " +
				"values(?,?,?,?,?,?);";
		String[] c = new String[]{
				orders.getDate(),
				orders.getMoney(),
				orders.getIn_out(),
				orders.getUse(),
				orders.getRemark(),
				orders.getUser_id()+""
		};
		database.execSQL(sql, c);
		return true;
	}
	//删
	public Boolean delOrders(int id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from orders where id="+id);
		
		return true;
	}
	//根据user_id删除
	public Boolean delOrdersByUserID(int user_id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from orders where user_id="+user_id);
		return true;
	}
	//改
	public void updateOrders(Orders orders){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update orders set _date=?, money=?, in_out=?, use=?, remark=? where id=?";
		String[] c = new String[]{
				orders.getDate(),
				orders.getMoney(),
				orders.getIn_out(),
				orders.getUse(),
				orders.getRemark(),
				orders.getId()+""
		};
		database.execSQL(sql, c);
	}
	//查
	public List<Orders> getAllOrders(int user_id){
		List<Orders> orders = new ArrayList<Orders>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from orders where user_id=?", new String[]{""+user_id});
		if(query.moveToFirst()){
			do {
				int id = query.getInt(query.getColumnIndex("id"));
				String date = query.getString(query.getColumnIndex("_date"));
				String money = query.getString(query.getColumnIndex("money"));
				String in_out = query.getString(query.getColumnIndex("in_out"));
				String use = query.getString(query.getColumnIndex("use"));
				String remark = query.getString(query.getColumnIndex("remark"));
				
				orders.add(new Orders(id, date, money, in_out, use, remark, user_id));
			} while (query.moveToNext());
		}
		return orders;
	}
	//根据ID查询
	public Orders getOrdersByID(int id){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from orders where id=?", new String[]{id+""});
		
		if(query!=null && query.moveToFirst()){
			int user_id = query.getInt(query.getColumnIndex("user_id"));
			String date = query.getString(query.getColumnIndex("_date"));
			String money = query.getString(query.getColumnIndex("money"));
			String in_out = query.getString(query.getColumnIndex("in_out"));
			String use = query.getString(query.getColumnIndex("use"));
			String remark = query.getString(query.getColumnIndex("remark"));
			return new Orders(id, date, money, in_out, use, remark, user_id);
		}
		return null;
	}
	public void closeDB(){
		dbHelper.close();
	}
}
