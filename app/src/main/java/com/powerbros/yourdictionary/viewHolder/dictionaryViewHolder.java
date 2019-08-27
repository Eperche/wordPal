package com.powerbros.yourdictionary.viewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.powerbros.yourdictionary.R;

public class dictionaryViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public TextView word;
    public TextView meaning;
    public TextView sentence;
    public TextView meaningSentence;
    public TextView  date;
    public ImageButton sentenceButton;
    public ImageButton displayButton;

 //   RelativeLayout parentLayout;

    public dictionaryViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        word    = (TextView) itemView.findViewById(R.id.wordField);
        meaning = (TextView) itemView.findViewById(R.id.meaningField);
        sentence = (TextView) itemView.findViewById(R.id.SentenceField);
        meaningSentence = (TextView) itemView.findViewById(R.id.meaningSentenceField);
        date    = (TextView) itemView.findViewById(R.id.getDate);

        sentenceButton = (ImageButton) itemView.findViewById(R.id.btnAddSentences);
        displayButton  = (ImageButton) itemView.findViewById(R.id.btnDisplaySentences);

       // date.setVisibility(View.INVISIBLE);
      //  parentLayout =itemView.findViewById(R.id.parent_layout);

    }
}