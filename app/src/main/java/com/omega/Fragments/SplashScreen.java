package com.omega.Fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.omega.R;
import com.omega.Util.ISwitchToFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreen extends Fragment {

    ISwitchToFragment ImplSwitchToFragment;

    public SplashScreen() {
        // Required empty public constructor
    }

    public static SplashScreen newInstance() {
        SplashScreen fragment = new SplashScreen();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ImplSwitchToFragment = (ISwitchToFragment) context;
        } catch (ClassCastException c ) {
            throw new ClassCastException();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ButterKnife.bind(this, mainView);

        return mainView;
    }

    @OnClick({R.id.button_check_out, R.id.button_create})
    public void play(View view) {
        Button btn = (Button) view;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btn, "rotation", 0f, 360f);
        objectAnimator.setDuration(800);

        float density = Resources.getSystem().getDisplayMetrics().density;
        int btnWidth = btn.getWidth() / 2;
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        float dp = widthPixels / density;

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(btn, "translationX", btn.getTranslationX(), dp + btnWidth);
        objectAnimator1.setDuration(800);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(objectAnimator1);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (btn.getId() == R.id.button_check_out) {
                    ImplSwitchToFragment.switchToCheckoutFlashCard();
                } else if (btn.getId() == R.id.button_create) {
                    ImplSwitchToFragment.switchToCreateFlashCard(null);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }


}
