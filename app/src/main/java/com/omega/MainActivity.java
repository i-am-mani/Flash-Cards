package com.omega;

import android.app.Dialog;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
        if (savedInstanceState == null) {
            SplashScreen splashScreen = SplashScreen.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_main_area, splashScreen);
            transaction.commit();
        } else {
            Log.d("MAIN", "onCreate: " + savedInstanceState.toString());
        }

    }


    @Override
    public void switchToCreateFlashCard(String groupName) {
        CreateFlashCardFragment createFlashCard = new CreateFlashCardFragment(groupName);
        addExplodeTransactionToFragment(createFlashCard);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area,createFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToPlayMode(String group) {
        PlayModeFragment playModeFragment = new PlayModeFragment(group);
        addExplodeTransactionToFragment(playModeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, playModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToCheckoutFlashCard() {
        CheckoutFlashCardFragment checkoutFlashCard = new CheckoutFlashCardFragment();
        addExplodeTransactionToFragment(checkoutFlashCard);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_help) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_help);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addExplodeTransactionToFragment(Fragment fragment) {
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
    }

}
