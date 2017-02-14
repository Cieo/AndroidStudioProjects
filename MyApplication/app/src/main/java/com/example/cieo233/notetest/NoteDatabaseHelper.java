package com.example.cieo233.notetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteDatabaseHelper extends SQLiteOpenHelper {
    public NoteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table note(_id integer primary key autoincrement, notename text, notecreatetime text, notecontent text, notebelongto text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists note";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public int delete(String id){
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("note","_id=?",new String[]{id});
    }

    public Cursor select(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query("note",null,null,null,null,null,null);
    }

    public long insert(NoteInfo noteInfo){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notename",noteInfo.getNoteName());
        contentValues.put("notecreatetime",noteInfo.getNoteCreateTime());
        contentValues.put("notecontent",noteInfo.getNoteContent());
        contentValues.put("notebelongto",noteInfo.getNoteBelongTo());
        return database.insert("note",null,contentValues);
    }

    public int update(NoteInfo noteInfo){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notename",noteInfo.getNoteName());
        contentValues.put("notecreatetime",noteInfo.getNoteCreateTime());
        contentValues.put("notecontent",noteInfo.getNoteContent());
        contentValues.put("notebelongto",noteInfo.getNoteBelongTo());
        return database.update("note",contentValues,"_id=?",new String[]{noteInfo.getNoteMark()});
    }

    public void clear(){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "drop table if exists note";
        database.execSQL(sql);
        onCreate(database);
    }

}
