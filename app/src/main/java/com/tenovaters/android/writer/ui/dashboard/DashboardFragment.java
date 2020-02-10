package com.tenovaters.android.writer.ui.dashboard;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenovaters.android.writer.Adapters.ReadersAdapter;
import com.tenovaters.android.writer.Adapters.StoryAdapter;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    public ReadersAdapter mAdapter;
    public StoryAdapter mAdapter1;
    private DatabaseReference mDataBase;
    public DatabaseReference mDatabaseRef;
    public RecyclerView mRecyclerView;
    public RecyclerView mRecyclerView1;
    public List<ReadersList> mUploads;
    public List<ReadersList> mUploads1;
    public String myinterest;

    public TextView t1;
    public String value;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_favourite);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView1 = (RecyclerView) root.findViewById(R.id.recycler_interest);
        this.mRecyclerView1.setHasFixedSize(true);
        this.mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.t1 = (TextView) root.findViewById(R.id.tv_no_notification);
        this.firebaseAuth = FirebaseAuth.getInstance();
        final String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mUploads = new ArrayList<>();
        this.mUploads1 = new ArrayList();
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDataBase = FirebaseDatabase.getInstance().getReference("Users");
        this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                DashboardFragment.this.mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                        if (((String) postSnapshot1.child("Favourites").child(currentUser).child("favourite").getValue(String.class)) != null) {
                            DashboardFragment.this.mUploads.add(workersdatabase);
                        }
                    }
                }
                if (DashboardFragment.this.mUploads.isEmpty()) {
                    DashboardFragment.this.t1.setVisibility(View.VISIBLE);
                }
                if (!DashboardFragment.this.mUploads.isEmpty()) {
                    DashboardFragment.this.t1.setVisibility(View.INVISIBLE);
                }
                DashboardFragment dashboardFragment = DashboardFragment.this;
                dashboardFragment.mAdapter = new ReadersAdapter(dashboardFragment.getContext(), DashboardFragment.this.mUploads);
                DashboardFragment.this.mRecyclerView.setAdapter(DashboardFragment.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DashboardFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.mDataBase.child(currentUser).child("Interests").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                DashboardFragment.this.mUploads1.clear();
                for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                    DashboardFragment.this.myinterest = (String) postSnapshot1.child("category").getValue(String.class);
                    if (DashboardFragment.this.myinterest.equals("Romance")) {
                        DashboardFragment.this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                for (DataSnapshot postSnapshot : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                                        ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                                        Iterator it = postSnapshot1.child("Category").getChildren().iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            DashboardFragment.this.value = (String) ((DataSnapshot) it.next()).child("category").getValue(String.class);
                                            if (DashboardFragment.this.value.compareTo(DashboardFragment.this.myinterest) != 0) {
                                                if (DashboardFragment.this.myinterest.compareTo(DashboardFragment.this.value) == 0) {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        DashboardFragment.this.mUploads1.add(workersdatabase);
                                    }
                                }
                                DashboardFragment.this.mUploads1.isEmpty();
                                DashboardFragment.this.mUploads1.isEmpty();
                                DashboardFragment.this.mAdapter1 = new StoryAdapter(DashboardFragment.this.getContext(), DashboardFragment.this.mUploads1);
                                DashboardFragment.this.mRecyclerView1.setAdapter(DashboardFragment.this.mAdapter1);
                            }

                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(DashboardFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DashboardFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
