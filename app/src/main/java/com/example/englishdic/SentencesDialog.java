package com.example.englishdic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class SentencesDialog extends AppCompatDialogFragment {

    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mWord;
    private FirebaseAuth mAuth;

    private EditText editTextWord;
    private EditText editTextMeaning;
    private String post_key;
    private String word;
    private TextView underlined;

    private  ExampleDialogListener listener;

    public void setWord(String word) {
        this.word = word;

    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }




    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser =mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).child("sentences");
     //   mWord     = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child(Dictionary).child()

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.open_dialog_sentences,null);


        builder.setCancelable(false);

        builder.setView(view)
                .setTitle("Create a sentence about "+ word)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDictionary();

                    }
                })
                .setNeutralButton("See Sentences", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts(post_key,word);
                    }
                });

        editTextWord = view.findViewById(R.id.edit_sentence);
        editTextMeaning = view.findViewById(R.id.edit_meaningSentence);


        return builder.create();
    }

    private void addDictionary() {
        String sentence = editTextWord.getText().toString().trim();
        String SentenceMeaning = editTextMeaning.getText().toString().trim();

        if(!TextUtils.isEmpty(sentence) && !TextUtils.isEmpty(SentenceMeaning)){
            final DatabaseReference newPost = mDatabase.push();
            addNewSample(newPost,sentence,SentenceMeaning);
        }
    }

    private void addNewSample(DatabaseReference newPost, String sentence, String SentenceMeaning) {
        Dictionary dictionary = new Dictionary();
        dictionary.DictionarySet(sentence,SentenceMeaning);
        newPost.setValue(dictionary);
        Map map = new HashMap();
        map.put("timestamp", ServerValue.TIMESTAMP);

        newPost.child("date").setValue(ServerValue.TIMESTAMP);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener =(ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement ExampleDialogListener");
        }
    }


    public interface ExampleDialogListener{
        void applyTexts(String post_key, String word);
    }
}
