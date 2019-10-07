package com.tenovaters.android.writer;

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

import java.util.ArrayList;
import java.util.List;

public class guest extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private CategoryAdapter mAdapter;
    private List<CategoryList> upload;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        mRecyclerView = (RecyclerView)findViewById(R.id.guest_recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(guest.this));
        upload = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader");
        mDatabaseRef.child("1234567890101112131415").child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                upload.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CategoryList workersdatabase = postSnapshot.getValue(CategoryList.class);
                    upload.add(workersdatabase);

                }
                mAdapter = new CategoryAdapter(guest.this,upload);
                Toast.makeText(guest.this, "run", Toast.LENGTH_SHORT).show();
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(guest.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }
}
