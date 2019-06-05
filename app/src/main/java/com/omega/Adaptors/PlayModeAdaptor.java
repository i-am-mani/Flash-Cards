package com.omega.Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omega.Database.FlashCards;
import com.omega.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayModeAdaptor extends RecyclerView.Adapter<PlayModeAdaptor.PlayModeViewHolder> {

    Context context;
    List<FlashCards> dataSet;
    String TAG = PlayModeAdaptor.class.getSimpleName();

    public PlayModeAdaptor(Context c) {
        context = c;
    }

    @NonNull
    @Override
    public PlayModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mainView = layoutInflater.inflate(R.layout.item_play_mode, parent, false);
        PlayModeViewHolder viewHolder = new PlayModeViewHolder(mainView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {

        View item = holder.mainView;

        String title = dataSet.get(position).getTitle();
        String content = dataSet.get(position).getContent();
        holder.tvMainContent.setText(title);

        item.setOnClickListener(v -> {
            if (dataSet != null) {
                float curY = item.getRotationY();
                Log.d(TAG, "onBindViewHolder: " + curY);
                if (curY < 180) {
                    item.animate().rotationY(180).setDuration(400)
                            .withStartAction(() -> holder.tvMainContent.animate().rotationY(180).alpha(0).setDuration(100)
                                    .withEndAction(() -> holder.tvMainContent.animate().alpha(1).setDuration(100)
                                            .withStartAction(() -> holder.tvMainContent.setText(content))))
                            .start();
//                            .withEndAction(() -> holder.tvMainContent.setText(content))
                } else {
                    item.animate().rotationY(0).setDuration(400)
                            .withStartAction(() -> holder.tvMainContent.animate().rotationY(0).alpha(0).setDuration(100)
                                    .withEndAction(() -> holder.tvMainContent.animate().alpha(1).setDuration(100).
                                            withStartAction(() -> holder.tvMainContent.setText(title))))
                            .start();
//                            .withEndAction(() -> holder.tvMainContent.setText(title))
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    public void setDataSet(List<FlashCards> ds) {
        dataSet = ds;
        notifyDataSetChanged();
    }

    class PlayModeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_play_mode_content)
        TextView tvMainContent;

        View mainView;

        public PlayModeViewHolder(@NonNull View itemView) {
            super(itemView);
            mainView = itemView;
            ButterKnife.bind(this, itemView);

        }

    }
}
