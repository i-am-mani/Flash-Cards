package com.omega.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReverseMatchPlayModeFragment extends Fragment {
    String GROUP_NAME;
    FlashCardViewModel flashCardViewModel;


    @BindView(R.id.recycler_view_title_flash_cards)
    RecyclerView rvTitleFlashCards;

    @BindView(R.id.recycler_view_solution_flash_cards)
    RecyclerView rvSolutionFlashCards;

    public ReverseMatchPlayModeFragment(String group) {
        GROUP_NAME = group;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
        rvSolutionFlashCards.addItemDecoration(decoration);
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
    }

    private void initSolutionRecyclerView() {

        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
        rvTitleFlashCards.addItemDecoration(decoration);
    }

    private void initTitleRecyclerView() {
        /**
         * Set adapter, get current title on screen, ask for 4 alternative solutions (1 right 3 wrong) for that title,
         * Set adapter of solution RV, on click check if it's the correct answer ( if yes ++score else --score )
         */
    }
}
