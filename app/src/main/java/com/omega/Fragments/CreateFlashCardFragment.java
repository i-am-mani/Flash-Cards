package com.omega.Fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.omega.Util.FlashCardViewModel;
import com.omega.R;
import com.omega.dialogs.SingleEditTextDialog;

public class CreateFlashCardFragment extends Fragment {

    private String GROUP_NAME = null;
    private FlashCardsAdaptor adaptor;

    private FloatingActionButton addFlashCard;
    private RecyclerView rvFlashCards;


    final String TAG = "CreateFlashCardFragment";
    private FlashCardViewModel flashCardViewModel;
    public CreateFlashCardFragment(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCard().observe(this, flashCards -> {
            adaptor.setDataset(flashCards);
            Log.d(TAG, "onActivityCreated: Dataset changed");
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_create_flash_card, container, false);
        getActivity().setTitle("Create New Flash Card");
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        initializeDialog();
        return viewGroup;
    }

    private void initializeDialog() {
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
                    flashCardViewModel.createGroup(group,desc);
                    GROUP_NAME = group;
                    dialog.dismiss();
                }
            });
        });

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
        });
    }

    private void initializeInstanceVariables(View viewGroup) {
        adaptor = new FlashCardsAdaptor(getActivity());
        rvFlashCards = viewGroup.findViewById(R.id.recycler_view_flash_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFlashCards.setLayoutManager(layoutManager);
        rvFlashCards.setAdapter(adaptor);

        rvFlashCards.addItemDecoration(new EqualSpaceItemDecoration(10));
    }

    public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
