package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class StoryActivity extends AppCompatActivity {
    private String authorid;
    private FirebaseAuth firebaseAuth;

    /* renamed from: id */
    private String f604id;
    PDFView pdf;
    private DatabaseReference rootRef;

    class RetrievePDFbyte extends AsyncTask<String, Void, byte[]> {
        ProgressDialog progressDialog;

        RetrievePDFbyte() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = new ProgressDialog(StoryActivity.this);
            this.progressDialog.setTitle("getting the book content...");
            this.progressDialog.setMessage("Please wait...");
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.show();
        }

        /* access modifiers changed from: protected */
        public byte[] doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(strings[0]).openConnection();
                if (httpsURLConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                }
                try {
                    return IOUtils.toByteArray(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e2) {
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(byte[] bytes) {
            StoryActivity.this.pdf.fromBytes(bytes).load();
            this.progressDialog.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_story);
        this.rootRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.f604id = getIntent().getStringExtra("Id");
        this.authorid = getIntent().getStringExtra("authorId");
        this.pdf = (PDFView) findViewById(R.id.pdf_viewrer);
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    StoryActivity.this.finish();
                }
            });
            builder.create().show();
            return;
        }
        this.rootRef.child(this.authorid).child(this.f604id).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String story = (String) dataSnapshot.child("story").getValue(String.class);
                if (story != null) {
                    new RetrievePDFbyte().execute(new String[]{story});
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
