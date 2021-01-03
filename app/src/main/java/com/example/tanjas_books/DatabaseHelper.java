package com.example.tanjas_books;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "books";
    private static final String COL0 = "id";
    private static final String COL1 = "author";
    private static final String COL2 = "title";
    private static final String COL3 = "isbn";
    private static final String COL4 = "done";
    private static final String COL5 = "borrowed";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " +TABLE_NAME+ " ( " +
                COL0+ " integer primary key autoincrement, " +
                COL1+ " text," +
                COL2+ " text," +
                COL3+ " text," +
                COL4+ " integer," +
                COL5+ " integer" +
                " ) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "drop table if exists " +TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public long add(String author, String title, String isbn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, author);
        contentValues.put(COL2, title);
        contentValues.put(COL3, isbn);
        contentValues.put(COL4, 0);
        contentValues.put(COL5, 0);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor readAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " +TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public Cursor read(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " +TABLE_NAME+ "where " +COL0+ " = '" +id+ "'";
        return db.rawQuery(query, null);
    }

    public void edit(int id, String author, String title, String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + TABLE_NAME + " set " +
                COL1 + " = '" + author + "', " +
                COL2 + " = '" + title + "', " +
                COL3 + " = '" + isbn + "'" +
                " where " + COL0 + " = '" + id + "'";
        db.execSQL(query);
    }

    public void done(int id, boolean done) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + TABLE_NAME + " set " +
                COL4 + " = '" + (done?1:0) + "' " +
                " where " + COL0 + " = '" + id + "'";
        db.execSQL(query);
    }

    public void borrowed(int id, boolean borrowed) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + TABLE_NAME + " set " +
                COL5 + " = '" + (borrowed?1:0) + "' " +
                " where " + COL0 + " = '" + id + "'";
        db.execSQL(query);
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "delete from " +TABLE_NAME + " where " +COL0+ " = '" +id+ "'";
        db.execSQL(query);
    }
}
