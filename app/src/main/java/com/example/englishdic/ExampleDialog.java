package com.example.englishdic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.Locale;


public class ExampleDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText editTextWord;
    private EditText editTextMeaning;
    private ImageButton imageButtonEdit;
    private ImageButton imageButtonMeaning;

    Spinner spinner;
    Spinner spinner1;


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
                .setTitle("Add")
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

        spinner =  view.findViewById(R.id.spinner);
        spinner1 =  view.findViewById(R.id.spinner1);
         ArrayAdapter arrayAdapter =  ArrayAdapter.createFromResource(getActivity(),R.array.languages,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(this);
        editTextWord = view.findViewById(R.id.edit_word);
        editTextMeaning = view.findViewById(R.id.edit_meaning);
        imageButtonEdit = view.findViewById(R.id.img_button_edit);
        imageButtonMeaning=view.findViewById(R.id.img_button_meaning);


        //set mic

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final SpeechRecognizer kSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());



        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        final Intent kSpeechRecognzierIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                //displaying the first match
                if (matches != null) {
                    editTextWord.setText(matches.get(0));

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        kSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matches1 = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches1 !=null){
                    editTextMeaning.setText(matches1.get(0));
                }


            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        imageButtonEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editTextWord.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editTextWord.setText("");
                        editTextWord.setHint("Listening...");
                        break;
                }
                return false;
            }
        });

        imageButtonMeaning.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        kSpeechRecognizer.stopListening();
                        editTextMeaning.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        kSpeechRecognizer.startListening(kSpeechRecognzierIntent);
                        editTextMeaning.setText("");
                        editTextMeaning.setHint("Listening...");
                        break;
                }
                return false;

            }
        });

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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
      //  Toast.makeText(getActivity(), "You selected"+myText.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
