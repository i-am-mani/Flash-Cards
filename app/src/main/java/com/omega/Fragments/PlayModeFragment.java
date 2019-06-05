package com.omega.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.omega.Adaptors.PlayModeAdaptor;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.ISwitchToFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayModeFragment extends Fragment {

    private final String groupName;
    @BindView(R.id.recycler_view_play_mode)
    RecyclerView rvPlayCard;
    private PlayModeAdaptor playModeAdaptor;
    private FlashCardViewModel flashCardViewModel;
    private ISwitchToFragment ImplSwitchToFragment;

    @BindView(R.id.text_play_mode_hint)
    TextView tvHint;


    public PlayModeFragment(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllFlashCardsOfGroup(groupName).observe(this, flashCards -> {
            playModeAdaptor.setDataSet(flashCards);
            if (flashCards.size() > 0) {
                tvHint.setVisibility(View.GONE);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_play_mode, container, false);
        ButterKnife.bind(this, mainView);
        initializeRecyclerView();
        tvHint.setText(R.string.hint_create_section_flashcard);
        getActivity().setTitle("Play mode");
        return mainView;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        playModeAdaptor = new PlayModeAdaptor(getActivity());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvPlayCard);

        EqualSpaceItemDecoration decoration = new EqualSpaceItemDecoration(40);
        rvPlayCard.addItemDecoration(decoration);

        rvPlayCard.setLayoutManager(linearLayoutManager);
        rvPlayCard.setAdapter(playModeAdaptor);
    }


}
