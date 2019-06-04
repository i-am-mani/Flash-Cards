package com.omega.Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omega.Database.FlashCards;
import com.omega.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlashCardsAdaptor extends RecyclerView.Adapter<FlashCardsAdaptor.FlashCardViewHolder> {


    private final Context context;
    private List<FlashCards> flashCardsList = null;
    private FlashCardAdaptorListener adaptorListener;

    public FlashCardsAdaptor(Context context){
            this.context = context;
    }


    @Override
    public FlashCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mainView = layoutInflater.inflate(R.layout.item_flash_card,parent,false);
        FlashCardViewHolder flashCardViewHolder = new FlashCardViewHolder(mainView);
        return flashCardViewHolder;
    }

    @Override
    public void onBindViewHolder(FlashCardViewHolder holder, int position) {

        if (flashCardsList != null) {
            holder.tvContent.setText(flashCardsList.get(position).getContent());
            holder.tvTitle.setText(flashCardsList.get(position).getTitle());
            Log.d("Adaptor", "onBindViewHolder: " + flashCardsList.size());
        }
        else {
            holder.tvTitle.setText("No More Flash cards");
        }
    }

    @Override
    public int getItemCount() {
        if (flashCardsList != null) {
            return flashCardsList.size();
        }
        else{
            return 0;
        }
    }


    public void setDataSet(List<FlashCards> dataset) {

        flashCardsList = dataset;
        notifyDataSetChanged();
    }

    public interface FlashCardAdaptorListener {
        void startPlayMode(String group);
    }

    public class FlashCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView tvTitle;
        @BindView(R.id.text_content)
        TextView tvContent;


        public FlashCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

//        @OnClick(R.id.button_play)
//        public void play(View view) {
//            adaptorListener.startPlayMode();
//        }
    }


}
