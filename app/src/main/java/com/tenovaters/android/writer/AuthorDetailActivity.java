package com.tenovaters.android.writer;

import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;

import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Adapters.StoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.ReadersList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog.Builder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AuthorDetailActivity extends AppCompatActivity {

    private DatabaseReference authorRef;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    /* access modifiers changed from: private */
    public ImageView img;
    /* access modifiers changed from: private */
    public CategoryAdapter mAdapter;
    /* access modifiers changed from: private */
    public StoryAdapter mAdapter1;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView1;
    private DatabaseReference rootRef;
    public TextView f548t1;
    public TextView f549t2;
    public List<CategoryList> upload;
    public List<ReadersList> upload1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_author_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.img = (ImageView) findViewById(R.id.img_authordetal_collapsing);
        this.f548t1 = (TextView) findViewById(R.id.tv_authordescrip);
        this.f549t2 = (TextView) findViewById(R.id.tv_nointerest);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_authorinterest);
        this.mRecyclerView1 = (RecyclerView) findViewById(R.id.recycler_morestrories);
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        String str = "Users";
        this.authorRef = this.rootRef.child(str);
        final String authorid = getIntent().getStringExtra("AuthorId");
        this.collapsingToolbarLayout.setTitle(getIntent().getStringExtra("Auther Name"));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.upload = new ArrayList();
        this.upload1 = new ArrayList();
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AuthorDetailActivity.this.finish();
                }
            });
            builder.create().show();
            return;
        }
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str).child(authorid);
        this.mDatabaseRef.child("Interests").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                AuthorDetailActivity.this.upload.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AuthorDetailActivity.this.upload.add((CategoryList) postSnapshot.getValue(CategoryList.class));
                }
                if (AuthorDetailActivity.this.upload.isEmpty()) {
                    AuthorDetailActivity.this.f549t2.setVisibility(View.VISIBLE);
                }
                AuthorDetailActivity authorDetailActivity = AuthorDetailActivity.this;
                authorDetailActivity.mAdapter = new CategoryAdapter(authorDetailActivity, authorDetailActivity.upload);
                AuthorDetailActivity.this.mRecyclerView.setAdapter(AuthorDetailActivity.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AuthorDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        String str2 = "Published";
        this.mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                AuthorDetailActivity.this.upload1.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                        String staus = workersdatabase.getStatus();
                        if (authorid.equals(workersdatabase.getAuthorid()) && "Published".equals(staus)) {
                            AuthorDetailActivity.this.upload1.add(workersdatabase);
                        }
                    }
                }
                if (AuthorDetailActivity.this.upload.isEmpty()) {
                    AuthorDetailActivity.this.f549t2.setVisibility(View.VISIBLE);
                }
                AuthorDetailActivity authorDetailActivity = AuthorDetailActivity.this;
                authorDetailActivity.mAdapter1 = new StoryAdapter(authorDetailActivity, authorDetailActivity.upload1);
                AuthorDetailActivity.this.mRecyclerView1.setAdapter(AuthorDetailActivity.this.mAdapter1);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AuthorDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.authorRef.child(authorid).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                String description = (String) dataSnapshot.child("description").getValue(String.class);
                if (image != null) {
                    Picasso.with(AuthorDetailActivity.this).load(image).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(AuthorDetailActivity.this.img);
                }
                if (description != null) {
                    AuthorDetailActivity.this.f548t1.setText(description);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AuthorDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean haveNetwork() {
        NetworkInfo[] networkInfos;
        boolean have_MobileData = false;
        boolean z = false;
        boolean have_WIFI = false;
        for (NetworkInfo info : ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (info.getTypeName().equalsIgnoreCase("WIFI") && info.isConnected()) {
                have_WIFI = true;
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE") && info.isConnected()) {
                have_MobileData = true;
            }
        }
        if (have_MobileData || have_WIFI) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
