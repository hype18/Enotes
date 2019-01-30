package com.example.hype.enotes;

/**
 * Created by hype on 24.01.19.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {

    private static final String DB_NAME = "notes";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytabs";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_THEME = "theme";
    public static final String COLUMN_TXT = "txt";
    public static final String COLUMN_FAM = "famous";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_THEME + " text, " +
                    COLUMN_TXT + " text," +
                    COLUMN_FAM + " integer" +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void updateNote(String id, String theme, String text, String important){
        ContentValues cv = new ContentValues();
        int fam = 0;
        if (important == "17301516")
            fam = android.R.drawable.btn_star_big_on;
        else
            fam = android.R.drawable.btn_star_big_off;

        cv.put("theme", theme);
        cv.put("txt", text);
        cv.put("famous", fam);
        mDB.update("mytabs", cv, "_id = ?", new String[] {id});
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }


    public Cursor getImportant(){return mDB.query(DB_TABLE, null, COLUMN_FAM+" = "+17301516, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String theme, String txt, int fam) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_THEME, theme);
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_FAM, fam);
        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // удалить запись из DB_TABLE
    public void delAll() {
        mDB.delete(DB_TABLE, COLUMN_ID + " > " + 0, null);
    }


    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}