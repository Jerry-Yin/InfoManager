package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.infomanager.module.Blog;


public class BlogService {
	private DatabaseHelper dbHelper;
	public BlogService(Context context){
		dbHelper=new DatabaseHelper(context);
	}

	//增
	public Boolean insertBlog(Blog blog){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql ="insert into blog(summary, world, _date, poto, video, audio, user_id) " +
				"values(?,?,?,?,?,?,?);";
		Object[] c = new Object[]{
				blog.getSummary(),
				blog.getWorld(),
				blog.getDate(),
				blog.getPoto(),
				blog.getVideo(),
				blog.getAudio(),
				blog.getUser_id()+""
		};
	  	database.execSQL(sql, c);
		return true;
	}
	//删
	public Boolean delBlog(int id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from blog where id="+id);
		
		return true;
	}
	//根据user_id删除
	public Boolean delBlogByUserID(int user_id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from blog where user_id="+user_id);
		return true;
	}
	
	//改
	public void updateBlog(Blog blog){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update blog set summary=?, world=?, _date=?, poto=?, video=?, audio=? where id=?";
		Object[] c = new Object[]{
				blog.getSummary(),
				blog.getWorld(),
				blog.getDate(),
				blog.getPoto(),
				blog.getVideo(),
				blog.getAudio(),
				blog.getId()+""
		};
		database.execSQL(sql, c);
	}
	//查
	public List<Blog> getAllBlog(int user_id){
		List<Blog> blog = new ArrayList<Blog>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from blog where user_id=? order by _date desc", new String[]{""+user_id});
		if(query.moveToFirst()){
			do {
				int id = query.getInt(query.getColumnIndex("id"));
				String date = query.getString(query.getColumnIndex("_date"));
				String summary = query.getString(query.getColumnIndex("summary"));
				String world = query.getString(query.getColumnIndex("world"));
				String poto = query.getString(query.getColumnIndex("poto"));
				String video = query.getString(query.getColumnIndex("video"));
				String audio = query.getString(query.getColumnIndex("audio"));
				
				blog.add(new Blog(id, world, summary, audio, video, user_id, poto, date));
			} while (query.moveToNext());
		}
		return blog;
	}
	//根据ID查询
	public Blog getBlogByID(int id){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from blog where id=?", new String[]{id+""});
		
		if(query!=null && query.moveToFirst()){
			int user_id = query.getInt(query.getColumnIndex("user_id"));
			String date = query.getString(query.getColumnIndex("_date"));
			String summary = query.getString(query.getColumnIndex("summary"));
			String world = query.getString(query.getColumnIndex("world"));
			String poto = query.getString(query.getColumnIndex("poto"));
			String video = query.getString(query.getColumnIndex("video"));
			String audio = query.getString(query.getColumnIndex("audio"));
			
			return new Blog(id, world, summary, audio, video, user_id, poto, date);
		}
		return null;
	}
	public void closeDB(){
		dbHelper.close();
	}
}
