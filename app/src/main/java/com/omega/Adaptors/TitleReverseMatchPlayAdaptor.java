package com.omega.Adaptors;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.omega.Database.FlashCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TitleReverseMatchPlayAdaptor extends SimpleFlashCardViewerAdapter {

    private List<FlashCards> removedItemFromDataSet = new ArrayList<>();

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

        List<FlashCards> totalDataSet = new ArrayList<>();
        totalDataSet.addAll(dataSet); // Add existing data set
        totalDataSet.addAll(removedItemFromDataSet); // Add removed items from data set

        int questionCount = totalDataSet.size() > 4 ? 4 : totalDataSet.size();

        while (questionCount > 1) {
            int randInt = random.nextInt(totalDataSet.size());
            if (!solutions.contains(totalDataSet.get(randInt).getContent())) {
                String randSolution = totalDataSet.get(randInt).getContent();
                solutions.add(randSolution);
                questionCount--;
            }
        }
        Collections.shuffle(solutions);
        Log.d(TAG, "getSolutionOptions: solutions list =" + solutions + "Size =" + solutions.size());
        return solutions;
    }

    @Override
    public FlashCards removeItemAtPos(int pos) {
        FlashCards removedFc = super.removeItemAtPos(pos);
        removedItemFromDataSet.add(removedFc);
        return removedFc;
    }
}
