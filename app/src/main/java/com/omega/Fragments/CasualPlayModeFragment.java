package com.omega.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.omega.Adaptors.CasualModePlayAdaptor;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CasualPlayModeFragment extends Fragment {

    private static String GROUP_NAME;  // Since it's static it would persists across Orientation Change

    @BindView(R.id.recycler_view_casual_mode_flash_cards)
    RecyclerView rvCasualPlay;
    private final String AdaptorPositionKey = "AdapterPositionKey";
    @BindView(R.id.edit_text_flashcard_number)
    EditText etFlashCardNumber;

    private CasualModePlayAdaptor casualModePlayAdaptor;
    private FlashCardViewModel flashCardViewModel;
    LinearLayoutManager linearLayoutManager;
    private int adaptorItemPosition = 0; // holds position of adaptor item currently visible

    public CasualPlayModeFragment() {
        //No-arg constructor
    }

    public CasualPlayModeFragment(String group) {
        GROUP_NAME = group;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(GROUP_NAME).observe(this, flashCards -> {
            if (savedInstanceState != null) {
                useSavedInstanceState(savedInstanceState);
            } else {
                if (flashCards.size() > 0) {
                    casualModePlayAdaptor.setDataSet(flashCards);
                }
            }
        });
    }

    private void useSavedInstanceState(Bundle savedInstanceState) {
        adaptorItemPosition = savedInstanceState.getInt(AdaptorPositionKey, adaptorItemPosition);
        scrollRecyclerView(adaptorItemPosition);
    }

    private void scrollRecyclerView(int itemPosition) {
        rvCasualPlay.scrollToPosition(itemPosition);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_play_mode_casual, container, false);
        ButterKnife.bind(this, mainView);
        initializeVariables();
        getActivity().setTitle("Casual Mode");
        setUpEditText();
        return mainView;
    }

    private void setUpEditText() {
        Utility.onReturnKeyEditText(etFlashCardNumber, getActivity());
        etFlashCardNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Utility.hideKeyboard(etFlashCardNumber, getActivity());
                    etFlashCardNumber.setFocusable(false);
                    etFlashCardNumber.setFocusableInTouchMode(true);

                    int pos = Integer.valueOf(etFlashCardNumber.getText().toString());
                    rvCasualPlay.scrollToPosition(getAdaptorItemPosition(pos));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AdaptorPositionKey, adaptorItemPosition);
    }

    private void initializeVariables() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d("TAG", "initRecyclerView: " + rvCasualPlay);
        casualModePlayAdaptor = new CasualModePlayAdaptor(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        rvCasualPlay.setAdapter(casualModePlayAdaptor);
        rvCasualPlay.setLayoutManager(linearLayoutManager);

        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
        rvCasualPlay.addItemDecoration(decoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvCasualPlay);

        rvCasualPlay.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (visibleItemPosition != -1) {
                    adaptorItemPosition = getDisplayPosition(visibleItemPosition);
                    etFlashCardNumber.setText(String.valueOf(adaptorItemPosition));
                }
            }
        });
    }

    @OnClick(R.id.button_shuffle)
    public void shuffleCards(View view) {
        casualModePlayAdaptor.shuffle();
        scrollRecyclerView(getAdaptorItemPosition(0));
    }

    private int getDisplayPosition(int pos) {
        return pos + 1;
    }

    private int getAdaptorItemPosition(int pos) {
        return pos - 1;
    }
}

