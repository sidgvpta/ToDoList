package com.example.android.todolistgh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ed on 27/11/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    //queries
    public String CREATE_TABLE_QUERY =
            "CREATE TABLE " +
                    Database.TasksTable.TABLE_NAME + "(" +
                    Database.TasksTable.ID + " INTEGER PRIMARY KEY," +
                    Database.TasksTable.CATEGORY + " TEXT," +
                    Database.TasksTable.TASK + " TEXT," +
                    Database.TasksTable.TIME_ADDED + " INTEGER," +
                    Database.TasksTable.DUE_DATE + " INTEGER," +
                    Database.TasksTable.COMPLETED + " BOOLEAN);";

    public String DELETE_TABLE_QUERY =
            "DROP TABLE IF EXISTS " + Database.TasksTable.TABLE_NAME + ";";

    public String SELECT_ALL_QUERY =
            "SELECT * FROM " + Database.TasksTable.TABLE_NAME + ";";

    public DatabaseHelper(Context context) {
        super(context, Database.DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Executes table instantiation when no prior table exists
     * @param db database in question to be queried
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    /**
     * Override called when update is instantiated in version
     * @param db the current database in question
     * @param oldVersion the old version integer
     * @param newVersion the new version integer being updated to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this must be modified in later additions as we
        // want to port the table over and not obliterate it
        // but for now this is okay
        db.execSQL(DELETE_TABLE_QUERY);
    }

    /**
     * Inserts a new row into the table with respect to row
     * @param id row id
     * @param category category or subject name
     * @param task task to be done by user
     * @param time_added time in millis of task added
     * @param due_date time in millis of task due date
     * @param completed has the task been completed?
     */
    public void insertTask(int id, String category, String task,
                           long time_added, long due_date, boolean completed){
        SQLiteDatabase writeDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.TasksTable.ID, id);
        contentValues.put(Database.TasksTable.CATEGORY, category);
        contentValues.put(Database.TasksTable.TASK, task);
        contentValues.put(Database.TasksTable.TIME_ADDED, time_added);
        contentValues.put(Database.TasksTable.DUE_DATE, due_date);
        contentValues.put(Database.TasksTable.COMPLETED, completed);
        writeDb.insert(Database.TasksTable.TABLE_NAME, null, contentValues);
    }

    /**
     * Remove rows from the task table given the id which should be searchable and unique
     * @param id the index id of the row
     */
    public void removeTask(int id){
        SQLiteDatabase writeDb = this.getWritableDatabase();

        //query to remove row with given id
        String removeRow =
                "DELETE FROM " + Database.TasksTable.TABLE_NAME +
                " WHERE " + Database.TasksTable.ID + "=" + id + ";";

        writeDb.execSQL(removeRow);
        writeDb.close();
    }

    /**
     * obliterates the entire table by remove all records from rows
     */
    public void removeAllTasks(){
        SQLiteDatabase writeDb = this.getWritableDatabase();

        //query to remove row with given id
        String removeAll =
                "DELETE FROM " + Database.TasksTable.TABLE_NAME + ";";

        writeDb.execSQL(removeAll);
        writeDb.close();
    }

    /**
     * Takes the id passed and modifies the columns for that row
     * @param id row id
     * @param category amended category
     * @param task amended task
     * @param time_added amended time
     * @param due_date amended date due
     * @param completed amended completion
     */
    public void editTask(int id, String category, String task, long time_added,
                         long due_date, boolean completed){
        SQLiteDatabase writeDb = this.getWritableDatabase();
        SQLiteDatabase readDb = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.TasksTable.CATEGORY, category);
        contentValues.put(Database.TasksTable.TASK, task);
        contentValues.put(Database.TasksTable.TIME_ADDED, time_added);
        contentValues.put(Database.TasksTable.DUE_DATE, due_date);
        contentValues.put(Database.TasksTable.COMPLETED, completed);

        String[] whereArgs = {String.valueOf(id)};
        writeDb.update(Database.TasksTable.TABLE_NAME, contentValues,
                Database.TasksTable.ID + "=" + "?", whereArgs);
        writeDb.close();
        readDb.close();
    }

    /**
     * Prints out the current values stored within the table in Sys
     * @param tableName the table to be printed out in monitor
     */
    public void printTableContents(String tableName){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL_QUERY, null);
        cursor.moveToFirst();

        int rowNum = cursor.getCount();
        int colNum = cursor.getColumnCount();

        System.out.println("# of rows in " + tableName + " is " + rowNum);
        System.out.println("# of columns in " + tableName + " is " + colNum);

        for (int r = 0; r < rowNum; r++){
            for(int c = 0; c < colNum; c++){
                System.out.println(c + ". " + cursor.getString(c) + "; ");
            }
            System.out.println("\n------------");
            cursor.moveToNext();
        }
        cursor.close();
    }
}