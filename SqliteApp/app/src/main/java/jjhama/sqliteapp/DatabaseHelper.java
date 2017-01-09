package jjhama.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Jason on 1/7/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "tblStudent";
    public static final String COL_1 = "flngId";
    public static final String COL_2 = "fstrName";
    public static final String COL_3 = "fstrSurname";
    public static final String COL_4 = "flngMarks";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 10);
        SQLiteDatabase db = this.getWritableDatabase();
        //Student.db
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String surname, String marks){
        Log.v(TAG, "Made it here");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        Log.v("DatabaseHelpler", "Made it before Inserting");
        //if data is not inserted -1 is returned
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.v("DatabaseHelpler", "Made it before Checking the result");
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public void insertDataPrepared(String name, String surname, String marks){
        Log.v(TAG, "Get db instance");
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Log.v(TAG, "Set up transaction");
            db.beginTransaction();

            Log.v(TAG, "Create sql statement");
            String sql = "INSERT INTO "+TABLE_NAME+" (" + COL_2 + ", " + COL_3 + ", " + COL_4 + ") VALUES (?, ?, ?)";

            Log.v(TAG, "Compile the statement");
            SQLiteStatement statement = db.compileStatement(sql);

            Log.v(TAG, "Binding Parameters");
            statement.bindString(1, name);
            statement.bindString(2, surname);
            statement.bindString(3, marks);

            Log.v(TAG, "Execute the transaction");
            statement.executeInsert();
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.v(TAG, "Exception: " + e);
        } finally {
            db.endTransaction();
        }
        db.endTransaction();
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return res;
    }



    public boolean updateData(String id, String name, String surname, String marks){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + " = ?", new String[] {id});

    }
}


/*DBName: Students.db
//cols:     ID      Name    Surname     Marks
//data:     1       Mark    Taylor      78
            2       Tom     Smith       89
            3       John    Mal         97
            4       Max     Nickson     90
 */
