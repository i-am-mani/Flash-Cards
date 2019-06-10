package com.omega.Fragments;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.omega.Adaptors.SimpleFlashCardViewerAdapter;
import com.omega.Adaptors.TrueFalseModePlayAdaptor;
import com.omega.Database.FlashCards;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.ISwitchToFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrueFalsePlayModeFragment extends Fragment {

    private final static String KEY = "GroupName";
    private static long START_TIME = 0;
    @BindView(R.id.button_correct)
    ImageButton btnCorrect;
    private String groupName;

    @BindView(R.id.text_time)
    TextView tvTime;

    @BindView(R.id.text_score)
    TextView tvScore;
    Handler timerHandler = new Handler();

    @BindView(R.id.recycler_view_play_mode_flashcards)
    RecyclerView rvPlayCard;

    @BindView(R.id.text_play_mode_hint)
    TextView tvHint;
    @BindView(R.id.button_wrong)
    ImageButton btnWrong;
    private Score scoreHandeler;

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - START_TIME;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            tvTime.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };
    private TrueFalseModePlayAdaptor trueFalseModePlayAdaptor;
    private FlashCardViewModel flashCardViewModel;
    private ISwitchToFragment ImplSwitchToFragment;

    public TrueFalsePlayModeFragment() {
        //No Argument constructor
    }

    public TrueFalsePlayModeFragment(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(groupName).observe(this, flashCards -> {
            trueFalseModePlayAdaptor.setDataSet(flashCards);
            if (flashCards.size() > 0) {
                tvHint.setVisibility(View.GONE);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_play_mode_true_false, container, false);
        ButterKnife.bind(this, mainView);
        if (savedInstanceState != null) {
            groupName = savedInstanceState.getString(KEY);
        }
        initializeRecyclerView();
        tvHint.setText(R.string.hint_create_section_flashcard);
        getActivity().setTitle("Play mode");
        scoreHandeler = new Score(tvScore);
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY, groupName);
    }

    @Override
    public void onStart() {
        super.onStart();
        START_TIME = START_TIME == 0 ? System.currentTimeMillis() : START_TIME;
        tvTime.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        START_TIME = System.currentTimeMillis();
        tvTime.removeCallbacks(timerRunnable);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trueFalseModePlayAdaptor = new TrueFalseModePlayAdaptor(getActivity());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvPlayCard);


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
            rvPlayCard.addItemDecoration(decoration);
        }
        rvPlayCard.setLayoutManager(linearLayoutManager);
        rvPlayCard.setAdapter(trueFalseModePlayAdaptor);
    }

    @OnClick({R.id.button_correct, R.id.button_wrong})
    public void removeItemOnCorrect(View view) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvPlayCard.getLayoutManager();
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();

        SimpleFlashCardViewerAdapter.PlayModeViewHolder holder =
                (SimpleFlashCardViewerAdapter.PlayModeViewHolder) rvPlayCard.findViewHolderForAdapterPosition(pos);
        FlashCards card = trueFalseModePlayAdaptor.removeItemAtPos(pos);
        String sol = holder.solution;

        boolean isCorrect = (sol == card.getContent());

        if (view.getId() == R.id.button_correct) {
            if (isCorrect) {
                scoreHandeler.incrementCorrectAnswer();
            } else {
                scoreHandeler.incrementWrongAnswer(card);
            }

        } else {
            if (!isCorrect) {
                scoreHandeler.incrementCorrectAnswer();
            } else {
                scoreHandeler.incrementWrongAnswer(card);
            }

        }

        if (trueFalseModePlayAdaptor.isDataSetEmpty()) {
            afterExhaustingDataset();
        }

    }


    public void afterExhaustingDataset() {
        List<FlashCards> wrongAnswers = scoreHandeler.getWrongAnswers();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Result");
        builder.setMessage("Out of " + scoreHandeler.getAttempted() + " you have scored " + scoreHandeler.getCorrect());

        if (wrongAnswers.size() > 0) {
            builder.setPositiveButton("Check Mistaken FlashCards", (dialog, which) -> {
                trueFalseModePlayAdaptor.setDataSet(wrongAnswers);
                resetScoreAndTime();
            });
        } else {
            builder.setPositiveButton("Exit", (dialog, which) -> getActivity().onBackPressed());
        }

        builder.setNegativeButton("Retry", (dialog, which) -> {
            flashCardViewModel.getAllFlashCardsOfGroup(groupName).observe(getActivity(), flashCards -> {
                trueFalseModePlayAdaptor.setDataSet(flashCards);
            });
            resetScoreAndTime();
        });
        builder.setOnCancelListener(dialog -> {
            getActivity().onBackPressed();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
        alertDialog.show();

    }

    private void resetScoreAndTime() {
        tvScore.setText("Score :");
        scoreHandeler = new Score(tvScore);
        START_TIME = System.currentTimeMillis();
    }
}

class Score {

    TextView scoreView;
    private int correct = 0;
    private int attempted = 0;
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
}
