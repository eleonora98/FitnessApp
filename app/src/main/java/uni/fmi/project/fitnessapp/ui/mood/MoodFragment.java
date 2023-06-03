package uni.fmi.project.fitnessapp.ui.mood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.adapter.MoodAdapter;
import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Mood;
import uni.fmi.project.fitnessapp.ui.add_mood.AddMoodFragment;

public class MoodFragment extends Fragment implements MoodAdapter.OnClickListener {

    private Button addBtn;

    private RecyclerView mRecyclerView;
    private MoodAdapter mCustomAfapter;
    private List<Mood> mDataset;
    SqLiteHelper dbHelper;
    SQLiteDatabase db;

    private Mood mood;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);
        dbHelper = new SqLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        mDataset = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerView);

        new Thread( new Runnable() { @Override public void run() {
            mDataset = getDbMoodsInfo();
        } } ).start();

        if (mDataset != null) {
            mCustomAfapter = new MoodAdapter(mDataset, this);
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mCustomAfapter);
        }
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        addBtn = view.findViewById(R.id.add_mood);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddMoodFragment(mRecyclerView, mCustomAfapter);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT);
            }
        });
    }

    @SuppressLint("Range")
    private List<Mood> getDbMoodsInfo() {
        Cursor cursor = db.rawQuery("Select * from moods order by id desc", null);
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                mood = new Mood();
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                Integer moodId = cursor.getInt(cursor.getColumnIndex("mood_id"));
                String date = cursor.getString(cursor.getColumnIndex("date_created"));
                mood.setId(id);
                mood.setMoodId(moodId);
                mood.setDate(date);
                mDataset.add(mood);
                Log.d("MoodFr", "mood" +  mood);
            }
        }
        cursor.close();
        return mDataset;
    }

    private void deleteMoodById(int position, int id) {
        db.delete(DbConstants.MOODS, "id = ?", new String[]{Integer.toString(id)});
        mCustomAfapter.removeItem(position);
    }

    private void showAlertToDelete(int position, int id) {
        AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        d.setTitle("Delete mood");
        d.setMessage("Are you sure you want to delete this mood?");
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMoodById(position, id);
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    @Override
    public void onClick(int position, int id) {
        showAlertToDelete(position, id);
    }
}