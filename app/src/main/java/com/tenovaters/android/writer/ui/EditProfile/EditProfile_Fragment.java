package com.tenovaters.android.writer.ui.EditProfile;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Picasso;

import com.tenovaters.android.writer.Database.UserList;
import com.tenovaters.android.writer.FileUtil;
import com.tenovaters.android.writer.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditProfile_Fragment extends Fragment implements View.OnClickListener {

    private static final int GALAARY_INTENT = 1;
    public String Description;
    public String Gender;
    public String Name;
    private final String TAG = "abc";
    public RadioButton action1;
    public RadioButton action2;
    public File actualImage;
    public String age;
    private ImageButton b1;
    private Button b2;
    public RadioButton children;
    public RadioButton classic;
    public RadioButton comedy;
    public File compressedImage;
    public RadioButton crime;
    public String currentUser;
    public String dateofbirth;
    public int day1;
    private DatabaseReference demoRef;
    public String dob;
    public RadioButton drama;
    public EditText e1;
    public EditText e2;
    private EditText e3;
    public EditText e4;
    public EditText e5;
    private FloatingActionButton fab;
    public RadioButton fantasy;
    public RadioButton female;
    private Uri filepath;
    private FirebaseAuth firebaseAuth;
    public RadioButton horror;
    public String image;
    public ImageView img;
    public RadioButton inspirational;
    public String location;
    public DatabaseReference mDataBase;
    public DatePickerDialog.OnDateSetListener mDateListener;
    private StorageReference mStorageRef;
    public StorageTask mUploadTask;
    public RadioButton male;
    public int month1;
    public RadioButton other;
    public ProgressBar pro;
    public ProgressDialog progressDialog;
    public RadioGroup radioGroup;
    public RadioButton romance;
    private DatabaseReference rootRef;
    public TextView t1;
    /* access modifiers changed from: private */
    public RadioButton thriller;
    /* access modifiers changed from: private */
    public RadioButton tragedy;
    /* access modifiers changed from: private */
    public int year1;

    public static EditProfile_Fragment newInstance() {
        return new EditProfile_Fragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.edit_profile__fragment, container, false);
        this.img = (ImageView) root.findViewById(R.id.circle_profile_edit);
        this.e1 = (EditText) root.findViewById(R.id.et_name_edit);
        this.e2 = (EditText) root.findViewById(R.id.et_description_edit);
        this.e3 = (EditText) root.findViewById(R.id.et_email_edit);
        this.e4 = (EditText) root.findViewById(R.id.et_age_edit);
        this.e5 = (EditText) root.findViewById(R.id.et_location_edit);
        this.t1 = (TextView) root.findViewById(R.id.date_of_birth);
        this.b1 = (ImageButton) root.findViewById(R.id.btn_edit);
        this.b2 = (Button) root.findViewById(R.id.submit_edit);
        this.radioGroup = (RadioGroup) root.findViewById(R.id.radiogroup_edit);
        this.male = (RadioButton) root.findViewById(R.id.male_edit);
        this.female = (RadioButton) root.findViewById(R.id.female_edit);
        this.fab = (FloatingActionButton) root.findViewById(R.id.choose_edit);
        this.pro = (ProgressBar) root.findViewById(R.id.propho_edit);
        this.action1 = (RadioButton) root.findViewById(R.id.abstarct1);
        this.action2 = (RadioButton) root.findViewById(R.id.action1);
        this.children = (RadioButton) root.findViewById(R.id.children);
        this.classic = (RadioButton) root.findViewById(R.id.classic);
        this.comedy = (RadioButton) root.findViewById(R.id.comedy);
        this.crime = (RadioButton) root.findViewById(R.id.crime);
        this.fantasy = (RadioButton) root.findViewById(R.id.fantasy);
        this.horror = (RadioButton) root.findViewById(R.id.horror);
        this.inspirational = (RadioButton) root.findViewById(R.id.inspirational);
        this.romance = (RadioButton) root.findViewById(R.id.romance);
        this.thriller = (RadioButton) root.findViewById(R.id.thriller);
        this.tragedy = (RadioButton) root.findViewById(R.id.tragedy);
        this.drama = (RadioButton) root.findViewById(R.id.drama);
        this.other = (RadioButton) root.findViewById(R.id.other);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = this.firebaseAuth.getCurrentUser().getUid();
        String email = this.firebaseAuth.getCurrentUser().getEmail();
        String str = "Users";
        this.mStorageRef = FirebaseStorage.getInstance().getReference(str).child(this.currentUser);
        this.e3.setText(email);
        this.mStorageRef = FirebaseStorage.getInstance().getReference(str).child(this.currentUser);
        this.rootRef = FirebaseDatabase.getInstance().getReference(str);
        this.demoRef = this.rootRef.child(this.currentUser);
        this.mDataBase = this.rootRef.child(this.currentUser);
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setTitle("Uploading Your Profile Pic...");
        this.progressDialog.setMessage("Please wait...");
        this.progressDialog.setCanceledOnTouchOutside(false);
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
        this.demoRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot2 = dataSnapshot;
                String name = (String) dataSnapshot2.child("name").getValue(String.class);
                String description = (String) dataSnapshot2.child("description").getValue(String.class);
                String gender = (String) dataSnapshot2.child("gender").getValue(String.class);
                String age = (String) dataSnapshot2.child("age").getValue(String.class);
                String date_of_birth = (String) dataSnapshot2.child("dateofbirth").getValue(String.class);
                String location = (String) dataSnapshot2.child("location").getValue(String.class);
                EditProfile_Fragment.this.image = (String) dataSnapshot2.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                String str = "Interests";
                String str2 = "category";
                String abstract_s = (String) dataSnapshot2.child(str).child("001").child(str2).getValue(String.class);
                String action_s = (String) dataSnapshot2.child(str).child("002").child(str2).getValue(String.class);
                String children_s = (String) dataSnapshot2.child(str).child("003").child(str2).getValue(String.class);
                String classic_s = (String) dataSnapshot2.child(str).child("005").child(str2).getValue(String.class);
                String comedy_s = (String) dataSnapshot2.child(str).child("006").child(str2).getValue(String.class);
                String crime_s = (String) dataSnapshot2.child(str).child("004").child(str2).getValue(String.class);
                String drama_s = (String) dataSnapshot2.child(str).child("007").child(str2).getValue(String.class);
                String fantasy_s = (String) dataSnapshot2.child(str).child("008").child(str2).getValue(String.class);
                String horror_s = (String) dataSnapshot2.child(str).child("009").child(str2).getValue(String.class);
                String inspirational_s = (String) dataSnapshot2.child(str).child("010").child(str2).getValue(String.class);
                String romance_s = (String) dataSnapshot2.child(str).child("011").child(str2).getValue(String.class);
                String thriller_s = (String) dataSnapshot2.child(str).child("012").child(str2).getValue(String.class);
                String tragedy_s = (String) dataSnapshot2.child(str).child("013").child(str2).getValue(String.class);
                String others_s = (String) dataSnapshot2.child(str).child("014").child(str2).getValue(String.class);
                if (EditProfile_Fragment.this.image != null) {
                    Picasso.with(EditProfile_Fragment.this.getContext()).load(EditProfile_Fragment.this.image).fit().centerCrop().placeholder((int) R.drawable.ic_profile).into(EditProfile_Fragment.this.img);
                }
                if (name != null) {
                    EditProfile_Fragment.this.e1.setText(name);
                }
                if (description != null) {
                    EditProfile_Fragment.this.e2.setText(description);
                }
                if (age != null) {
                    EditProfile_Fragment.this.e4.setText(age);
                }
                if (location != null) {
                    EditProfile_Fragment.this.e5.setText(location);
                }
                if (date_of_birth != null) {
                    EditProfile_Fragment.this.t1.setText(date_of_birth);
                }
                if (gender != null) {
                    if (gender.equals("Male")) {
                        EditProfile_Fragment.this.male.setChecked(true);
                    } else {
                        EditProfile_Fragment.this.female.setChecked(true);
                    }
                }
                if (abstract_s != null) {
                    EditProfile_Fragment.this.action1.setSelected(true);
                    EditProfile_Fragment.this.action1.setChecked(true);
                }
                if (action_s != null) {
                    EditProfile_Fragment.this.action2.setSelected(true);
                    EditProfile_Fragment.this.action2.setChecked(true);
                }
                if (children_s != null) {
                    EditProfile_Fragment.this.children.setSelected(true);
                    EditProfile_Fragment.this.children.setChecked(true);
                }
                if (classic_s != null) {
                    EditProfile_Fragment.this.classic.setSelected(true);
                    EditProfile_Fragment.this.classic.setChecked(true);
                }
                if (comedy_s != null) {
                    EditProfile_Fragment.this.comedy.setSelected(true);
                    EditProfile_Fragment.this.comedy.setChecked(true);
                }
                if (crime_s != null) {
                    EditProfile_Fragment.this.crime.setSelected(true);
                    EditProfile_Fragment.this.crime.setChecked(true);
                }
                if (drama_s != null) {
                    EditProfile_Fragment.this.drama.setSelected(true);
                    EditProfile_Fragment.this.drama.setChecked(true);
                }
                if (fantasy_s != null) {
                    EditProfile_Fragment.this.action1.setSelected(true);
                    EditProfile_Fragment.this.fantasy.setChecked(true);
                }
                if (horror_s != null) {
                    EditProfile_Fragment.this.horror.setSelected(true);
                    EditProfile_Fragment.this.horror.setChecked(true);
                }
                if (inspirational_s != null) {
                    EditProfile_Fragment.this.inspirational.setSelected(true);
                    EditProfile_Fragment.this.inspirational.setChecked(true);
                }
                if (romance_s != null) {
                    EditProfile_Fragment.this.romance.setSelected(true);
                    EditProfile_Fragment.this.romance.setChecked(true);
                }
                if (thriller_s != null) {
                    EditProfile_Fragment.this.thriller.setSelected(true);
                    EditProfile_Fragment.this.thriller.setChecked(true);
                }
                if (tragedy_s != null) {
                    EditProfile_Fragment.this.tragedy.setSelected(true);
                    EditProfile_Fragment.this.tragedy.setChecked(true);
                }
                if (others_s != null) {
                    EditProfile_Fragment.this.other.setSelected(true);
                    EditProfile_Fragment.this.other.setChecked(true);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile_Fragment.this.getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        this.b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(EditProfile_Fragment.this.getContext(), AlertDialog.THEME_HOLO_LIGHT, EditProfile_Fragment.this.mDateListener, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();
            }
        });
        this.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                EditProfile_Fragment.this.startActivityForResult(intent, 1);
            }
        });
        this.mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                EditProfile_Fragment.this.year1 = year;
                EditProfile_Fragment.this.month1 = month;
                EditProfile_Fragment.this.day1 = day;
                EditProfile_Fragment editProfile_Fragment = EditProfile_Fragment.this;
                editProfile_Fragment.month1 = editProfile_Fragment.month1 + 1;
                EditProfile_Fragment editProfile_Fragment2 = EditProfile_Fragment.this;
                StringBuilder sb = new StringBuilder();
                sb.append(EditProfile_Fragment.this.day1);
                String str = "/";
                sb.append(str);
                sb.append(EditProfile_Fragment.this.month1);
                sb.append(str);
                sb.append(EditProfile_Fragment.this.year1);
                editProfile_Fragment2.dob = sb.toString();
                EditProfile_Fragment.this.t1.setText(EditProfile_Fragment.this.dob);
            }
        };
        this.b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditProfile_Fragment editProfile_Fragment = EditProfile_Fragment.this;
                editProfile_Fragment.Name = editProfile_Fragment.e1.getText().toString();
                EditProfile_Fragment editProfile_Fragment2 = EditProfile_Fragment.this;
                editProfile_Fragment2.Description = editProfile_Fragment2.e2.getText().toString();
                EditProfile_Fragment editProfile_Fragment3 = EditProfile_Fragment.this;
                editProfile_Fragment3.age = editProfile_Fragment3.e4.getText().toString();
                EditProfile_Fragment editProfile_Fragment4 = EditProfile_Fragment.this;
                editProfile_Fragment4.location = editProfile_Fragment4.e5.getText().toString();
                EditProfile_Fragment editProfile_Fragment5 = EditProfile_Fragment.this;
                editProfile_Fragment5.dateofbirth = editProfile_Fragment5.t1.getText().toString();
                if (EditProfile_Fragment.this.haveNetwork().booleanValue()) {
                    int selectedId = EditProfile_Fragment.this.radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.female_edit) {
                        EditProfile_Fragment.this.Gender = "Female";
                    } else if (selectedId == R.id.male_edit) {
                        EditProfile_Fragment.this.Gender = "Male";
                    }
                    if (TextUtils.isEmpty(EditProfile_Fragment.this.Name) || TextUtils.isEmpty(EditProfile_Fragment.this.age)) {
                        Toast.makeText(EditProfile_Fragment.this.getContext(), "Enter the details", Toast.LENGTH_SHORT).show();
                    } else if (EditProfile_Fragment.this.mUploadTask != null && EditProfile_Fragment.this.mUploadTask.isInProgress()) {
                        Toast.makeText(EditProfile_Fragment.this.getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else if (EditProfile_Fragment.this.actualImage == null) {
                        UserList userList = new UserList(EditProfile_Fragment.this.Name, EditProfile_Fragment.this.currentUser, EditProfile_Fragment.this.age, EditProfile_Fragment.this.image, EditProfile_Fragment.this.Gender, EditProfile_Fragment.this.Description, EditProfile_Fragment.this.dateofbirth, EditProfile_Fragment.this.location);
                        EditProfile_Fragment.this.mDataBase.setValue(userList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(EditProfile_Fragment.this.getContext(), "Data Saved", Toast.LENGTH_LONG).show();
                            }
                        });
                        EditProfile_Fragment.this.category();
                    } else {
                        EditProfile_Fragment.this.compressImage();
                    }
                } else {
                    Toast.makeText(EditProfile_Fragment.this.getContext(), "NO INTERNET", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
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

    private void uploadFile() {
        if (this.filepath != null) {
            this.progressDialog.show();
            this.fab.setEnabled(false);
            this.pro.setVisibility(View.VISIBLE);
            StorageReference storageReference = this.mStorageRef;
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            sb.append(".");
            sb.append(getFileExtension(this.filepath));
            final StorageReference fileReference = storageReference.child(sb.toString());
            this.mUploadTask = fileReference.putFile(this.filepath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                        }
                    }, 500);
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            Toast.makeText(EditProfile_Fragment.this.getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            UserList userList = new UserList(EditProfile_Fragment.this.Name, EditProfile_Fragment.this.currentUser, EditProfile_Fragment.this.age, uri.toString(), EditProfile_Fragment.this.Gender, EditProfile_Fragment.this.Description, EditProfile_Fragment.this.dateofbirth, EditProfile_Fragment.this.location);
                            EditProfile_Fragment.this.mDataBase.setValue(userList);
                            EditProfile_Fragment.this.category();
                            EditProfile_Fragment.this.pro.setVisibility(View.GONE);
                            EditProfile_Fragment.this.pic();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception e) {
                    Toast.makeText(EditProfile_Fragment.this.getContext(), "Error", Toast.LENGTH_SHORT).show();
                    EditProfile_Fragment.this.pro.setVisibility(View.GONE);
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
        Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public Boolean haveNetwork() {

        boolean have_MobileData = false;
        boolean z = false;
        boolean have_WIFI = false;
        for (NetworkInfo info : ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
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
        return z;
    }

    public void pic() {
        this.demoRef.child(MessengerShareContentUtility.MEDIA_IMAGE).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (((String) dataSnapshot.getValue(String.class)) != null) {
                    Toast.makeText(EditProfile_Fragment.this.getContext(), "pic is uploaded", Toast.LENGTH_SHORT).show();
                    EditProfile_Fragment.this.progressDialog.dismiss();
                    return;
                }
                EditProfile_Fragment.this.pic();
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile_Fragment.this.getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void compressImage() {
        if (this.actualImage == null) {
            Toast.makeText(getContext(), "Please choose an image!", Toast.LENGTH_SHORT).show();
        } else {
            new Compressor(getContext()).compressToFileAsFlowable(this.actualImage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
                public void accept(File file) {
                    EditProfile_Fragment.this.compressedImage = file;
                    EditProfile_Fragment.this.setCompressedImage();
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable throwable) {
                    throwable.printStackTrace();
                    Toast.makeText(EditProfile_Fragment.this.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void print(String str) {
        Log.d("abc", str);
    }

    public void SaveFile() {
        String str = "onPictureTaken - wrote to ";
        Bitmap bitmap = ((BitmapDrawable) this.img.getDrawable()).getBitmap();
        print("Creating cw");
        ContextWrapper cw = new ContextWrapper(getActivity());
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Context context = getContext();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(outFile.getAbsolutePath());
            Toast.makeText(context, sb4.toString(), Toast.LENGTH_SHORT).show();
            this.filepath = Uri.fromFile(outFile);
            uploadFile();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(outFile.getAbsolutePath());
            Log.d("abc", sb5.toString());
        } catch (FileNotFoundException e) {
            print("FNF");
            Toast.makeText(getContext(), "File Not Found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e2) {
            Toast.makeText(getContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void setCompressedImage() {
        this.img.setImageBitmap(BitmapFactory.decodeFile(this.compressedImage.getAbsolutePath()));
        SaveFile();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getActivity();
            if (resultCode == -1) {
                this.filepath = data.getData();
                if (data == null) {
                    Toast.makeText(getContext(), "Failed to open picture!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    this.actualImage = FileUtil.from(getContext(), data.getData());
                    this.img.setImageBitmap(BitmapFactory.decodeFile(this.actualImage.getAbsolutePath()));
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Failed to read picture data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

    public void category() {
        String str = "category";
        String str2 = "Interests";
        if (this.action1.isChecked()) {
            this.demoRef.child(str2).child("001").child(str).setValue("Abstract");
        }
        if (this.action2.isChecked()) {
            this.demoRef.child(str2).child("002").child(str).setValue("Action");
        }
        if (this.children.isChecked()) {
            this.demoRef.child(str2).child("003").child(str).setValue("Children Story");
        }
        if (this.crime.isChecked()) {
            this.demoRef.child(str2).child("004").child(str).setValue("Crime");
        }
        if (this.classic.isChecked()) {
            this.demoRef.child(str2).child("005").child(str).setValue("Classical");
        }
        if (this.comedy.isChecked()) {
            this.demoRef.child(str2).child("006").child(str).setValue("Comedy");
        }
        if (this.drama.isChecked()) {
            this.demoRef.child(str2).child("007").child(str).setValue("Drama");
        }
        if (this.fantasy.isChecked()) {
            this.demoRef.child(str2).child("008").child(str).setValue("Fantasy");
        }
        if (this.horror.isChecked()) {
            this.demoRef.child(str2).child("009").child(str).setValue("Horror");
        }
        if (this.inspirational.isChecked()) {
            this.demoRef.child(str2).child("010").child(str).setValue("Inspirational");
        }
        if (this.romance.isChecked()) {
            this.demoRef.child(str2).child("011").child(str).setValue("Romance");
        }
        if (this.thriller.isChecked()) {
            this.demoRef.child(str2).child("012").child(str).setValue("Thriller");
        }
        if (this.tragedy.isChecked()) {
            this.demoRef.child(str2).child("013").child(str).setValue("Tragedy");
        }
        if (this.other.isChecked()) {
            this.demoRef.child(str2).child("014").child(str).setValue("Others");
        }
    }
}
