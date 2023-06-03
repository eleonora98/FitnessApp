package uni.fmi.project.fitnessapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.interfaces.OnLanguageChangeListener;
import uni.fmi.project.fitnessapp.util.LocaleHelper;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnLanguageChangeListener {

    private Button registerBtn;
    private Button loginBtn;

    private EditText usernameEt;
    private EditText passwordEt;

    private TextView tvEn;
    private TextView tvBg;

    SqLiteHelper helper;
    SQLiteDatabase db;

    OnLanguageChangeListener onLanguageChangeListener;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new SqLiteHelper(this);
        db = helper.getWritableDatabase();
        onLanguageChangeListener = this;
        initViews();
    }

    private void initViews() {
        registerBtn = findViewById(R.id.btn_signUp);
        loginBtn = findViewById(R.id.btn_signIn);
        usernameEt = findViewById(R.id.login_username);
        passwordEt = findViewById(R.id.login_pass);
        tvEn = findViewById(R.id.tvEn);
        tvBg = findViewById(R.id.tvBg);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        tvEn.setOnClickListener(this);
        tvBg.setOnClickListener(this);
    }

    private void switchToAppContent() {
        Intent intent = new Intent(MainActivity.this, TabsActivity.class);
        intent.putExtra("user", helper.getCurrentUser(userId));
        startActivityForResult(intent, 200);
    }
    public int getLoginData(String username, String password)
    {
        SQLiteDatabase sql = helper.getReadableDatabase();
        String query=" select count(*) from "+"USERS"+" where username ='"+username+"' and password='"+password+"'";
        Cursor cursor =sql.rawQuery(query,null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }
    private void login() {
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        int status = getLoginData(username, password);
        userId = status;

        if (status>0) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            switchToAppContent();
        } else {
            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
        }
    }

    private void switchToSignUp(){
        startActivity(new Intent(MainActivity.this, Registration.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signIn) {
            login();
        } else  if (view.getId() == R.id.btn_signIn) {
            Intent intent = new Intent(MainActivity.this, Registration.class);
            startActivityForResult(intent, 200);

        } else  if (view.getId() == R.id.tvBg) {
            LocaleHelper.setLocale(MainActivity.this, "bg");
            onLanguageChangeListener.onLanguageChange();
        } else  if (view.getId() == R.id.tvEn) {
            LocaleHelper.setLocale(MainActivity.this, "en");
            onLanguageChangeListener.onLanguageChange();
        }
    }

    @Override
    public void onLanguageChange() {
        registerBtn.setText(R.string.register);
        loginBtn.setText(R.string.login);
    }
}