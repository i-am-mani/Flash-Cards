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
    private static String GROUP_NAME;
    private CasualModePlayAdaptor casualModePlayAdaptor;
    private FlashCardViewModel flashCardViewModel;

    public CasualPlayModeFragment() {
        //No-arg constructor
    }

    public CasualPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
//            if (savedInstanceState != null) {
//                useSavedInstanceState();
//            } else {
            casualModePlayAdaptor.setDataSet(flashCards);
//            }
        });
    }
//
//    private void useSavedInstanceState() {
//        casualModePlayAdaptor.set
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_casual, container, false);
        ButterKnife.bind(this, mainView);
        initializeVariables();
        getActivity().setTitle("Casual Mode");
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("GroupName", GROUP_NAME);
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

