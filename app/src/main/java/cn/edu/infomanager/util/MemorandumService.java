package cn.edu.infomanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.infomanager.module.Memorandum;

public class MemorandumService {
    private DatabaseHelper dbHelper;

    public MemorandumService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //增
    public Boolean insertMemorandum(Memorandum m) {

        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        String sql = "insert into memorandum (id,category,summary,description,_date,is_remind,user_id) values(?,?,?,?,?,?,?)";
        Object obj[] = {
                m.getId()
                , m.getCategory()
                , m.getSummary()
                , m.getDescription()
                , m.getDate()
                , m.isRemind()
                , m.getUserid()
        };
        sdb.execSQL(sql, obj);
        return true;
    }

    //查
    public List<Memorandum> slectAllInfo(int _id) {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select * from memorandum where user_id=? order by _date desc, category";
        Cursor query = sdb.rawQuery(sql, new String[]{_id + ""});
        ArrayList<Memorandum> infos = new ArrayList<Memorandum>();
        if (query.moveToFirst()) {
            do {
                int id = query.getInt(query.getColumnIndex("id"));
                int category = query.getInt(query.getColumnIndex("category"));
                String summary = query.getString(query.getColumnIndex("summary"));
                String description = query.getString(query.getColumnIndex("description"));
                String _date = query.getString(query.getColumnIndex("_date"));
                int user_id = query.getInt(query.getColumnIndex("user_id"));
                int isRemind = query.getInt(query.getColumnIndex("is_remind"));
                infos.add(new Memorandum(id, category, summary, description, _date, user_id, isRemind));
            } while (query.moveToNext());
        }
        return infos;
    }

    //查
    public Memorandum selectInfoByID(int id) {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql = "select * from memorandum where id=? order by _date desc, category";
        Cursor query = sdb.rawQuery(sql, new String[]{id + ""});

        if (query != null && query.moveToFirst()) {
            int category = query.getInt(query.getColumnIndex("category"));
            String summary = query.getString(query.getColumnIndex("summary"));
            String description = query.getString(query.getColumnIndex("description"));
            String _date = query.getString(query.getColumnIndex("_date"));
            int user_id = query.getInt(query.getColumnIndex("user_id"));
            int isRemind = query.getInt(query.getColumnIndex("is_remind"));
            return new Memorandum(id, category, summary, description, _date, user_id, isRemind);
        }
        return null;
    }

    //删
    public boolean deleteMemorandum(int id, int userid) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("delete from memorandum where id=?", new String[]{id + ""});
        //每次删除以后就自动更行 ID， 重新排列id
        updateId(database, userid);
        return true;
    }

    //改
    public boolean updateMemorandum(Memorandum m) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "update memorandum set category=?,summary=?,description=?,_date=? where id=?;";
        Object obj[] = {
                m.getCategory()
                , m.getSummary()
                , m.getDescription()
                , m.getDate()
                , m.getId()};
        database.execSQL(sql, obj);
        return true;
    }

    //每次删除后重新更新排序ID
    private void updateId(SQLiteDatabase db, int usrId){
        //update test set _id=3 where _id=4;
        List<Memorandum> list = slectAllInfo(usrId);
        if (list.size()>0){
            for (int i=0; i<list.size(); i++){
                String updateId = "update memorandum set id="+i+" where id="+list.get(i).getId();
                db.execSQL(updateId);
            }
        }
    }

    //删
    public Boolean delInfoByUserID(String userid) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("delete from memorandum where user_id=" + userid);

        return true;
    }

    public void closeDB() {
        dbHelper.close();
    }

}
