package com.tenovaters.android.writer.ui.Profile;

import android.app.ProgressDialog;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.ProfileActivity;
import com.tenovaters.android.writer.R;

public class ProfileFragment extends Fragment {

    int count = 0;
    int count3 = 0;
    private DatabaseReference demoRef;
    private FirebaseAuth firebaseAuth;
    public ImageView i1;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private ProfileViewModel mViewModel;
    public ProgressBar pro;
    ProgressDialog progressDialog;
    private DatabaseReference rootRef;
    public TextView t1;
    public TextView t2;
    private TextView t3;
    public TextView t4;
    public TextView t5;
    public TextView t6;
    public TextView t7;
    public TextView t8;
    public TextView t9;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.i1 = (ImageView) root.findViewById(R.id.img_profileimage);
        this.t1 = (TextView) root.findViewById(R.id.tv_username);
        this.t2 = (TextView) root.findViewById(R.id.tv_age);
        this.t3 = (TextView) root.findViewById(R.id.tv_useremail);
        this.t4 = (TextView) root.findViewById(R.id.tv_aboutauthor);
        this.t5 = (TextView) root.findViewById(R.id.tv_gender_profile);
        this.t6 = (TextView) root.findViewById(R.id.tv_dateofbirth);
        this.t7 = (TextView) root.findViewById(R.id.tv_location);
        this.t8 = (TextView) root.findViewById(R.id.tv_story_published);
        this.t9 = (TextView) root.findViewById(R.id.tv_favourites);
        this.pro = (ProgressBar) root.findViewById(R.id.pro_image);
        this.t3.setText(this.firebaseAuth.getCurrentUser().getEmail());
        final String currentUser = this.firebaseAuth.getCurrentUser().getUid();
        this.mStorageRef = FirebaseStorage.getInstance().getReference(currentUser);
        this.demoRef = this.rootRef.child("Users").child(currentUser);
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setTitle("Profile");
        this.progressDialog.setMessage("Retrieving Information");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.demoRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("name").getValue(String.class);
                String UserAge = (String) dataSnapshot.child("age").getValue(String.class);
                String dateofbirth = (String) dataSnapshot.child("dateofbirth").getValue(String.class);
                String description = (String) dataSnapshot.child("description").getValue(String.class);
                String gender = (String) dataSnapshot.child("gender").getValue(String.class);
                String location = (String) dataSnapshot.child("location").getValue(String.class);
                if (value != null) {
                    ProfileFragment.this.t1.setText(value);
                }
                if (UserAge != null) {
                    ProfileFragment.this.t2.setText(UserAge);
                }
                if (dateofbirth != null) {
                    ProfileFragment.this.t6.setText(dateofbirth);
                }
                if (description != null) {
                    ProfileFragment.this.t4.setText(description);
                }
                if (gender != null) {
                    ProfileFragment.this.t5.setText(gender);
                }
                if (location != null) {
                    ProfileFragment.this.t7.setText(location);
                    return;
                }
                Toast.makeText(ProfileFragment.this.getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                ProfileFragment.this.progressDialog.dismiss();
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileFragment.this.getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        this.demoRef.child(MessengerShareContentUtility.MEDIA_IMAGE).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageurl = (String) dataSnapshot.getValue(String.class);
                ProfileFragment.this.pro.setVisibility(View.VISIBLE);
                if (imageurl != null) {
                    Picasso.with(ProfileFragment.this.getContext()).load(imageurl).fit().centerCrop().placeholder((int) R.drawable.profile).into(ProfileFragment.this.i1, new Callback() {
                        public void onSuccess() {
                            ProfileFragment.this.pro.setVisibility(View.GONE);
                        }

                        public void onError() {
                            ProfileFragment.this.pro.setVisibility(View.GONE);
                        }
                    });
                    ProfileFragment.this.progressDialog.dismiss();
                    return;
                }
                Toast.makeText(ProfileFragment.this.getContext(), "No Profile Picture", Toast.LENGTH_SHORT).show();
                ProfileFragment.this.progressDialog.dismiss();
                ProfileFragment.this.pro.setVisibility(View.GONE);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileFragment.this.getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (((String) postSnapshot.child("status").getValue(String.class)).equals("Published")) {
                        ProfileFragment.this.count++;
                    }
                }
                ProfileFragment.this.t8.setText(String.valueOf(ProfileFragment.this.count));
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.mDatabaseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        if (((String) postSnapshot1.child("Favourites").child(currentUser).child("favourite").getValue(String.class)) != null) {
                            ProfileFragment.this.count3++;
                        }
                    }
                }
                ProfileFragment.this.t9.setText(String.valueOf(ProfileFragment.this.count3));
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileFragment.this.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mViewModel = (ProfileViewModel) ViewModelProviders.of((Fragment) this).get(ProfileViewModel.class);
    }

    private Boolean haveNetwork() {
        boolean have_MobileData;
        boolean have_WIFI;
        boolean z = false;
        try {
            FragmentActivity activity = getActivity();
            getActivity();
            NetworkInfo[] networkInfos = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
            int length = networkInfos.length;
            have_MobileData = false;
            have_WIFI = false;
            int i = 0;
            while (i < length) {
                try {
                    NetworkInfo info = networkInfos[i];
                    if (info.getTypeName().equalsIgnoreCase("WIFI") && info.isConnected()) {
                        have_WIFI = true;
                    }
                    if (info.getTypeName().equalsIgnoreCase("MOBILE") && info.isConnected()) {
                        have_MobileData = true;
                    }
                    i++;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
                    z = true;
                    return Boolean.valueOf(z);
                }
            }
        } catch (Exception e2) {
            have_MobileData = false;
            have_WIFI = false;
            Exception exc = e2;
            Toast.makeText(getContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
            z = true;
            return Boolean.valueOf(z);
        }
        if (have_MobileData || have_WIFI) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
