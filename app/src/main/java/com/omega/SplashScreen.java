package com.omega;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SplashScreen extends Fragment {

    SplashScreenCallbacks mSplashScreenCallbacks;

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
            mSplashScreenCallbacks = (SplashScreenCallbacks) context;
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

        initializeCallbacks(mainView);

        return mainView;
    }

    private void initializeCallbacks(View viewGroup) {
        Button createFlashCard = viewGroup.findViewById(R.id.button_create);
        Button checkoutFlashCard = viewGroup.findViewById(R.id.button_check_out);
        Button options = viewGroup.findViewById(R.id.button_options);

        createFlashCard.setOnClickListener(v ->  mSplashScreenCallbacks.switchToCreateFlashCard() );
        checkoutFlashCard.setOnClickListener(v -> mSplashScreenCallbacks.switchToCheckoutFlashCard());
        options.setOnClickListener(v -> mSplashScreenCallbacks.switchToOptions());
    }

    public interface SplashScreenCallbacks {
        void switchToCreateFlashCard();
        void switchToOptions();
        void switchToCheckoutFlashCard();

    }

}
