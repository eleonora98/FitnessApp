package uni.fmi.project.fitnessapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.ui.model.SpinnerModel;

public class SpinAdapter<T> extends ArrayAdapter<SpinnerModel> {
    private Context context;
    private List<SpinnerModel> values;

    private OnClickListener onCLickListener;

    private LayoutInflater layoutInflater;

    public SpinAdapter(Context context, int textViewResourceId, List<SpinnerModel> values,
                       OnClickListener oncCLickListener) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.onCLickListener = oncCLickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return values.size();
    }

    public SpinnerModel getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.custom_spinner, null);

        TextView label = convertView.findViewById(R.id.spinner_tv);
        label.setTextColor(R.color.black);
        label.setText(values.get(position).getName());
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickListener.onClick(values.get(position).getId());
            }
        });
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.custom_spinner, null);
        TextView label = convertView.findViewById(R.id.spinner_tv);
        label.setTextColor(R.color.black);
        label.setText(values.get(position).getName());

        return convertView;
    }

    public interface OnClickListener {
        public void onClick(int id);
    }

}
