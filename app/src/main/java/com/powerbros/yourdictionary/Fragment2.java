package com.powerbros.yourdictionary;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.powerbros.yourdictionary.viewHolder.dictionaryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Fragment2 extends Fragment  {


    private static final String TAG = "anonim" ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
   // private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private TextToSpeech mTTS;
    private String post_key;
  //  private String post_keySentence;
    private String post_word;
    private AdView adView1;




    DatabaseReference  databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Dictionary> options;
    FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder> adapter;

    TextView mTextView;



 /*   private boolean flagDecoration = false;
    private boolean showB = false;*/



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        return root;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){



                }

            }
        });

        adView1 =getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        mTextView = (TextView)getView().findViewById(R.id.no_word2);
        mTextView.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        if(post_key!=null){
       //     Log.d(TAG, "Buraya bak");
            mCurrentUser = mAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).child("sentences");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                    //    Toast.makeText(getActivity(), " exixst", Toast.LENGTH_SHORT).show();
                        mTextView.setVisibility(View.INVISIBLE);
                        showSentences();
                    }
                    else{
                     //   Toast.makeText(getActivity(), "No exixst", Toast.LENGTH_SHORT).show();
                        mTextView.setText("No sentences added to "+"'"+post_word+"'" +" yet");
                        mTextView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{

            mTextView.setVisibility(View.VISIBLE);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    //Log.d(TAG, "getCurrentUser() == null)");
                 //   signInAnonymously();
                }
                else{
                //    Log.d(TAG, "getCurrentUser() not null)");
                    final String user_id = mAuth.getCurrentUser().getUid();
                    mCurrentUser = mAuth.getCurrentUser();

                    //checkUserExist();
                }
            }


        };
       /*
       if (getArguments() != null) {
            String strtext = getArguments().getString("params");
            post_key = strtext;
            Toast.makeText(getActivity(), post_key, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "noooooo", Toast.LENGTH_SHORT).show();
        }
        */

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void   sendGetRequest(String post_key,String post_word,Fragment frg){

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        this.post_key = post_key;
        this.post_word = post_word;


       // Toast.makeText(getActivity(), this.post_key, Toast.LENGTH_SHORT).show();

    }

    private void showSentences() {


      //  databaseReference.keepSynced(true);
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerviewFragment2);



        options = new FirebaseRecyclerOptions.Builder<Dictionary>()
                .setQuery(databaseReference,Dictionary.class).build();

        adapter = new FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull dictionaryViewHolder holder, int position, @NonNull final Dictionary model) {
                final String post_keySentence = getRef(position).getKey();

            //   Toast.makeText(getActivity(), "here we are == "+post_keySentence, Toast.LENGTH_SHORT).show();


                holder.sentence.setText(model.getSentence());
                holder.meaningSentence.setText(model.getMeaningSentence());

                final DatabaseReference mWordLang = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).child("wordLang");;//Sözcük push key değeri
                final String[] mGetLang = new String[1];
                mWordLang.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            mGetLang[0] = dataSnapshot.child("getName").getValue().toString();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTTS.setLanguage(Locale.forLanguageTag( mGetLang[0]));
                            }
                        }
                      catch (Exception e){

                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        speak();
                    }
                    private void speak(){

                        mTTS.speak(model.getSentence(),TextToSpeech.QUEUE_FLUSH,null);

                    }
                });
                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChangeDialog changeDialog = new ChangeDialog();
                        changeDialog.setSentence(model.getSentence());
                        changeDialog.setSentenceEquivalent(model.getMeaningSentence());
                        changeDialog.setPost_key(post_key);
                        changeDialog.setPost_keySentence(post_keySentence);
                        changeDialog.show(getFragmentManager(),"change dialog");
                        return false;
                    }
                });

            }
            @NonNull
            @Override
            public dictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_fragment2,viewGroup,false);
                return new dictionaryViewHolder(view);
            }
        };


         //   Toast.makeText(getActivity(),"showb=false", Toast.LENGTH_SHORT).show();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
            recyclerView.setLayoutManager(gridLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                    gridLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        /*    if(!flagDecoration)//RecyclerView decorator not adding extra padding on refresh when adding this code
            {
                recyclerView.addItemDecoration(dividerItemDecoration);
                flagDecoration = true;
            }*/

            adapter.startListening();
            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(adapter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTTS != null){
            mTTS.stop();
        }
    }
}
    /*    Query query =  FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).orderByChild("sentences").equalTo("sentences");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getActivity(), "dataSnapshot is exist", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "dataSnapshot not  exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/