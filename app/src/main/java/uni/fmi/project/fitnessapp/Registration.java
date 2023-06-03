package uni.fmi.project.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.User;

public class Registration extends AppCompatActivity {
    SqLiteHelper helper;
    SQLiteDatabase db;

    private EditText usernameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        helper = new SqLiteHelper(this);
        db = helper.getWritableDatabase();
        initViews();
    }


    private void initViews() {
        usernameEt = findViewById(R.id.username_et);
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);

        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUser();
            }
        });

    }

    private void insertUser() {
        try {
            String email = emailEt.getText().toString();
            String username = usernameEt.getText().toString();
            String password = passwordEt.getText().toString();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                throw new Exception("Please input all data");
            }
            if (password.length() < 8){
                throw new Exception("Your password should be minimum 8 symbols and contains at least at least 1 Alphabet, 1 Number and 1 Special Character");
            }
            if (username.equals(getUsername())){
                throw new Exception("Username already taken!");
            }
            if (email.equals(getEmail())){
                throw new Exception("Email already taken!");
            }

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            insertUser(user);
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG)
                    .show();

            startActivity(new Intent(Registration.this, MainActivity.class));
            finish();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        } finally {
            if (helper != null)
//                helper.close();
                helper = null;
        }
    }

    private void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());

        db.insert("USERS", null, values);
        db.close();
    }

    public String getEmail() throws SQLException {
        String email = "";
        Cursor cursor = helper.getReadableDatabase().query(
                "USERS", new String[] { "email" },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                email = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return email;
    }

    public String getUsername() throws SQLException {
        String username = "";
        Cursor cursor = helper.getReadableDatabase().query(
                "USERS", new String[] { "username" },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return username;
    }
}