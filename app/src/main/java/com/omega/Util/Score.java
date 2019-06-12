package com.omega.Util;

import android.widget.TextView;

import com.omega.Database.FlashCards;

import java.util.ArrayList;
import java.util.List;

public class Score {

    TextView scoreView;
    private int correct = 0;
    private int attempted = 0;
    private int wrongAnswer = 0;
    private List<FlashCards> wrongAnswerList = new ArrayList<>();

    public Score(TextView scoreView) {
        this.scoreView = scoreView;
    }

    public int getCorrect() {
        return correct;
    }

    public int getAttempted() {
        return attempted;
    }

    public void incrementCorrectAnswer() {
        ++attempted;
        ++correct;
        updateScore();
    }

    public void incrementWrongAnswer(FlashCards card) {
        attempted++;
        wrongAnswerList.add(card);
        updateScore();
    }

    private void updateScore() {
        scoreView.setText(correct + " : " + attempted);
    }

    public List<FlashCards> getWrongAnswers() {
        return wrongAnswerList;
    }

    public void incrementWrongAnswer() {
        ++attempted;
        ++wrongAnswer;
        updateScore();
    }
}
