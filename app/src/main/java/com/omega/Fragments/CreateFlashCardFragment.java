package com.omega.Fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
import com.omega.Util.FlashCardViewModel;
import com.omega.R;

public class CreateFlashCardFragment extends Fragment {

    private MutableLiveData<Groups> GROUP_NAME = new MutableLiveData(null);
    private FlashCardsAdaptor adaptor;

    private FloatingActionButton addFlashCard;
    private RecyclerView rvFlashCards;


    final String TAG = "CreateFlashCardFragment";
    private FlashCardViewModel flashCardViewModel;
    public CreateFlashCardFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_create_flash_card, container, false);
        getActivity().setTitle("Create New Flash Card");
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        GROUP_NAME.observe(this,s -> {
            if (s != null) {
                Log.d(TAG, "onCreateView: inside group ovserver");
                flashCardViewModel.createGroup(s.getGroupName(),s.getGroupDescription());

                flashCardViewModel.getAllFlashCardsOfGroup(s.getGroupName()).observe(this, flashCards -> {
                    adaptor.setDataset(flashCards);
                    Log.d(TAG, "onActivityCreated: Dataset changed");
                });
            }

        });
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
                    GROUP_NAME.setValue(new Groups(group, desc));
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
                flashCardViewModel.createFlashCard(title,content,GROUP_NAME.getValue().getGroupName());
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void initializeCallbacks(View viewGroup) {
        Log.d(TAG, "initializeCallbacks: Creating new dialog");
        addFlashCard = viewGroup.findViewById(R.id.fab_create_flash_card);
        addFlashCard.setOnClickListener(v -> {
            if (GROUP_NAME.getValue() == null) {
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
