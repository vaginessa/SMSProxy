package net.dgistudio.guillaume.webbysms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Guillaume on 10/01/2016.
 */



public class SqlDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_SESSION_NAME = "sessions";

    private static final String DATABASE_NAME = "sessions.db";

    // table : sessions -> all column name fixed !
    public static final String TABLE_SESSION_COLUMN_ID = "id";
    public static final String TABLE_SESSION_COLUMN_DATE = "date";
    public static final String TABLE_SESSION_COLUMN_SESSION_NAME = "sessionName";
    public static final String TABLE_SESSION_COLUMN_CONTACT_NAME = "contactName";
    public static final String TABLE_SESSION_COLUMN_CONTACT_NUMBER = "contactNumber";
    public static final String TABLE_SESSION_COLUMN_LAST_USE = "lastUse";
    public static final String TABLE_SESSION_COLUMN_RX_SMS = "rxSMS";
    public static final String TABLE_SESSION_COLUMN_TX_SMS = "txSMS";
    public static final String TABLE_SESSION_COLUMN_HIDE_MSG = "hideMsg";
    public static final String[] TABLE_SESSION_ALLCOLUMN = {TABLE_SESSION_COLUMN_ID,
                                                            TABLE_SESSION_COLUMN_DATE,
                                                            TABLE_SESSION_COLUMN_SESSION_NAME,
                                                            TABLE_SESSION_COLUMN_CONTACT_NAME,
                                                            TABLE_SESSION_COLUMN_CONTACT_NUMBER,
                                                            TABLE_SESSION_COLUMN_LAST_USE,
                                                            TABLE_SESSION_COLUMN_RX_SMS,
                                                            TABLE_SESSION_COLUMN_TX_SMS,
                                                            TABLE_SESSION_COLUMN_HIDE_MSG};


    private static final int DATABASE_VERSION = 2;

    public SqlDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dbCreate = "CREATE TABLE \"sessions\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, \"date\" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, \"sessionName\" TEXT NOT NULL, \"contactName\" TEXT NOT NULL, \"contactNumber\" TEXT NOT NULL, \"lastUse\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \"rxSMS\" INTEGER DEFAULT 0, \"txSMS\" INTEGER DEFAULT 0, \"hideMsg\" BOOLEAN DEFAULT 0)";
        db.execSQL(dbCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("upgradeDB", "from :"+oldVersion+" to :"+newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION_NAME);
        onCreate(db);
    }

    public Sessions cursorToSession(Cursor cursor)
    {
        Sessions session = new Sessions();

        session.setId(cursor.getLong(0));
        session.setDate(cursor.getString(1));
        session.setSessionName(cursor.getString(2));
        session.setContactName(cursor.getString(3));
        session.setContactNumber(cursor.getString(4));
        session.setLastUse(cursor.getString(5));
        session.setRxSMS(cursor.getInt(6));
        session.setTxSMS(cursor.getInt(7));
        session.setHideMsg((cursor.getInt(8) == 0));

        return session;
    }
}
