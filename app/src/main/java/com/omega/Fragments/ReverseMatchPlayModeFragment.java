package com.omega.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.omega.Adaptors.SolutionReverseMatchPlayAdaptor;
import com.omega.Adaptors.TitleReverseMatchPlayAdaptor;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.Score;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReverseMatchPlayModeFragment extends Fragment {
    static String GROUP_NAME;
    TitleReverseMatchPlayAdaptor titleAdaptor;
    FlashCardViewModel flashCardViewModel = null;
    SolutionReverseMatchPlayAdaptor solutionAdaptor = null;
    private long START_TIME;


    @BindView(R.id.text_time)
    TextView tvTime;

    @BindView(R.id.recycler_view_title_flash_cards)
    RecyclerView rvTitleFlashCards;

    @BindView(R.id.recycler_view_solution_flash_cards)
    RecyclerView rvSolutionFlashCards;

    @BindView(R.id.button_start_reverse_match)
    Button btnStart;

    @BindView(R.id.text_score)
    TextView tvScore;

    @BindView(R.id.text_solution_reverse_match)
    TextView tvSolution;

    private int prePos = -1;

    private Score scoreHandler;
    private Handler timerHandler = new Handler();

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

    public ReverseMatchPlayModeFragment() {
        //no arg constructor
    }

    public ReverseMatchPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
            if (savedInstanceState != null) {
                long time = savedInstanceState.getLong("StartTime");
                START_TIME = time;
                scoreHandler = flashCardViewModel.getReverseScore();
                resetTitleAdaptor();
                resetSolutionAdaptor();
            } else {
                titleAdaptor.setDataSet(flashCards);
            }
        });
    }

    private void resetTitleAdaptor() {
        titleAdaptor = flashCardViewModel.getTitleReverseMatchPlayAdaptor();
        rvTitleFlashCards.setAdapter(titleAdaptor);
    }

    private void resetSolutionAdaptor() {
        solutionAdaptor = flashCardViewModel.getSolutionReverseMatchPlayAdaptor();
        rvSolutionFlashCards.setAdapter(solutionAdaptor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_reverse_match, container, false);
        ButterKnife.bind(this, mainView);
        hideViews();
        initializeVariables();
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("StartTime", START_TIME);
        flashCardViewModel.setTitleReverseMatchPlayAdaptor(titleAdaptor);
        flashCardViewModel.setSolutionReverseMatchPlayAdaptor(solutionAdaptor);
        flashCardViewModel.setReverseScore(scoreHandler);
    }

    private void hideViews() {
        rvTitleFlashCards.setVisibility(View.INVISIBLE);
        rvSolutionFlashCards.setVisibility(View.INVISIBLE);
        tvSolution.setVisibility(View.INVISIBLE);
    }

    private void initializeVariables() {
        initTitleRecyclerView();
        initSolutionRecyclerView();
        if (scoreHandler == null) {
            scoreHandler = new Score(tvScore);
        }
    }

    private void initSolutionRecyclerView() {
        if (solutionAdaptor == null) {
            solutionAdaptor = new SolutionReverseMatchPlayAdaptor(getActivity(), new ImplSolutionAdaptorCallbacks());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(20);

        rvSolutionFlashCards.setAdapter(solutionAdaptor);
        rvSolutionFlashCards.setLayoutManager(linearLayoutManager);
        rvSolutionFlashCards.addItemDecoration(decoration);
    }

    private void initTitleRecyclerView() {
        if (titleAdaptor == null) {
            titleAdaptor = new TitleReverseMatchPlayAdaptor(getActivity());
        }

        LinearLayoutManager titleLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvTitleFlashCards);

        rvTitleFlashCards.setAdapter(titleAdaptor);
        rvTitleFlashCards.setLayoutManager(titleLinearLayoutManager);
        rvTitleFlashCards.addItemDecoration(decoration);

        rvTitleFlashCards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("OnScrolled", "onScrolled: " + dx + " " + dy);
                int widthPixels = rvTitleFlashCards.getWidth();
                // Set Solution RV data set
                setSolutionAdaptorDataSet();
            }
        });
    }

    @OnClick(R.id.button_start_reverse_match)
    public void startReverseMatch(View v) {
        v.setVisibility(View.GONE);
        rvSolutionFlashCards.setVisibility(View.VISIBLE);
        rvTitleFlashCards.setVisibility(View.VISIBLE);
        tvSolution.setVisibility(View.VISIBLE);
        // init  timer
        startTimer();
        setSolutionAdaptorDataSet(); // Initial Data set
    }

    private void startTimer() {
        START_TIME = START_TIME == 0 ? System.currentTimeMillis() : START_TIME;
        tvTime.post(timerRunnable);
    }

    public void setSolutionAdaptorDataSet() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvTitleFlashCards.getLayoutManager();
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (pos >= 0 && prePos != pos) {
            setSolutionDataSet(pos);
        }
    }

    private void setSolutionDataSet(int pos) {
        List<String> options = titleAdaptor.getSolutionOptions(pos);
        solutionAdaptor.setDataSet(options);
        prePos = pos;
    }


    public class ImplSolutionAdaptorCallbacks implements SolutionReverseMatchPlayAdaptor.ISolutionCallbacks {
        @Override
        public String getValidateSolution() {
            LinearLayoutManager layoutManager = (LinearLayoutManager) rvTitleFlashCards.getLayoutManager();

            int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
            String solution = titleAdaptor.getItemAt(pos).getContent();
            return solution;
        }

        @Override
        public void updateScore(boolean isCorrect) {
            if (isCorrect) {
                scoreHandler.incrementCorrectAnswer();
            } else {
                scoreHandler.incrementWrongAnswer();
            }
        }

        @Override
        public void moveToNextCard() {
            LinearLayoutManager layoutManager = (LinearLayoutManager) rvTitleFlashCards.getLayoutManager();
            int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (!removeFlashCard(pos)) {
                setNewDataSet(pos);
            }
        }

        private void setNewDataSet(int pos) {
            if (pos != 0) {
                setSolutionDataSet(pos - 1);
            } else if (pos == 0) {
                setSolutionDataSet(pos);
            }
        }

        private boolean removeFlashCard(int pos) {
            titleAdaptor.removeItemAtPos(pos);

            if (titleAdaptor.isDataSetEmpty()) {
                showFinishedAlertDialog();
                return true;
            }
            return false;
        }

        private void showFinishedAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Result");
            builder.setMessage("Out of " + scoreHandler.getAttempted() + " you have scored " + scoreHandler.getCorrect());

            builder.setPositiveButton("Exit", (dialog, which) -> getActivity().onBackPressed());

            builder.setOnCancelListener(dialog -> {
                getActivity().onBackPressed();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            alertDialog.show();
        }

    }
}
