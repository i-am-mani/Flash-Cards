package com.omega.Adaptors;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

public class SimpleFlashCardViewerAdapter extends RecyclerView.Adapter<SimpleFlashCardViewerAdapter.PlayModeViewHolder> {

    Context context;
    protected List<FlashCards> dataSet;
    String TAG = TrueFalseModePlayAdaptor.class.getSimpleName();

    public SimpleFlashCardViewerAdapter(Context c) {
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
        String title = dataSet.get(position).getTitle();
        String content = dataSet.get(position).getContent();
        holder.onBind(title, content);
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    public FlashCards getItemAt(int pos) {
        return dataSet.get(pos);
    }

    public FlashCards removeItemAtPos(int pos) {
        if (pos < dataSet.size() && pos >= 0) {
            FlashCards cardToReturn = dataSet.get(pos);
            dataSet.remove(pos);
            notifyItemRemoved(pos);
            return cardToReturn;
        }
        return null;
    }

    public boolean isDataSetEmpty() {
        return dataSet.isEmpty();
    }

    public void setDataSet(List<FlashCards> ds) {
        dataSet = ds;
        notifyDataSetChanged();
    }

    public class PlayModeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_play_mode_content)
        TextView tvMainContent;

        View mainView;

        public String title, solution;

        public PlayModeViewHolder(@NonNull View itemView) {
            super(itemView);
            mainView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void onBind(String title, String solution) {
            this.title = title;
            this.solution = solution;
            tvMainContent.setText(title);
            attachListener();
        }

        private void attachListener() {

            mainView.setOnClickListener(v -> {
                revertCardAction();
            });
        }

        public void detachListener() {
            mainView.setOnClickListener(null);
        }

        public void revertCardAction() {

            float curX = mainView.getRotationX();
            Log.d(TAG, "onBindViewHolder: " + curX);
            if (curX < 180) {

                ObjectAnimator rotationY = ObjectAnimator.ofFloat(itemView, "rotationX", 180);
                rotationY.setDuration(400);

                ObjectAnimator textRotation = ObjectAnimator.ofFloat(tvMainContent, "rotationX", 180);
                textRotation.setDuration(400);

                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(tvMainContent, "text", (fraction, startValue, endValue) -> {
                    Log.d(TAG, "revertCardAction: startValue = " + startValue);
                    Log.d(TAG, "revertCardAction: endValue = " + endValue);
                    Log.d(TAG, "revertCardAction: fraction = " + fraction);
                    tvMainContent.setText((String) endValue);
                    return endValue;
                }, solution);
                objectAnimator.setDuration(1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(rotationY).with(textRotation);
                animatorSet.play(objectAnimator).after(200);
                animatorSet.start();

            } else {
                ObjectAnimator rotationY = ObjectAnimator.ofFloat(itemView, "rotationX", 0);
                rotationY.setDuration(400);

                ObjectAnimator textRotation = ObjectAnimator.ofFloat(tvMainContent, "rotationX", 0);
                textRotation.setDuration(400);

                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(tvMainContent, "text", (fraction, startValue, endValue) -> {
                    tvMainContent.setText((String) endValue);
                    return startValue;
                }, title);
                objectAnimator.setDuration(1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(rotationY).with(textRotation);
                animatorSet.play(objectAnimator).after(200);
                animatorSet.start();

            }
        }

    }

}
