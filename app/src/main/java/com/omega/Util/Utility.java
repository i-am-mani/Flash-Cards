package com.omega.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.omega.R;

public class Utility {

    public static void hideKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void onReturnKeyEditText(View view, Activity activity) {
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Utility.hideKeyboard(view, activity);
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return true;
            }
            return false;
        });
    }

    public static Dialog getDialog(Activity activity, int resID) {
        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryColor);
        dialog.setContentView(resID);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return dialog;
    }
}
