package com.omega.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.omega.Util.Utility;

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

    public CreateFlashCardFragment() {
        //no-argument constructor
    }

    public CreateFlashCardFragment(String groupName){
        GROUP_NAME  = groupName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_item_import) {
            Dialog dialog = Utility.getDialog(getActivity());

            dialog.setContentView(R.layout.dialog_import_flashcards);
            EditText etTitleSep = dialog.findViewById(R.id.edit_text_import_title_separator);
            EditText etCardsSep = dialog.findViewById(R.id.edit_text_import_flashcards_separator);
            EditText etContent = dialog.findViewById(R.id.edit_text_import_content);
            Button btnImport = dialog.findViewById(R.id.button_import);

            btnImport.setOnClickListener(v -> {
                String titleSep = etTitleSep.getText().toString();
                String cardsSep = etCardsSep.getText().toString();
                String content = etContent.getText().toString();

                if (titleSep.length() == 0 || content.length() == 0) {
                    Toast.makeText(getActivity(), "Empty separator details", Toast.LENGTH_SHORT).show();
                } else if (content.length() == 0) {
                    Toast.makeText(getActivity(), "Empty Content", Toast.LENGTH_SHORT).show();
                } else {
                    createFlashCardsFromContent(content, titleSep, cardsSep);
                }
            });

            dialog.show();
            return true;
        }
        return false;
    }


    private void createFlashCardsFromContent(String content, String titleSep, String cardSep) {
        String[] split = content.split(cardSep);
        int nAddedFlashcards = 0;

        for (String s : split) {
            String[] titleSolution = s.split(titleSep);

            if (titleSolution.length == 2) {
                String title = titleSolution[0];
                String solution = titleSolution[1];

                flashCardViewModel.createFlashCard(title, solution, GROUP_NAME);
                nAddedFlashcards++;
            }
        }
        Toast.makeText(getActivity(), nAddedFlashcards + " Flashcard Created", Toast.LENGTH_SHORT).show();
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
        setActionBarTitle();
        initializeInstanceVariables(viewGroup);
        initializeCallbacks(viewGroup);
        ButterKnife.bind(this, viewGroup);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: GROUP_NAME + " + savedInstanceState.getString("GROUP_NAME"));
            GROUP_NAME = savedInstanceState.getString("GROUP_NAME");
        }

        setHints();

        return viewGroup;
    }

    private void setActionBarTitle() {
        if (GROUP_NAME == null) {
            getActivity().setTitle("Create Group");
        } else {
            getActivity().setTitle("Create FlashCards");
        }
    }

    private void setHints() {
        if (GROUP_NAME != null) {
            tvHint.setText(R.string.hint_create_section_flashcard);
        } else {
            tvHint.setText(R.string.hint_create_section_group);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("GROUP_NAME", GROUP_NAME);
        Log.d(TAG, "onSaveInstanceState: GROUP_NAME = " + GROUP_NAME);
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
        Dialog dialog = Utility.getDialog(getActivity());
        dialog.setContentView(R.layout.dailog_create_flash_card);
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
        Dialog dialog = Utility.getDialog(getActivity());

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
                    setHints();
                    setActionBarTitle();
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
            if (flashCards.size() > 0) {
                tvHint.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });

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

            Dialog dialog = Utility.getDialog(getActivity());

            dialog.setContentView(R.layout.dialog_edit);
            ((TextView) (dialog.findViewById(R.id.text_dialog_title))).setText("Edit FlashCard");
            TextInputEditText etContent = dialog.findViewById(R.id.edit_text_new_description);
            TextInputEditText etTitle = dialog.findViewById(R.id.edit_text_new_group_name);

            //set et text
            etTitle.setText(flashCard.getTitle());
            etContent.setText(flashCard.getContent());

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
