package com.tenovaters.android.writer;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import com.tenovaters.android.writer.Database.ReadersList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import id.zelory.compressor.Compressor;

public class NewWrittingActivity extends AppCompatActivity implements OnClickListener {
    private static final int GALAARY_INTENT = 1;
    /* access modifiers changed from: private */
    public String Genere;
    private RadioButton Poem;
    private RadioButton Story;
    private final String TAG = "abc";
    private RadioButton action1;
    private RadioButton action2;
    private File actualImage;
    private Button btn;
    private RadioButton children;
    private RadioButton classic;
    private RadioButton comedy;
    /* access modifiers changed from: private */
    public File compressedImage;
    /* access modifiers changed from: private */
    public int count = 0;
    /* access modifiers changed from: private */
    public String count1;
    private RadioButton crime;
    /* access modifiers changed from: private */
    public String currentUser;
    private DatabaseReference databaseArtist;
    /* access modifiers changed from: private */
    public DatabaseReference demoref;
    /* access modifiers changed from: private */
    public String description;
    private RadioButton drama;
    /* access modifiers changed from: private */
    public EditText et1;
    /* access modifiers changed from: private */
    public EditText et2;
    private RadioButton fantasy;
    private Uri filepath;
    private FirebaseAuth firebaseAuth;
    private RadioButton horror;
    private String image;
    private ImageView img;
    private RadioButton inspirational;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private RadioButton other;
    /* access modifiers changed from: private */
    public ProgressBar pro;
    /* access modifiers changed from: private */
    public RadioGroup radioGroup;
    private RadioButton romance;
    private DatabaseReference rootref;
    private RadioButton thriller;
    /* access modifiers changed from: private */
    public String title;
    private Toolbar toolbar;
    private RadioButton tragedy;
    /* access modifiers changed from: private */
    public String userid;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_new_writting);
        this.toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        this.btn = (Button) findViewById(R.id.btn_next);
        this.et1 = (EditText) findViewById(R.id.et_title);
        this.et2 = (EditText) findViewById(R.id.et_description);
        this.img = (ImageView) findViewById(R.id.img_story);
        this.toolbar.setTitle((CharSequence) "Information");
        this.pro = (ProgressBar) findViewById(R.id.pro_writting);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mStorageRef = FirebaseStorage.getInstance().getReference("Writting").child(this.currentUser);
        this.databaseArtist = FirebaseDatabase.getInstance().getReference("Readers");
        this.userid = this.databaseArtist.push().getKey();
        this.rootref = this.databaseArtist.child(this.currentUser);
        this.demoref = this.databaseArtist.child(this.currentUser);
        this.radioGroup = (RadioGroup) findViewById(R.id.radiogroup_story);
        this.Story = (RadioButton) findViewById(R.id.story_story);
        this.Poem = (RadioButton) findViewById(R.id.poem_story);
        this.action1 = (RadioButton) findViewById(R.id.abstarct1);
        this.action2 = (RadioButton) findViewById(R.id.action1);
        this.children = (RadioButton) findViewById(R.id.children);
        this.classic = (RadioButton) findViewById(R.id.classic);
        this.comedy = (RadioButton) findViewById(R.id.comedy);
        this.crime = (RadioButton) findViewById(R.id.crime);
        this.fantasy = (RadioButton) findViewById(R.id.fantasy);
        this.horror = (RadioButton) findViewById(R.id.horror);
        this.inspirational = (RadioButton) findViewById(R.id.inspirational);
        this.romance = (RadioButton) findViewById(R.id.romance);
        this.thriller = (RadioButton) findViewById(R.id.thriller);
        this.tragedy = (RadioButton) findViewById(R.id.tragedy);
        this.drama = (RadioButton) findViewById(R.id.drama);
        this.other = (RadioButton) findViewById(R.id.other);
        this.action1.setOnClickListener(this);
        this.action2.setOnClickListener(this);
        this.children.setOnClickListener(this);
        this.classic.setOnClickListener(this);
        this.comedy.setOnClickListener(this);
        this.crime.setOnClickListener(this);
        this.drama.setOnClickListener(this);
        this.fantasy.setOnClickListener(this);
        this.horror.setOnClickListener(this);
        this.inspirational.setOnClickListener(this);
        this.romance.setOnClickListener(this);
        this.thriller.setOnClickListener(this);
        this.tragedy.setOnClickListener(this);
        this.other.setOnClickListener(this);
        this.img.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                NewWrittingActivity.this.startActivityForResult(intent, 1);
            }
        });
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!NewWrittingActivity.this.haveNetwork().booleanValue()) {
                    Toast.makeText(NewWrittingActivity.this.getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                NewWrittingActivity newWrittingActivity = NewWrittingActivity.this;
                newWrittingActivity.title = newWrittingActivity.et1.getText().toString();
                NewWrittingActivity newWrittingActivity2 = NewWrittingActivity.this;
                newWrittingActivity2.description = newWrittingActivity2.et2.getText().toString();
                int selectedId = NewWrittingActivity.this.radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.poem_story) {
                    NewWrittingActivity.this.Genere = "Poem";
                } else if (selectedId == R.id.story_story) {
                    NewWrittingActivity.this.Genere = "Story";
                }
                NewWrittingActivity.this.category();
                NewWrittingActivity newWrittingActivity3 = NewWrittingActivity.this;
                newWrittingActivity3.count1 = String.valueOf(newWrittingActivity3.count);
                NewWrittingActivity.this.compressImage();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abstarct1 /*2131296263*/:
                if (this.action1.isSelected()) {
                    this.action1.setSelected(false);
                    this.action1.setChecked(false);
                    return;
                }
                this.action1.setSelected(true);
                this.action1.setChecked(true);
                return;
            case R.id.action1 /*2131296298*/:
                if (this.action2.isSelected()) {
                    this.action2.setSelected(false);
                    this.action2.setChecked(false);
                    return;
                }
                this.action2.setSelected(true);
                this.action2.setChecked(true);
                return;
            case R.id.children /*2131296374*/:
                if (this.children.isSelected()) {
                    this.children.setSelected(false);
                    this.children.setChecked(false);
                    return;
                }
                this.children.setSelected(true);
                this.children.setChecked(true);
                return;
            case R.id.classic /*2131296381*/:
                if (this.classic.isSelected()) {
                    this.classic.setSelected(false);
                    this.classic.setChecked(false);
                    return;
                }
                this.classic.setSelected(true);
                this.classic.setChecked(true);
                return;
            case R.id.comedy /*2131296395*/:
                if (this.comedy.isSelected()) {
                    this.comedy.setSelected(false);
                    this.comedy.setChecked(false);
                    return;
                }
                this.comedy.setSelected(true);
                this.comedy.setChecked(true);
                return;
            case R.id.crime /*2131296401*/:
                if (this.crime.isSelected()) {
                    this.crime.setSelected(false);
                    this.crime.setChecked(false);
                    return;
                }
                this.crime.setSelected(true);
                this.crime.setChecked(true);
                return;
            case R.id.drama /*2131296420*/:
                if (this.drama.isSelected()) {
                    this.drama.setSelected(false);
                    this.drama.setChecked(false);
                    return;
                }
                this.drama.setSelected(true);
                this.drama.setChecked(true);
                return;
            case R.id.fantasy /*2131296449*/:
                if (this.fantasy.isSelected()) {
                    this.fantasy.setSelected(false);
                    this.fantasy.setChecked(false);
                    return;
                }
                this.fantasy.setSelected(true);
                this.fantasy.setChecked(true);
                return;
            case R.id.horror /*2131296468*/:
                if (this.horror.isSelected()) {
                    this.horror.setSelected(false);
                    this.horror.setChecked(false);
                    return;
                }
                this.horror.setSelected(true);
                this.horror.setChecked(true);
                return;
            case R.id.inspirational /*2131296487*/:
                if (this.inspirational.isSelected()) {
                    this.inspirational.setSelected(false);
                    this.inspirational.setChecked(false);
                    return;
                }
                this.inspirational.setSelected(true);
                this.inspirational.setChecked(true);
                return;
            case R.id.other /*2131296555*/:
                if (this.other.isSelected()) {
                    this.other.setSelected(false);
                    this.other.setChecked(false);
                    return;
                }
                this.other.setSelected(true);
                this.other.setChecked(true);
                return;
            case R.id.romance /*2131296606*/:
                if (this.romance.isSelected()) {
                    this.romance.setSelected(false);
                    this.romance.setChecked(false);
                    return;
                }
                this.romance.setSelected(true);
                this.romance.setChecked(true);
                return;
            case R.id.thriller /*2131296675*/:
                if (this.thriller.isSelected()) {
                    this.thriller.setSelected(false);
                    this.thriller.setChecked(false);
                    return;
                }
                this.thriller.setSelected(true);
                this.thriller.setChecked(true);
                return;
            case R.id.tragedy /*2131296690*/:
                if (this.tragedy.isSelected()) {
                    this.tragedy.setSelected(false);
                    this.tragedy.setChecked(false);
                    return;
                }
                this.tragedy.setSelected(true);
                this.tragedy.setChecked(true);
                return;
            default:
                return;
        }
    }

    public void category() {
        this.count = 0;
        if (this.action1.isChecked()) {
            this.count++;
        }
        if (this.action2.isChecked()) {
            this.count++;
        }
        if (this.children.isChecked()) {
            this.count++;
        }
        if (this.crime.isChecked()) {
            this.count++;
        }
        if (this.classic.isChecked()) {
            this.count++;
        }
        if (this.comedy.isChecked()) {
            this.count++;
        }
        if (this.drama.isChecked()) {
            this.count++;
        }
        if (this.fantasy.isChecked()) {
            this.count++;
        }
        if (this.horror.isChecked()) {
            this.count++;
        }
        if (this.inspirational.isChecked()) {
            this.count++;
        }
        if (this.romance.isChecked()) {
            this.count++;
        }
        if (this.thriller.isChecked()) {
            this.count++;
        }
        if (this.tragedy.isChecked()) {
            this.count++;
        }
        if (this.other.isChecked()) {
            this.count++;
        }
    }

    public void category_set() {
        String str = "category";
        String str2 = "Category";
        if (this.action1.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("001").child(str).setValue("Abstract");
        }
        if (this.action2.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("002").child(str).setValue("Action");
        }
        if (this.children.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("003").child(str).setValue("Children Story");
        }
        if (this.crime.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("004").child(str).setValue("Crime");
        }
        if (this.classic.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("005").child(str).setValue("Classical");
        }
        if (this.comedy.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("006").child(str).setValue("Comedy");
        }
        if (this.drama.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("007").child(str).setValue("Drama");
        }
        if (this.fantasy.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("008").child(str).setValue("Fantasy");
        }
        if (this.horror.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("009").child(str).setValue("Horror");
        }
        if (this.inspirational.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("010").child(str).setValue("Inspirational");
        }
        if (this.romance.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("011").child(str).setValue("Romance");
        }
        if (this.thriller.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("012").child(str).setValue("Thriller");
        }
        if (this.tragedy.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("013").child(str).setValue("Tragedy");
        }
        if (this.other.isChecked()) {
            this.rootref.child(this.userid).child(str2).child("014").child(str).setValue("Others");
        }
    }

    public void compressImage() {
        if (this.actualImage == null) {
            ReadersList readersList = new ReadersList(this.title, this.description, this.count1, "testing", this.userid, this.currentUser, this.Genere, "Unpublished");
            this.demoref.child(this.userid).setValue(readersList).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    NewWrittingActivity.this.category_set();
                    Intent intent = new Intent(NewWrittingActivity.this, Writting2Activity.class);
                    intent.putExtra("Id", NewWrittingActivity.this.userid);
                    intent.putExtra("Title", NewWrittingActivity.this.title);
                    NewWrittingActivity.this.startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception e) {
                }
            });
            return;
        }
        new Compressor(this).compressToFileAsFlowable(this.actualImage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            public void accept(File file) {
                NewWrittingActivity.this.compressedImage = file;
                NewWrittingActivity.this.setCompressedImage();
            }
        }, new Consumer<Throwable>() {
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(NewWrittingActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
            this.pro.setVisibility(View.VISIBLE);
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
                        }
                    }, 500);
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            ReadersList userList = new ReadersList(NewWrittingActivity.this.title, NewWrittingActivity.this.description, NewWrittingActivity.this.count1, uri.toString(), NewWrittingActivity.this.userid, NewWrittingActivity.this.currentUser, NewWrittingActivity.this.Genere, "Unpublished");
                            NewWrittingActivity.this.demoref.child(NewWrittingActivity.this.userid).setValue(userList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    NewWrittingActivity.this.category_set();
                                    Intent intent = new Intent(NewWrittingActivity.this, Writting2Activity.class);
                                    intent.putExtra("Id", NewWrittingActivity.this.userid);
                                    intent.putExtra("Title", NewWrittingActivity.this.title);
                                    NewWrittingActivity.this.pro.setVisibility(View.GONE);
                                    NewWrittingActivity.this.startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(Exception e) {
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    Toast.makeText(NewWrittingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    NewWrittingActivity.this.pro.setVisibility(View.GONE);
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

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
