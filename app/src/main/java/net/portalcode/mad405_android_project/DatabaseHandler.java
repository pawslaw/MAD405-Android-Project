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

    private static final int DATABASE_VERSION = 2;

    /**
     * Create the name of the database
     */

    private static final String DATABASE_NAME = "icicle";

    /**
     * Create the names of all the tables
     */

    private static final String TABLE_MESSAGES = "message";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PERMISSIONS = "permissions";

    /**
     * Common column names
     */

    private static final String KEY_ID = "id";
    private static final String KEY_USER_KEY = "user_key";

    /**
     * Message Table column names
     */

    private static final String KEY_TIME = "timesent";
    private static final String KEY_CONTENT = "content";

    /**
     * User Table column names
     */

    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "avatar";
    private static final String KEY_PERMS = "permissions";

    /**
     * Permissions Table column names
     */

    private static final String KEY_CAN_EDIT = "canEdit";
    private static final String KEY_CAN_READ = "canRead";
    private static final String KEY_CAN_WRITE = "canWrite";


    /**
     * Create statements for all the tables
     */

    private static final String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_KEY + " INTEGER REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
            + KEY_TIME + " DATETIME NOT NULL,"
            + KEY_CONTENT + " TEXT)";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_IMAGE + " INTEGER, "
            + KEY_PERMS + " INTEGER REFERENCES " + TABLE_PERMISSIONS + "(" + KEY_ID + "))";

    private static final String CREATE_PERMISSIONS_TABLE = "CREATE TABLE " + TABLE_PERMISSIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CAN_EDIT + " TINYINT(1), "
            + KEY_CAN_READ + " TINYINT(1), "
            + KEY_CAN_WRITE + " TINYINT(1))";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Permissions table MUST go before Users as it is references in Users
        db.execSQL(CREATE_PERMISSIONS_TABLE);
        // Users table MUST go before Messages as it is references in Users
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);

    }

    // This will drop all existing tables and create them from scratch
    // This is useful if we make any major changes to the database and need to reset everything
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISSIONS);
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
        // Using close because closeDB does not exist at this point in time.
        db.close();
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_IMAGE, user.getAvatar());
        values.put(KEY_PERMS, user.getPermissions());
        db.insert(TABLE_USERS, null, values);
        // Using close because closeDB does not exist at this point in time.
        db.close();
    }

    public void addPermissions(Permissions permissions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAN_EDIT, permissions.getCanEdit());
        values.put(KEY_CAN_READ, permissions.getCanRead());
        values.put(KEY_CAN_WRITE, permissions.getCanWrite());
        db.insert(TABLE_PERMISSIONS, null, values);
        // Using close because closeDB does not exist at this point in time.
        db.close();
    }

    /**
     * READ OPERATIONS
     */

    // get individual user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor firstCursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_NAME, KEY_IMAGE, KEY_PERMS},  KEY_ID + " = " + String.valueOf(id), null, null, null, null, null);
        if(cursor.moveToFirst()){
            user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        }
        // Using close because closeDB does not exist at this point in time.
        db.close();
        return user;
    }

    // get all messages
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<User>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setAvatar(Integer.parseInt(cursor.getString(2)));
               userList.add(user);
            }while(cursor.moveToNext());
        }
        // Using close because closeDB does not exist at this point in time.
        db.close();
        return userList;
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
        // Using close because closeDB does not exist at this point in time.
        db.close();
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
        // Using close because closeDB does not exist at this point in time.
        db.close();
        return messageList;
    }

    // get individual permission
    public Permissions getPermissions(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Permissions permissions = null;
        Cursor firstCursor = db.rawQuery("SELECT * FROM " + TABLE_PERMISSIONS, null);
        Cursor cursor = db.query(TABLE_PERMISSIONS, new String[]{KEY_ID, KEY_CAN_EDIT, KEY_CAN_READ, KEY_CAN_WRITE},  KEY_ID + " = " + String.valueOf(id), null, null, null, null, null);
        if(cursor.moveToFirst()){
            permissions = new Permissions(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        }
        // Using close because closeDB does not exist at this point in time.
        db.close();
        return permissions;
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
        values.put(KEY_PERMS, user.getPermissions());
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(user.getId())});
    }

    // Update the Permissions
    public int updatePermissions(Permissions permissions) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAN_EDIT, permissions.getCanEdit());
        values.put(KEY_CAN_READ, permissions.getCanRead());
        values.put(KEY_CAN_WRITE, permissions.getCanWrite());
        return db.update(TABLE_PERMISSIONS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(permissions.getId())});
    }

    /**
     * DELETE OPERATIONS
     */

    // Delete a Message
    public void deleteMessage(long message_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MESSAGES, KEY_ID + " = ?",
                new String[] {String.valueOf(message_id)});
        // Using close because closeDB does not exist at this point in time.
        db.close();
    }

    // Delete a User
    public void deleteUser(long user_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] {String.valueOf(user_id)});
        // Using close because closeDB does not exist at this point in time.
        db.close();
    }

    // Delete a Permission
    public void deletePermissions(long permissions_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PERMISSIONS, KEY_ID + " = ?",
                new String[] {String.valueOf(permissions_id)});
        // Using close because closeDB does not exist at this point in time.
        db.close();
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