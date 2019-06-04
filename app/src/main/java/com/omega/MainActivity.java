package com.omega;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.omega.Fragments.CheckoutFlashCardFragment;
import com.omega.Fragments.CreateFlashCardFragment;
import com.omega.Fragments.PlayModeFragment;
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
    public void switchToPlayMode(String group) {
        PlayModeFragment playModeFragment = new PlayModeFragment(group);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, playModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToCheckoutFlashCard() {
        CheckoutFlashCardFragment checkoutFlashCard = new CheckoutFlashCardFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, checkoutFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
