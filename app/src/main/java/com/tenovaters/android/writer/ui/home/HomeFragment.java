package com.tenovaters.android.writer.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenovaters.android.writer.Adapters.ReadersAdapter;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.Database.UserList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseReference mDataBase,mDatabaseRef;
    private RecyclerView mRecyclerView;
    private ReadersAdapter mAdapter;
    private List<ReadersList> mUploads;
    private ProgressBar mProgressCircle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mDataBase= FirebaseDatabase.getInstance().getReference("Reader").child("1234567890101112167");

        ReadersList user= new ReadersList("Dhamaka har jgha","Devesh","Dancing","devesh is a very good boy he is very stomg");
        mDataBase.setValue(user);

        mRecyclerView = (RecyclerView)root.findViewById(R.id.recycle_home);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // mProgressCircle = rootView.findViewById(R.id.pro);

        mUploads = new ArrayList<>();
        //mProgressCircle = (ProgressBar) findViewById(R.id.progressBar56);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReadersList workersdatabase = postSnapshot.getValue(ReadersList.class);
                    mUploads.add(workersdatabase);

                }
                mAdapter = new ReadersAdapter(getContext(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }
}