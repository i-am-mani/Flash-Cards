package com.omega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.omega.Database.Groups;
import com.omega.Fragments.CheckoutFlashCardFragment;
import com.omega.Fragments.CreateFlashCardFragment;
import com.omega.Fragments.SplashScreen;
import com.omega.Util.ISwitchToFragment;

public class MainActivity extends AppCompatActivity implements ISwitchToFragment {



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
    public void switchToCreateFlashCard(String groupName) {
        CreateFlashCardFragment createFlashCard = new CreateFlashCardFragment(groupName);
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
