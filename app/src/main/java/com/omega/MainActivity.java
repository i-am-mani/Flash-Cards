package com.omega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SplashScreen.SplashScreenCallbacks {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SplashScreen splashScreen = SplashScreen.newInstance();
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_main_area, splashScreen);
        transaction.commit();
    }


    @Override
    public void switchToCreateFlashCard() {
        CreateFlashCardFragment createFlashCard = new CreateFlashCardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area,createFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToOptions() {

    }

    @Override
    public void switchToCheckoutFlashCard() {
        CheckoutFlashCardFragment checkoutFlashCard = new CheckoutFlashCardFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, checkoutFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
