package com.example.leunglokyin03a.MyDBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
//call by MyDbAdapter
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //create table
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE record (_id INTEGER PRIMARY KEY, MediumName TEXT, time TEXT , date TEXT, everyday TEXT, Duration TEXT)";
        db.execSQL("CREATE TABLE photo(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, image BLOB)");
        db.execSQL("CREATE TABLE phone(id INTEGER PRIMARY KEY AUTOINCREMENT, phone TEXT)");
        db.execSQL("CREATE TABLE stepcount(id INTEGER PRIMARY KEY AUTOINCREMENT, totalsteps TEXT, stepsnow TEXT)");
        Log.v("myLog", "sql: " +sql);
        db.execSQL(sql);
    }
    //drop table
    //onUpgrade when the database change will do it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "DROP TABLE IF EXISTS record";

        // Drop older table if existed
        db.execSQL(sql);
        db.execSQL("DROP TABLE IF EXISTS stepcount");
        db.execSQL("DROP TABLE IF EXISTS phone");
        db.execSQL("DROP TABLE IF EXISTS photo");
        onCreate(db);
    }

    public boolean insertdata(String name, byte[] image) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        long ins = MyDB.insert("photo", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    public MyDBHelper(Context context) {
        super(context, "photo.db" , null, 1);
    }

    public void deleteData(int _id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "DELETE FROM photo WHERE _id = ?";
        SQLiteStatement statement = MyDB.compileStatement(sql);
        statement.bindDouble(1,(double)_id);

        statement.execute();
        MyDB.close();
    }
}