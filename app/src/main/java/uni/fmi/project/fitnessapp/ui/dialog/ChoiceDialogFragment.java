package uni.fmi.project.fitnessapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.interfaces.ChoiceDialogClickListener;

public class ChoiceDialogFragment extends DialogFragment implements View.OnClickListener {

    ChoiceDialogClickListener clickListener;
    Button cancelBtn;
    Button saveBothBtn;
    Button replaceBtn;

    public ChoiceDialogFragment(ChoiceDialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choice_dialog, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        cancelBtn = view.findViewById(R.id.cancel_btn);
        saveBothBtn = view.findViewById(R.id.save_both_btn);
        replaceBtn = view.findViewById(R.id.update_with_remote);

        cancelBtn.setOnClickListener(this);
        saveBothBtn.setOnClickListener(this);
        replaceBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancel_btn) {
            dismiss();
        } else if (view.getId() == R.id.save_both_btn) {
            clickListener.onSaveBothClickListener();
            dismiss();
        } else if (view.getId() == R.id.update_with_remote) {
            clickListener.onReplaceClickListener();
            dismiss();
        }
    }
}