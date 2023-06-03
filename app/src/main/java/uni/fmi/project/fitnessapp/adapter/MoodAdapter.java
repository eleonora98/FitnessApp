package uni.fmi.project.fitnessapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.entity.Mood;
import uni.fmi.project.fitnessapp.enums.MoodEnum;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {

    private List<Mood> listdata;

    // RecyclerView recyclerView;
    private MoodAdapter.OnClickListener onClickListener = null;

    public MoodAdapter(List<Mood> listdata, OnClickListener onClickListener)
    {
        this.listdata = listdata;
        this.onClickListener = onClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_moods, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mood mood = listdata.get(position);
        String name = MoodEnum.getStringMood(mood.getMoodId());
        int resId = MoodEnum.getDrawableByMood(mood.getMoodId());
        holder.dateTv.setText(mood.getDate());
        holder.nameTv.setText(name);
        holder.imageView.setBackgroundResource(resId);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(holder.getAdapterPosition(), mood.getId());
                Log.d("adapter", "click listenr");
            }
        });
    }

    public void updateData(ArrayList<Mood> viewModels) {
        listdata.clear();
        listdata.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, Mood viewModel) {
        listdata.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        listdata.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public TextView dateTv;
        public ImageView imageView;
        public ConstraintLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nameTv = (TextView) itemView.findViewById(R.id.mood_tv);
            this.dateTv = (TextView) itemView.findViewById(R.id.date_tv);
            this.imageView = (ImageView) itemView.findViewById(R.id.mood_iv);
            relativeLayout = (ConstraintLayout) itemView.findViewById(R.id.itemCl);
        }
    }

    public interface OnClickListener {
        public void onClick(int position, int id);
    }
}