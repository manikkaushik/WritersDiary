package com.tenovaters.android.writer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.UserList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    private Button sub,choose;
    private EditText nam,ag;
    private ImageView img;
    private DatabaseReference rootRef,demoRef,root1,demo1;
    private DatabaseReference mDataBase;
    private FirebaseAuth firebaseAuth;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private static final int GALAARY_INTENT=1;
    private Uri filepath;
    private ProgressBar mprogressbar;
    private ProgressBar pro;

    private String Name;
    private String age;
     private String currentUser;
    private File actualImage;
    private File compressedImage;

    private final String TAG = "abc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sub=(Button)findViewById(R.id.btn_prosub);
        nam=(EditText)findViewById(R.id.et_namee);
        ag=(EditText)findViewById(R.id.et_age);
        choose=(Button)findViewById(R.id.choose);
        img=(ImageView)findViewById(R.id.circle_profile);
        mprogressbar=(ProgressBar)findViewById(R.id.progress);
        pro=(ProgressBar)findViewById(R.id.propho);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference(currentUser);
        rootRef = FirebaseDatabase.getInstance().getReference();
        root1 = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("Users").child(currentUser);
        mDataBase=FirebaseDatabase.getInstance().getReference("Users").child(currentUser);
        demo1 = root1.child("Users").child(currentUser);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALAARY_INTENT);

            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Name = nam.getText().toString();
                 age=ag.getText().toString();

                if(haveNetwork()) {

                   // if(filepath == null){
                     //   Toast.makeText(ProfileActivity.this, "Please Upload Your Profile Picture", Toast.LENGTH_SHORT).show();
                   // }
                     if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(age)) {

                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(ProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            demo1.child("image").setValue("null");
                            compressImage();
                           // uploadFile();
                        }
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "Enter the details", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, "NO INTERNET", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void compressImage() {
        if (actualImage == null) {
            Toast.makeText(ProfileActivity.this, "Please choose an image!", Toast.LENGTH_SHORT).show();
        } else {

            new id.zelory.compressor.Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            Toast.makeText(ProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void print(String str){
        Log.d(TAG, str);
    }

    public void SaveFile(){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        print("Creating cw");
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        print("Creating dir");
        File directory = cw.getDir("ImagesDir", Context.MODE_PRIVATE);
        print("Created dir" + directory);
        File mypath=new File(directory,"myImagesDGS.jpg");
        print("path is"+mypath);

        FileOutputStream outStream = null;

        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/Android/data/com.tenovaters.android.writer/Images");
            dir.mkdirs();

            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

            Toast.makeText(getApplicationContext(), "onPictureTaken - wrote to " + outFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            filepath=Uri.fromFile(outFile);
            uploadFile();

            Log.d(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());

            //refreshGallery(outFile);
        } catch (FileNotFoundException e) {
            print("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void setCompressedImage() {
        img.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
        SaveFile();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALAARY_INTENT && resultCode == RESULT_OK) {
            filepath=data.getData();
            if (data == null) {
                Toast.makeText(ProfileActivity.this, "Failed to open picture!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                actualImage = FileUtil.from(this, data.getData());
                img.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
            } catch (IOException e) {
                Toast.makeText(ProfileActivity.this, "Failed to read picture data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (filepath != null) {
            choose.setEnabled(false);
            pro.setVisibility(View.VISIBLE);
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(filepath));

            mUploadTask = fileReference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogressbar.setProgress(0);
                                }
                            }, 500);

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    UserList user= new UserList(Name,currentUser,age,uri.toString());
                                    mDataBase.setValue(user);
                                    pro.setVisibility(View.GONE);
                                    pic();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            pro.setVisibility(View.GONE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mprogressbar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean haveNetwork(){
        boolean have_WIFI=false;
        boolean have_MobileData=false;

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info:networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                    have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    have_MobileData=true;
        }
        return have_MobileData||have_WIFI;

    }
    public void pic(){
        demoRef.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageurl = dataSnapshot.getValue(String.class);
                if(imageurl!=null){
                    Toast.makeText(ProfileActivity.this,"pic is uploaded",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    finish();
                }
                else{
                    pic();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,"Check your Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
