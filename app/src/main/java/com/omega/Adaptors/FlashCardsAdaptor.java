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

public class FlashCardsAdaptor extends RecyclerView.Adapter<FlashCardsAdaptor.FlashCardViewHolder> {


    private final Context context;
    private List<FlashCards> flashCardsList = null;

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

    public class FlashCardViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvContent;

        public FlashCardViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_title);
            tvContent = itemView.findViewById(R.id.text_content);
        }


    }


}
