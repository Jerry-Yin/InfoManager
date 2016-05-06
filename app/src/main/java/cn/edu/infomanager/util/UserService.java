package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.edu.infomanager.module.User;

public class UserService {
	public DatabaseHelper dbHelper;
	public UserService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	
	/**
	 * 登录	并获得User的信息
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username,String password){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username=? and password=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{username,password});
		if(cursor.moveToFirst()==true){
			String id = cursor.getString(cursor.getColumnIndex(User.ID));
			String name = cursor.getString(cursor.getColumnIndex(User.NAME));
			String email = cursor.getString(cursor.getColumnIndex(User.EMAIL));
			String age = cursor.getString(cursor.getColumnIndex(User.AGE));
			String sex = cursor.getString(cursor.getColumnIndex(User.SEX));
//			String poto = cursor.getString(cursor.getColumnIndex(User.POTO));
			cursor.close();
			return new User(Integer.parseInt(id), name, "", email, Integer.parseInt(age), sex, null);
		}
		cursor.close();
		return null;
	}
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	public boolean register(User user){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="insert into user("+User.NAME+","+User.PASSWORD+","+User.EMAIL+","+User.AGE+","+User.SEX+","+User.POTO+") values(?,?,?,?,?,?)";
		Object obj[]={user.getUsername().trim(),user.getPassword().trim(),user.getEmail().trim(),user.getAge(),user.getSex(),user.getPoto()};
		sdb.execSQL(sql, obj);	
		return true;
	}
	/**
	 * 验证    给定用户名称  是否已经存在
	 * @param name
	 * @return
	 */
	public boolean checkUserExist(String name){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		String sql="select * from user where username=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{name.trim()});
		if(cursor.moveToFirst()==true){
			cursor.close();
			return true;
		}
		return false;
	}
	/**
	 * 根据ID查询用户信息
	 * @param id
	 * @return
	 */
	public User getUserInfoByID(int id){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where id=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{id+""});
		if(cursor.moveToFirst()==true){
			String name = cursor.getString(cursor.getColumnIndex(User.NAME));
			String email = cursor.getString(cursor.getColumnIndex(User.EMAIL));
			String age = cursor.getString(cursor.getColumnIndex(User.AGE));
			String sex = cursor.getString(cursor.getColumnIndex(User.SEX));
			byte[] poto = cursor.getBlob(cursor.getColumnIndex(User.POTO));
			cursor.close();
			return new User(id, name, "", email, Integer.parseInt(age), sex, poto);
		}
		cursor.close();
		return null;
	}
	/**
	 * 根据Name查询用户信息
	 * @param id
	 * @return
	 */
	public User getUserInfoByName(String name){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{name.trim()});
		if(cursor.moveToFirst()==true){
			int id = cursor.getInt(cursor.getColumnIndex(User.ID));
			String email = cursor.getString(cursor.getColumnIndex(User.EMAIL));
			String age = cursor.getString(cursor.getColumnIndex(User.AGE));
			String sex = cursor.getString(cursor.getColumnIndex(User.SEX));
			byte[] poto = cursor.getBlob(cursor.getColumnIndex(User.POTO));
			cursor.close();
			return new User(id, name, "", email, Integer.parseInt(age), sex, poto);
		}
		cursor.close();
		return null;
	}

	public User findPassword(String name, String email){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username=? and email=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{name.trim(), email.trim()});
		if(cursor.moveToFirst()==true){
			int id = cursor.getInt(cursor.getColumnIndex(User.ID));
			String pass = cursor.getString(cursor.getColumnIndex(User.PASSWORD));
			String age = cursor.getString(cursor.getColumnIndex(User.AGE));
			String sex = cursor.getString(cursor.getColumnIndex(User.SEX));
			byte[] poto = cursor.getBlob(cursor.getColumnIndex(User.POTO));
			cursor.close();
			return new User(id, name, pass, email, Integer.parseInt(age), sex, poto);
		}
		cursor.close();
		return null;
	}
	
	public Boolean changePass(User user){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update user set password=? where id=?";
		database.execSQL(sql, new String[]{user.getPassword(), user.getId()+""});
		return true;
	}
	
	public Boolean delUser(User user){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "delete from user where id="+user.getId();
		database.execSQL(sql);
		return true;
	}
	
	
	public void closeDB(){
		dbHelper.close();
	}
}
