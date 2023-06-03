package uni.fmi.project.fitnessapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.databinding.FragmentProfileBinding;
import uni.fmi.project.fitnessapp.entity.User;

public class ProfileFragment extends Fragment {

    private TextView name, email;
    User user;

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Intent intent = requireActivity().getIntent();
        if (intent == null) {
            Log.v("", "Intent is null");
        }
        user = (User) getActivity().getIntent().getParcelableExtra("user");
        if (user == null) {
            Log.v("", "bundle is: NULL");
        }
        Log.i("hbk", "user" + user.getEmail());

        initViews(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initViews(View view) {
        email = view.findViewById(R.id.email_tv);
        name = view.findViewById(R.id.username_tv);

        email.setText("Email: " + user.getEmail());
        name.setText("Name: " + user.getUsername());
    }
}