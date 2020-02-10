package com.tenovaters.android.writer.GuestMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.List;

public class guest extends AppCompatActivity {
    /* access modifiers changed from: private */
    public GuestAdapter mAdapter;
    private DatabaseReference mDatabaseRef;

    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */

    public List<ReadersList> upload;

    /* renamed from: z */
    int f571z;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_guest);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_guest);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.upload = new ArrayList();
        String str = "Published";
        this.f571z = 0;
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (guest.this.f571z == 0) {
                    guest guest = guest.this;
                    guest.f571z = 1;
                    guest.upload.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                            if ("Published".equals(workersdatabase.getStatus())) {
                                guest.this.upload.add(workersdatabase);
                            }
                        }
                    }
                    guest guest2 = guest.this;
                    guest2.mAdapter = new GuestAdapter(guest2, guest2.upload);
                    guest.this.mRecyclerView.setAdapter(guest.this.mAdapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(guest.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
