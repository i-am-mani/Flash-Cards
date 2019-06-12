package com.omega.Adaptors;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Random;


public class TrueFalseModePlayAdaptor extends SimpleFlashCardViewerAdapter {

    public TrueFalseModePlayAdaptor(Context c) {
        super(c);
    }


    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {

        Random random = new Random();
        Boolean isSame = random.nextBoolean();

        if (isSame) {
            super.onBindViewHolder(holder, position);
        } else {
            setRandomSolution(holder, position);
        }

    }

    private void setRandomSolution(PlayModeViewHolder holder, int pos) {
        Random random = new Random();
        String title = dataSet.get(pos).getTitle();
        int randomSolutionIndex = random.nextInt(dataSet.size());
        String solution = dataSet.get(randomSolutionIndex).getContent();
        holder.onBind(title, solution);
    }
}
