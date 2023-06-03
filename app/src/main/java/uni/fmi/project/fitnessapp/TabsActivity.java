package uni.fmi.project.fitnessapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.fmi.project.fitnessapp.config.ApiRestProvider;
import uni.fmi.project.fitnessapp.databinding.ActivityTabsBinding;
import uni.fmi.project.fitnessapp.db.DbConstants;
import uni.fmi.project.fitnessapp.db.SqLiteHelper;
import uni.fmi.project.fitnessapp.entity.Training;
import uni.fmi.project.fitnessapp.entity.User;
import uni.fmi.project.fitnessapp.service.TrainingsService;

public class TabsActivity extends FragmentActivity {

    private ActivityTabsBinding binding;

    SqLiteHelper dbHelper;
    SQLiteDatabase db;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = (User) getIntent().getExtras().getParcelable("user");
        getIntent().putExtra("user", user);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_moods, R.id.navigation_trainings, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        dbHelper = new SqLiteHelper(this);
        db = dbHelper.getWritableDatabase();
//        trainingsService = ApiRestProvider.getClient().create(TrainingsService.class);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("user", user);
    }

}