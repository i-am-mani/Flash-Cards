package com.omega.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
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

    public CheckoutFlashCardFragment(){

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
        flashCardViewModel.getAllGroups().observe(this, groups -> groupsAdaptor.setDataSet(groups));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(new OnSwipeDeleteItem(groupsAdaptor, flashCardViewModel, getActivity())));
        itemTouchHelper.attachToRecyclerView(rvGroups);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_check_out_flash_cards, container, false);
        ButterKnife.bind(this, viewGroup);
        setActionBarTitle();
        initialize();
        et_search.setHint("Search");
        return viewGroup;
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
        et_search.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "initialize: " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Utility.hideKeyboard(et_search, getActivity());
                et_search.setFocusable(false);
                et_search.setFocusableInTouchMode(true);
                return true;
            }
            return false;
        });
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