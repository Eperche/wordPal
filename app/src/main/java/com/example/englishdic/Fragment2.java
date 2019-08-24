package com.example.englishdic;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.englishdic.viewHolder.dictionaryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment2 extends Fragment  {


    private static final String TAG = "anonim" ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;

    private String post_key;
    private String post_keySentence;
    private String post_word;


    DatabaseReference  databaseReference;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Dictionary> options;
    FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder> adapter;
    TextView mTextView;



    private boolean flagDecoration = false;

      View a, b;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_activity, container, false);

       /* a = root.findViewById(R.id.ActivityMain);
        b = root.findViewById(R.id.fragment2);*/

        return root;

    }
 /*  public void showB() {
        a.setVisibility(View.INVISIBLE);
        b.setVisibility(View.VISIBLE);
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //    showB();
        mAuth = FirebaseAuth.getInstance();

        if(post_key!=null){
            Log.d(TAG, "Buraya bak");
            showSentences();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Log.d(TAG, "getCurrentUser() == null)");
                 //   signInAnonymously();
                }
                else{
                    Log.d(TAG, "getCurrentUser() not null)");
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
        mCurrentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(mCurrentUser.getUid()).child("Dictionary").child(post_key).child("sentences");
      //  databaseReference.keepSynced(true);
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerviewFragment2);



        options = new FirebaseRecyclerOptions.Builder<Dictionary>()
                .setQuery(databaseReference,Dictionary.class).build();

        adapter = new FirebaseRecyclerAdapter<Dictionary, dictionaryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull dictionaryViewHolder holder, int position, @NonNull final Dictionary model) {
                final String post_keySentence = getRef(position).getKey();
                holder.sentence.setText(model.getSentence());
                holder.meaningSentence.setText(model.getMeaningSentence());

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

}
