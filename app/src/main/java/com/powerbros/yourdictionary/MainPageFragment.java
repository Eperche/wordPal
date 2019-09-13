package com.powerbros.yourdictionary;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;


import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
//import android.util.Log;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.powerbros.yourdictionary.viewHolder.dictionaryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import de.hdodenhof.circleimageview.CircleImageView;

public class MainPageFragment extends Fragment {


    public List<Languages> getLanguagesList() {
        return languagesList;
    }
   // private MainPageFragmentListener mainPageFragmentlistener; dil listesi için gerek yok exampleDialog sınıfından türetilerek aciliyor yani fragment yeniden olusuturuloyr

    private   List<Languages> languagesList = new ArrayList<>();
    Locale[] locales = Locale.getAvailableLocales();
    private ViewPager mviewPager;
    private  PagerAdapter mpagerAdapter;
    private static final String TAG = "STT" ;
    private TextToSpeech mTTS;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
  //  private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    DatabaseReference  databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions <Dictionary> options;
    FirebaseRecyclerAdapter<Dictionary,dictionaryViewHolder> adapter;
  //  CircleImageView addImage;
    TextView mTextView;
    ImageButton  addImage1;
    private AdView mAdView;
    ExampleDialog exampleDialog = new ExampleDialog();

 //   private ExampleDialog.ExampleDialogListener listener;

    ProgressBar progressBar;


    private boolean flagDecoration = false;
    private boolean flagList = true;
 //   View a, b;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            setHasOptionsMenu(true);
       /* View root = inflater.inflate(R.layout.tumlesik, container, false);
        a = root.findViewById(R.id.layoutListem);
        b = root.findViewById(R.id.mainPage);*/
        return inflater.inflate(R.layout.main_page_fragment, container, false);

    }
  /*  public void showB() {
        a.setVisibility(View.INVISIBLE);
        b.setVisibility(View.VISIBLE);
    }*/


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   showB();
     //   FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        flagDecoration = false;

        mAuth = FirebaseAuth.getInstance();

        mTextView = (TextView)getView().findViewById(R.id.no_word);
        mTextView.setVisibility(View.INVISIBLE);

        progressBar= (ProgressBar)getView().findViewById(R.id.progrresbarr);
        progressBar.setVisibility(View.VISIBLE);


        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){

                    //      Log.i("STT", "getLanguages() was called");

                    for(Locale l:locales)
                    {
                        int res = mTTS.isLanguageAvailable(l);
                        if(res == TextToSpeech.LANG_COUNTRY_AVAILABLE && flagList){
                            Languages languages = new Languages(l.getDisplayName(),l.getLanguage());
                            //   Toast.makeText(getActivity(), l.getDisplayName(), Toast.LENGTH_SHORT).show();
                            languagesList.add(languages);

            /*    hashSet.add(l);
                listem.add(l.getLanguage());*/

                        }
                    }
                    //     Log.d("STT", "MainPageFrag-size: "+languagesList.size());
                    //mainPageFragmentlistener.applyList(languagesList);


                }
                flagList=false; // oncreated her açıldığında array liste tekrar tekrar diller ekleniyor.Bu yüzden flag kullandıldı.
                //Oncreatead Tekrar çalışınca bu metot tekrar calisiyor ama sınıf tekrar tanımlanmıyor.Flag list yukarda true onCreated tekrar calisinca tekrardan true olmuyor.
            }
        });
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
              //      Log.d(TAG, "getCurrentUser() == null)");
                    signInAnonymously();
                }
                else{
             //       Log.d(TAG, "getCurrentUser() not null)");
              //      final String user_id = mAuth.getCurrentUser().getUid();
                    mCurrentUser = mAuth.getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");
                    databaseReference.keepSynced(true);
                  DatabaseReference no_word =FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");
                //    DatabaseReference no_word =FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid());

                    showDictionary(null);
                    no_word.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                //    Toast.makeText(getActivity(), " exixst", Toast.LENGTH_SHORT).show();
                                mTextView.setVisibility(View.INVISIBLE);

                            }
                            else{
                                //   Toast.makeText(getActivity(), "No exixst", Toast.LENGTH_SHORT).show();
                                mTextView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    addImage1.setVisibility(View.VISIBLE);
                    //checkUserExist();
                }
            }


        };

        //addImage1 = (CircleImageView) getView().findViewById(R.id.addWord);
        addImage1 = (ImageButton) getView().findViewById(R.id.addWord);
        addImage1.setVisibility(View.INVISIBLE);
        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }




        });


    }

    private void showDictionary(String searchText) {
   //     Log.d(TAG, "showDictionary:success");
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerview);
        mCurrentUser = mAuth.getCurrentUser();

      databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");


        /*   Query firebaseSearcQuery=databaseReference.orderByKey();
        options =new FirebaseRecyclerOptions.Builder<Dictionary>().setQuery(firebaseSearcQuery,Dictionary.class).build();*/
        if(!TextUtils.isEmpty(searchText)){
            String query= searchText.toLowerCase();
            Query irebaseSearcQuery=databaseReference.orderByChild("word").startAt(query).endAt(query+"\uf8ff");
            options =new FirebaseRecyclerOptions.Builder<Dictionary>().setQuery(irebaseSearcQuery,Dictionary.class).build();
        }

    else{
            options = new FirebaseRecyclerOptions.Builder<Dictionary>()
                    .setQuery(databaseReference,Dictionary.class).build();
        }


        adapter = new FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final dictionaryViewHolder holder, int position, @NonNull final Dictionary model) {
                final String post_key = getRef(position).getKey();
                holder.word.setText(model.getWord());
                holder.meaning.setText(model.getMeaning());

                final DatabaseReference mDatabaseDickey = databaseReference.child(post_key);//Sözcük push key değeri
               // final DatabaseReference mWordLang = mDatabaseDickey.child("wordLang");




                // Listedeki itemlere tiklandiği zaman..
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int result = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                            try
                            {
                                    result = mTTS.setLanguage(Locale.forLanguageTag( model.getWordLang().get("getName")));

                            }catch (Exception e){
                                //  result = mTTS.setLanguage(Locale.getDefault());
                            }
                        }

                        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                            //Log.d("TTS", "Language not supported");
                        }
                        else{
                            //.setEnabled(true);
                        }
                        speak();
                    }
                    private void speak(){

                        mTTS.speak(model.getWord(),TextToSpeech.QUEUE_FLUSH,null);

                    }

                });

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChangeDialog changeDialog = new ChangeDialog();
                        //changeDialog.languagesList = this
                        changeDialog.languagesList = languagesList;
                        changeDialog.setEquivalent(model.getMeaning());
                        changeDialog.setWord(model.getWord());
                        changeDialog.setPost_key(post_key);
                        changeDialog.show(getFragmentManager(),"change dialog");
                        return false;
                    }
                });

                holder.sentenceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSentencesDialog(post_key,model.getWord());
                   //     Log.d(TAG, post_key+" :Postkeysuccess");
                    }
                });
                holder.displayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                MainActivity mainActivity =new MainActivity();
                       mviewPager = mainActivity.viewPager;
                       mpagerAdapter =mainActivity.pagerAdapter;
                   //     mviewPager.setAdapter(mpagerAdapter);
                        Fragment frg = null;
                        frg = ((PageAdapter)mviewPager.getAdapter()).getFragment(1);


                        if (frg != null && frg instanceof Fragment2) {
                            ((Fragment2)frg).sendGetRequest(post_key,model.getWord(),frg);
                        }
                        mviewPager.setCurrentItem(1);
                    }
                });

                holder.date.setVisibility(View.INVISIBLE);
                mDatabaseDickey.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final long timestamp;
                        if(dataSnapshot.hasChild("date")){
                            timestamp = (long) dataSnapshot.child("date").getValue();
                            //     holder.date.setText(getDate( (long) dataSnapshot.child("date").getValue()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


            @NonNull
            @Override
            public dictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listem,viewGroup,false);
                return new dictionaryViewHolder(view);
            }
        };

    //    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());

        if(!flagDecoration)//RecyclerView decorator not adding extra padding on refresh when adding this code
        {
            recyclerView.addItemDecoration(dividerItemDecoration);
            flagDecoration = true;
        }
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int testimonycount = adapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (testimonycount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        adapter.startListening();
        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(adapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        }


 /*   private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(user_id);
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Toast.makeText(getActivity(), "showDictionary", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getActivity(), "Nothing to show", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

 /*   private void makePermanent() {
        String password="passworrd";
        String email="example@example.com";
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "make-Permanent -Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }*/
 /*
    private void makePermanet() {
        String password="passworrd";
        String email="example@example.com";

        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        mAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        });
    }*/

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  makePermanent();
                           // Toast.makeText(MainPageFragment.this, "SignInAnony..Ekleniyor.."+user.getUid(), Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Dictionary");




                        } else {
                            // If sign in fails, display a message to the user.
                   //         Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void openDialog() {
        exampleDialog.languagesList = this.getLanguagesList();
        exampleDialog.show(getFragmentManager(),"word dialog");
    }

    private void openSentencesDialog(String post_key,String word) {
        ExampleDialog sentencesDialog = new ExampleDialog();
        sentencesDialog.setPost_key(post_key);
        sentencesDialog.setWord(word);
        sentencesDialog.languagesList=languagesList;
        sentencesDialog.show(getFragmentManager(),"sentence dialog");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);

         SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showDictionary(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showDictionary(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu,inflater);



    }



    @Override
    public void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if(adapter!=null){
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTTS != null){
            mTTS.stop();
        }
    }

    public  String getDate(long timestamp)
    {
        Date date = new Date(timestamp);

        return date.toString();
    }
  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mainPageFragmentlistener =(MainPageFragmentListener) context;
        } catch (ClassCastException e)  {
            throw new ClassCastException(context.toString()+"must implement MainPageFragmentListener");
        }
    }
    public interface MainPageFragmentListener{
        void applyList(List<Languages> mlist);
    }

*/
}
