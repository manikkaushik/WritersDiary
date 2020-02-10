package com.tenovaters.android.writer;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.AppBarConfiguration.Builder;

import androidx.navigation.ui.NavigationUI;
import com.facebook.share.internal.MessengerShareContentUtility;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;

import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.iid.InstanceIdResult;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.tenovaters.android.writer.ui.EditProfile.EditProfile_Fragment;

import com.tenovaters.android.writer.ui.Profile.ProfileFragment;

import com.tenovaters.android.writer.ui.home.HomeFragment;


public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    /* access modifiers changed from: private */
    public DatabaseReference demoRef;

    /* access modifiers changed from: private */
    public FirebaseAuth firebaseAuth;
    Fragment fragment = new HomeFragment();
    Fragment fragment2 = this.fragment;
    /* access modifiers changed from: private */
    public ImageView img;
    ConstraintLayout layoutbottom;
    ConstraintLayout layoutdrawer;
    ProgressDialog progressDialog;
    private DatabaseReference rootRef;
    /* access modifiers changed from: private */

    /* renamed from: t1 */
    public TextView f572t1;
    /* access modifiers changed from: private */

    /* renamed from: t2 */
    public TextView f573t2;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view1);
        AppBarConfiguration appBarConfiguration = new Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_write, R.id.navigation_notifications, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = this.firebaseAuth.getCurrentUser();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("Your Account");
        this.progressDialog.setMessage("Loading");
        this.progressDialog.show();
        this.layoutdrawer = (ConstraintLayout) findViewById(R.id.drawer_navigation);
        this.layoutbottom = (ConstraintLayout) findViewById(R.id.bottom_navigation);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setCancelable(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        this.img = (ImageView) hView.findViewById(R.id.img_drawerimage);
        this.f572t1 = (TextView) hView.findViewById(R.id.tv_drawerusername);
        this.f573t2 = (TextView) hView.findViewById(R.id.tv_draweremail);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken", newToken);
                    }
                });
        if (!haveNetwork().booleanValue()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage((CharSequence) "Check your Internet Connection!!");
            builder.setCancelable(false);
            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    HomeActivity.this.finish();
                    System.exit(0);
                }
            });
            builder.create().show();
        }
        if (user == null) {
            this.progressDialog.dismiss();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        this.demoRef = this.rootRef.child("Users").child(this.firebaseAuth.getCurrentUser().getUid());
        this.demoRef.child("name").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue(String.class);
                if (value == null) {
                    HomeActivity.this.progressDialog.dismiss();
                    HomeActivity homeActivity = HomeActivity.this;
                    homeActivity.startActivity(new Intent(homeActivity, ProfileActivity.class));
                    HomeActivity.this.finish();
                    return;
                }
                HomeActivity.this.progressDialog.dismiss();
                HomeActivity.this.f572t1.setText(value);
                HomeActivity.this.f573t2.setText(HomeActivity.this.firebaseAuth.getCurrentUser().getEmail());
                HomeActivity.this.demoRef.child(MessengerShareContentUtility.MEDIA_IMAGE).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageurl = (String) dataSnapshot.getValue(String.class);
                        if (imageurl != null) {
                            Picasso.with(HomeActivity.this).load(imageurl).fit().centerCrop().placeholder((int) R.mipmap.ic_launcher_round).into(HomeActivity.this.img, new Callback() {
                                public void onSuccess() {
                                }

                                public void onError() {
                                }
                            });
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(HomeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onCancelled(DatabaseError databaseError) {
                HomeActivity.this.progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else if (this.layoutbottom.getVisibility() == View.INVISIBLE) {
            this.layoutbottom.setVisibility(View.VISIBLE);
            this.layoutdrawer.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment3 = null;
        if (id == R.id.nav_edit_profile) {
            fragment3 = new EditProfile_Fragment();
        }
        if (id == R.id.logout) {
            this.firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        if (fragment3 != null) {
            if (fragment3.equals(null)) {
                this.layoutbottom.setVisibility(View.VISIBLE);
                this.layoutdrawer.setVisibility(View.INVISIBLE);
            } else if (this.layoutbottom.getVisibility() == View.VISIBLE) {
                this.layoutbottom.setVisibility(View.INVISIBLE);
                this.layoutdrawer.setVisibility(View.VISIBLE);
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment3);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        this.fragment = null;
        Fragment fragment1 = null;
        if (itemId == R.id.nav_home) {
            this.fragment = new HomeFragment();
            fragment1 = this.fragment;
            this.fragment2 = this.fragment;
        } else if (itemId == R.id.nav_profile) {
            this.fragment = new ProfileFragment();
        }
        Fragment fragment3 = this.fragment;
        if (fragment3 != null) {
            if (fragment3.equals(fragment1)) {
                this.layoutbottom.setVisibility(View.VISIBLE);
                this.layoutdrawer.setVisibility(View.INVISIBLE);
            } else if (this.layoutbottom.getVisibility() == View.VISIBLE) {
                this.layoutbottom.setVisibility(View.INVISIBLE);
                this.layoutdrawer.setVisibility(View.VISIBLE);

            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, this.fragment);
            ft.commit();
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
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

    public void onStart() {
        super.onStart();
        this.firebaseAuth = FirebaseAuth.getInstance();
        if (this.firebaseAuth.getCurrentUser() == null) {
            this.progressDialog.dismiss();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
