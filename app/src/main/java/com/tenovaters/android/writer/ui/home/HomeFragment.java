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
    /* access modifiers changed from: private */

    public ReadersAdapter mAdapter;
    private DatabaseReference mDataBase;

    private DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */

    public ProgressBar mProgressCircle;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public List<ReadersList> mUploads;

    /* renamed from: z */
    int f630z;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycle_home);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mUploads = new ArrayList();
        String str = "Published";
        this.f630z = 0;
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (HomeFragment.this.f630z == 0) {
                    HomeFragment homeFragment = HomeFragment.this;
                    homeFragment.f630z = 1;
                    homeFragment.mUploads.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                            if ("Published".equals(workersdatabase.getStatus())) {
                                HomeFragment.this.mUploads.add(workersdatabase);
                            }
                        }
                    }
                    HomeFragment homeFragment2 = HomeFragment.this;
                    homeFragment2.mAdapter = new ReadersAdapter(homeFragment2.getContext(), HomeFragment.this.mUploads);
                    HomeFragment.this.mRecyclerView.setAdapter(HomeFragment.this.mAdapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                HomeFragment.this.mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        return root;
    }
}
