package com.example.mauriciogodinez.basedatos1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Student.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + StudentContract.StudentEntry.TABLE_NAME + " (" +
                StudentContract.StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudentContract.StudentEntry.COLUMN_NAME + " TEXT, " +
                StudentContract.StudentEntry.COLUMN_LASTNAME + " TEXT, " +
                StudentContract.StudentEntry.COLUMN_MARKS + " INTEGER" +
                ");";

        sqliteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) {
        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + StudentContract.StudentEntry.TABLE_NAME);
        onCreate(sqliteDatabase);
    }

    public boolean insertData(String name, String lastName, String marks){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        contentValues.put(StudentContract.StudentEntry.COLUMN_LASTNAME, lastName);
        contentValues.put(StudentContract.StudentEntry.COLUMN_MARKS, marks);

        long result = sqliteDatabase.insert(StudentContract.StudentEntry.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();

        return sqliteDatabase.rawQuery("SELECT * FROM " + StudentContract.StudentEntry.TABLE_NAME, null);
    }

    public boolean upDateDataNow(String id, String name, String lastName, String marks){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(StudentContract.StudentEntry._ID, id);
        contentValues.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        contentValues.put(StudentContract.StudentEntry.COLUMN_LASTNAME, lastName);
        contentValues.put(StudentContract.StudentEntry.COLUMN_MARKS, marks);

        sqliteDatabase.update(StudentContract.StudentEntry.TABLE_NAME, contentValues, "_ID = ?", new String[] { id});

        return true;
    }

    public Integer deleteDataNow(String id){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        return sqliteDatabase.delete(StudentContract.StudentEntry.TABLE_NAME, "_ID = ?", new String[] {id});
    }
}