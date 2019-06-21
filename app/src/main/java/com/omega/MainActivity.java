package com.omega;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.omega.Fragments.CasualPlayModeFragment;
import com.omega.Fragments.CheckoutFlashCardFragment;
import com.omega.Fragments.CreateFlashCardFragment;
import com.omega.Fragments.ReverseMatchPlayModeFragment;
import com.omega.Fragments.SplashScreenFragment;
import com.omega.Fragments.SplashScreenPlayModeFragment;
import com.omega.Fragments.TrueFalsePlayModeFragment;
import com.omega.Util.ISwitchToFragment;

public class MainActivity extends AppCompatActivity implements ISwitchToFragment {


    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            SplashScreenFragment splashScreenFragment = SplashScreenFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_main_area, splashScreenFragment);
            transaction.commit();
        } else {
            Log.d("MAIN", "onCreate: " + savedInstanceState.toString());
        }
    }


    @Override
    public void switchToCreateFlashCard(String groupName) {
        CreateFlashCardFragment createFlashCard = new CreateFlashCardFragment(groupName);
//        addExplodeTransactionToFragment(createFlashCard);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area,createFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToPlayMode(String group) {
        SplashScreenPlayModeFragment splashScreenPlayModeFragment = new SplashScreenPlayModeFragment(group);
        addExplodeTransactionToFragment(splashScreenPlayModeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, splashScreenPlayModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToCheckoutFlashCard() {
        CheckoutFlashCardFragment checkoutFlashCard = new CheckoutFlashCardFragment();
        checkoutFlashCard.setEnterTransition(new Slide(Gravity.RIGHT));
        checkoutFlashCard.setExitTransition(new Explode());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, checkoutFlashCard);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchToPlayModeCasual(String group) {
        CasualPlayModeFragment casualPlayModeFragment = new CasualPlayModeFragment(group);
        addExplodeTransactionToFragment(casualPlayModeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, casualPlayModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void switchToPlayModeReverseMatch(String group) {
        ReverseMatchPlayModeFragment reverseMatchPlayModeFragment = new ReverseMatchPlayModeFragment(group);

        addExplodeTransactionToFragment(reverseMatchPlayModeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, reverseMatchPlayModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void switchToPlayModeTrueFalse(String group) {
        Log.d(TAG, "switchToPlayModeTrueFalse:");
        TrueFalsePlayModeFragment trueFalsePlayModeFragment = new TrueFalsePlayModeFragment(group);
        addExplodeTransactionToFragment(trueFalsePlayModeFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_area, trueFalsePlayModeFragment);
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

    //On outside touch, Remove focus from Edit text.
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
