package com.example.englishdic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;


public class ExampleDialog extends AppCompatDialogFragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText editTextWord;
    private EditText editTextMeaning;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {




        mAuth = FirebaseAuth.getInstance();

      //  makePermanet();
        mCurrentUser =mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");

        mDatabase.keepSynced(true);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.open_dialog,null);

        builder.setView(view)
                .setTitle("Add new word")
                .setNeutralButton("good", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
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
                });

        editTextWord = view.findViewById(R.id.edit_word);
        editTextMeaning = view.findViewById(R.id.edit_meaning);

        return builder.create();
    }


    private void addDictionary() {
         String word = editTextWord.getText().toString().trim();
         String meaning = editTextMeaning.getText().toString().trim();

         if(!TextUtils.isEmpty(word) && !TextUtils.isEmpty(meaning)){
             final DatabaseReference newPost = mDatabase.push();
             addNewSample(newPost,word,meaning);
         }
    }

    private void addNewSample(DatabaseReference newPost,String word, String meaning) {
            Dictionary dictionary = new Dictionary(word,meaning);
            newPost.setValue(dictionary);
            Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);
            //newPost.child("data").updateChildren(map);
            newPost.child("date").setValue(ServerValue.TIMESTAMP);
            mDatabase.keepSynced(true);
            newPost.keepSynced(true);

    }


}
