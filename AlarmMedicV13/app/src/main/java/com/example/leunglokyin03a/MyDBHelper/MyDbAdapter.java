package com.example.leunglokyin03a.MyDBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leunglokyin03a.MainActivity;
import com.example.leunglokyin03a.R;
import com.example.leunglokyin03a.photo;
import com.example.leunglokyin03a.photolist;

import java.util.ArrayList;

//call by MainActivity
public class MyDbAdapter extends BaseAdapter {
    private static String DB_NAME = "medicine record";
    private static int DB_VERSION = 1;

    private Context context;
    private int layout;
    private ArrayList<photo> photolist;
     private SQLiteOpenHelper myDBHelper;
    //set version and dbname
    public MyDbAdapter(Context context){
        this.context = context;
        this.myDBHelper= new MyDBHelper(this.context,DB_NAME,null,DB_VERSION);
    }

    public MyDbAdapter(Context context, int layout, ArrayList<photo> photolist) {
        this.context = context;
        this.layout = layout;
        this.photolist = photolist;
    }

    //insert data
    public long insert(String MediumName, String time, String date, String everyday, String Duration){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MediumName", MediumName);
        contentValues.put("time", time);
        contentValues.put("date", date);
        contentValues.put("everyday", everyday);
        contentValues.put("Duration", Duration);
        //write database
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        long numRows = sqlDB.insert("record",null,contentValues);
        sqlDB.close();

        return numRows;
    }
    public long insertsteps(String totalsteps, String stepsnow){
        ContentValues contentValues = new ContentValues();
        contentValues.put("totalsteps", totalsteps);
        contentValues.put("stepsnow", stepsnow);
        //write database
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        long numRows = sqlDB.insert("stepcount",null,contentValues);
        sqlDB.close();

        return numRows;
    }

    public long insertPhone(String phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        //write database
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        long numRows = sqlDB.insert("phone",null,contentValues);
        sqlDB.close();

        return numRows;
    }
    //select all item in database
    public Cursor selectAll() {
        //read database
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        //select sql
        //cursor is data type
        Cursor cursor = sqlDB.query("record", new String[]{"_id", "MediumName", "time", "date" ,"everyday", "Duration"}
        , null, null, null, null, null);
        displayCursor(cursor);
        //close database
        sqlDB.close();
        //return cursor
        return cursor;
    }
    //select all item in database
    public Cursor selectAllPhone() {
        //read database
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        //select sql
        //cursor is data type
        Cursor cursor = sqlDB.query("phone", new String[]{"id", "phone"}
                , null, null, null, null, null);
        displayCursor(cursor);
        //close database
        sqlDB.close();
        //return cursor
        return cursor;
    }

    public Cursor getdata() {
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("record", new String[]{"_id", "MediumName", "time", "date" ,"everyday", "Duration"}
                , null, null, null, null, null);
        return cursor;

    }
    public Cursor getsteps() {
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("stepcount", new String[]{"id", "totalsteps", "stepsnow"}
                , null, null, null, null, null);
        return cursor;

    }
    public Cursor getdataPhone() {
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("phone", new String[]{"id", "phone"}
                , null, null, null, null, null);
        return cursor;

    }
    public Cursor selectname(String contact_name){
        // public Cursor query (
        //   String   table,
        //	 String[] columns,
        //	 String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("record", new String[]{"_id", "MediumName", "time", "date" ,"everyday", "Duration"}
        , "MediumName = ?", new String[]{contact_name}, null, null, null);

        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i++) {
            int _id = cursor.getInt(0);
            String MediumName = cursor.getString(1);
            int time = cursor.getInt(2);
            int date = cursor.getInt(3);

            cursor.moveToNext();
        }

        return cursor;
    }
    public Cursor selectPhone(String contact_name){
        // public Cursor query (
        //   String   table,
        //	 String[] columns,
        //	 String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("phone", new String[]{"id", "phone"}
                , "phone = ?", new String[]{contact_name}, null, null, null);

        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(0);
            String phone = cursor.getString(1);

            cursor.moveToNext();
        }

        return cursor;
    }
    public Cursor selectstep(String contact_name){
        // public Cursor query (
        //   String   table,
        //	 String[] columns,
        //	 String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query("stepcount", new String[]{"id", "totalsteps", "stepsnow"}
                , "id = ?", new String[]{contact_name}, null, null, null);

        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(0);
            String totalsteps = cursor.getString(1);
            String stepsnow = cursor.getString(1);

            cursor.moveToNext();
        }

        return cursor;
    }
    //display Cursor
    private void displayCursor(Cursor cursor) {
        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++) {
            //getInt(column 0)
            int id = cursor.getInt(0);
            //getInt(column 1)
            String MediumName = cursor.getString(1);
            String time = cursor.getString(2);
            String date = cursor.getString(3);
            Log.v("myLog", "id: "+ id +" MedicineName: "+MediumName+" time: " + time +"date" + date);
            //next row
            cursor.moveToNext();
        }
    }
    public Cursor selectOnesteps(String contact_name){
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        //selection = where
        //contact_name = the name you want to search
        Cursor cursor = sqlDB.query("stepcount", new String[]{"id", "totalsteps", "stepsnow"}
                , "id = ?", new String[]{contact_name}, null, null, null);
        displayCursor(cursor);
        sqlDB.close();
        return cursor;
    }
    public Cursor selectOnePhone(String contact_name){
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        //selection = where
        //contact_name = the name you want to search
        Cursor cursor = sqlDB.query("photo", new String[]{"id", "photo"}
                , "photo = ?", new String[]{contact_name}, null, null, null);
        displayCursor(cursor);
        sqlDB.close();
        return cursor;
    }
    public Cursor selectOneRecord(String contact_name){
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
        //selection = where
        //contact_name = the name you want to search
        Cursor cursor = sqlDB.query("record", new String[]{"_id", "MediumName", "time", "date" ,"everyday", "Duration"}
                , "MediumName = ?", new String[]{contact_name}, null, null, null);
        displayCursor(cursor);
        sqlDB.close();
        return cursor;
    }
    public int updatetotalsteps(String currentstep, String new_step){

        ContentValues values = new ContentValues();
        values.put("totalsteps", new_step);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("stepcount", values, "id = ?",
                new String[] {currentstep});
        sqlDB.close();
        return numRows;
    }
    public int updatesteps(String currentstepnow, String new_stepsnow){

        ContentValues values = new ContentValues();
        values.put("stepsnow", new_stepsnow);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        int numRows = sqlDB.update("stepcount", values, "id = ?",
                new String[] {currentstepnow});
        sqlDB.close();
        return numRows;
    }
    //update data
    public int updateName(String currentName, String new_name){

        ContentValues values = new ContentValues();
        values.put("MediumName", new_name);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("record", values, "MediumName = ?",
                new String[] {currentName});
        sqlDB.close();
        return numRows;
    }
    public int updatePhone(String currentPhone, String new_Phone){

        ContentValues values = new ContentValues();
        values.put("phone", new_Phone);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("phone", values, "phone = ?",
                new String[] {currentPhone});
        sqlDB.close();
        return numRows;
    }
    public int updatetime(String currenttime, String new_time){

        ContentValues values = new ContentValues();
        values.put("time", new_time);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("record", values, "time = ?",
                new String[] {currenttime});
        sqlDB.close();
        return numRows;
    }
    public int updatedate(String currentdate, String new_date){

        ContentValues values = new ContentValues();
        values.put("date", new_date);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("record", values, "date = ?",
                new String[] {currentdate});
        sqlDB.close();
        return numRows;
    }
    public int updateeveryday(String currentdate, String new_everyday){

        ContentValues values = new ContentValues();
        values.put("everyday", new_everyday);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("record", values, "everyday = ?",
                new String[] {currentdate});
        sqlDB.close();
        return numRows;
    }
    public int updateDuration(String currentdate, String new_Duration){

        ContentValues values = new ContentValues();
        values.put("Duration", new_Duration);

        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int numRows = sqlDB.update("record", values, "Duration = ?",
                new String[] {currentdate});
        sqlDB.close();
        return numRows;
    }
    //delete all data
    public void deleteAllRecords(){
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        sqlDB.delete("record",null,null);
        sqlDB.close();
    }
    public int deleteOneRecord1(String _id){
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int count = sqlDB.delete("record", "_id = ?",new String[]{_id});
        sqlDB.close();
        return count;
    }
    public int deleteOnestep(String id){
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int count = sqlDB.delete("stepcount", "_id = ?",new String[]{id});
        sqlDB.close();
        return count;
    }
    public int deleteOneRecord2(String id) {
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int count = sqlDB.delete("record", "_id = ?",
                new String[]{id});
        sqlDB.close();
        return count;
    }
    public int deleteOnePhone(String id) {
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        int count = sqlDB.delete("phone", "id = ?",
                new String[]{id});
        sqlDB.close();
        return count;
    }

    @Override
    public int getCount() {
        return photolist.size();
    }

    @Override
    public Object getItem(int position) {
        return photolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row ==  null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.textView = (TextView) row.findViewById(R.id.name);
            holder.imageView = (ImageView) row.findViewById(R.id.imgphoto);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }
        photo photo = photolist.get(position);
        holder.textView.setText(photo.getName());
        byte[] photoImage = photo.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(photoImage,0,photoImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

}

