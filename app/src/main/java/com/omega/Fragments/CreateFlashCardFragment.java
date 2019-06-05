package com.omega.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.omega.Adaptors.FlashCardsAdaptor;
import com.omega.Database.FlashCards;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.ISwitchToFragment;
import com.omega.Util.SwipeCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateFlashCardFragment extends Fragment {

    @BindView(R.id.button_play)
    Button btnPlay;

    @BindView(R.id.text_create_section_hint)
    TextView tvHint;

    private String GROUP_NAME;
    private final String TAG = CreateFlashCardFragment.class.getSimpleName();
    private FlashCardsAdaptor rvAdaptor;
    private FloatingActionButton btnAddFlashCard;
    private RecyclerView rvFlashCards;
    private FlashCardViewModel flashCardViewModel;
    private ISwitchToFragment ImplSwitchToFragment;
    private String newContent;

    public CreateFlashCardFragment(String groupName){
        GROUP_NAME  = groupName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        //If Group Name is passed in constructor, attach the observer.
        if(GROUP_NAME != null){
            attachObserver();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ImplSwitchToFragment = (ISwitchToFragment) context;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_create_flash_card, container, false);
        getActivity().setTitle("Create New Flash Card"); //set Fragment title
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        ButterKnife.bind(this, viewGroup);

        setHints();

        return viewGroup;
    }

    private void setHints() {

        if (GROUP_NAME != null) {
            tvHint.setText(R.string.hint_create_section_flashcard);
        } else {
            tvHint.setText(R.string.hint_create_section_group);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initializeCallbacks(View viewGroup) {
        Log.d(TAG, "initializeCallbacks: Creating new dialog");
        btnAddFlashCard = viewGroup.findViewById(R.id.fab_create_flash_card);
        btnAddFlashCard.setOnClickListener(v -> {
            if (GROUP_NAME == null) {
                initializeGroupDialog();
            } else {
                initializeFlashCardDialog();
            }
        });
    }

    private void initializeInstanceVariables(View viewGroup) {
        rvAdaptor = new FlashCardsAdaptor(getActivity());
        rvFlashCards = viewGroup.findViewById(R.id.recycler_view_flash_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFlashCards.setLayoutManager(layoutManager);
        rvFlashCards.setAdapter(rvAdaptor);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(new CreateFlashCardFragment.ImplOnSwipe()));
        itemTouchHelper.attachToRecyclerView(rvFlashCards);
        rvFlashCards.addItemDecoration(new EqualSpaceItemDecoration(40));
    }

    private void initializeFlashCardDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Create new Flash Card");
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dailog_create_flash_card);

        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setOnShowListener(dialog1 -> {
            Button btnCreateFlashCard = dialog.findViewById(R.id.button_create_flash_card);
            btnCreateFlashCard.setOnClickListener(v1 -> {
                String title = ((EditText) dialog.findViewById(R.id.edit_text_title)).getText().toString();
                String content = ((EditText) dialog.findViewById(R.id.edit_text_message)).getText().toString();
                Log.d(TAG, "initializeCallbacks: creating new flash card");
                flashCardViewModel.createFlashCard(title, content, GROUP_NAME);
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void initializeGroupDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_group);
        Button btnCreateGroup = dialog.findViewById(R.id.button_create_group);
        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    setHints();
                    attachObserver();
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private void attachObserver() {
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
            rvAdaptor.setDataSet(flashCards);
            if (flashCards.size() > 0)
                tvHint.setVisibility(View.GONE);
        });
        btnPlay.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_play)
    public void startPlayMode(View v) {
        ImplSwitchToFragment.switchToPlayMode(GROUP_NAME);
    }

    class ImplOnSwipe implements SwipeCallback.OnSwiped {

        private String newTitle;

        @Override
        public void deleteItem(int adapterPosition) {
            FlashCards itemTitle = rvAdaptor.getItemAtPosition(adapterPosition);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Confirmation Alert");
            alertDialog.setMessage("Are you sure you want to delete item ? ");


            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                flashCardViewModel.deleteFlashcard(itemTitle);
            });
            alertDialog.setNegativeButton("Undo", (dialog, which) -> {
                rvAdaptor.refresh(adapterPosition);
            });


            alertDialog.setOnCancelListener(dialog -> rvAdaptor.refresh(adapterPosition));

            AlertDialog dialog = alertDialog.create();
            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.show();
        }

        @Override
        public void editItem(int adapterPosition) {
            FlashCards flashCard = rvAdaptor.getItemAtPosition(adapterPosition);

            Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_edit);

            ((TextView) (dialog.findViewById(R.id.text_dialog_title))).setText("Edit FlashCard");
            TextInputEditText etContent = dialog.findViewById(R.id.edit_text_new_description);
            TextInputEditText etTitle = dialog.findViewById(R.id.edit_text_new_group_name);

            //set et text
            etTitle.setText(flashCard.getTitle());
            etContent.setText(flashCard.getContent());


            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button btnConfirmEdit = dialog.findViewById(R.id.button_confirm_edit);
            btnConfirmEdit.setOnClickListener(v -> {
                newContent = etContent.getText().toString();
                newTitle = etTitle.getText().toString();

                flashCard.setContent(newContent);
                flashCard.setTitle(newTitle);
                flashCardViewModel.updateFlashCard(flashCard);
                dialog.dismiss();
                rvAdaptor.refresh(adapterPosition);
            });
            dialog.setOnCancelListener(dialog1 -> rvAdaptor.refresh(adapterPosition));
            dialog.show();
        }
    }

}
