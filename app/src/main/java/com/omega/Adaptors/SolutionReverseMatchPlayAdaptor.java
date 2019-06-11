package com.omega.Adaptors;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.omega.Fragments.ReverseMatchPlayModeFragment;
import com.omega.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SolutionReverseMatchPlayAdaptor extends RecyclerView.Adapter<SolutionReverseMatchPlayAdaptor.SolutionsAdaptor> {

    private final Context context;
    List<String> dataSet;
    ReverseMatchPlayModeFragment.ImplSolutionAdaptorCallbacks adaptorCallbacks;

    public SolutionReverseMatchPlayAdaptor(Context c, ReverseMatchPlayModeFragment.ImplSolutionAdaptorCallbacks implSolutionAdaptorCallbacks) {
        context = c;
        adaptorCallbacks = implSolutionAdaptorCallbacks;
    }

    @NonNull
    @Override
    public SolutionsAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mainView = layoutInflater.inflate(R.layout.item_flash_card, parent, false);
        SolutionsAdaptor solutionsAdaptor = new SolutionsAdaptor(mainView);
        return solutionsAdaptor;
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionsAdaptor holder, int position) {
        holder.onBind(dataSet.get(position));
    }

    public void setDataSet(List<String> data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dataSet != null) {
            return dataSet.size();
        } else {
            return 0;
        }
    }

    public interface ISolutionCallbacks {
        String getValidateSolution();
    }

    public class SolutionsAdaptor extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView tvTitle;
        @BindView(R.id.card_flash_card)
        CardView cardView;

        public SolutionsAdaptor(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void attachCallback(String solution) {
            itemView.setOnClickListener(v -> {
                String sol = adaptorCallbacks.getValidateSolution();
                if (sol == solution) {
                    Log.d("SolutionsAdaptor", "attachCallback: Correct answer");

                    ObjectAnimator changeColor = ObjectAnimator.ofArgb(cardView, "backgroundColor",
                            context.getResources().getColor(R.color.G1),
                            context.getResources().getColor(R.color.G2),
                            context.getResources().getColor(R.color.G3),
                            context.getResources().getColor(R.color.G4),
                            context.getResources().getColor(R.color.G5));
                    changeColor.setDuration(400);
                    changeColor.start();
                } else {
                    Log.d("SolutionsAdaptor", "attachCallback: Wrong answer");
                    ObjectAnimator changeColor = ObjectAnimator.ofArgb(cardView, "backgroundColor",
                            context.getResources().getColor(R.color.R1),
                            context.getResources().getColor(R.color.R2),
                            context.getResources().getColor(R.color.R3),
                            context.getResources().getColor(R.color.R4),
                            context.getResources().getColor(R.color.R5));
                    changeColor.setDuration(400);
                    changeColor.start();
                }
            });
        }

        public void onBind(String solution) {
            tvTitle.setText(solution);
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.DarkModePrimaryDarkColor));
            attachCallback(solution);
        }
    }
}
