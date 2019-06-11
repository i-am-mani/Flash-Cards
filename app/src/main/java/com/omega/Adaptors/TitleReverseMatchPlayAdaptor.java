package com.omega.Adaptors;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TitleReverseMatchPlayAdaptor extends SimpleFlashCardViewerAdapter {

    public TitleReverseMatchPlayAdaptor(Context c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.detachListener();
    }

    public List<String> getSolutionOptions(int pos) {
        List<String> solutions = new ArrayList<>();
        String correctSol = getItemAt(pos).getContent();
        Random random = new Random();
        //add correct answer
        solutions.add(correctSol);

        int questionCount = dataSet.size() > 4 ? 4 : dataSet.size();

        while (questionCount > 1) {
            int randInt = random.nextInt(dataSet.size());
            if (!solutions.contains(dataSet.get(randInt).getContent())) {
                String randSolution = getItemAt(randInt).getContent();
                solutions.add(randSolution);
                questionCount--;
            }
        }
        Collections.shuffle(solutions);
        Log.d(TAG, "getSolutionOptions: solutions list =" + solutions + "Size =" + solutions.size());
        return solutions;
    }
}
