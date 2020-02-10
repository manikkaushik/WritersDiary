package com.tenovaters.android.writer;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.NotificationList;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadersActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public DatabaseReference NotificationRef;
    /* access modifiers changed from: private */

    /* renamed from: a */
    public int f590a = 0;
    private DatabaseReference authorRef;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public int f591b = 0;
    /* access modifiers changed from: private */

    /* renamed from: b1 */
    public Button f592b1;

    /* renamed from: b2 */
    private Button f593b2;

    /* renamed from: b3 */
    private Button f594b3;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    /* access modifiers changed from: private */
    public DatabaseReference demoRef;
    /* access modifiers changed from: private */
    public FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;
    /* access modifiers changed from: private */

    /* renamed from: id */
    public String f595id;
    private ImageView img;
    /* access modifiers changed from: private */
    public ImageView img2;
    /* access modifiers changed from: private */
    public String like;
    /* access modifiers changed from: private */
    public String likes;
    /* access modifiers changed from: private */
    public CategoryAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public RatingBar ratingBar;
    private DatabaseReference rootRef;

    /* renamed from: t1 */
    private TextView f596t1;

    /* renamed from: t3 */
    private TextView f597t3;
    /* access modifiers changed from: private */

    /* renamed from: t4 */
    public TextView f598t4;

    /* renamed from: t5 */
    private TextView f599t5;

    /* renamed from: t6 */
    private TextView f600t6;
    /* access modifiers changed from: private */

    /* renamed from: t7 */
    public TextView f601t7;
    /* access modifiers changed from: private */
    public List<CategoryList> upload;
    /* access modifiers changed from: private */
    public String value;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_readers);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.f596t1 = (TextView) findViewById(R.id.tv_readersTitle);
        this.f597t3 = (TextView) findViewById(R.id.tv_readersdescription);
        this.f598t4 = (TextView) findViewById(R.id.tv_readersauthorname);
        this.f599t5 = (TextView) findViewById(R.id.tv_viewsreader);
        this.f600t6 = (TextView) findViewById(R.id.tv_commentreader);
        this.f601t7 = (TextView) findViewById(R.id.tv_ratingreader);
        this.img = (ImageView) findViewById(R.id.img_collapsing);
        this.img2 = (ImageView) findViewById(R.id.img_authorimage);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_reader);
        this.f592b1 = (Button) findViewById(R.id.btn_like);
        this.f593b2 = (Button) findViewById(R.id.btn_comment);
        this.f594b3 = (Button) findViewById(R.id.btn_readStory);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        String title = getIntent().getStringExtra("Title");
        String desrciption = getIntent().getStringExtra("Description");
        final String authorName = getIntent().getStringExtra("Auther Name");
        String imageurl = getIntent().getStringExtra("Image");
        String views = getIntent().getStringExtra("Views");
        String comment = getIntent().getStringExtra("Comment");
        final String authorid = getIntent().getStringExtra("Author Id");
        this.f595id = getIntent().getStringExtra("ID");
        this.f596t1.setText(title);
        this.f597t3.setText(desrciption);
        this.f598t4.setText(authorName);
        this.f599t5.setText(views);
        this.f600t6.setText(comment);
        Picasso.with(this).load(imageurl).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(this.img);
        this.collapsingToolbarLayout.setTitle(authorName);
        this.firebaseAuth = FirebaseAuth.getInstance();
        final String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        String str = "Readers";
        this.demoRef = this.rootRef.child(str);
        this.authorRef = this.rootRef.child("Users");
        this.NotificationRef = this.rootRef.child("Notifications").child(authorid).child(this.f595id).child(currentUser).child("like");
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.upload = new ArrayList();
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ReadersActivity.this.finish();
                }
            });
            builder.create().show();
        } else {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str).child(authorid).child(this.f595id);
            this.mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ReadersActivity.this.upload.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ReadersActivity.this.upload.add((CategoryList) postSnapshot.getValue(CategoryList.class));
                    }
                    ReadersActivity readersActivity = ReadersActivity.this;
                    readersActivity.mAdapter = new CategoryAdapter(readersActivity, readersActivity.upload);
                    ReadersActivity.this.mRecyclerView.setAdapter(ReadersActivity.this.mAdapter);
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReadersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            this.demoRef.child(authorid).child(this.f595id).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String str = "views";
                    String view = (String) dataSnapshot.child(str).getValue(String.class);
                    ReadersActivity.this.likes = (String) dataSnapshot.child("likes").getValue(String.class);
                    ReadersActivity.this.like = (String) dataSnapshot.child("Likes").child(currentUser).getValue(String.class);
                    String rating = (String) dataSnapshot.child("Rating").child(currentUser).child("rate").getValue(String.class);
                    String str2 = (String) dataSnapshot.child("rating").getValue(String.class);
                    if (view != null && ReadersActivity.this.f590a == 0) {
                        ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str).setValue(String.valueOf(Integer.parseInt(view) + 1));
                        ReadersActivity.this.f590a = ReadersActivity.this.f590a + 1;
                    }
                    if (rating != null) {
                        ReadersActivity.this.ratingBar.setRating(Float.parseFloat(rating));
                    }
                    if (ReadersActivity.this.like != null) {
                        ReadersActivity.this.f592b1.setText("UnLike");
                    } else if (ReadersActivity.this.like == null) {
                        ReadersActivity.this.f592b1.setText("Like");
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                        ReadersActivity.this.f601t7.setText(String.valueOf(sumrate / ((float) b)));
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReadersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            this.authorRef.child(authorid).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                    String name = (String) dataSnapshot.child("name").getValue(String.class);
                    if (image != null) {
                        Picasso.with(ReadersActivity.this).load(image).fit().placeholder((int) R.drawable.com_facebook_profile_picture_blank_portrait).into(ReadersActivity.this.img2);
                    }
                    if (name != null) {
                        ReadersActivity.this.f598t4.setText(name);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
            this.demoRef.child(authorid).child(this.f595id).child("Favourites").child(currentUser).child("favourite").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ReadersActivity.this.value = (String) dataSnapshot.getValue(String.class);
                    ReadersActivity.this.fab.setVisibility(View.VISIBLE);
                    if (ReadersActivity.this.value != null) {
                        ReadersActivity.this.fab.setImageResource(R.drawable.heart_filled1);
                    } else {
                        ReadersActivity.this.fab.setImageResource(R.drawable.heart_hollow1);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        this.ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child("Rating").child(currentUser).child("rate").setValue(String.valueOf(rating));
            }
        });
        this.f594b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ReadersActivity.this, StoryActivity.class);
                intent.putExtra("Id", ReadersActivity.this.f595id);
                intent.putExtra("authorId", authorid);
                ReadersActivity.this.startActivity(intent);
            }
        });
        this.f593b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ReadersActivity.this, CommentActivity.class);
                intent.putExtra("Id", ReadersActivity.this.f595id);
                intent.putExtra("authorId", authorid);
                ReadersActivity.this.startActivity(intent);
            }
        });
        this.img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ReadersActivity.this, AuthorDetailActivity.class);
                intent.putExtra("Auther Name", authorName);
                intent.putExtra("AuthorId", authorid);
                ReadersActivity.this.startActivity(intent);
            }
        });
        this.f598t4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ReadersActivity.this, AuthorDetailActivity.class);
                intent.putExtra("AuthorId", authorid);
                intent.putExtra("Auther Name", authorName);
                ReadersActivity.this.startActivity(intent);
            }
        });
        this.f592b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                String str = "Action";
                String str2 = "likes";
                String str3 = "Likes";
                if (ReadersActivity.this.like != null) {
                    ReadersActivity.this.f591b = 1;
                    ReadersActivity.this.f592b1.setText("Like");
                    ReadersActivity.this.NotificationRef.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (ReadersActivity.this.f591b == 1) {
                                if (ReadersActivity.this.f595id.equals((String) dataSnapshot.child("storyid").getValue(String.class))) {
                                    ReadersActivity.this.NotificationRef.removeValue();
                                    ReadersActivity.this.f591b = 0;
                                }
                                ReadersActivity.this.f591b = 0;
                            }
                            ReadersActivity.this.f591b = 0;
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str3).child(currentUser).removeValue();
                    ReadersActivity.this.likes = String.valueOf(Integer.parseInt(ReadersActivity.this.likes) - 1);
                    ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str2).setValue(ReadersActivity.this.likes);
                    Snackbar.make(view2, (CharSequence) "You UnLiked this story", Snackbar.LENGTH_LONG).setAction((CharSequence) str, (View.OnClickListener) null).show();
                    return;
                }
                ReadersActivity.this.f592b1.setText("UnLike");
                NotificationList notificationList = new NotificationList("Liked Your", ReadersActivity.this.f595id, currentUser, new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date()), authorid);
                ReadersActivity.this.NotificationRef.setValue(notificationList);
                ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str3).child(currentUser).setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                ReadersActivity.this.likes = String.valueOf(Integer.parseInt(ReadersActivity.this.likes) + 1);
                ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str2).setValue(ReadersActivity.this.likes);
                Snackbar.make(view2, (CharSequence) "You Liked this story", Snackbar.LENGTH_LONG).setAction((CharSequence) str, (View.OnClickListener) null).show();
            }
        });
        this.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String str = "Action";
                String str2 = "Favourites";
                if (ReadersActivity.this.value != null) {
                    ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str2).child(currentUser).removeValue();
                    ReadersActivity.this.fab.setImageResource(R.drawable.heart_hollow1);
                    Snackbar.make(view, (CharSequence) "Removed From your Favourites", Snackbar.LENGTH_LONG).setAction((CharSequence) str, (View.OnClickListener) null).show();
                    return;
                }
                ReadersActivity.this.demoRef.child(authorid).child(ReadersActivity.this.f595id).child(str2).child(currentUser).child("favourite").setValue(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                ReadersActivity.this.fab.setImageResource(R.drawable.heart_filled1);
                Snackbar.make(view, (CharSequence) "Added to your Favourites", Snackbar.LENGTH_LONG).setAction((CharSequence) str, (View.OnClickListener) null).show();
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
