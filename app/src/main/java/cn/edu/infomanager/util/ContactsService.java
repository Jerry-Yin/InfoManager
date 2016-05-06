package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.infomanager.module.Contacts;

public class ContactsService {
	private DatabaseHelper dbHelper;
	public ContactsService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	
	//增
	public Boolean insertContacts(Contacts contacts){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql ="insert into contacts(name, sex, birthday, nation, nickname, bloodtype, address, tel, email, user_id) " +
				"values(?,?,?,?,?,?,?,?,?,?);";
		String[] c = new String[]{
				contacts.getName(),
				contacts.getSex(),
				contacts.getBirthday(),
				contacts.getNation(),
				contacts.getNickname(),
				contacts.getBloodtype(),
				contacts.getAddress(),
				contacts.getTel(),
				contacts.getEmail(),
				contacts.getUserid()+""
		};
		database.execSQL(sql, c);
		
		return true;
	}
	//删
	public Boolean delContacts(int id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from contacts where id="+id);
		
		return true;
	}
	//根据user_id删除
	public Boolean delContactsByUserID(int user_id){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from contacts where user_id="+user_id);
		return true;
	}
	//改
	public void updateContacts(Contacts contacts){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "update contacts set name=?, sex=?, birthday=?, nation=?, nickname=?, bloodtype=?, " +
				"address=?, tel=?, email=? where id=?";
		String[] c = new String[]{
				contacts.getName(),
				contacts.getSex(),
				contacts.getBirthday(),
				contacts.getNation(),
				contacts.getNickname(),
				contacts.getBloodtype(),
				contacts.getAddress(),
				contacts.getTel(),
				contacts.getEmail(),
				contacts.getId()+""
		};
		database.execSQL(sql, c);
	}
	//查
	public List<Contacts> getAllContacts(int user_id){
		List<Contacts> contacts = new ArrayList<Contacts>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from contacts where user_id=?", new String[]{""+user_id});
		if(query.moveToFirst()){
			do {
				int id = query.getInt(query.getColumnIndex("id"));
				String name = query.getString(query.getColumnIndex("name"));
				String sex = query.getString(query.getColumnIndex("sex"));
				String birthday = query.getString(query.getColumnIndex("birthday"));
				String nation = query.getString(query.getColumnIndex("nation"));
				String nickname = query.getString(query.getColumnIndex("nickname"));
				String bloodtype = query.getString(query.getColumnIndex("bloodtype"));
				String address = query.getString(query.getColumnIndex("address"));
				String tel = query.getString(query.getColumnIndex("tel"));
				String email = query.getString(query.getColumnIndex("email"));
				
				contacts.add(new Contacts(id, user_id, name, sex, tel, email, birthday, nation, bloodtype, address, nickname));
			} while (query.moveToNext());
		}
		return contacts;
	}
	//根据ID查询
	public Contacts getContactsByID(int id){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor query = database.rawQuery("select * from contacts where id=?", new String[]{id+""});
		
		if(query!=null && query.moveToFirst()){
			int user_id = query.getInt(query.getColumnIndex("user_id"));
			String name = query.getString(query.getColumnIndex("name"));
			String sex = query.getString(query.getColumnIndex("sex"));
			String birthday = query.getString(query.getColumnIndex("birthday"));
			String nation = query.getString(query.getColumnIndex("nation"));
			String nickname = query.getString(query.getColumnIndex("nickname"));
			String bloodtype = query.getString(query.getColumnIndex("bloodtype"));
			String address = query.getString(query.getColumnIndex("address"));
			String tel = query.getString(query.getColumnIndex("tel"));
			String email = query.getString(query.getColumnIndex("email"));
			return new Contacts(id, user_id, name, sex, tel, email, birthday, nation, bloodtype, address, nickname);
		}
		return null;
	}
	public void closeDB(){
		dbHelper.close();
	}
}
