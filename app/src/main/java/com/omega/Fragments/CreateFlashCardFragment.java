package com.omega.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omega.Adaptors.FlashCardsAdaptor;
import com.omega.Database.Groups;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.R;

public class CreateFlashCardFragment extends Fragment {

    private String GROUP_NAME;
    private FlashCardsAdaptor adaptor;

    private FloatingActionButton addFlashCard;
    private RecyclerView rvFlashCards;


    final String TAG = "CreateFlashCardFragment";
    private FlashCardViewModel flashCardViewModel;
    public CreateFlashCardFragment(String groupName){
        GROUP_NAME  = groupName;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        if(GROUP_NAME != null){
            attachObserver();
        }
    }

    private void attachObserver() {
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this,flashCards -> adaptor.setDataset(flashCards));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_create_flash_card, container, false);
        getActivity().setTitle("Create New Flash Card");
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        return viewGroup;
    }

    private void initializeGroupDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_group);
        Button btnCreateGroup = dialog.findViewById(R.id.button_create_group);

        dialog.setOnShowListener(dialog1 -> {
            Log.d(TAG, "initializeDialog: onShow fired");
            btnCreateGroup.setOnClickListener(v -> {
                Log.d(TAG, "initializeDialog: Group creation button clicked");
                String desc = ((EditText) dialog.findViewById(R.id.edit_text_description)).getText().toString();
                String group = ((EditText) dialog.findViewById(R.id.edit_text_group_name)).getText().toString();
                if (!desc.equals("") && !group.equals("")) {
                    Log.d(TAG, "initializeDialog: creating new group");
                    GROUP_NAME = group;
                    flashCardViewModel.createGroup(group,desc);
                    attachObserver();
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    public void initializeFlashCardDialog(){
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Create new Flash Card");
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dailog_create_flash_card);

        dialog.setOnShowListener(dialog1 -> {
            Button btnCreateFlashCard = dialog.findViewById(R.id.button_create_flash_card);
            btnCreateFlashCard.setOnClickListener(v1 -> {
                String title =((EditText) dialog.findViewById(R.id.edit_text_title)).getText().toString();
                String content = ((EditText) dialog.findViewById(R.id.edit_text_message)).getText().toString();
                Log.d(TAG, "initializeCallbacks: creating new flash card");
                flashCardViewModel.createFlashCard(title,content,GROUP_NAME);
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void initializeCallbacks(View viewGroup) {
        Log.d(TAG, "initializeCallbacks: Creating new dialog");
        addFlashCard = viewGroup.findViewById(R.id.fab_create_flash_card);
        addFlashCard.setOnClickListener(v -> {
            if (GROUP_NAME == null) {
                initializeGroupDialog();
            }
            else{
                initializeFlashCardDialog();
            }
        });
    }

    private void initializeInstanceVariables(View viewGroup) {
        adaptor = new FlashCardsAdaptor(getActivity());
        rvFlashCards = viewGroup.findViewById(R.id.recycler_view_flash_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFlashCards.setLayoutManager(layoutManager);
        rvFlashCards.setAdapter(adaptor);

        rvFlashCards.addItemDecoration(new EqualSpaceItemDecoration(40));
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
