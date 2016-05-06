package cn.edu.infomanager.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.infomanager.module.User;


public class DatabaseHelper extends SQLiteOpenHelper {
    static String name = "user.db";
    static int dbVersion = 1;

    public DatabaseHelper(Context context) {
        super(context, name, null, dbVersion);
    }

    /* 创建表
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
//		String sql="create table user(id integer primary key autoincrement,username varchar(20),password varchar(20),age integer,sex varchar(2))";
        String usersql = "create table user("
                + User.ID + " integer primary key autoincrement,"
                + User.NAME + " varchar(20),"
                + User.PASSWORD + " varchar(20),"
                + User.EMAIL + " varchar(30),"
                + User.AGE + " integer,"
                + User.SEX + " varchar(2),"
                + User.POTO + " blob" +
                ");";

        String memorsql = "create table memorandum(" +
                "id integer primary key autoincrement, " +
                "category integer not null, " +
                "summary text not null, " +
                "description text not null, " +
                "_date varchar not null, " +
                "is_remind integer not null," +
                "user_id integer, " +
                "constraint user_id_FK foreign key(user_id) references user(id) on delete cascade" +
                ");";
        String contactssql = "create table contacts(" +
                "id integer primary key autoincrement, " +
                "name varchar not null, " +
                "sex varchar, " +
                "birthday varchar, " +
                "nation varchar, " +
                "nickname varchar, " +
                "bloodtype varchar, " +
                "address varchar , " +
                "tel varchar, " +
                "email varchar, " +
                "user_id integer, " +
                "constraint user_id_FK foreign key(user_id) references user(id) on delete cascade" +
                ");";
        String orderssql = "create table orders(" +
                "id integer primary key autoincrement, " +
                "_date varchar, " +
                "money varchar, " +
                "in_out varchar, " +
                "use varchar, " +
                "remark varchar, " +
                "user_id integer, " +
                "constraint user_id_FK foreign key(user_id) references user(id) on delete cascade" +
                ");";
        String blogsql = "create table blog(" +
                "id integer primary key autoincrement, " +
                "poto varchar, " +
                "video varchar, " +
                "audio varchar, " +
                "world text, " +
                "summary varchar, " +
                "_date varchar, " +
                "user_id integer, " +
                "constraint user_id_FK foreign key(user_id) references user(id) on delete cascade" +
                ");";

        String workplansql = "create table workplan(" +
                "id integer primary key autoincrement, " +
                "summary varchar, " +
                "remark varchar, " +
                "time varchar, " +
                "_date varchar, " +
                "user_id integer, " +
                "constraint user_id_FK foreign key(user_id) references user(id) on delete cascade" +
                ");";

        db.execSQL(usersql);
        db.execSQL(memorsql);
        db.execSQL(contactssql);
        db.execSQL(orderssql);
        db.execSQL(blogsql);
        db.execSQL(workplansql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Con
//        db.update("memorandum", )
    }

}
