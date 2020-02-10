package com.tenovaters.android.writer.GuestMode;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import androidx.appcompat.app.AlertDialog.Builder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.ArrayList;
import java.util.List;

public class GuestAuthorDetailsActivity extends AppCompatActivity {
    private DatabaseReference authorRef;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    /* access modifiers changed from: private */
    public ImageView img;
    /* access modifiers changed from: private */
    public CategoryAdapter mAdapter;
    /* access modifiers changed from: private */
    public GuestStoryAdapter mAdapter1;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView1;
    private DatabaseReference rootRef;
    /* access modifiers changed from: private */

    /* renamed from: t1 */
    public TextView f559t1;
    /* access modifiers changed from: private */

    /* renamed from: t2 */
    public TextView f560t2;
    /* access modifiers changed from: private */
    public List<CategoryList> upload;
    /* access modifiers changed from: private */
    public List<ReadersList> upload1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_guest_author_details);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.img = (ImageView) findViewById(R.id.img_authordetal_collapsing);
        this.f559t1 = (TextView) findViewById(R.id.tv_authordescrip);
        this.f560t2 = (TextView) findViewById(R.id.tv_nointerest);
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
        if (!haveNetwork()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GuestAuthorDetailsActivity.this.finish();
                }
            });
            builder.create().show();
            return;
        }
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str).child(authorid);
        this.mDatabaseRef.child("Interests").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                GuestAuthorDetailsActivity.this.upload.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GuestAuthorDetailsActivity.this.upload.add((CategoryList) postSnapshot.getValue(CategoryList.class));
                }
                if (GuestAuthorDetailsActivity.this.upload.isEmpty()) {
                    GuestAuthorDetailsActivity.this.f560t2.setVisibility(View.VISIBLE);
                }
                GuestAuthorDetailsActivity guestAuthorDetailsActivity = GuestAuthorDetailsActivity.this;
                guestAuthorDetailsActivity.mAdapter = new CategoryAdapter(guestAuthorDetailsActivity, guestAuthorDetailsActivity.upload);
                GuestAuthorDetailsActivity.this.mRecyclerView.setAdapter(GuestAuthorDetailsActivity.this.mAdapter);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GuestAuthorDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        String str2 = "Published";
        this.mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                GuestAuthorDetailsActivity.this.upload1.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        ReadersList workersdatabase = (ReadersList) postSnapshot1.getValue(ReadersList.class);
                        String staus = workersdatabase.getStatus();
                        if (authorid.equals(workersdatabase.getAuthorid()) && "Published".equals(staus)) {
                            GuestAuthorDetailsActivity.this.upload1.add(workersdatabase);
                        }
                    }
                }
                if (GuestAuthorDetailsActivity.this.upload1.isEmpty()) {
                    GuestAuthorDetailsActivity.this.f560t2.setVisibility(View.VISIBLE);
                }
                GuestAuthorDetailsActivity guestAuthorDetailsActivity = GuestAuthorDetailsActivity.this;
                guestAuthorDetailsActivity.mAdapter1 = new GuestStoryAdapter(guestAuthorDetailsActivity, guestAuthorDetailsActivity.upload1);
                GuestAuthorDetailsActivity.this.mRecyclerView1.setAdapter(GuestAuthorDetailsActivity.this.mAdapter1);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GuestAuthorDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.authorRef.child(authorid).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                String description = (String) dataSnapshot.child("description").getValue(String.class);
                if (image != null) {
                    Picasso.with(GuestAuthorDetailsActivity.this).load(image).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(GuestAuthorDetailsActivity.this.img);
                }
                if (description != null) {
                    GuestAuthorDetailsActivity.this.f559t1.setText(description);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GuestAuthorDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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

