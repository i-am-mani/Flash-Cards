package com.omega.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.omega.Adaptors.GroupsAdaptor;
import com.omega.Database.FlashCards;
import com.omega.Database.Groups;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.ISwitchToFragment;
import com.omega.Util.SwipeCallback;
import com.omega.Util.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;


public class CheckoutFlashCardFragment extends Fragment {

    FlashCardViewModel flashCardViewModel;
    GroupsAdaptor groupsAdaptor;
    ISwitchToFragment switchToFragment;

    String TAG = CheckoutFlashCardFragment.class.getSimpleName();

    @BindView(R.id.edit_text_search)
    TextInputEditText et_search;

    @BindView(R.id.recycler_view_group)
    RecyclerView rvGroups;

    @BindView(R.id.text_checkout_flashcards_hint)
    TextView tvHint;

    @BindView(R.id.divider_checkout_flashcards)
    View divider;

    @BindView(R.id.text_input_layout_checkout)
    TextInputLayout textInputLayout;

    OnSwipeDeleteItem onSwipeDeleteItem;

    public CheckoutFlashCardFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            switchToFragment = (ISwitchToFragment) context;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllGroups().observe(this, groups -> {
            if (groups.size() > 0) {
                groupsAdaptor.setDataSet(groups);
                tvHint.setVisibility(View.GONE);
            } else {
                actionOnNoGroup();
            }
        });
        ItemTouchHelper itemTouchHelper;
        onSwipeDeleteItem = new OnSwipeDeleteItem(groupsAdaptor, flashCardViewModel, getActivity());
        itemTouchHelper = new ItemTouchHelper(new SwipeCallback(onSwipeDeleteItem));
        itemTouchHelper.attachToRecyclerView(rvGroups);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_check_out_flash_cards, container, false);
        ButterKnife.bind(this, viewGroup);
        setActionBarTitle();
        initialize();
        setHints();
        return viewGroup;
    }

    private void setHints() {
        et_search.setHint("Search");
    }

    private void actionOnNoGroup() {
        tvHint.setVisibility(View.VISIBLE);
        tvHint.setText("No Groups Found");
        et_search.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.INVISIBLE);
//        textInputLayout.setVisibility(View.INVISIBLE);
    }

    private void initialize() {
        setUpRecyclerView();
        setEditTextListeners();
    }

    private void setUpRecyclerView() {
        groupsAdaptor = new GroupsAdaptor(getActivity(), new GroupsItemImpl(switchToFragment));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvGroups.setLayoutManager(linearLayoutManager);
        rvGroups.setAdapter(groupsAdaptor);
        rvGroups.addItemDecoration(new EqualSpaceItemDecoration(40));
    }

    @OnTextChanged(R.id.edit_text_search)
    public void adapterSearchKeyword(CharSequence text) {
        Log.d(TAG, "adapterSearchKeyword: " + text);
        groupsAdaptor.filter(text.toString());
    }

    private void setActionBarTitle() {
        getActivity().setTitle("Checkout Groups");
    }

    private void setEditTextListeners() {
        Utility.onReturnKeyEditText(et_search, getActivity());
    }

    /***
     * Implementation of GroupsAdaptorListenerInterface, used by Adaptor to provided callbacks
     */
    class GroupsItemImpl implements GroupsAdaptor.GroupsAdaptorListenerInterface {

        private ISwitchToFragment switchToFragment;


        GroupsItemImpl(ISwitchToFragment implSwitchToFragment) {
            switchToFragment = implSwitchToFragment;
        }

        @Override
        public void onItemClick(View view, String groupName) {
            switchToFragment.switchToPlayMode(groupName);
        }

        @Override
        public void onPlayButtonClicked(View view, String groupName) {
            switchToFragment.switchToCreateFlashCard(groupName);
        }

        @Override
        public void getNumberOfFlashCards(String groupName, TextView textView) {
            Log.d(TAG, "getNumberOfFlashCards: textView" + textView);
            LiveData<List<FlashCards>> allFlashCardsOfGroup = flashCardViewModel.getAllFlashCardsOfGroup(groupName);
            allFlashCardsOfGroup.observe(CheckoutFlashCardFragment.this, flashCards -> {
                if (textView != null) {

                    textView.setText(String.valueOf(flashCards.size()));
                }

            });
        }

        @Override
        public void deleteGroup(int pos) {
            onSwipeDeleteItem.deleteItem(pos);
        }

        @Override
        public void editGroup(int pos) {
            onSwipeDeleteItem.editItem(pos);
        }

        @Override
        public void exportFlashCardsOfGroup(int pos) {
            LiveData<List<FlashCards>> cards = flashCardViewModel.getAllFlashCardsOfGroup(groupsAdaptor.getItemAtPosition(pos).getGroupName());
            cards.observe(CheckoutFlashCardFragment.this, flashCards -> {

                Dialog dialog = Utility.getDialog(getActivity(), R.layout.dialog_export_flashcards);


                TextInputEditText etTitleSeparator = dialog.findViewById(R.id.edit_text_title_separator);
                TextInputEditText etFlashCardsSeparator = dialog.findViewById(R.id.edit_text_flashcards_separator);
                TextView tvContent = dialog.findViewById(R.id.text_export_content);
                Button btnCopy = dialog.findViewById(R.id.button_export_copy);
                Button btnGenerate = dialog.findViewById(R.id.button_export_generate);

                btnCopy.setOnClickListener(v -> copyToSystemClipboard(tvContent.getText().toString()));
                btnGenerate.setOnClickListener(v -> {
                    String titleSep = etTitleSeparator.getEditableText().toString();
                    String cardSep = etFlashCardsSeparator.getText().toString();
                    Log.d(TAG, "exportFlashCardsOfGroup: titleSep = " + titleSep + " cardSep = " + cardSep);
                    if (etTitleSeparator.length() == 0 || etFlashCardsSeparator.length() == 0) {
                        Toast.makeText(getActivity(), "Enter Proper Separator", Toast.LENGTH_SHORT);
                    }
                    if (titleSep.length() > 0 && cardSep.length() > 0) {
                        String content = getFlashCardExportableContent(flashCards, titleSep, cardSep);
                        tvContent.setText(content);
                    }
                });
                Utility.onReturnKeyEditText(etFlashCardsSeparator, getActivity());
                Utility.onReturnKeyEditText(etTitleSeparator, getActivity());

                String content = getFlashCardExportableContent(flashCards, "-", "\n\n");
                tvContent.setText(content);
                dialog.show();

            });

        }

        private void copyToSystemClipboard(String content) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FlashCart content", content);
            clipboard.setPrimaryClip(clip);
        }

        public String getFlashCardExportableContent(List<FlashCards> flashCards, String titleSeparator, String flashCardSeparator) {
            StringBuilder builder = new StringBuilder();
            titleSeparator = titleSeparator.replaceAll("\\\\n", "\n");
            flashCardSeparator = flashCardSeparator.replaceAll("\\\\n", "\n");
            Log.d(TAG, "getFlashCardExportableContent: " + titleSeparator + "  " + flashCardSeparator);
            for (FlashCards flashCard : flashCards) {
                String title = flashCard.getTitle();
                String solution = flashCard.getContent();
                builder.append(title);
                builder.append(titleSeparator);
                builder.append(solution);
                builder.append(flashCardSeparator);
            }
            return builder.toString();
        }
    }

}


/**
 * Implantation for OnSwiped, Provides callbacks for swipe.
 */
class OnSwipeDeleteItem implements SwipeCallback.OnSwiped {

    private GroupsAdaptor groupsAdaptor;
    private FlashCardViewModel flashCardViewModel;
    private Activity activity;
    private String newDesc;
    private String newName;

    public OnSwipeDeleteItem(GroupsAdaptor adaptor, FlashCardViewModel viewModel, Activity activity) {
        groupsAdaptor = adaptor;
        flashCardViewModel = viewModel;
        this.activity = activity;
    }

    @Override
    public void deleteItem(int adapterPosition) {

        Groups group = groupsAdaptor.getItemAtPosition(adapterPosition);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Confirmation Alert");
        alertDialog.setMessage("Are you sure you want to delete item ? ");

        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            flashCardViewModel.deleteGroup(group);
            groupsAdaptor.removeItemFromDataSet(adapterPosition);
        });
        alertDialog.setNegativeButton("Undo", (dialog, which) -> {
            groupsAdaptor.refresh(adapterPosition);
        });

        alertDialog.setOnCancelListener(dialog -> groupsAdaptor.refresh(adapterPosition));

        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
        dialog.show();
    }

    @Override
    public void editItem(int adapterPosition) {
        Groups group = groupsAdaptor.getItemAtPosition(adapterPosition);

        Dialog dialog = getDialog();

        // fill the existing info
        TextInputEditText etDesc = dialog.findViewById(R.id.edit_text_new_description);
        TextInputEditText etName = dialog.findViewById(R.id.edit_text_new_group_name);
        autoFillDialogFields(group, etDesc, etName);

        // set dialog's window properties
        setDialogWindow(dialog);

        // set button click actions
        Button btnConfirmEdit = dialog.findViewById(R.id.button_confirm_edit);
        setUpButtonClickListeners(adapterPosition, group, dialog, etDesc, etName, btnConfirmEdit);
        dialog.show();
    }

    private void autoFillDialogFields(Groups group, TextInputEditText etDesc, TextInputEditText etName) {
        etName.setText(group.getGroupName());
        etDesc.setText(group.getGroupDescription());
    }

    private void setDialogWindow(Dialog dialog) {
        dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryColor);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setUpButtonClickListeners(int adapterPosition, Groups group, Dialog dialog, TextInputEditText etDesc, TextInputEditText etName, Button btnConfirmEdit) {
        btnConfirmEdit.setOnClickListener(v -> {
            newDesc = etDesc.getText().toString();
            newName = etName.getText().toString();

            group.setGroupDescription(newDesc);
            group.setGroupName(newName);

            flashCardViewModel.updateGroup(group);
            dialog.dismiss();
            groupsAdaptor.refresh(adapterPosition);
        });

        dialog.setOnCancelListener(dialog1 -> groupsAdaptor.refresh(adapterPosition));
    }

    private Dialog getDialog() {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_edit);

        ((TextView) (dialog.findViewById(R.id.text_dialog_title))).setText("Edit Group");
        return dialog;
    }

}