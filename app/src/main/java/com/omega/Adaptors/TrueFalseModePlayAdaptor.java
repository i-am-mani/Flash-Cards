package com.omega.Adaptors;

import android.content.Context;

import androidx.annotation.NonNull;

import com.omega.Database.FlashCards;

import java.util.List;
import java.util.Random;


public class TrueFalseModePlayAdaptor extends SimpleFlashCardViewerAdapter {

    public TrueFalseModePlayAdaptor(Context c) {
        super(c);
    }


    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {

        Random random = new Random();
        boolean isSame = random.nextBoolean();

        if (isSame) {
            super.onBindViewHolder(holder, position);
        } else {
            setRandomSolution(holder, position);
        }


    }

    private void setRandomSolution(PlayModeViewHolder holder, int pos) {
        Random random = new Random();
        int randomSolutionIndex = random.nextInt(dataSet.size());
        while (randomSolutionIndex == pos && dataSet.size() > 1) {
            randomSolutionIndex = random.nextInt(dataSet.size());
        }
        String title = dataSet.get(pos).getTitle();
        String solution = dataSet.get(randomSolutionIndex).getContent();
        holder.onBind(title, solution);
    }

    public List<FlashCards> getDataSet() {
        return dataSet;
    }
}
