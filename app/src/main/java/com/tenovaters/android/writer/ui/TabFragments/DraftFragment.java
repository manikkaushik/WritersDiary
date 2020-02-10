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
import com.tenovaters.android.writer.Adapters.DraftAdapter;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.List;

public class DraftFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    /* access modifiers changed from: private */
    public DraftAdapter mAdapter;
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
        View root = inflater.inflate(R.layout.fragment_draft, container, false);
        this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_draft);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.t1 = (TextView) root.findViewById(R.id.tv_no_draft);
        this.firebaseAuth = FirebaseAuth.getInstance();
        String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mUploads = new ArrayList<>();
        String str = "Unpublished";
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                DraftFragment.this.mUploads.clear();
                for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                    ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                    if ("Unpublished".equals(workersdatabase.getStatus())) {
                        DraftFragment.this.mUploads.add(workersdatabase);
                    }
                }
                if (DraftFragment.this.mUploads.isEmpty()) {
                    DraftFragment.this.t1.setVisibility(View.VISIBLE);
                }
                if (!DraftFragment.this.mUploads.isEmpty()) {
                    DraftFragment.this.t1.setVisibility(View.INVISIBLE);
                }
                DraftFragment draftFragment = DraftFragment.this;
                draftFragment.mAdapter = new DraftAdapter(draftFragment.getContext(), DraftFragment.this.mUploads);
                DraftFragment.this.mRecyclerView.setAdapter(DraftFragment.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DraftFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
