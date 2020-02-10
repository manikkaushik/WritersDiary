package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.tenovaters.android.writer.Database.UserList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private static final int GALAARY_INTENT = 1;
    /* access modifiers changed from: private */
    public String Gender;
    /* access modifiers changed from: private */
    public String Name;
    private final String TAG = "abc";
    private File actualImage;
    /* access modifiers changed from: private */

    /* renamed from: ag */
    public EditText f577ag;
    /* access modifiers changed from: private */
    public String age;
    private FloatingActionButton choose;
    /* access modifiers changed from: private */
    public File compressedImage;
    /* access modifiers changed from: private */
    public String currentUser;
    /* access modifiers changed from: private */
    public DatabaseReference demo1;
    private DatabaseReference demoRef;
    private EditText email;
    private Uri filepath;
    private FirebaseAuth firebaseAuth;
    private ImageView img;
    /* access modifiers changed from: private */
    public DatabaseReference mDataBase;
    private StorageReference mStorageRef;
    /* access modifiers changed from: private */
    public StorageTask mUploadTask;
    /* access modifiers changed from: private */
    public ProgressBar mprogressbar;
    /* access modifiers changed from: private */
    public EditText nam;
    /* access modifiers changed from: private */
    public ProgressBar pro;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public RadioGroup radioGroup;
    private DatabaseReference root1;
    private DatabaseReference rootRef;
    private Button sub;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_profile);
        this.sub = (Button) findViewById(R.id.btn_prosub);
        this.nam = (EditText) findViewById(R.id.et_namee);
        this.f577ag = (EditText) findViewById(R.id.et_age);
        this.email = (EditText) findViewById(R.id.et_profileemail);
        this.choose = (FloatingActionButton) findViewById(R.id.choose);
        this.img = (ImageView) findViewById(R.id.circle_profile);
        this.mprogressbar = (ProgressBar) findViewById(R.id.progress);
        this.pro = (ProgressBar) findViewById(R.id.propho);
        this.radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = this.firebaseAuth.getCurrentUser().getUid();
        String str = "Users";
        this.mStorageRef = FirebaseStorage.getInstance().getReference(str).child(this.currentUser);
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.root1 = FirebaseDatabase.getInstance().getReference();
        this.demoRef = this.rootRef.child(str).child(this.currentUser);
        this.mDataBase = FirebaseDatabase.getInstance().getReference(str).child(this.currentUser);
        this.demo1 = this.root1.child(str).child(this.currentUser);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Uploading your details...");
        this.progressDialog.setMessage("Please wait...");
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.email.setText(this.firebaseAuth.getCurrentUser().getEmail());
        this.email.setEnabled(false);
        this.choose.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                ProfileActivity.this.startActivityForResult(intent, 1);
            }
        });
        this.sub.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProfileActivity profileActivity = ProfileActivity.this;
                profileActivity.Name = profileActivity.nam.getText().toString();
                ProfileActivity profileActivity2 = ProfileActivity.this;
                profileActivity2.age = profileActivity2.f577ag.getText().toString();
                int y = 0;
                if (ProfileActivity.this.haveNetwork().booleanValue()) {
                    int selectedId = ProfileActivity.this.radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.female) {
                        y = 1;
                        ProfileActivity.this.Gender = "Female";
                    } else if (selectedId == R.id.male) {
                        y = 1;
                        ProfileActivity.this.Gender = "Male";
                    }
                    if (TextUtils.isEmpty(ProfileActivity.this.Name) || TextUtils.isEmpty(ProfileActivity.this.age) || y != 1) {
                        Toast.makeText(ProfileActivity.this, "Enter the details", Toast.LENGTH_SHORT).show();
                    } else if (ProfileActivity.this.mUploadTask == null || !ProfileActivity.this.mUploadTask.isInProgress()) {
                        ProfileActivity.this.demo1.child(MessengerShareContentUtility.MEDIA_IMAGE).setValue("null");
                        ProfileActivity.this.compressImage();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "NO INTERNET", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void compressImage() {
        if (this.actualImage == null) {
            this.progressDialog.show();
            UserList userList = new UserList(this.Name, this.currentUser, this.age, "null", this.Gender);
            this.mDataBase.setValue(userList).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    Toast.makeText(ProfileActivity.this, "Details uploaded", Toast.LENGTH_SHORT).show();
                    ProfileActivity.this.progressDialog.dismiss();
                    ProfileActivity profileActivity = ProfileActivity.this;
                    profileActivity.startActivity(new Intent(profileActivity, HomeActivity.class));
                    ProfileActivity.this.finish();
                }
            });
            return;
        }
        new Compressor(this).compressToFileAsFlowable(this.actualImage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            public void accept(File file) {
                ProfileActivity.this.compressedImage = file;
                ProfileActivity.this.setCompressedImage();
            }
        }, new Consumer<Throwable>() {
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(ProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void print(String str) {
        Log.d("abc", str);
    }

    public void SaveFile() {
        String str = "onPictureTaken - wrote to ";
        Bitmap bitmap = ((BitmapDrawable) this.img.getDrawable()).getBitmap();
        print("Creating cw");
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        print("Creating dir");
        File directory = cw.getDir("ImagesDir", 0);
        StringBuilder sb = new StringBuilder();
        sb.append("Created dir");
        sb.append(directory);
        print(sb.toString());
        File mypath = new File(directory, "myImagesDGS.jpg");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("path is");
        sb2.append(mypath);
        print(sb2.toString());
        try {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(mypath);
            sb3.append("/Android/data/com.tenovaters.android.writer/Images");
            File dir = new File(sb3.toString());
            dir.mkdirs();
            File outFile = new File(dir, String.format("%d.jpg", new Object[]{Long.valueOf(System.currentTimeMillis())}));
            FileOutputStream outStream = new FileOutputStream(outFile);
            bitmap.compress(CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Context applicationContext = getApplicationContext();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(outFile.getAbsolutePath());
            Toast.makeText(applicationContext, sb4.toString(), Toast.LENGTH_SHORT).show();
            this.filepath = Uri.fromFile(outFile);
            uploadFile();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(outFile.getAbsolutePath());
            Log.d("abc", sb5.toString());
        } catch (FileNotFoundException e) {
            print("FNF");
            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e2) {
            Toast.makeText(getApplicationContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void setCompressedImage() {
        this.img.setImageBitmap(BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath()));
        SaveFile();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            this.filepath = data.getData();
            if (data == null) {
                Toast.makeText(this, "Failed to open picture!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                this.actualImage = FileUtil.from(this, data.getData());
                this.img.setImageBitmap(BitmapFactory.decodeFile(this.actualImage.getAbsolutePath()));
            } catch (IOException e) {
                Toast.makeText(this, "Failed to read picture data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    private void uploadFile() {
        if (this.filepath != null) {
            this.choose.setEnabled(false);
            this.pro.setVisibility(View.VISIBLE);
            this.progressDialog.show();
            StorageReference storageReference = this.mStorageRef;
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            sb.append(".");
            sb.append(getFileExtension(this.filepath));
            final StorageReference fileReference = storageReference.child(sb.toString());
            this.mUploadTask = fileReference.putFile(this.filepath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ProfileActivity.this.mprogressbar.setProgress(0);
                        }
                    }, 500);
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            UserList userList = new UserList(ProfileActivity.this.Name, ProfileActivity.this.currentUser, ProfileActivity.this.age, uri.toString(), ProfileActivity.this.Gender);
                            ProfileActivity.this.mDataBase.setValue(userList);
                            ProfileActivity.this.pro.setVisibility(View.GONE);
                            ProfileActivity.this.progressDialog.dismiss();
                            ProfileActivity.this.pic();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    ProfileActivity.this.pro.setVisibility(View.GONE);
                }
            }).addOnProgressListener((OnProgressListener) new OnProgressListener<TaskSnapshot>() {
                public void onProgress(TaskSnapshot taskSnapshot) {
                    double bytesTransferred = (double) taskSnapshot.getBytesTransferred();
                    Double.isNaN(bytesTransferred);
                    double d = bytesTransferred * 100.0d;
                    double totalByteCount = (double) taskSnapshot.getTotalByteCount();
                    Double.isNaN(totalByteCount);
                    ProfileActivity.this.mprogressbar.setProgress((int) (d / totalByteCount));
                }
            });
            return;
        }
        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
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

    public void pic() {
        this.demoRef.child(MessengerShareContentUtility.MEDIA_IMAGE).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (((String) dataSnapshot.getValue(String.class)) != null) {
                    Toast.makeText(ProfileActivity.this, "pic is uploaded", Toast.LENGTH_SHORT).show();
                    ProfileActivity profileActivity = ProfileActivity.this;
                    profileActivity.startActivity(new Intent(profileActivity, HomeActivity.class));
                    ProfileActivity.this.finish();
                    return;
                }
                ProfileActivity.this.pic();
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
