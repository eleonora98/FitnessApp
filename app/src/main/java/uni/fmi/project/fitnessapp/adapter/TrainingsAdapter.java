package uni.fmi.project.fitnessapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.entity.Training;
import uni.fmi.project.fitnessapp.enums.TrainingStatusEnum;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.ViewHolder> {

    private List<Training> listdata;
    private OnClickListener onClickListener = null;

    // RecyclerView recyclerView;
    public TrainingsAdapter(List<Training> listdata, OnClickListener onClickListener) {
        this.listdata = listdata;
        this.onClickListener = onClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_trainings, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Training training = listdata.get(position);
        holder.nameTv.setText(training.getName());
        holder.durationTv.setText(training.getDuration());
        holder.statusTv.setText(TrainingStatusEnum.getStatusById(training.getStatusId()));

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(holder.getAdapterPosition(), training.getId());
                Log.d("adapter", "click listenr");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public TextView durationTv;
        public TextView statusTv;
        public ImageView imageView;
        public ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            this.durationTv = (TextView) itemView.findViewById(R.id.durationTv);
            this.statusTv = (TextView) itemView.findViewById(R.id.status_tv);
            relativeLayout = (ConstraintLayout) itemView.findViewById(R.id.itemCl);
        }
    }

    public interface OnClickListener {
        public void onClick(int position, int id);
    }
}