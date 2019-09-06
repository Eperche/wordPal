package com.powerbros.yourdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;

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
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Locale;


public class ExampleDialog extends AppCompatDialogFragment {
    private static final String TAG = "STT" ;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseSentences;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText editTextWord;
    private EditText editTextMeaning;
    private ImageButton imageButtonEdit;
    private ImageButton imageButtonMeaning;

    private ExampleDialogListener listener;
    private String post_key;
    private String word;

    public void setWord(String word) {
        this.word = word;

    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }
    Spinner spinner;
    Spinner spinner1;

 //   int sum = 0;

    private TextToSpeech mTTS;

    Locale[] locales = Locale.getAvailableLocales();

/*    Locale loc;
    List<Locale> localeList = new ArrayList<Locale>();
    List<String> country = new ArrayList<String>();*/

    // * - Move these two variables out here
    /*ArrayAdapter<Locale> aa;
    ArrayAdapter<String> bb;*/
    ArrayAdapter<Languages> Ladapter;
 //   private String selectedLanguage;

    private Languages selectedLang;
    private Languages selectedLang2;
    private int defaultPosition;
    private int selectedPosition;
    private boolean addPosition = false;
    private boolean addPosition1 = false;
    public   List<Languages> languagesList = new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        addPosition = false;
        addPosition1 = false;



        mAuth = FirebaseAuth.getInstance();

      //  makePermanet();
        mCurrentUser =mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");

        mDatabase.keepSynced(true);




        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.open_dialog,null);
        if(!TextUtils.isEmpty(word))
        builder.setView(view).setTitle("Create a sentence about "+ word);
        else
        builder.setView(view).setTitle("Add");

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(word)){

                            mDatabaseSentences = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).child("sentences");
                            addSentences();
                        }
                        else
                        addDictionary();
                    }
                }) ;
        if(!TextUtils.isEmpty(word)){
                builder.setNeutralButton("Sentences", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts(post_key,word);

                    }
                });
        }
         mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
               //     Log.d(TAG, "TextToSpeech.SUCCESS");
                    // * - delay important tasks until TTS is initialized
                    startWhenTTSIsInitialized();

                }
                setLanguagePrefence();
            }

        });
        spinner =  view.findViewById(R.id.spinner);

        spinner1 =  view.findViewById(R.id.spinner1);
        setLanguagePrefence();
        // ArrayAdapter arrayAdapter =  ArrayAdapter.createFromResource(getActivity(),R.array.languages,R.layout.support_simple_spinner_dropdown_item);
     //   spinner.setAdapter(arrayAdapter);
     //   spinner.setOnItemSelectedListener(this);
     /*   spinner1.setAdapter(arrayAdapter);

        spinner1.setOnItemSelectedListener(this);*/

        editTextWord = view.findViewById(R.id.edit_word);
        editTextMeaning = view.findViewById(R.id.edit_meaning);
        imageButtonEdit = view.findViewById(R.id.img_button_edit);
        imageButtonMeaning=view.findViewById(R.id.img_button_meaning);
        if(!TextUtils.isEmpty(word)){
            editTextWord.setHint("Enter your sentence");
            editTextMeaning.setHint("Enter the meaning of sentence");
        }
      /*  LinkedHashSet<String> hashSet = new LinkedHashSet<String>();
        Locale[] locales = Locale.getAvailableLocales();
        for(Locale l:locales)
        {
            hashSet.add(l.getDisplayLanguage());
        }
*/



        return builder.create();
    }
    private void startMic2(){

        final SpeechRecognizer kSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        final Intent kSpeechRecognzierIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
 //       kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
       //  kSpeechRecognzierIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
        //        Locale.getDefault());


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
   //     Log.i("STT", "startWhenTTSIsInitialized() was called");


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
       // List<Languages> languagesList = new ArrayList<>();
    /*
        int x = 0;
        int y = 0;

        for(Locale l:locales)
        {
            int res = mTTS.isLanguageAvailable(l);
            if(res == TextToSpeech.LANG_COUNTRY_AVAILABLE){
                Languages languages = new Languages(l.getDisplayName(),l.getLanguage());

                languagesList.add(languages);
            //    hashSet.add(l);
          //      listem.add(l.getLanguage());
                if(l.getLanguage().equals(Locale.getDefault().getLanguage())){
                    defaultPosition=x;
              //      Toast.makeText(getActivity(), "Default: "+defaultPosition, Toast.LENGTH_SHORT).show();
                }
                if(l.getLanguage().equals(Locale.ENGLISH.getLanguage())){
                    selectedPosition=y;
                }
                y++;
                x++;
            }
        }*/

    //    Log.d(TAG, "Ladapter list size ===> "+languagesList.size());
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


    private void addDictionary() {
         String word = editTextWord.getText().toString().trim().toLowerCase();
         String meaning = editTextMeaning.getText().toString().trim().toLowerCase();

         if(!TextUtils.isEmpty(word) && !TextUtils.isEmpty(meaning)){
             final DatabaseReference newPost = mDatabase.push();
             addNewSample(newPost,word,meaning);
         }
         else if(TextUtils.isEmpty(word)){
             Toast.makeText(getActivity(), "Please enter your word", Toast.LENGTH_SHORT).show();
         }
         else if(TextUtils.isEmpty(meaning)){
             Toast.makeText(getActivity(), "Please enter the  meaning", Toast.LENGTH_SHORT).show();
         }
    }


    private void addNewSample(final DatabaseReference newPost, String word, String meaning) {
            Dictionary dictionary = new Dictionary(word,meaning);
            newPost.setValue(dictionary);
            Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);
            //newPost.child("data").updateChildren(map);
            newPost.child("wordLang").setValue(spinner.getSelectedItem());
            newPost.child("meaningLang").setValue(spinner1.getSelectedItem());
            newPost.child("date").setValue(ServerValue.TIMESTAMP);

          //  mDatabase.keepSynced(true);
       //     newPost.keepSynced(true);

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

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence").child("positon");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int deger =Integer.parseInt(dataSnapshot.getValue().toString());
                    spinner.setSelection(deger);
                }
            /*    else{
                    spinner.setSelection(selectedPosition);
                }*/
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

        DatabaseReference myref1 =  FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("LanguagePrefence").child("positon1");
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int deger =Integer.parseInt(dataSnapshot.getValue().toString());
                    spinner1.setSelection(deger);
                }
        /*        else{
                    spinner1.setSelection(defaultPosition);
                }*/
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
    private void addSentences() {
        String sentence = editTextWord.getText().toString().trim();
        String SentenceMeaning = editTextMeaning.getText().toString().trim();

        if(!TextUtils.isEmpty(sentence) && !TextUtils.isEmpty(SentenceMeaning)){
            final DatabaseReference newPost = mDatabaseSentences.push();
            addNewSampleaddSentences(newPost,sentence,SentenceMeaning);
        }
        else if(TextUtils.isEmpty(sentence)){
            Toast.makeText(getActivity(), "Please enter your sentence", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(SentenceMeaning)){
            Toast.makeText(getActivity(), "Please enter the meaning of sentence", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewSampleaddSentences(DatabaseReference newPost, String sentence, String SentenceMeaning) {
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

     /*   public void   sendGetRequestLanguage(List<Languages> mlist){

        this.languagesList=mlist;

            Log.i("STT","ExampleDialog applyList size :"+languagesList.size());

    }*/

}
