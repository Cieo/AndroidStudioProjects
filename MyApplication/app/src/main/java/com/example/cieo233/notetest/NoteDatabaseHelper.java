package com.example.cieo233.notetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteDatabaseHelper extends SQLiteOpenHelper {
    public NoteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String noteSql = "create table note(_id integer primary key autoincrement, notename text, notecreatetime text, notecontent text, notebelongto text)";
        String folderSql = "create table folder(_id integer primary key autoincrement, foldername text)";
        String imageSql = "create table imagefolder(_id integer primary key autoincrement, foldername text)";
        sqLiteDatabase.execSQL(noteSql);
        sqLiteDatabase.execSQL(folderSql);
        sqLiteDatabase.execSQL(imageSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String noteSql = "drop table if exists note";
        String folderSql = "drop table if exists folder";
        String imageSql = "drop table if exists imagefolder";
        sqLiteDatabase.execSQL(noteSql);
        sqLiteDatabase.execSQL(folderSql);
        sqLiteDatabase.execSQL(imageSql);
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

    public void clearAll(){
        SQLiteDatabase database = getWritableDatabase();
        String noteSql = "drop table if exists note";
        String folderSql = "drop table if exists folder";
        String imageSql = "drop table if exists imagefolder";
        database.execSQL(noteSql);
        database.execSQL(folderSql);
        database.execSQL(imageSql);
        onCreate(database);
    }

    public void clear(){
        SQLiteDatabase database = getWritableDatabase();
        String noteSql = "drop table if exists note";
        database.execSQL(noteSql);
        noteSql = "create table note(_id integer primary key autoincrement, notename text, notecreatetime text, notecontent text, notebelongto text)";
        database.execSQL(noteSql);
    }

    public long createNoteFolder(String folderName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("foldername",folderName);
        return database.insert("folder",null,contentValues);
    }

    public Cursor selectFolder(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query("folder",null,null,null,null,null,null);
    }

    public long createImageFolder(String folderName){
        String path = "/storage/emulated/0/"+folderName;
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("foldername",folderName);
        return database.insert("imagefolder",null,contentValues);
    }

    public Cursor selectImageFolder(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query("imagefolder",null,null,null,null,null,null);
    }

}
