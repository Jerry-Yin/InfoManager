package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.infomanager.module.WorkPlan;

public class WorkplanService {
	private DatabaseHelper dbHelper;
	public WorkplanService(Context context){
		dbHelper=new DatabaseHelper(context);
	}

	//增
	public Boolean insertWorkPlan(WorkPlan workplan){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql ="insert into workplan(summary, remark, time, _date, user_id) " +
				"values(?,?,?,?,?);";
		String[] c = new String[]{
				workplan.getSummary(),
				workplan.getRemark(),
				workplan.getTime(),
				workplan.getDate(),
				workplan.getUser_id()+""
		};
		database.execSQL(sql, c);
		return true;
	}
	//删
	public Boolean delWorkPlan(int id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from workplan where id="+id);
		
		return true;
	}
	//根据user_id删除
	public Boolean delWorkPlanByUserID(int user_id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from workplan where user_id="+user_id);
		return true;
	}
	//改
	public void updateWorkPlan(WorkPlan workplan){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update workplan set summary=?, remark=?, time=?, _date=? where id=?";
		String[] c = new String[]{
				workplan.getSummary(),
				workplan.getRemark(),
				workplan.getTime(),
				workplan.getDate(),
				workplan.getId()+""
		};
		database.execSQL(sql, c);
	}
	//查
	public List<WorkPlan> getAllWorkPlan(int user_id){
		List<WorkPlan> workplan = new ArrayList<WorkPlan>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from workplan where user_id=?", new String[]{""+user_id});
		if(query.moveToFirst()){
			do {
				int id = query.getInt(query.getColumnIndex("id"));
				String date = query.getString(query.getColumnIndex("_date"));
				String summary = query.getString(query.getColumnIndex("summary"));
				String remark = query.getString(query.getColumnIndex("remark"));
				String time = query.getString(query.getColumnIndex("time"));
				
				workplan.add(new WorkPlan(id, summary, remark, time, date, user_id));
			} while (query.moveToNext());
		}
		return workplan;
	}
	//根据ID查询
	public WorkPlan getWorkPlanByID(int id){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from workplan where id=?", new String[]{id+""});
		
		if(query!=null && query.moveToFirst()){
			int user_id = query.getInt(query.getColumnIndex("user_id"));
			String date = query.getString(query.getColumnIndex("_date"));
			String summary = query.getString(query.getColumnIndex("summary"));
			String remark = query.getString(query.getColumnIndex("remark"));
			String time = query.getString(query.getColumnIndex("time"));
			
			return new WorkPlan(id, summary, remark, time, date, user_id);
		}
		return null;
	}
	public void closeDB(){
		dbHelper.close();
	}
}
