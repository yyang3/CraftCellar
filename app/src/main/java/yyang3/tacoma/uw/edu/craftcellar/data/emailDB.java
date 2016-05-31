package yyang3.tacoma.uw.edu.craftcellar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yyang3.tacoma.uw.edu.craftcellar.R;

/**
 * Created by RickYang on 5/31/2016.
 */
public class EmailDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Email.db";

    private EmailDBHelper mEmailDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public EmailDB(Context context) {
        mEmailDBHelper = new EmailDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mEmailDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param email
     * @return true or false
     */
    public boolean insertEmail(String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        long rowId = mSQLiteDatabase.insert("Email", null, contentValues);
        return rowId != -1;
    }
    public void closeDB() {
        mSQLiteDatabase.close();
    }

    /**
     * Delete all the data from the Email table
     */
    public void deleteEmail() {
        mSQLiteDatabase.delete("Email", null, null);
    }


    public List<String> getDB() {
        String[] columns = {"email"};
        Cursor c = mSQLiteDatabase.query(
                "Email",  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<String> list = new ArrayList<String>();
        for (int i=0; i<c.getCount(); i++) {
            String email = c.getString(0);
            list.add(email);
            c.moveToNext();
        }

        return list;
    }




    class EmailDBHelper extends SQLiteOpenHelper {

        private final String CREATE_EMAIL_SQL;
        private final String DROP_EMAIL_SQL;

        public EmailDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_EMAIL_SQL = context.getString(R.string.CREATE_EMAIL_SQL);
            DROP_EMAIL_SQL = context.getString(R.string.DROP_EMAIL_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_EMAIL_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_EMAIL_SQL);
            onCreate(sqLiteDatabase);
        }
    }

}
