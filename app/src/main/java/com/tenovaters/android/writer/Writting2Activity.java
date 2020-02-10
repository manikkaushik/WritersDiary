package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Writting2Activity extends AppCompatActivity {

    /* renamed from: b1 */
    private Button f605b1;

    /* renamed from: b2 */
    private Button f606b2;
    /* access modifiers changed from: private */
    public DatabaseReference demoRef;
    /* access modifiers changed from: private */

    /* renamed from: e1 */
    public EditText f607e1;
    private FirebaseAuth firebaseAuth;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private DatabaseReference rootRef;

    /* renamed from: t1 */
    private TextView f608t1;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_writting2);
        this.toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        this.toolbar.setTitle((CharSequence) "Writting");
        this.f607e1 = (EditText) findViewById(R.id.et_story);
        this.f605b1 = (Button) findViewById(R.id.btn_save);
        this.f606b2 = (Button) findViewById(R.id.btn_publish);
        this.f608t1 = (TextView) findViewById(R.id.tv_title_writting);
        this.firebaseAuth = FirebaseAuth.getInstance();
        final String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.demoRef = this.rootRef.child("Readers");
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Saving Your Story");
        this.progressDialog.setMessage("Please Wait....");
        this.progressDialog.setCanceledOnTouchOutside(false);
        final String story_id = getIntent().getStringExtra("Id");
        final String title = getIntent().getStringExtra("Title");
        if (title == null) {
            this.f608t1.setText("**UNTITLED**");
        }
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Writting2Activity.this.finish();
                }
            });
            builder.create().show();
        } else {
            this.demoRef.child(currentUser).child(story_id).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String str = (String) dataSnapshot.child("title").getValue(String.class);
                    String story = (String) dataSnapshot.child("story").getValue(String.class);
                    if (story != null) {
                        Writting2Activity.this.f607e1.setText(story);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Writting2Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        this.f608t1.setText(title);
        this.f605b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Writting2Activity.this.haveNetwork().booleanValue()) {
                    Toast.makeText(Writting2Activity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Writting2Activity.this.progressDialog.show();
                Writting2Activity.this.demoRef.child(currentUser).child(story_id).child("story").setValue(Writting2Activity.this.f607e1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(Writting2Activity.this, "Saved....", Toast.LENGTH_SHORT).show();
                        Writting2Activity.this.progressDialog.dismiss();
                    }
                });
            }
        });
        this.f606b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Writting2Activity.this.haveNetwork().booleanValue()) {
                    Toast.makeText(Writting2Activity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String story = Writting2Activity.this.f607e1.getText().toString();
                Writting2Activity.this.progressDialog.show();
                Writting2Activity.this.demoRef.child(currentUser).child(story_id).child("story").setValue(story).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        Writting2Activity.this.progressDialog.dismiss();
                        Toast.makeText(Writting2Activity.this, "Saved....", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Writting2Activity.this, PublishStoryActivity.class);
                        intent.putExtra("Id", story_id);
                        intent.putExtra("Title", title);
                        Writting2Activity.this.startActivity(intent);
                        Writting2Activity.this.finish();
                    }
                });
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
