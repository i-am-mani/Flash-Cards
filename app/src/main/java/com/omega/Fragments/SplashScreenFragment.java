package com.omega.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.omega.R;
import com.omega.Util.ISwitchToFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreenFragment extends Fragment {

    ISwitchToFragment ImplSwitchToFragment;

    @BindView(R.id.button_check_out)
    CardView btnCheckout;

    @BindView(R.id.button_create)
    CardView btnCreate;

    @BindView(R.id.text_create_flashcards)
    TextView tvCreate;
    @BindView(R.id.text_checkout_flashcards)
    TextView tvCheckout;

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
        } catch (ClassCastException c) {
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
//        initAnimation();
        return mainView;
    }

    @OnClick({R.id.button_check_out, R.id.button_create})
    public void play(View view) {
        if (view.getId() == R.id.button_check_out) {

            view.animate().translationZBy(400).setDuration(300)
                    .rotation(0).setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ImplSwitchToFragment.switchToCheckoutFlashCard();
                        }
                    });

        } else if (view.getId() == R.id.button_create) {
            view.animate().translationZBy(400).setDuration(300)
                    .rotation(0).setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ImplSwitchToFragment.switchToCreateFlashCard(null);
                        }
                    });

        }
    }

}
