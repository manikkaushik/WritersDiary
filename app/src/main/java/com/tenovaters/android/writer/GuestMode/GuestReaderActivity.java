package com.tenovaters.android.writer.GuestMode;

import android.content.DialogInterface;
import android.content.Intent;
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

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog.Builder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.StoryActivity;

import java.util.ArrayList;
import java.util.List;

public class GuestReaderActivity extends AppCompatActivity {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public int f561a = 0;
    private DatabaseReference authorRef;

    /* renamed from: b */
    private int f562b = 0;

    /* renamed from: b3 */
    private Button f563b3;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    /* access modifiers changed from: private */
    public DatabaseReference demoRef;
    /* access modifiers changed from: private */

    /* renamed from: id */
    public String f564id;
    private ImageView img;
    /* access modifiers changed from: private */
    public ImageView img2;
    /* access modifiers changed from: private */
    public CategoryAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    private DatabaseReference rootRef;

    /* renamed from: t1 */
    private TextView f565t1;

    /* renamed from: t3 */
    private TextView f566t3;
    /* access modifiers changed from: private */

    /* renamed from: t4 */
    public TextView f567t4;

    /* renamed from: t5 */
    private TextView f568t5;

    /* renamed from: t6 */
    private TextView f569t6;
    /* access modifiers changed from: private */

    /* renamed from: t7 */
    public TextView f570t7;
    /* access modifiers changed from: private */
    public List<CategoryList> upload;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_guest_reader);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.f565t1 = (TextView) findViewById(R.id.tv_readersTitle);
        this.f566t3 = (TextView) findViewById(R.id.tv_readersdescription);
        this.f567t4 = (TextView) findViewById(R.id.tv_readersauthorname);
        this.f568t5 = (TextView) findViewById(R.id.tv_viewsreader);
        this.f569t6 = (TextView) findViewById(R.id.tv_commentreader);
        this.f570t7 = (TextView) findViewById(R.id.tv_ratingreader);
        this.img = (ImageView) findViewById(R.id.img_collapsing);
        this.img2 = (ImageView) findViewById(R.id.img_authorimage);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_reader);
        this.f563b3 = (Button) findViewById(R.id.btn_readStory);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        String title = getIntent().getStringExtra("Title");
        String desrciption = getIntent().getStringExtra("Description");
        final String authorName = getIntent().getStringExtra("Auther Name");
        String imageurl = getIntent().getStringExtra("Image");
        String views = getIntent().getStringExtra("Views");
        String comment = getIntent().getStringExtra("Comment");
        final String authorid = getIntent().getStringExtra("Author Id");
        this.f564id = getIntent().getStringExtra("ID");
        this.f565t1.setText(title);
        this.f566t3.setText(desrciption);
        this.f567t4.setText(authorName);
        this.f568t5.setText(views);
        this.f569t6.setText(comment);
        Picasso.with(this).load(imageurl).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(this.img);
        this.collapsingToolbarLayout.setTitle(authorName);
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        String str = "Readers";
        this.demoRef = this.rootRef.child(str);
        this.authorRef = this.rootRef.child("Users");
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.upload = new ArrayList<>();
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GuestReaderActivity.this.finish();
                }
            });
            builder.create().show();
        } else {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str).child(authorid).child(this.f564id);
            this.mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GuestReaderActivity.this.upload.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        GuestReaderActivity.this.upload.add((CategoryList) postSnapshot.getValue(CategoryList.class));
                    }
                    GuestReaderActivity guestReaderActivity = GuestReaderActivity.this;
                    guestReaderActivity.mAdapter = new CategoryAdapter(guestReaderActivity, guestReaderActivity.upload);
                    GuestReaderActivity.this.mRecyclerView.setAdapter(GuestReaderActivity.this.mAdapter);
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestReaderActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            this.demoRef.child(authorid).child(this.f564id).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String str = "views";
                    String view = (String) dataSnapshot.child(str).getValue(String.class);
                    String str2 = (String) dataSnapshot.child("rating").getValue(String.class);
                    if (view != null && GuestReaderActivity.this.f561a == 0) {
                        GuestReaderActivity.this.demoRef.child(authorid).child(GuestReaderActivity.this.f564id).child(str).setValue(String.valueOf(Integer.parseInt(view) + 1));
                        GuestReaderActivity.this.f561a = GuestReaderActivity.this.f561a + 1;
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestReaderActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
            this.mDatabaseRef.child("Rating").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int b = 0;
                    int flag = 0;
                    float sumrate = 0.0f;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String rate = (String) postSnapshot.child("rate").getValue(String.class);
                        if (rate != null) {
                            sumrate += Float.parseFloat(rate);
                            flag = 1;
                            b++;
                        }
                    }
                    if (flag == 1) {
                        GuestReaderActivity.this.f570t7.setText(String.valueOf(sumrate / ((float) b)));
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestReaderActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            this.authorRef.child(authorid).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                    String name = (String) dataSnapshot.child("name").getValue(String.class);
                    if (image != null) {
                        Picasso.with(GuestReaderActivity.this).load(image).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(GuestReaderActivity.this.img2);
                    }
                    if (name != null) {
                        GuestReaderActivity.this.f567t4.setText(name);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestReaderActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        this.f563b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GuestReaderActivity.this, StoryActivity.class);
                intent.putExtra("Id", GuestReaderActivity.this.f564id);
                intent.putExtra("authorId", authorid);
                GuestReaderActivity.this.startActivity(intent);
            }
        });
        this.img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GuestReaderActivity.this, GuestAuthorDetailsActivity.class);
                intent.putExtra("Auther Name", authorName);
                intent.putExtra("AuthorId", authorid);
                GuestReaderActivity.this.startActivity(intent);
            }
        });
        this.f567t4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GuestReaderActivity.this, GuestAuthorDetailsActivity.class);
                intent.putExtra("AuthorId", authorid);
                intent.putExtra("Auther Name", authorName);
                GuestReaderActivity.this.startActivity(intent);
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
