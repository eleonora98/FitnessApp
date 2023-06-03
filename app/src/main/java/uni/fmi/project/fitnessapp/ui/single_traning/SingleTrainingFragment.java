package uni.fmi.project.fitnessapp.ui.single_traning;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.config.ApiRestProvider;
import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Training;
import uni.fmi.project.fitnessapp.enums.TrainingStatusEnum;
import uni.fmi.project.fitnessapp.interfaces.ChoiceDialogClickListener;
import uni.fmi.project.fitnessapp.service.TrainingsService;
import uni.fmi.project.fitnessapp.ui.dialog.ChoiceDialogFragment;

public class SingleTrainingFragment extends Fragment implements View.OnClickListener,
        ChoiceDialogClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static int trainingId = 1;

    private String mParam1;
    private String mParam2;

    private TextView nameTv;
    private TextView statusTv;
    private TextView descrTv;
    private TextView durationTV;

    private TextView nameRemoteTv;
    private TextView statusRemoteTv;
    private TextView descrRemoteTv;
    private TextView durationRemoteTV;
    private Button editBtn;
    private Button checkBtn;
    private Button getInfoBtn;

    SqLiteHelper dbHelper;
    SQLiteDatabase db;
    private TrainingsService trainingsService;
    private Training trainingRemote;
    private Training training;
    private NavController navController;

    public SingleTrainingFragment() {
        // Required empty public constructor
    }

    public static SingleTrainingFragment newInstance(String param1, String param2) {
        SingleTrainingFragment fragment = new SingleTrainingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SqLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        if (getArguments() != null) {
            trainingId = getArguments().getInt("trainingId");
        }
        trainingsService = ApiRestProvider.getClient().create(TrainingsService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_training, container, false);
//        trainingId = getActivity().getIntent().getExtras().getInt("trainingId");
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void initViews(View view){
        nameTv = view.findViewById(R.id.nameTv);
        durationTV = view.findViewById(R.id.durationTv);
        descrTv = view.findViewById(R.id.descrTv);
        statusTv = view.findViewById(R.id.statusTv);
        nameRemoteTv = view.findViewById(R.id.remote_name_tv);
        descrRemoteTv = view.findViewById(R.id.remote_descr_tv);
        durationRemoteTV = view.findViewById(R.id.remote_duration_tv);
        statusRemoteTv = view.findViewById(R.id.remote_status_tv);
        editBtn = view.findViewById(R.id.edit_btn);
        checkBtn = view.findViewById(R.id.check_btn);
        getInfoBtn = view.findViewById(R.id.get_info_btn);

        editBtn.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
        getInfoBtn.setOnClickListener(this);

        getDbTrainingById();

    }

    @SuppressLint("Range")
    private void getDbTrainingById() {
        Cursor cursor = db.rawQuery("Select * from trainings where id = " + trainingId, null);
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                int statusId = cursor.getInt(cursor.getColumnIndex("status_id"));

                nameTv.setText(name);
                descrTv.setText(description);
                durationTV.setText(duration);
                statusTv.setText(TrainingStatusEnum.getStatusById(statusId));

                training = new Training();
                training.setId(id);
                training.setName(name);
                training.setDuration(duration);
                training.setDescription(description);
                training.setStatusId(statusId);
            }
        }
    }

    private void getTrainingByName(String name) {
        Call<Training> trainingCall = trainingsService.getTrainingByName(name);
        trainingCall.enqueue(new Callback<Training>() {
            @Override
            public void onResponse(Call<Training> call, Response<Training> response) {
                trainingRemote = response.body();
                nameRemoteTv.setText(trainingRemote.getName());
                durationRemoteTV.setText(trainingRemote.getDuration());
                descrRemoteTv.setText(trainingRemote.getDescription());
                statusRemoteTv.setText(TrainingStatusEnum.getStatusById(
                        trainingRemote.getStatusId()));
            }

            @Override
            public void onFailure(Call<Training> call, Throwable t) {

            }
        });
    }

    private void checkForChanges() {
        if (training == null || trainingRemote == null) {
            return;
        }
        if (training.getName().equals(trainingRemote.getName())) {
            if (!training.getDescription().equals(trainingRemote.getDescription())
            || !training.getDuration().equals(trainingRemote.getDuration())
            || !training.getStatusId().equals(trainingRemote.getStatusId())) {
                showPopup();
            } else {
                showOkPopu("Info", "No changes.");
            }
        }
    }

    private void showPopup() {
        DialogFragment dialogFragment = new ChoiceDialogFragment(this);
        dialogFragment.show(getParentFragmentManager(), "dialog");
    }

    private void showOkPopu(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show().create();
    }

    private void insertTraining(Training training) {
        String sql = "INSERT INTO Trainings (name, description, duration, status_id, level_id) "
                + "VALUES ('" + training.getName() + "', '" + training.getDescription() + "', '"
                + training.getDuration() + "', '" + String.valueOf(training.getStatusId()) + "', '"
                + String.valueOf(training.getLevelId() + "'); " );
        ContentValues cv = new ContentValues();
        cv.put("name", training.getName());
        cv.put("description", training.getDescription());
        cv.put("duration", training.getDuration());
        cv.put("status_id", training.getStatusId());
        if (training.getLevelId() != null) cv.put("level_id", training.getLevelId());
        db.insertOrThrow(DbConstants.TRAININGS, "level_id", cv);
    }

    private void updateTrainingWith(Training training) {
        String sql = "UPDATE Trainings SET name = ?, description = ?, duration = ?, status_id = ?," +
                "level_id = ? ";
        ContentValues cv = new ContentValues();
        cv.put("description", training.getDescription());
        cv.put("duration", training.getDuration());
        cv.put("status_id", training.getStatusId());
        cv.put("level_id", training.getLevelId());
        try {
            db.update(DbConstants.TRAININGS, cv, "name = ?", new String[]{training.getName()});
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edit_btn) {
            Bundle bundle = new Bundle();
            bundle.putInt("trainingId", trainingId);
            Log.i("SingleTr", "singleee " + trainingId);
            navController.navigate(R.id.action_singleTrainingFragment_to_addEditTrainingFragment, bundle);
        } else if (view.getId() == R.id.check_btn) {
            checkForChanges();
        } else if (view.getId() == R.id.get_info_btn) {
            getTrainingByName(training.getName());
        }
    }

    @Override
    public void onCancelClickListener() {

    }

    @Override
    public void onSaveBothClickListener() {
        insertTraining(trainingRemote);
        navController.navigate(R.id.action_singleTrainingFragment_to_navigation_trainings);
    }

    @Override
    public void onReplaceClickListener() {
        updateTrainingWith(trainingRemote);
        navController.navigate(R.id.action_singleTrainingFragment_to_navigation_trainings);
    }
}