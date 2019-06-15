package com.omega.Fragments;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.omega.Util.Score;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrueFalsePlayModeFragment extends Fragment {

    private final static String GROUP_NAME_KEY = "GroupName";
    private static final String TIME_KEY = "Time";
    private String TAG = getClass().getSimpleName();

    private static long START_TIME = 0;

    private String groupName;

    private boolean STOP_TIMER = false;

    @BindView(R.id.text_time)
    TextView tvTime;

    @BindView(R.id.text_score)
    TextView tvScore;
    Handler timerHandler = new Handler();

    @BindView(R.id.recycler_view_play_mode_flashcards)
    RecyclerView rvPlayCard;


    @BindView(R.id.button_correct)
    ImageButton btnCorrect;

    @BindView(R.id.button_wrong)
    ImageButton btnWrong;

    @BindView(R.id.button_start_true_false)
    Button btnStart;

    private Score scoreHandler;

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!STOP_TIMER) {
                long millis = System.currentTimeMillis() - START_TIME;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvTime.setText(String.format("%d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 500);
            }
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
        if (flashCardViewModel.getTrueFalseAdaptorDataSet() == null && savedInstanceState == null) { // No Previous DataSet found ( in case on Orientation change)
            flashCardViewModel.getAllFlashCardsOfGroup(groupName).observe(this, flashCards -> {
                trueFalseModePlayAdaptor.setDataSet(flashCards);
            });
        } else {
            // Set previous DataSet and Score ( before orientation change)
            trueFalseModePlayAdaptor.setDataSet(flashCardViewModel.getTrueFalseAdaptorDataSet());
            scoreHandler = flashCardViewModel.getTrueFalseScore();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_play_mode_true_false, container, false);
        ButterKnife.bind(this, mainView);
        Log.d(TAG, "onCreateView: savedState " + savedInstanceState);
        if (savedInstanceState != null) {
            groupName = savedInstanceState.getString(GROUP_NAME_KEY);
            String timeString = savedInstanceState.getString(TIME_KEY);
            Log.d(TAG, "onCreateView: time " + timeString);
            START_TIME = Long.parseLong(timeString);
            btnStart.setVisibility(View.GONE);
            startTimer();
        } else {
            hideAllViews();
        }

        getActivity().setTitle("Play mode");
        initializeRecyclerView();
        scoreHandler = new Score(tvScore);
        return mainView;
    }

    private void hideAllViews() {
        rvPlayCard.setVisibility(View.GONE);
        btnCorrect.setVisibility(View.GONE);
        btnWrong.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GROUP_NAME_KEY, groupName);
        String time = String.valueOf(START_TIME);
        outState.putString(TIME_KEY, time);
        flashCardViewModel.setTrueFalseAdaptorDataSet(trueFalseModePlayAdaptor.getDataSet());
        flashCardViewModel.setTrueFalseScore(scoreHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
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
                scoreHandler.incrementCorrectAnswer();
            } else {
                scoreHandler.incrementWrongAnswer(card);
            }

        } else {
            if (!isCorrect) {
                scoreHandler.incrementCorrectAnswer();
            } else {
                scoreHandler.incrementWrongAnswer(card);
            }

        }

        if (trueFalseModePlayAdaptor.isDataSetEmpty()) {
            afterExhaustingDataset();
        }

    }

    @OnClick(R.id.button_start_true_false)
    public void startTrueFalse(View view) {
        btnStart.setVisibility(View.GONE);

        btnCorrect.setVisibility(View.VISIBLE);
        btnWrong.setVisibility(View.VISIBLE);
        rvPlayCard.setVisibility(View.VISIBLE);
        startTimer();
    }


    public void afterExhaustingDataset() {
        stopTimer();
        List<FlashCards> wrongAnswers = scoreHandler.getWrongAnswers();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Result");
        builder.setMessage("Out of " + scoreHandler.getAttempted() + " you have scored " + scoreHandler.getCorrect());

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
        tvScore.setText("");
        scoreHandler = new Score(tvScore);
        START_TIME = System.currentTimeMillis();
    }

    private void startTimer() {
        Log.d(TAG, "startTimer: start time " + START_TIME);
        START_TIME = START_TIME == 0 ? System.currentTimeMillis() : START_TIME;
        tvTime.postDelayed(timerRunnable, 0);
        STOP_TIMER = false;
    }

    private void stopTimer() {
        STOP_TIMER = true;
        tvTime.removeCallbacks(timerRunnable);
    }
}

