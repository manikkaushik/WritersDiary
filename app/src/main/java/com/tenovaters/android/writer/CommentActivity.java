package com.tenovaters.android.writer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CommentAdapter;
import com.tenovaters.android.writer.Database.CommentList;
import com.tenovaters.android.writer.Database.NotificationList;

import androidx.appcompat.app.AlertDialog.Builder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    public DatabaseReference NotificationRef;
    /* access modifiers changed from: private */
    public CommentAdapter adapter;
    /* access modifiers changed from: private */
    public String commentnumber;
    private DatabaseReference demoRef;
    public EditText f550e1;
    private FirebaseAuth firebaseAuth;
    public ImageView f551i1;
    private ImageView f552i2;
    public String f553id;
    public DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */
    public List<CommentList> mupload;
    ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private DatabaseReference rootRef;
    /* access modifiers changed from: private */

    /* renamed from: t1 */
    public TextView f554t1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_comment);
        this.f551i1 = (ImageView) findViewById(R.id.iv_user_comment);
        this.f552i2 = (ImageView) findViewById(R.id.iv_send_comment);
        this.f550e1 = (EditText) findViewById(R.id.et_comment2);
        this.f554t1 = (TextView) findViewById(R.id.tv_nocommentadapter);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_comment);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.f553id = getIntent().getStringExtra("Id");
        final String authorid = getIntent().getStringExtra("authorId");
        final String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.demoRef = this.rootRef.child("Users").child(currentUser);
        this.NotificationRef = this.rootRef.child("Notifications").child(authorid).child(this.f553id).child(currentUser).child("comment");
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Posting Your Comment");
        this.progressDialog.setMessage("Posting");
        this.mupload = new ArrayList();
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CommentActivity.this.finish();
                }
            });
            builder.create().show();
        } else {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers").child(authorid).child(this.f553id);
            this.mDatabaseRef.child("Comments").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CommentActivity.this.mupload.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CommentActivity.this.mupload.add((CommentList) postSnapshot.getValue(CommentList.class));
                    }
                    if (CommentActivity.this.mupload.isEmpty()) {
                        CommentActivity.this.f554t1.setVisibility(View.VISIBLE);
                    }
                    if (!CommentActivity.this.mupload.isEmpty()) {
                        CommentActivity.this.f554t1.setVisibility(View.INVISIBLE);
                    }
                    CommentActivity commentActivity = CommentActivity.this;
                    commentActivity.adapter = new CommentAdapter(commentActivity, commentActivity.mupload);
                    CommentActivity.this.recyclerView.setAdapter(CommentActivity.this.adapter);
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CommentActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CommentActivity.this.commentnumber = (String) dataSnapshot.child("comment").getValue(String.class);
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CommentActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
            this.demoRef.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageurl = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                    if (imageurl != null) {
                        Picasso.with(CommentActivity.this).load(imageurl).fit().centerCrop().placeholder((int) R.drawable.picture1).into(CommentActivity.this.f551i1);
                    } else {
                        Toast.makeText(CommentActivity.this, "No Profile Picture", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CommentActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        this.f552i2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String comment = CommentActivity.this.f550e1.getText().toString();
                if (comment.isEmpty()) {
                    Toast.makeText(CommentActivity.this, "Write the comment", Toast.LENGTH_SHORT).show();
                } else if (!CommentActivity.this.haveNetwork().booleanValue()) {
                    Toast.makeText(CommentActivity.this, "No internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    CommentActivity.this.progressDialog.show();
                    Date today = new Date();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                    String dateToStr = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(today);
                    String dateformat1 = dateformat.format(today);
                    String dateformat2 = timeformat.format(today);
                    StringBuilder sb = new StringBuilder();
                    sb.append(dateformat1);
                    sb.append(" at ");
                    sb.append(dateformat2);
                    CommentActivity.this.mDatabaseRef.child("Comments").child(dateToStr).setValue(new CommentList(comment, currentUser, sb.toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(Task<Void> task) {
                            CommentActivity.this.commentnumber = String.valueOf(Integer.parseInt(CommentActivity.this.commentnumber) + 1);
                            CommentActivity.this.mDatabaseRef.child("comment").setValue(CommentActivity.this.commentnumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    CommentActivity.this.progressDialog.dismiss();
                                    CommentActivity.this.f550e1.setText("");
                                    NotificationList notificationList = new NotificationList("Commented on your", CommentActivity.this.f553id, currentUser, new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date()), authorid);
                                    CommentActivity.this.NotificationRef.setValue(notificationList);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(Exception e) {
                                    CommentActivity.this.progressDialog.dismiss();
                                    Toast.makeText(CommentActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        public void onFailure(Exception e) {
                            CommentActivity.this.progressDialog.dismiss();
                            Toast.makeText(CommentActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public Boolean haveNetwork() {
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
