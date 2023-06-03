package uni.fmi.project.fitnessapp.ui.add_edit_training;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.adapter.SpinAdapter;
import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Training;
import uni.fmi.project.fitnessapp.entity.TrainingStatus;
import uni.fmi.project.fitnessapp.enums.TrainingStatusEnum;
import uni.fmi.project.fitnessapp.ui.model.SpinnerModel;

public class AddEditTrainingFragment extends Fragment implements SpinAdapter.OnClickListener {

    private static Integer trainingId;

    private EditText nameEt;
    private TextView nameTv;
    private EditText descrEt;
    private EditText durationEt;
    private Button saveBtn;

    SqLiteHelper dbHelper;
    SQLiteDatabase db;
    List<TrainingStatus> list = new ArrayList<TrainingStatus>();
    List<SpinnerModel> listSpinner = new ArrayList<SpinnerModel>();
    Spinner spinner;
    NavController navController;
    ArrayAdapter<SpinnerModel> dataAdapter;

    int statusId;

    public AddEditTrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SqLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        if (getArguments() != null) {
            trainingId = getArguments().getInt("trainingId");
            Log.i("AddEDitTR" , "trId " + trainingId);
        }

        getStatusesNm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_training, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
    private void initViews(View view) {
        nameTv = view.findViewById(R.id.name_tv);
        nameEt = view.findViewById(R.id.name_et);
        durationEt = view.findViewById(R.id.duration_et);
        descrEt = view.findViewById(R.id.descr_et);
        saveBtn = view.findViewById(R.id.save_btn);
        spinner = view.findViewById(R.id.spinner_status);
        setupSpinner();

        if (trainingId != null && trainingId != 0) {
            getDbTrainingById();
        } else {
            nameTv.setVisibility(View.VISIBLE);
            nameEt.setVisibility(View.VISIBLE);
        }
        clickSave();
    }

    @SuppressLint("Range")
    private void getDbTrainingById() {
        Cursor cursor = db.rawQuery("Select * from trainings where id = " + trainingId, null);
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                int statusId = cursor.getInt(cursor.getColumnIndex("status_id"));

                descrEt.setText(description);
                durationEt.setText(duration);
                SpinnerModel model = new SpinnerModel();
                model.setId(statusId);
                model.setName(TrainingStatusEnum.getStatusById(statusId));
                spinner.setSelection(dataAdapter.getPosition(model));
            }
        }
    }

    private void clickSave() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trainingId != null && trainingId != 0) {
                    update(descrEt.getText().toString(), durationEt.getText().toString(), statusId);
                } else {
                    String nameText = nameEt.getText().toString();
                    String descrText = descrEt.getText().toString();
                    String durationText = durationEt.getText().toString();
                    if (nameText.isEmpty() || durationText.isEmpty() || descrText.isEmpty()) {
                        Toast.makeText(getContext(), "Please, input all fields.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Training training = new Training();
                    training.setStatusId(statusId);
                    training.setName(nameEt.getText().toString());
                    training.setDuration(durationEt.getText().toString());
                    training.setDescription(descrEt.getText().toString());
                    insertTraining(training);
                }
                navController.popBackStack();
            }
        });
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    private void setupSpinner() {
        // Creating adapter for spinner
        dataAdapter = new SpinAdapter<>(
                getContext(),android.R.layout.simple_spinner_item, listSpinner, this);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setBackgroundColor(R.color.white);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusId = list.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void update(String description, String duration, int statusId) throws SQLException {
        String q = "UPDATE " + DbConstants.TRAININGS + " SET description=?, duration=?, status_id=?" +
                " WHERE id=" + trainingId;
        db.execSQL(q, new String[]{description,
                duration, String.valueOf(statusId)});
        db.close();
    }

    private void insertTraining(Training training) {
        ContentValues cv = new ContentValues();
        cv.put("name", training.getName());
        cv.put("description", training.getDescription());
        cv.put("duration", training.getDuration());
        cv.put("status_id", training.getStatusId());
        if (training.getLevelId() != null) cv.put("level_id", training.getLevelId());
        try {
            db.insertOrThrow(DbConstants.TRAININGS, "level_id", cv);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("Range")
    private List<TrainingStatus> getStatusesNm() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DbConstants.TRAINING_STATUSES;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                TrainingStatus trainingStatus = new TrainingStatus();
                trainingStatus.setId(cursor.getInt(
                        cursor.getColumnIndex("id")));
                trainingStatus.setName(cursor.getString(
                        cursor.getColumnIndex("name")));
                list.add(trainingStatus);

                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setId(trainingStatus.getId());
                spinnerModel.setName(trainingStatus.getName());
                listSpinner.add(spinnerModel);
            }
        }
        // closing connection
        cursor.close();
        return list;
    }

    @Override
    public void onClick(int id) {

    }
}