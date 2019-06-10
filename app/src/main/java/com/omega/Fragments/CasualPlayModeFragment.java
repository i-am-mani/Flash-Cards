package com.omega.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.omega.Adaptors.CasualModePlayAdaptor;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CasualPlayModeFragment extends Fragment {

    @BindView(R.id.recycler_view_casual_mode_flash_cards)
    RecyclerView rvCasualPlay;
    private String GROUP_NAME;
    private CasualModePlayAdaptor casualModePlayAdaptor;
    private FlashCardViewModel flashCardViewModel;

    public CasualPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
            casualModePlayAdaptor.setDataSet(flashCards);
//            if (flashCards.size() > 0) {
//                tvHint.setVisibility(View.GONE);
//            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_casual, container, false);
        ButterKnife.bind(this, mainView);
        initializeVariables();
        return mainView;
    }

    private void initializeVariables() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d("TAG", "initRecyclerView: " + rvCasualPlay);
        casualModePlayAdaptor = new CasualModePlayAdaptor(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        rvCasualPlay.setAdapter(casualModePlayAdaptor);
        rvCasualPlay.setLayoutManager(linearLayoutManager);

        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
        rvCasualPlay.addItemDecoration(decoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvCasualPlay);

    }
}

