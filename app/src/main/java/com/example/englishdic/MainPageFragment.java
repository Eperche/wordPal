package com.example.englishdic;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.englishdic.viewHolder.dictionaryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Date;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPageFragment extends Fragment  {

    private static final String TAG = "anonim" ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    DatabaseReference  databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions <Dictionary> options;
    FirebaseRecyclerAdapter<Dictionary,dictionaryViewHolder> adapter;
    CircleImageView addImage;
    private AdView mAdView;





    private boolean flagDecoration = false;
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
        mAuth = FirebaseAuth.getInstance();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("47900e12c2797100").build();
        mAdView.loadAd(adRequest);












        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Log.d(TAG, "getCurrentUser() == null)");
                    signInAnonymously();
                }
                else{
                    Log.d(TAG, "getCurrentUser() not null)");
                    final String user_id = mAuth.getCurrentUser().getUid();
                    mCurrentUser = mAuth.getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");
                    databaseReference.keepSynced(true);
                    showDictionary();
                    //checkUserExist();
                }
            }


        };
        addImage = (CircleImageView) getView().findViewById(R.id.addWord);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void showDictionary() {
        Log.d(TAG, "showDictionary:success");
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerview);
        mCurrentUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary");

        options = new FirebaseRecyclerOptions.Builder<Dictionary>()
                .setQuery(databaseReference,Dictionary.class).build();

        adapter = new FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final dictionaryViewHolder holder, int position, @NonNull final Dictionary model) {
                final String post_key = getRef(position).getKey();
                holder.word.setText(model.getWord());
                holder.meaning.setText(model.getMeaning());

                final DatabaseReference mDatabaseDickey = databaseReference.child(post_key);//Sözcük push key değeri

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChangeDialog changeDialog = new ChangeDialog();
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
                        Log.d(TAG, post_key+" :Postkeysuccess");
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                gridLayoutManager.getOrientation());

        if(!flagDecoration)//RecyclerView decorator not adding extra padding on refresh when adding this code
        {
            recyclerView.addItemDecoration(dividerItemDecoration);
            flagDecoration = true;
        }

        adapter.startListening();
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);



        }


    private void checkUserExist() {

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
    }

    private void makePermanent() {
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
    }/*
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
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  makePermanent();
                           // Toast.makeText(MainPageFragment.this, "SignInAnony..Ekleniyor.."+user.getUid(), Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Dictionary");




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getFragmentManager(),"word dialog");
    }
    private void openSentencesDialog(String post_key,String word) {
        SentencesDialog sentencesDialog = new SentencesDialog();
        sentencesDialog.setPost_key(post_key);
        sentencesDialog.setWord(word);
        sentencesDialog.show(getFragmentManager(),"sentence dialog");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
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
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
    public  String getDate(long timestamp)
    {
        Date date = new Date(timestamp);

        return date.toString();
    }


}
