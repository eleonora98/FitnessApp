package uni.fmi.project.fitnessapp.ui.trainings;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.adapter.TrainingsAdapter;
import uni.fmi.project.fitnessapp.databinding.FragmentTrainingsBinding;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Training;

public class TrainingsFragment extends Fragment implements TrainingsAdapter.OnClickListener {

    private FragmentTrainingsBinding binding;
    private RecyclerView mRecyclerView;
    private TrainingsAdapter mCustomAfapter;
    private List<Training> mDataset;
    SqLiteHelper dbHelper;
    SQLiteDatabase db;

    Button addBtn;
    private Training training;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrainingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addBtn = root.findViewById(R.id.add_training);
        dbHelper = new SqLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        mDataset = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.recyclerView);

        new Thread( new Runnable() { @Override public void run() {
            getDbTrainingsInfo();
        } } ).start();
        mCustomAfapter = new TrainingsAdapter(mDataset, this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCustomAfapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("trainingId", 0);
                navController.navigate(R.id.action_navigation_trainings_to_addEditTrainingFragment, bundle);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("Range")
    private List<Training> getDbTrainingsInfo() {
        Cursor cursor = db.rawQuery("Select * from trainings", null);
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                training = new Training();

                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String descr = cursor.getString(cursor.getColumnIndex("description"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                Integer statusId = cursor.getInt(cursor.getColumnIndex("status_id"));
                training.setId(id);
                training.setName(name);
                training.setDescription(descr);
                training.setStatusId(statusId);
                training.setDuration(duration);
                mDataset.add(training);
                Log.d("TrFr" ,"tarrara" + training.getDescription());
            }
        }
        cursor.close();
        return mDataset;
    }

    @Override
    public void onClick(int position, int id) {
        Toast.makeText(getContext(),"Clicked!", Toast.LENGTH_SHORT).show();
        Log.d("fragment", "click listenr");
        Bundle bundle = new Bundle();
        bundle.putInt("trainingId", id);
        navController.navigate(R.id.action_navigation_trainings_to_singleTrainingFragment, bundle);
    }
}