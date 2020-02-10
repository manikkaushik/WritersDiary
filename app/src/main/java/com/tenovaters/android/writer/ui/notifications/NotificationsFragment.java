package com.tenovaters.android.writer.ui.notifications;

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
import com.tenovaters.android.writer.Adapters.NotificationAdapter;
import com.tenovaters.android.writer.Database.NotificationList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    public NotificationAdapter mAdapter;
    private DatabaseReference mDataBase;
    private DatabaseReference mDatabaseRef;
    public RecyclerView mRecyclerView;
    public List<NotificationList> mUploads;
    public TextView f631t1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_notification);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.f631t1 = (TextView) root.findViewById(R.id.tv_no_notification);
        this.firebaseAuth = FirebaseAuth.getInstance();
        String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mUploads = new ArrayList<>();
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Notifications");
        this.mDatabaseRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotificationsFragment.this.mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshot2 : postSnapshot1.getChildren()) {
                            NotificationsFragment.this.mUploads.add((NotificationList) postSnapshot2.getValue(NotificationList.class));
                        }
                    }
                }
                Collections.sort(NotificationsFragment.this.mUploads, new Comparator<NotificationList>() {
                    public int compare(NotificationList lhs, NotificationList rhs) {
                        return rhs.getDate().compareTo(lhs.getDate());
                    }
                });
                if (NotificationsFragment.this.mUploads.isEmpty()) {
                    NotificationsFragment.this.f631t1.setVisibility(View.VISIBLE);
                }
                if (!NotificationsFragment.this.mUploads.isEmpty()) {
                    NotificationsFragment.this.f631t1.setVisibility(View.INVISIBLE);
                }
                NotificationsFragment notificationsFragment = NotificationsFragment.this;
                notificationsFragment.mAdapter = new NotificationAdapter(notificationsFragment.getContext(), NotificationsFragment.this.mUploads);
                NotificationsFragment.this.mRecyclerView.setAdapter(NotificationsFragment.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotificationsFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
