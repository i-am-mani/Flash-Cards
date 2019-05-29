package com.omega;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateFlashCardFragment extends Fragment {
    public FloatingActionButton addFlashCard;
    final String TAG = "CreateFlashCardFragment";

    Dialog dialog;
    public CreateFlashCardFragment(){

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_create_flash_card, container, false);
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        initializeDialog();
        return viewGroup;
    }

    private void initializeDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_group);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void initializeCallbacks(View viewGroup) {
        Log.d(TAG, "initializeCallbacks: Creating new dialog");
        addFlashCard = viewGroup.findViewById(R.id.fab_create_flash_card);
        addFlashCard.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getActivity());
            dialog.setTitle("Create new Flash Card");
            dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dailog_create_flash_card);
            dialog.show();
        });
    }

    private void initializeInstanceVariables(View viewGroup) {

    }

    @Override
    public void onPause() {
        super.onPause();
        dialog.dismiss();
    }
}
