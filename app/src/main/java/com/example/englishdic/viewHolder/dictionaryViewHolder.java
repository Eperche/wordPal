package com.example.englishdic.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.englishdic.R;

import org.w3c.dom.Text;

public class dictionaryViewHolder extends RecyclerView.ViewHolder {

   public TextView word;
    public TextView meaning;
    public TextView  date;
    public ImageButton sentenceButton;

 //   RelativeLayout parentLayout;

    public dictionaryViewHolder(@NonNull View itemView) {
        super(itemView);

        word    = (TextView) itemView.findViewById(R.id.wordField);
        meaning = (TextView) itemView.findViewById(R.id.meaningField);
        date    = (TextView) itemView.findViewById(R.id.getDate);
        sentenceButton = (ImageButton) itemView.findViewById(R.id.btnAddSentences);
        date.setVisibility(View.INVISIBLE);
      //  parentLayout =itemView.findViewById(R.id.parent_layout);

    }
}