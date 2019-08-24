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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;


public class    ChangeDialog extends AppCompatDialogFragment {

    private DatabaseReference mDatabase;
    private DatabaseReference rootDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText editTextWord;
    private EditText editTextMeaning;
    private ImageButton imageButtonEdit;
    private ImageButton imageButtonMeaning;

    private String word;
    private String equivalent;
    private String post_key;
    private String post_keySentence;
    private String sentence;
    private String sentenceEquivalent;


    public void setPost_keySentence(String post_keySentence) {
        this.post_keySentence = post_keySentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setSentenceEquivalent(String sentenceEquivalent) {
        this.sentenceEquivalent = sentenceEquivalent;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

      //  makePermanet();
        rootDatabase =  FirebaseDatabase.getInstance().getReference();
        mCurrentUser =mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");


        mDatabase.keepSynced(true);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.open_dialog,null);

        builder.setView(view)
                .setTitle("Change")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(word))
                        saveDictionary();
                        else{
                            Toast.makeText(getActivity(), post_keySentence, Toast.LENGTH_SHORT).show();
                           saveSentece();
                        }
                    }
                });

        editTextWord = view.findViewById(R.id.edit_word);
        editTextMeaning = view.findViewById(R.id.edit_meaning);
        imageButtonEdit = view.findViewById(R.id.img_button_edit);
        imageButtonMeaning=view.findViewById(R.id.img_button_meaning);

        if(!TextUtils.isEmpty(word)){
            editTextWord.setText(word);
            editTextMeaning.setText(equivalent);
        }
        else {
            editTextWord.setText(sentence);
            editTextMeaning.setText(sentenceEquivalent);
        }


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

    private void saveSentece() {
        String wordx = editTextWord.getText().toString().trim();
        String meaning = editTextMeaning.getText().toString().trim();


        if(!TextUtils.isEmpty(wordx) && !TextUtils.isEmpty(meaning)){
            //   final DatabaseReference newPost = mDatabase.push();
            DatabaseReference saveDictionary = mDatabase.child(post_key).child("sentences").child(post_keySentence);//post_key=sözcük keyi
            addNewSampleSentences(saveDictionary,wordx,meaning);
        }
    }
    private void addNewSampleSentences(DatabaseReference saveDictionary,String word, String meaning) {

        saveDictionary.child("meaningSentence").setValue(meaning);
        saveDictionary.child("sentence").setValue(word);

      /*     Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);*/
        //newPost.child("data").updateChildren(map);
        //     saveDictionary.child("date").setValue(ServerValue.TIMESTAMP);
        mDatabase.keepSynced(true);
        saveDictionary.keepSynced(true);

    }

    private void saveDictionary() {

         String wordx = editTextWord.getText().toString().trim();
         String meaning = editTextMeaning.getText().toString().trim();


         if(!TextUtils.isEmpty(wordx) && !TextUtils.isEmpty(meaning)){
          //   final DatabaseReference newPost = mDatabase.push();
             DatabaseReference saveDictionary = mDatabase.child(post_key);//post_key=sözcük keyi
             addNewSample(saveDictionary,wordx,meaning);
         }
    }

    private void addNewSample(DatabaseReference saveDictionary,String word, String meaning) {

         saveDictionary.child("meaning").setValue(meaning);
         saveDictionary.child("word").setValue(word);

      /*     Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);*/
            //newPost.child("data").updateChildren(map);
      //     saveDictionary.child("date").setValue(ServerValue.TIMESTAMP);
            mDatabase.keepSynced(true);
            saveDictionary.keepSynced(true);

    }

}
