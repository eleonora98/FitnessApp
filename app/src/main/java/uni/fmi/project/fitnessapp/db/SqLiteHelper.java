package uni.fmi.project.fitnessapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import uni.fmi.project.fitnessapp.entity.User;

public class SqLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "fitness4.db";

    private static final String CREATE_TABLE_MOODS_NOMENCLATURE = "CREATE TABLE "
            + DbConstants.MOODS_NOMENCLATURE + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbConstants.NAME + " varchar(255) unique" + " );";

    private static final String CREATE_TABLE_MOODS = "CREATE TABLE "
            + DbConstants.MOODS + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "date_created varchar(255) not null, "
            + "mood_id integer not null, "
            + "user_id integer not null, " +
            "FOREIGN KEY " + "(" + "mood_id" + ")" + " REFERENCES " + DbConstants.MOODS_NOMENCLATURE+" (" + DbConstants.ID + "), "
            + "FOREIGN KEY " + "(" + "user_id" + ")" + " REFERENCES " + "USERS" + " (" + DbConstants.ID + ")" +
            ");";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + "USERS" + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "username varchar(255) not null unique, "
            + "email varchar(255) not null unique, "
            + "password varchar(255) not null" + " );";

    private static final String CREATE_TABLE_MUSCLE_GROUPS = "CREATE TABLE "
            + DbConstants.MUSCLE_GROUPS + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbConstants.NAME + " varchar(255)" + " );";

    private static final String CREATE_TABLE_TRAININGS = "CREATE TABLE "
            + DbConstants.TRAININGS + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbConstants.NAME + " varchar(255) not null, "
            + "description varchar(3000) not null, "
            + "duration varchar(1000) not null, "
            + "level_id integer, "
            + "status_id integer, "
            + "FOREIGN KEY " + "(" + "status_id" + ")" + " REFERENCES " + DbConstants.TRAINING_STATUSES+" (" + DbConstants.ID
            + ")); ";

    private static final String CREATE_TABLE_TRAINING_STATUSES = "CREATE TABLE "
            + DbConstants.TRAINING_STATUSES + "( "
            + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbConstants.NAME + " varchar(255) unique" + " );";

    public SqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_MOODS_NOMENCLATURE);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRAININGS);
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_MOODS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRAINING_STATUSES);
        insertIntoMoodsNomenclature(sqLiteDatabase);
        insertIntoStatuses(sqLiteDatabase);
        insertTrainings(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertIntoStatuses(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);
        contentValues.put("name", "New");
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("name", "In progress");
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("name", "Done");
        sqLiteDatabase.insert(DbConstants.TRAINING_STATUSES, null, contentValues);
        sqLiteDatabase.insert(DbConstants.TRAINING_STATUSES, null, contentValues2);
        sqLiteDatabase.insert(DbConstants.TRAINING_STATUSES, null, contentValues3);
    }

    public void insertIntoMoodsNomenclature(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);
        contentValues.put("name", "Happy");
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("id", 2);
        contentValues2.put("name", "Nervous");
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("id", 3);
        contentValues3.put("name", "Sad");
        sqLiteDatabase.insert(DbConstants.MOODS_NOMENCLATURE, null, contentValues);
        sqLiteDatabase.insert(DbConstants.MOODS_NOMENCLATURE, null, contentValues2);
        sqLiteDatabase.insert(DbConstants.MOODS_NOMENCLATURE, null, contentValues3);
    }

    public void insertTrainings(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);
        contentValues.put("name", "Knee pushup");
        contentValues.put("description", "A beginner-style pushup, this move will help you build strength before attempting a standard pushup.");
        contentValues.put("status_id", 1);
        contentValues.put("duration", "25 min");
        contentValues.put("level_id", 1);
        ContentValues contentValues2 = new ContentValues();
        contentValues.put("id", 2);
        contentValues2.put("name", "Stationary lunge");
        contentValues2.put("description", "Hit your quads, hamstrings, and glutes with a stationary lunge.");
        contentValues2.put("status_id", 2);
        contentValues2.put("duration", "30 min");
        contentValues2.put("level_id", 1);
        ContentValues contentValues3 = new ContentValues();
        contentValues.put("id", 3);
        contentValues3.put("name", "Forearm plank");
        contentValues3.put("description", "A full-body exercise that requires strength and balance, planks put the core into overdrive.");
        contentValues3.put("status_id", 1);
        contentValues3.put("duration", "40 min");
        contentValues3.put("level_id", 1);
        sqLiteDatabase.insert(DbConstants.TRAININGS, null, contentValues);
        sqLiteDatabase.insert(DbConstants.TRAININGS, null, contentValues2);
        sqLiteDatabase.insert(DbConstants.TRAININGS, null, contentValues3);
    }
    @SuppressLint("Range")
    public User getCurrentUser(int id) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * from Users " +
                "where id = " + id + "", null);
        User user = new User();
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                user.setId(id);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                return user;
            }
        }
        return user;
    }
}
