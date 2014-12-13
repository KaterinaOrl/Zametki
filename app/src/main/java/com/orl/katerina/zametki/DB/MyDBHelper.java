package com.orl.katerina.zametki.DB;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.orl.katerina.zametki.Notification.TimeNotification;

/**
 * Created by ZZ on 14.11.2014.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private final static String MYTABLE = "itemtable";
    private final static int DB_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_NOTIFY = "notify";

    public static final String sqlQuery = "CREATE TABLE "+ MYTABLE +" ("+
            COLUMN_ID+" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TEXT+" text NOT NULL," +
            COLUMN_TIME+" text NOT NULL," +
            COLUMN_NOTIFY+" integer NOT NULL,"+
            COLUMN_STATUS+" integer NOT NULL);";


    private static MyDBHelper helper;
    static SQLiteDatabase db;
    public static Context myContext;

    public MyDBHelper() {
        super(myContext, MYTABLE, null, DB_VERSION);
        db=this.getWritableDatabase();
    }
    public static MyDBHelper getInstance(Context context) {
        myContext = context;
        if (helper == null) {
            helper = new MyDBHelper();
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlQuery);

    }

    public SQLiteDatabase getDataBase() {
        return db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
       db.execSQL("drop table "+MYTABLE+";");
       sqLiteDatabase.execSQL(sqlQuery);

    }

    public void addRec(ContentValues cv) {
       db.insert(MYTABLE, null, cv);
    }
    public void delRec(long id) {
        if (id!=0)
        db.delete(MYTABLE, COLUMN_ID + " = " + id, null);
    }

    public void editRec(long id, ContentValues cv) {
        int raw=0;
        if (id!=0) {
            raw = db.update(MYTABLE, cv, COLUMN_ID + " = " + id, null);}
        if (raw==0){
            Toast.makeText(myContext,"raw " + id + " does not exist", Toast.LENGTH_SHORT).show();
        }

    }


    public Cursor getCursor(String query, String orderBy) {
        String selection = null;
        String [] selectionArgs = null;
        if (query!=null){
            selection =COLUMN_TEXT+" LIKE ?";
            query="%"+query+"%";
            selectionArgs = new String[] { query };
         }
        if (orderBy==MyDBHelper.COLUMN_STATUS){
            selection =COLUMN_STATUS+" LIKE ?";
            query="1";
            selectionArgs = new String[] { query };
            orderBy=null;
        }
            return db.query(MYTABLE, null, selection, selectionArgs, null, null,orderBy);
    }

    public void startNotify(){
        long curentTime=System.currentTimeMillis();
        String selection =COLUMN_NOTIFY+" >= ?";
        String query=""+curentTime;
        String [] selectionArgs = new String[] { query };
        String orderBy=COLUMN_NOTIFY;
        Log.d("aaa", "in DB");
        Cursor cursor = db.query(MYTABLE, null, selection, selectionArgs, null, null,orderBy);
        Log.d("aaa", "get cursor");
        Log.d("aaa", ""+cursor.getCount());
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NOTIFY));
            AlarmManager am = (AlarmManager) myContext.getSystemService(myContext.ALARM_SERVICE);
            Intent intent = new Intent(myContext, TimeNotification.class);
            String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
            intent.putExtra("text", text);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.cancel(pendingIntent);
            am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            Log.d("aaa", "set Alarm Manager");
        }
    }

}

