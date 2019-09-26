package com.tenovaters.android.writer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.ui.Profile.ProfileFragment;
import com.tenovaters.android.writer.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef,demoRef;
    private ImageView img;
    private TextView t1,t2;
    ProgressDialog progressDialog;
    ConstraintLayout layoutdrawer,layoutbottom;
    Fragment fragment=new HomeFragment();
    Fragment fragment2=fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        BottomNavigationView navView = findViewById(R.id.nav_view1);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_notifications,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setTitle("Your Account");
        progressDialog.setMessage("Loading");
        progressDialog.show();

         layoutdrawer=(ConstraintLayout)findViewById(R.id.drawer_navigation);
         layoutbottom=(ConstraintLayout)findViewById(R.id.bottom_navigation);


        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        img=(ImageView)hView.findViewById(R.id.img_drawerimage);
        t1=(TextView)hView.findViewById(R.id.tv_drawerusername);
        t2=(TextView)hView.findViewById(R.id.tv_draweremail);

        if (user == null) {
            progressDialog.dismiss();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
        else{
            String currentUser = firebaseAuth.getCurrentUser().getUid();
            demoRef = rootRef.child("Users").child(currentUser);

            demoRef.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String value = dataSnapshot.getValue(String.class);

                    if(value==null){
                        progressDialog.dismiss();
                        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        t1.setText(value);
                        t2.setText(firebaseAuth.getCurrentUser().getEmail());
                        demoRef.child("image").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String imageurl = dataSnapshot.getValue(String.class);
                                //Log.d(TAG,"Value is"+ value);


                                if (imageurl != null) {
                                    Picasso.with(HomeActivity.this).load(imageurl).fit().centerCrop().placeholder(R.mipmap.ic_launcher_round).into(img, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(HomeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"Check your Internet Connection",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(layoutbottom.getVisibility()==View.INVISIBLE){
            layoutbottom.setVisibility(View.VISIBLE);
            layoutdrawer.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
            //Toast.makeText(HomeActivity.this, "Check your ", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       displaySelectedScreen(id);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
         fragment = null;
         Fragment fragment1 = null;
        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_home:
                fragment = new HomeFragment();
                fragment1=fragment;
                fragment2=fragment;
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            if(fragment.equals(fragment1)){
                layoutbottom.setVisibility(View.VISIBLE);
                layoutdrawer.setVisibility(View.INVISIBLE);
            }
            else if(layoutbottom.getVisibility()==View.VISIBLE){
                layoutbottom.setVisibility(View.INVISIBLE);
                layoutdrawer.setVisibility(View.VISIBLE);
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
