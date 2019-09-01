package com.powerbros.yourdictionary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class    ChangeDialog extends AppCompatDialogFragment {
 //   private static final String TAG = "STT" ;
    private DatabaseReference mDatabase;
    private DatabaseReference rootDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText editTextWord;
    private EditText editTextMeaning;
    private ImageButton imageButtonEdit;
    private ImageButton imageButtonMeaning;
    public   List<Languages> languagesList = new ArrayList<>();
    private String word;
    private String equivalent;
    private String post_key;
    private String post_keySentence;
    private String sentence;
    private String sentenceEquivalent;


    Spinner spinner;
    Spinner spinner1;
    private TextToSpeech mTTS;

    Locale[] locales = Locale.getAvailableLocales();
    ArrayAdapter<Languages> Ladapter;
    //   private String selectedLanguage;
    private Languages selectedLang;
    private Languages selectedLang2;
    private int defaultPosition;
    private boolean addPosition = false;
    private boolean addPosition1 = false;

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
        addPosition = false;
        addPosition1 = false;

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
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(word)){
                            DatabaseReference deleteWord = mDatabase.child(post_key);//post_key=sözcük keyi
                            deleteWord.removeValue();
                        }
                        else{
                            DatabaseReference deleteSentence = mDatabase.child(post_key).child("sentences").child(post_keySentence);//post_key=sözcük keyi
                            deleteSentence.removeValue();
                        }
                    }
                })
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(word))
                        saveDictionary();
                        else{
                   //         Toast.makeText(getActivity(), post_keySentence, Toast.LENGTH_SHORT).show();
                           saveSentece();
                        }
                    }
                });
        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
              //      Log.d(TAG, "TextToSpeech.SUCCESS");
                    // * - delay important tasks until TTS is initialized
                    startWhenTTSIsInitialized();

                }
                setLanguagePrefence();
            }

        });
        spinner =  view.findViewById(R.id.spinner);

        spinner1 =  view.findViewById(R.id.spinner1);
        setLanguagePrefence();


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

        return builder.create();
    }
    private void startMic2(){

        final SpeechRecognizer kSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        final Intent kSpeechRecognzierIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //       kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //    kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
        //           Locale.getDefault());


        kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLang2.getGetName());
        //   kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, selectedLang2.getGetName());
        //  kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,selectedLang2.getGetName());

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

    }

    private void startMic() {
        //set mic

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //   mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      /*  mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                "en-US");*/
        //    String languagePref = loc.toString();//or, whatever iso code...


        //  Log.d(TAG, "TextToSpeech ===="+ selectedLang.getGetName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLang.getGetName());
        //    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, selectedLang.getGetName());
        //   mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, selectedLang.getGetName());




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


    }
    private void startWhenTTSIsInitialized() {
     //   Log.i("STT", "startWhenTTSIsInitialized() was called");


     /*   for (Locale locale : locales) {
            int res = mTTS.isLanguageAvailable(locale);
            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                sum++;
                localeList.add(locale);
                // * - 'country' is used to populate the adapter, so
                // this line must come first
               country.add(locale.getDisplayName());
            }
        }*/


    /*    ArrayList<String> localCountries = new ArrayList<String>();
        for(Locale l:locales)
        {

            localCountries.add(l.getDisplayLanguage());
        }*/
      /*  LinkedHashSet<Locale> hashSet = new LinkedHashSet<Locale>();
        LinkedHashSet<String> listem = new LinkedHashSet<String>();*/

       /*
        int x = 0;

        for(Locale l:locales)
        {

            int res = mTTS.isLanguageAvailable(l);
            if(res == TextToSpeech.LANG_COUNTRY_AVAILABLE){
                Languages languages = new Languages(l.getDisplayName(),l.getLanguage());

                languagesList.add(languages);
            //    hashSet.add(l);
            //    listem.add(l.getLanguage());
                if(l.getLanguage()==Locale.getDefault().getLanguage()){
                    defaultPosition=x;
                    //      Toast.makeText(getActivity(), "Default: "+defaultPosition, Toast.LENGTH_SHORT).show();

                }
                x++;

            }

        }*/

        Ladapter = new ArrayAdapter<Languages>(getActivity(), android.R.layout.simple_spinner_item,languagesList){
    /*        @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;

                // If this is the initial blnk entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }

                return v;
            }*/
        };

        Ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner1.setAdapter(Ladapter);
        spinner.setAdapter(Ladapter);

      /*

        LinkedHashSet<Languages> langSet = new LinkedHashSet<Languages>(languagesList);
        List<Languages> langList = new ArrayList<Languages>(langSet);*/




    /*    List<Locale> list = new ArrayList<Locale>(hashSet);
        List<String> liste = new ArrayList<String>(listem);*/



        //Creating the ArrayAdapter instance having the country list
        // * - now it's safe to set the adapter because the country list has been populated
        // aa = new ArrayAdapter<Locale>(getActivity(), android.R.layout.simple_spinner_item,list);
        //  aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner


     /* Bu kısım geçerli üstteki yorum satırları değil
       bb = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,liste);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(bb);
        spinner.setOnItemSelectedListener(this);*/
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
   //     mDatabase.keepSynced(true);
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
         //   mDatabase.keepSynced(true);
            saveDictionary.keepSynced(true);

    }
    private  void addLanguagePreference(int position) {
        final DatabaseReference prefence  = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence");
        prefence.child("positon").setValue(position);
    }

    private  void addLanguagePreference1(int position) {
        final DatabaseReference prefence  = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence");
        prefence.child("positon1").setValue(position);
    }
    private void setLanguagePrefence(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence").child("positon");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int deger =Integer.parseInt(dataSnapshot.getValue().toString());
                    spinner.setSelection(deger);
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myText = (TextView) view;
//        loc = (Locale) spinner.getSelectedItem();
                // selectedLanguage =myText.getText().toString();
                selectedLang= (Languages) parent.getSelectedItem();
                startMic();
                if(addPosition){
                    addLanguagePreference(position);

                }

                addPosition=true;
                //   Toast.makeText(getActivity(), "You selected spinner "+selectedLang.getGetName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatabaseReference myref =  FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence").child("positon1");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int deger =Integer.parseInt(dataSnapshot.getValue().toString());
                    spinner1.setSelection(deger);
                }
                else{
                    spinner1.setSelection(defaultPosition);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView myText = (TextView) view;
//        loc = (Locale) spinner.getSelectedItem();
                // selectedLanguage =myText.getText().toString();
                selectedLang2= (Languages) parent.getSelectedItem();
                startMic2();
                if(addPosition1){
                    addLanguagePreference1(position);

                }
                addPosition1=true;

                //  Toast.makeText(getActivity(), "You selected spinner1 "+selectedLang2.getGetName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
