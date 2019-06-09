package com.omega.Util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {

    public static void hideKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
