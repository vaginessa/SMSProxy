package net.dgistudio.guillaume.webbysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 10/01/2016.
 */
public class SqlDbAccess {
    private SQLiteDatabase database;
    private SqlDbHelper dbHelper;

    public SqlDbAccess(Context context) {
        dbHelper = new SqlDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Sessions createNewSession(String name, String contactName, String contactNumber, boolean hideMsg) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.TABLE_SESSION_COLUMN_SESSION_NAME, name);
        values.put(SqlDbHelper.TABLE_SESSION_COLUMN_CONTACT_NAME, contactName);
        values.put(SqlDbHelper.TABLE_SESSION_COLUMN_CONTACT_NUMBER, contactNumber);
        values.put(SqlDbHelper.TABLE_SESSION_COLUMN_HIDE_MSG, hideMsg);

        long insertId = database.insert(SqlDbHelper.TABLE_SESSION_NAME, null,
                values);

        return this.getSession(insertId);
    }

    public Sessions getSession(long id) {
        Cursor cursor = database.query(SqlDbHelper.TABLE_SESSION_NAME,
                SqlDbHelper.TABLE_SESSION_ALLCOLUMN, SqlDbHelper.TABLE_SESSION_COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Sessions session = dbHelper.cursorToSession(cursor);
        cursor.close();
        return session;
    }

    public void deleteSession(Sessions session) {
        long id = session.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SqlDbHelper.TABLE_SESSION_NAME, SqlDbHelper.TABLE_SESSION_COLUMN_ID
                + " = " + id, null);
    }

    public List<Sessions> getSessions() {
        List<Sessions> sessions = new ArrayList<>();
        Cursor cursor = database.query(SqlDbHelper.TABLE_SESSION_NAME,
                SqlDbHelper.TABLE_SESSION_ALLCOLUMN, null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                Sessions session = dbHelper.cursorToSession(cursor);
                sessions.add(session);

                cursor.moveToNext();
            }

            cursor.close();
        }
        return sessions;
    }

    public boolean searchByPhoneNumber(String phoneNumber) {
        Cursor cursor = database.query(SqlDbHelper.TABLE_SESSION_NAME, SqlDbHelper.TABLE_SESSION_ALLCOLUMN, SqlDbHelper.TABLE_SESSION_COLUMN_CONTACT_NUMBER + "=?", new String[]{phoneNumber}, null, null, null);
        int nb = cursor.getCount();
        cursor.close();
        return (nb>0);
    }

    public Sessions findSessionByPhoneNumber(String stringExtra) {
        Cursor cursor = database.query(SqlDbHelper.TABLE_SESSION_NAME, SqlDbHelper.TABLE_SESSION_ALLCOLUMN, SqlDbHelper.TABLE_SESSION_COLUMN_CONTACT_NUMBER + "=?", new String[]{stringExtra}, null, null, null);
        Sessions session = null;
        if (cursor != null)
        {
            cursor.moveToFirst();
            long id  = cursor.getLong(cursor.getColumnIndex(SqlDbHelper.TABLE_SESSION_COLUMN_ID));
            session = this.getSession(id);
            return session;
        }
        else
            return null;
    }

   /* public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return comments;
    }*/
}
