package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Database.CategoryList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PublishStoryActivity extends AppCompatActivity {
    private static final String TAG = "PdfCreatorActivity";
    private final int REQUEST_CODE_ASK_PERMISSIONS = 111;
    /* access modifiers changed from: private */

    /* renamed from: b1 */
    public Button f578b1;
    /* access modifiers changed from: private */

    /* renamed from: b2 */
    public Button f579b2;
    /* access modifiers changed from: private */
    public String category;
    /* access modifiers changed from: private */
    public String currentUser;
    /* access modifiers changed from: private */
    public DatabaseReference demoRef;
    /* access modifiers changed from: private */
    public String description;
    private FirebaseAuth firebaseAuth;

    /* renamed from: g */
    int f580g = 0;
    /* access modifiers changed from: private */
    public String genere;
    /* access modifiers changed from: private */
    public String imageurl;
    /* access modifiers changed from: private */
    public ImageView img;
    /* access modifiers changed from: private */
    public CategoryAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    StorageReference mStorageReference;
    private File pdfFile;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    private DatabaseReference rootRef;
    /* access modifiers changed from: private */
    public String story;
    /* access modifiers changed from: private */
    public String story_id;
    /* access modifiers changed from: private */

    /* renamed from: t1 */
    public TextView f581t1;

    /* renamed from: t2 */
    private TextView f582t2;
    /* access modifiers changed from: private */

    /* renamed from: t3 */
    public TextView f583t3;
    /* access modifiers changed from: private */

    /* renamed from: t4 */
    public TextView f584t4;
    /* access modifiers changed from: private */
    public String title;
    private Toolbar toolbar;
    /* access modifiers changed from: private */
    public List<CategoryList> upload;

    /* renamed from: z */
    int f585z = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_publish_story);
        this.toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        String str = "Readers";
        this.demoRef = this.rootRef.child(str);
        this.toolbar.setTitle((CharSequence) "Publish");
        this.story_id = getIntent().getStringExtra("Id");
        this.title = getIntent().getStringExtra("Title");
        this.f581t1 = (TextView) findViewById(R.id.tv_1);
        this.f582t2 = (TextView) findViewById(R.id.tv_2);
        this.f583t3 = (TextView) findViewById(R.id.tv_genere_publish);
        this.f584t4 = (TextView) findViewById(R.id.tv_description_publish);
        this.img = (ImageView) findViewById(R.id.circle_profile_publish);
        this.f578b1 = (Button) findViewById(R.id.btn_previewpdf);
        this.f579b2 = (Button) findViewById(R.id.btn_uploadpdf);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_publish);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.mStorageReference = FirebaseStorage.getInstance().getReference("Readers Story").child(this.currentUser).child(this.story_id);
        this.progressBar = (ProgressBar) findViewById(R.id.pro_publish);
        this.f578b1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PublishStoryActivity.this, PreviewPDFActivity.class);
                intent.putExtra("Id", PublishStoryActivity.this.story_id);
                intent.putExtra("Title", PublishStoryActivity.this.title);
                PublishStoryActivity.this.startActivity(intent);
            }
        });
        this.f579b2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!PublishStoryActivity.this.haveNetwork().booleanValue()) {
                    Toast.makeText(PublishStoryActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                } else {
                    PublishStoryActivity.this.uploadFile();
                }
            }
        });
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str).child(this.currentUser).child(this.story_id);
        this.upload = new ArrayList();
        if (!haveNetwork().booleanValue()) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PublishStoryActivity.this.finish();
                }
            });
            builder.create().show();
            return;
        }
        this.mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (PublishStoryActivity.this.f580g == 0) {
                    PublishStoryActivity.this.upload.clear();
                    PublishStoryActivity.this.f580g = 1;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        PublishStoryActivity.this.upload.add((CategoryList) postSnapshot.getValue(CategoryList.class));
                    }
                    PublishStoryActivity publishStoryActivity = PublishStoryActivity.this;
                    publishStoryActivity.mAdapter = new CategoryAdapter(publishStoryActivity, publishStoryActivity.upload);
                    PublishStoryActivity.this.mRecyclerView.setAdapter(PublishStoryActivity.this.mAdapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PublishStoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.demoRef.child(this.currentUser).child(this.story_id).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (PublishStoryActivity.this.f585z == 0) {
                    PublishStoryActivity.this.title = (String) dataSnapshot.child("title").getValue(String.class);
                    PublishStoryActivity.this.imageurl = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                    PublishStoryActivity.this.story = (String) dataSnapshot.child("story").getValue(String.class);
                    PublishStoryActivity.this.description = (String) dataSnapshot.child("description").getValue(String.class);
                    PublishStoryActivity.this.genere = (String) dataSnapshot.child("genere").getValue(String.class);
                    PublishStoryActivity.this.category = (String) dataSnapshot.child("category").getValue(String.class);
                    PublishStoryActivity publishStoryActivity = PublishStoryActivity.this;
                    publishStoryActivity.f585z = 1;
                    String str = "testing";
                    String str2 = "";
                    if (publishStoryActivity.description == null || PublishStoryActivity.this.description.equals(str2) || PublishStoryActivity.this.genere == null || PublishStoryActivity.this.imageurl == null || PublishStoryActivity.this.imageurl.equals(str) || PublishStoryActivity.this.imageurl.equals(str2) || PublishStoryActivity.this.title == null || PublishStoryActivity.this.title.equals(str2) || PublishStoryActivity.this.story == null || PublishStoryActivity.this.category == null || PublishStoryActivity.this.category.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) || PublishStoryActivity.this.category.equals(str2)) {
                        PublishStoryActivity.this.f579b2.setEnabled(false);
                        Toast.makeText(PublishStoryActivity.this, "Complete Your Story Details", Toast.LENGTH_LONG).show();
                    }
                    if (PublishStoryActivity.this.story == null || PublishStoryActivity.this.story.equals(str2)) {
                        PublishStoryActivity.this.f578b1.setEnabled(false);
                    }
                    if (PublishStoryActivity.this.description != null && !PublishStoryActivity.this.description.equals(str2)) {
                        PublishStoryActivity.this.f584t4.setText(PublishStoryActivity.this.description);
                    }
                    if (PublishStoryActivity.this.genere != null) {
                        PublishStoryActivity.this.f583t3.setText(PublishStoryActivity.this.genere);
                    }
                    if (PublishStoryActivity.this.imageurl != null && !PublishStoryActivity.this.imageurl.equals(str) && !PublishStoryActivity.this.imageurl.equals(str2)) {
                        Picasso.with(PublishStoryActivity.this).load(PublishStoryActivity.this.imageurl).fit().centerCrop().placeholder((int) R.drawable.no_preview).into(PublishStoryActivity.this.img);
                    }
                    if (PublishStoryActivity.this.title != null && !PublishStoryActivity.this.title.equals(str2)) {
                        PublishStoryActivity.this.f581t1.setText(PublishStoryActivity.this.title);
                    }
                    if (PublishStoryActivity.this.story != null && !PublishStoryActivity.this.story.equals(str2)) {
                        try {
                            PublishStoryActivity.this.progressDialog = new ProgressDialog(PublishStoryActivity.this);
                            PublishStoryActivity.this.progressDialog.setTitle("Generating your book content...");
                            PublishStoryActivity.this.progressDialog.setMessage("Please wait...");
                            PublishStoryActivity.this.progressDialog.setCanceledOnTouchOutside(false);
                            PublishStoryActivity.this.progressDialog.show();
                            createPdfWrapper();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(PublishStoryActivity.this, "File Data1 ", Toast.LENGTH_SHORT).show();
                        } catch (DocumentException e2) {
                            Toast.makeText(PublishStoryActivity.this, "File Data2 ", Toast.LENGTH_SHORT).show();
                            e2.printStackTrace();
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PublishStoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void uploadFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Documents/HelloWorld.pdf");
        Uri file = Uri.fromFile(new File(sb.toString()));
        this.progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = this.mStorageReference;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(System.currentTimeMillis());
        sb2.append(".pdf");
        final StorageReference sRef = storageReference.child(sb2.toString());
        sRef.putFile(file).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
            public void onSuccess(TaskSnapshot taskSnapshot) {
                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        PublishStoryActivity.this.progressBar.setVisibility(View.GONE);
                        Toast.makeText(PublishStoryActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("story").setValue(uri.toString());
                        DatabaseReference child = PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("comment");
                        String str = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                        child.setValue(str);
                        PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("likes").setValue(str);
                        PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("views").setValue(str);
                        PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("status").setValue("Published").addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                PublishStoryActivity.this.finish();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception exception) {
                Toast.makeText(PublishStoryActivity.this.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener((OnProgressListener) new OnProgressListener<TaskSnapshot>() {
            public void onProgress(TaskSnapshot taskSnapshot) {
                double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                Double.isNaN(bytesTransferred);
                double d = bytesTransferred * 100.0d;
                double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                double d2 = d / totalByteCount;
            }
        });
    }

    private void uploadFile2() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Documents/HelloWorld.pdf");
        Uri file = Uri.fromFile(new File(sb.toString()));
        StorageReference storageReference = this.mStorageReference;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.title);
        sb2.append(".pdf");
        final StorageReference sRef = storageReference.child(sb2.toString());
        sRef.putFile(file).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
            public void onSuccess(TaskSnapshot taskSnapshot) {
                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        PublishStoryActivity.this.progressDialog.dismiss();
                        Toast.makeText(PublishStoryActivity.this, "Book Generated successful", Toast.LENGTH_LONG).show();
                        PublishStoryActivity.this.demoRef.child(PublishStoryActivity.this.currentUser).child(PublishStoryActivity.this.story_id).child("story_preview").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception exception) {
                Toast.makeText(PublishStoryActivity.this.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener((OnProgressListener) new OnProgressListener<TaskSnapshot>() {
            public void onProgress(TaskSnapshot taskSnapshot) {
                double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                Double.isNaN(bytesTransferred);
                double d = bytesTransferred * 100.0d;
                double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                Double.isNaN(totalByteCount);
                double d2 = d / totalByteCount;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createPdfWrapper() throws FileNotFoundException, DocumentException {
        Toast.makeText(this, "File Dataemnte ", Toast.LENGTH_SHORT).show();
        String str = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (ActivityCompat.checkSelfPermission(this, str) != 0) {
            if (VERSION.SDK_INT >= 23) {
                if (!shouldShowRequestPermissionRationale("android.permission.WRITE_CONTACTS")) {
                    showMessageOKCancel("You need to allow access to Storage", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (VERSION.SDK_INT >= 23) {
                                PublishStoryActivity.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 111);
                            }
                        }
                    });
                    return;
                }
                requestPermissions(new String[]{str}, 111);
            }
            return;
        }
        createPdf();
        Toast.makeText(this, "Creating your PDF", Toast.LENGTH_SHORT).show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 111) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults[0] == 0) {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e2) {
                e2.printStackTrace();
            }
        } else {
            Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new Builder(this).setMessage((CharSequence) message).setPositiveButton((CharSequence) "OK", okListener).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) null).create().show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Documents");
        File docsFolder = new File(sb.toString());
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
        this.pdfFile = new File(docsFolder.getAbsolutePath(), "HelloWorld.pdf");
        OutputStream output = new FileOutputStream(this.pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph(this.story));
        document.close();
        uploadFile2();
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
