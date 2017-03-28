package net.portalcode.mad405_android_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by James Pierce on 3/24/2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * Keep track of the database version
     */

    private static final int DATABASE_VERSION = 1;

    /**
     * Create the name of the database
     */

    private static final String DATABASE_NAME = "androidproject";

    /**
     * Create the names of all the tables
     */

    private static final String TABLE_MESSAGES = "message";
    private static final String TABLE_USERS = "users";

    /**
     * Common column names
     */

    private static final String KEY_ID = "id";

    /**
     * Message Table column names
     */

    private static final String KEY_TIME = "timesent";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_USER_KEY = "user_key";

    /**
     * User Table column names
     */

    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "avatar";

    /**
     * Create statements for all the tables
     */

    private static final String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_KEY
            + " INTEGER REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
            + KEY_TIME + " DATETIME NOT NULL," + KEY_CONTENT + " TEXT)";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, "
            + KEY_IMAGE + " INTEGER)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);

    }

    // This will drop all existing tables and create them from scratch
    // This is useful if we make any major changes to the database and need to reset everything
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    /**
     * CRUD OPERATIONS FOR THE DATABASE AND TABLES
     * Create
     * Read
     * Update
     * Delete
     */


    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_KEY, message.getUser_id());
        values.put(KEY_TIME, message.getTimeSent());
        values.put(KEY_CONTENT, message.getContent());
        db.insert(TABLE_MESSAGES, null, values);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_IMAGE, user.getAvatar());
        db.insert(TABLE_USERS, null, values);
    }

    /**
     * READ OPERATIONS
     */

    // get individual user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor firstCursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_NAME, KEY_IMAGE},  KEY_ID + " = " + String.valueOf(id), null, null, null, null, null);
        if(cursor.moveToFirst()){
            user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        }


        /**
         * We create a User object using the cursor record
         */

//        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        return user;
    }

    // get individual Message
    public Message getMessage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /**
         * Create a cursor
         * (Which is able to move through and access database records)
         * Have it store all the records retrieved from the db.query()
         * cursor starts by pointing at record 0
         * Databases do not have a record 0
         * we use cursor.moveToFirst() to have it at the first record returned
         */
        Cursor cursor = db.query(TABLE_MESSAGES, new String[] {KEY_ID, KEY_USER_KEY, KEY_TIME, KEY_CONTENT}, "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        /**
         * We create a user object using the cursor record
         */
        Message message = new Message(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        return message;
    }

    // get all messages
    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messageList = new ArrayList<Message>();
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setUser_id(Integer.parseInt(cursor.getString(1)));
                message.setTimeSent(cursor.getString(2));
                message.setContent(cursor.getString(3));
                messageList.add(message);
            }while(cursor.moveToNext());
        }

        return messageList;
    }

    /**
     * UPDATE OPERATIONS
     */

    // Update the Message
    public int updateMessage(Message message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_KEY, message.getUser_id());
        values.put(KEY_TIME, message.getTimeSent());
        values.put(KEY_CONTENT, message.getContent());
        return db.update(TABLE_MESSAGES, values, KEY_ID + " = ?",
                new String[] {String.valueOf(message.getId())});
    }

    // Update the User
    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_IMAGE, user.getAvatar());
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(user.getId())});
    }

    /**
     * DELETE OPERATIONS
     */

    // Delete a Message
    public void deleteMessage(long message_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MESSAGES, KEY_ID + " = ?",
                new String[] {String.valueOf(message_id)});
    }

    // Delete a User
    public void deleteUser(long user_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] {String.valueOf(user_id)});
    }



    /**
     * Closing the database connection
     */

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()) {
            db.close();
        }
    }




}