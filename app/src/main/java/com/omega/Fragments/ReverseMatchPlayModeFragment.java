package com.omega.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    TitleReverseMatchPlayAdaptor titleAdaptor;
    String GROUP_NAME;
    FlashCardViewModel flashCardViewModel;
    SolutionReverseMatchPlayAdaptor solutionAdaptor;
    FlashCardViewModel viewModel;
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

    private int prePos = -1;

    private Score score;
    private long START_TIME;
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


    public ReverseMatchPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        viewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
            titleAdaptor.setDataSet(flashCards);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_reverse_match, container, false);
        ButterKnife.bind(this, mainView);
        initializeVariables();
        return mainView;
    }

    private void initializeVariables() {
        initTitleRecyclerView();
        initSolutionRecyclerView();
        score = new Score(tvScore);
    }

    private void initSolutionRecyclerView() {
        solutionAdaptor = new SolutionReverseMatchPlayAdaptor(getActivity(), new ImplSolutionAdaptorCallbacks());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(20);

        rvSolutionFlashCards.setAdapter(solutionAdaptor);
        rvSolutionFlashCards.setLayoutManager(linearLayoutManager);
        rvSolutionFlashCards.addItemDecoration(decoration);
    }

    private void initTitleRecyclerView() {
        titleAdaptor = new TitleReverseMatchPlayAdaptor(getActivity());

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
        // init  timer
        START_TIME = System.currentTimeMillis();
        tvTime.post(timerRunnable);
        setSolutionAdaptorDataSet(); // Initial Data set
    }


    public void setSolutionAdaptorDataSet() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvTitleFlashCards.getLayoutManager();
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (pos >= 0 && prePos != pos) {
            List<String> options = titleAdaptor.getSolutionOptions(pos);
            solutionAdaptor.setDataSet(options);
            prePos = pos;
        }
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
                score.incrementCorrectAnswer();
            } else {
                score.incrementWrongAnswer();
            }
        }

        @Override
        public void moveToNextCard() {
            LinearLayoutManager layoutManager = (LinearLayoutManager) rvTitleFlashCards.getLayoutManager();
            int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
            titleAdaptor.removeItemAtPos(pos);
            setSolutionAdaptorDataSet();
        }
    }
}
