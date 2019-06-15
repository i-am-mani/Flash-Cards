package com.omega.Fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreenFragment extends Fragment {

    ISwitchToFragment ImplSwitchToFragment;

    @BindView(R.id.button_check_out)
    Button btnCheckout;

    @BindView(R.id.button_create)
    Button btnCreate;
    public SplashScreenFragment() {
        // Required empty public constructor
    }

    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();

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
        getActivity().setTitle("Home");
        return mainView;
    }

    @OnClick({R.id.button_check_out, R.id.button_create})
    public void play(View view) {
        Button btn = (Button) view;
        ObjectAnimator rotateAnimator, translateAnimator = new ObjectAnimator();
        rotateAnimator = ObjectAnimator.ofFloat(btn, "rotation", 0f, 360f);
        translateAnimator = getTranslationAnimator(view, btn, translateAnimator);
        setUpAnimatorSet(btn, rotateAnimator, translateAnimator);
    }

    private void setUpAnimatorSet(Button btn, ObjectAnimator rotateAnimator, ObjectAnimator translateAnimator) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotateAnimator).with(translateAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(800);
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

    private ObjectAnimator getTranslationAnimator(View view, Button btn, ObjectAnimator translateAnimator) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        int btnWidth = btn.getWidth();
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        float dp = widthPixels / density;

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            translateAnimator = ObjectAnimator.ofFloat(btn, "translationX", btn.getTranslationX(), dp + btnWidth + 100);

        } else {
            if (view.getId() == R.id.button_check_out) {
                translateAnimator = ObjectAnimator.ofFloat(btn, "translationX", btn.getTranslationX(), -(dp + btnWidth + 100));
            } else if (view.getId() == R.id.button_create) {
                translateAnimator = ObjectAnimator.ofFloat(btn, "translationX", btn.getTranslationX(), dp + btnWidth + 100);
            }
        }
        return translateAnimator;
    }


}
