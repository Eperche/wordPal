package com.example.englishdic;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.englishdic.viewHolder.dictionaryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.text.DateFormat.getDateTimeInstance;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "anonim" ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabasetimeStamp;
    private FirebaseUser mCurrentUser;
    DatabaseReference  databaseReference;
    DatabaseReference  root;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions <Dictionary> options;
    FirebaseRecyclerAdapter<Dictionary,dictionaryViewHolder> adapter;
    CircleImageView addImage;

    private boolean flagDecoration = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();



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

        addImage = (CircleImageView) findViewById(R.id.addWord);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    private void showDictionary() {
        Log.d(TAG, "showDictionary:success");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);



        options = new FirebaseRecyclerOptions.Builder<Dictionary>()
                .setQuery(databaseReference,Dictionary.class).build();

        adapter = new FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final dictionaryViewHolder holder, int position, @NonNull Dictionary model) {
                final String post_key = getRef(position).getKey();

                holder.word.setText(model.getWord());
                holder.meaning.setText(model.getMeaing());

                final DatabaseReference mDatabaseDickey = databaseReference.child(post_key);

                mDatabaseDickey.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      final   long timeStamp;
                      if(dataSnapshot.hasChild("date")){
                        timeStamp = (long) dataSnapshot.child("date").getValue();
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


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
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
                    Toast.makeText(MainActivity.this, "showDictionary", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "make-Permanent -Authentication failed.",
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  makePermanent();
                           // Toast.makeText(MainActivity.this, "SignInAnony..Ekleniyor.."+user.getUid(), Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Dictionary");




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager()," DIALOG");
    }
    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if(adapter!=null){
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
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
