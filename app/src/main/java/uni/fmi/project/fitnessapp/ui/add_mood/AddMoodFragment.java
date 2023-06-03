package uni.fmi.project.fitnessapp.ui.add_mood;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.adapter.MoodAdapter;
import uni.fmi.project.fitnessapp.adapter.SpinAdapter;
import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Mood;
import uni.fmi.project.fitnessapp.entity.MoodNomenclature;
import uni.fmi.project.fitnessapp.entity.User;
import uni.fmi.project.fitnessapp.ui.model.SpinnerModel;

public class AddMoodFragment extends DialogFragment implements MoodAdapter.OnClickListener,
        SpinAdapter.OnClickListener {

    SqLiteHelper dbHelper;
    SQLiteDatabase db;

    Spinner spinner;
    RecyclerView recyclerView;
    MoodAdapter moodAdapter;

    int moodId;

    private Button addBtn;

    List<MoodNomenclature> list = new ArrayList<MoodNomenclature>();
    List<SpinnerModel> listSpinner = new ArrayList<SpinnerModel>();
    User user;

    public AddMoodFragment(RecyclerView recyclerView,
                           MoodAdapter moodAdapter) {
        this.recyclerView = recyclerView;
        this.moodAdapter = moodAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, com.google.android.material.R.style.Base_Theme_AppCompat_Dialog_Alert);
//        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
//        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
//        getActivity().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dbHelper = new SqLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        user = (User) getActivity().getIntent().getParcelableExtra("user");

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_mood, container, false);

        spinner = view.findViewById(R.id.spinner_moods);
        addBtn = view.findViewById(R.id.add_mood);

        getMoodsNm();

        // Creating adapter for spinner
        ArrayAdapter<SpinnerModel> dataAdapter = new SpinAdapter<>(
                getContext(),android.R.layout.simple_spinner_item, listSpinner, this);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setBackgroundColor(R.color.white);

        // Drop down layout style - list view with radio button

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moodId = list.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMood();
            }
        });

        return view;
    }
    @SuppressLint("Range")
    private List<MoodNomenclature> getMoodsNm() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DbConstants.MOODS_NOMENCLATURE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                MoodNomenclature moodNomenclature = new MoodNomenclature();
                moodNomenclature.setId(cursor.getInt(
                        cursor.getColumnIndex("id")));
                moodNomenclature.setName(cursor.getString(
                        cursor.getColumnIndex("name")));
                list.add(moodNomenclature);

                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setId(moodNomenclature.getId());
                spinnerModel.setName(moodNomenclature.getName());
                listSpinner.add(spinnerModel);
            }
        }
        // closing connection
        cursor.close();
        return list;
    }

    private void insertMood() {
        Log.d("AddMood", "onclick");
        ContentValues values = new ContentValues();
        values.put("mood_id", moodId);
        values.put("date_created", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        values.put("user_id", user.getId());
//        String q="INSERT INTO "+ DbConstants.MOODS +"(name, date) values("
//                + moodId + ", " + DateFormat.format("MMMM d, yyyy ", new Date().getTime()); + ", " + 1);";
//        db.execSQL(q);
        try {
            db.insert("MOODS", null, values);
            Mood mood = new Mood();
            mood.setMoodId(moodId);
            mood.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            moodAdapter.addItem(0, mood);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onClick(int position, int id) {
    }

    @Override
    public void onClick(int id) {
        moodId = id;
    }
}
