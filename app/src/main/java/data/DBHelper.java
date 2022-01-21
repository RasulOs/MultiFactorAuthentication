package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.multifactorauthenticationjava.MenuActivity;
import com.example.multifactorauthenticationjava.PinCodeActivity;

public class DBHelper extends SQLiteOpenHelper {

    static final String DBNAME = "Login.db";
    static final int DATABASE_VERSION = 1;



    public DBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, " +
                "mobile TEXT, pin TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");

    }

    public boolean insertData(String username, String email, String mobile, String pin, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("mobile", mobile);
        contentValues.put("pin", pin);
        contentValues.put("password", password);

        long result = myDB.insert("users", null, contentValues);

        return result != -1;
    }

    public boolean checkUsername(String email) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE email = ?", new String[] {email});

        return cursor.getCount() > 0;
    }

    public boolean checkUsernameAndPassword(String email, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?",
                new String[] {email, password});

        return cursor.getCount() > 0;
    }

    public void initializePIN(String email) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT pin FROM users WHERE email = ?",
                new String[] {email});

        while (cursor.moveToNext()) {
            PinCodeActivity.setPinFromDB(cursor.getString(0));
        }
    }

    public void initializeName(String email) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT username FROM users WHERE email = ?",
                new String[] {email});

        while (cursor.moveToNext()) {
            MenuActivity.setName(cursor.getString(0));
        }
    }
}
