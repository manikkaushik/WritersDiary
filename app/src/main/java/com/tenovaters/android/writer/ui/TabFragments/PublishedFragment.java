package com.tenovaters.android.writer.ui.TabFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenovaters.android.writer.Adapters.ReadersAdapter;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.List;

public class PublishedFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    /* access modifiers changed from: private */
    public ReadersAdapter mAdapter;
    private DatabaseReference mDataBase;
    private DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public List<ReadersList> mUploads;
    /* access modifiers changed from: private */

    /* renamed from: t1 */
    public TextView t1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_published, container, false);
        this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_publish);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.t1 = (TextView) root.findViewById(R.id.tv_no_publish);
        this.firebaseAuth = FirebaseAuth.getInstance();
        String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mUploads = new ArrayList<>();
        String str = "Published";
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                PublishedFragment.this.mUploads.clear();
                for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                    ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                    if ("Published".equals(workersdatabase.getStatus())) {
                        PublishedFragment.this.mUploads.add(workersdatabase);
                    }
                }
                if (PublishedFragment.this.mUploads.isEmpty()) {
                    PublishedFragment.this.t1.setVisibility(View.INVISIBLE);
                }
                if (!PublishedFragment.this.mUploads.isEmpty()) {
                    PublishedFragment.this.t1.setVisibility(View.INVISIBLE);
                }
                PublishedFragment publishedFragment = PublishedFragment.this;
                publishedFragment.mAdapter = new ReadersAdapter(publishedFragment.getContext(), PublishedFragment.this.mUploads);
                PublishedFragment.this.mRecyclerView.setAdapter(PublishedFragment.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PublishedFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
