package com.omega.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.omega.R;
import com.omega.Util.ISwitchToFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreenPlayModeFragment extends Fragment {

    private static final String KEY = "GROUP_KEY";
    ISwitchToFragment switchToFragmentImpl;
    private String GROUP_NAME;

    public SplashScreenPlayModeFragment() {

    }

    public SplashScreenPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            switchToFragmentImpl = (ISwitchToFragment) context;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_splash_screen, container, false);
        ButterKnife.bind(this, mainView);
        if (savedInstanceState != null) {
            GROUP_NAME = savedInstanceState.getString(KEY);
        }
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY, GROUP_NAME);
    }

    @OnClick(R.id.button_casual)
    public void goToCasualPlay(View view) {
        switchToFragmentImpl.switchToPlayModeCasual(GROUP_NAME);
    }

    @OnClick(R.id.button_true_false)
    public void goToTrueFlasePlay(View view) {
        switchToFragmentImpl.switchToPlayModeTrueFalse(GROUP_NAME);
    }

    @OnClick(R.id.button_reverse_match)
    public void goToReverseMatchPlay(View view) {
        switchToFragmentImpl.switchToPlayModeReverseMatch(GROUP_NAME);
    }
}
