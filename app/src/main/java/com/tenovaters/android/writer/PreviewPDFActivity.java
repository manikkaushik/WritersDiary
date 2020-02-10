package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
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

public class PreviewPDFActivity extends AppCompatActivity {
    private String currentUser;
    private FirebaseAuth firebaseAuth;

    /* renamed from: id */
    private String f575id;
    PDFView pdf;
    private DatabaseReference rootRef;

    /* renamed from: z */
    int f576z = 0;

    class RetrievePDFbyte extends AsyncTask<String, Void, byte[]> {
        ProgressDialog progressDialog;

        RetrievePDFbyte() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = new ProgressDialog(PreviewPDFActivity.this);
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
            PreviewPDFActivity.this.pdf.fromBytes(bytes).load();
            this.progressDialog.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_preview_pdf);
        this.rootRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.f575id = getIntent().getStringExtra("Id");
        this.pdf = (PDFView) findViewById(R.id.pdf_viewer);
        this.rootRef.child(this.currentUser).child(this.f575id).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (PreviewPDFActivity.this.f576z == 0) {
                    PreviewPDFActivity.this.f576z = 1;
                    String story = (String) dataSnapshot.child("story_preview").getValue(String.class);
                    if (story != null) {
                        new RetrievePDFbyte().execute(new String[]{story});
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PreviewPDFActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
