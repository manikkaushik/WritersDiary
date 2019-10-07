package com.tenovaters.android.writer.ui.Profile;

import android.app.ProgressDialog;
import androidx.lifecycle.ViewModelProviders;
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

    private ProfileViewModel mViewModel;
    private DatabaseReference rootRef, demoRef;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;

    private TextView t1,t2;
    private ImageView i1;
    private ProgressBar pro;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        i1=(ImageView)root.findViewById(R.id.img_profileimage);
        t1=(TextView)root.findViewById(R.id.tv_username);
        t2=(TextView)root.findViewById(R.id.tv_age);
        pro=(ProgressBar)root.findViewById(R.id.pro_image);


        String currentUser = firebaseAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference(currentUser);
        demoRef = rootRef.child("Users").child(currentUser);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Profile");
        progressDialog.setMessage("Retrieving Information");
        progressDialog.show();

        progressDialog.setCanceledOnTouchOutside(false);


        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("name").getValue(String.class);
                //Log.d(TAG,"Value is"+ value);
                //Toast.makeText(Details.this,"value is"+value,Toast.LENGTH_SHORT).show();
                if(haveNetwork()) {
                    pro.setVisibility(View.VISIBLE);
                    if (value != null) {
                        t1.setText(value);

                    } else {
                        startActivity(new Intent(getContext(), ProfileActivity.class));
                        getActivity().finish();
                        pro.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });
        demoRef.child("age").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserAge = dataSnapshot.getValue(String.class);
                //Log.d(TAG,"Value is"+ value);
                //Toast.makeText(Details.this,"value is"+value,Toast.LENGTH_SHORT).show();
                if(haveNetwork()) {

                    if (UserAge != null) {
                        t2.setText(UserAge);
                    }
                }
                else{
                    Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        demoRef.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageurl = dataSnapshot.getValue(String.class);
                //Log.d(TAG,"Value is"+ value);


                    if (imageurl != null) {
                        Picasso.with(getContext()).load(imageurl).fit().centerCrop().placeholder(R.drawable.profile).into(i1, new Callback() {
                            @Override
                            public void onSuccess() {
                                pro.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                pro.setVisibility(View.GONE);
                            }
                        });
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "No Profile Picture", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        pro.setVisibility(View.GONE);
                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private Boolean haveNetwork(){
        boolean have_WIFI=false;
        boolean have_MobileData=false;

        ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
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
}
