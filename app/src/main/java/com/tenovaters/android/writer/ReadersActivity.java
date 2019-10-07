package com.tenovaters.android.writer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Adapters.CategoryAdapter;
import com.tenovaters.android.writer.Adapters.ReadersAdapter;
import com.tenovaters.android.writer.Database.CategoryList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReadersActivity extends AppCompatActivity {
    private TextView t1,t3,t4;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DatabaseReference rootRef,demoRef;
    private FirebaseAuth firebaseAuth;
    private ImageView img;
    private FloatingActionButton fab;
    private Button b1;
    private String id,value,like,likes;
    private int a=0;
    private DatabaseReference mDatabaseRef;
    private CategoryAdapter mAdapter;
    private List<CategoryList> upload;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t1=(TextView)findViewById(R.id.tv_readersTitle);
        t3=(TextView)findViewById(R.id.tv_readersdescription);
        t4=(TextView)findViewById(R.id.tv_readersauthorname);
        img=(ImageView)findViewById(R.id.img_collapsing);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_reader);
        b1=(Button)findViewById(R.id.btn_like);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        String title=getIntent().getStringExtra("Title");
        String desrciption=getIntent().getStringExtra("Description");
        String authorName=getIntent().getStringExtra("Auther Name");
        String imageurl=getIntent().getStringExtra("Image");
        id=getIntent().getStringExtra("ID");

        t1.setText(title);
        t3.setText(desrciption);
        t4.setText(authorName);
        Picasso.with(ReadersActivity.this).load(imageurl).fit().centerCrop().placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).into(img);

        /*Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        Drawable d = new BitmapDrawable(getResources(), bmp);
        collapsingToolbarLayout.setBackground(d);*/

        firebaseAuth = FirebaseAuth.getInstance();
        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("Reader");


        mRecyclerView.setLayoutManager(new LinearLayoutManager(ReadersActivity.this));
         mRecyclerView.setLayoutManager(new LinearLayoutManager(ReadersActivity.this, LinearLayoutManager.HORIZONTAL, false));

        upload = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(id);
        mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                upload.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CategoryList workersdatabase = postSnapshot.getValue(CategoryList.class);
                    upload.add(workersdatabase);

                }
                mAdapter = new CategoryAdapter(ReadersActivity.this,upload);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReadersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        demoRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String view = dataSnapshot.child("views").getValue(String.class);
                likes = dataSnapshot.child("likes").getValue(String.class);
                like = dataSnapshot.child("Likes").child(currentUser).getValue(String.class);
                if (view!= null) {
                    if(a==0){
                        int viewers=Integer.parseInt(view);
                        viewers=viewers+1;
                        view=String.valueOf(viewers);
                        demoRef.child(id).child("views").setValue(view);
                        a++;
                    }
                }
                if(like!=null){
                    b1.setText("UnLike");
                }
                else if(like==null){
                    b1.setText("Like");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        demoRef.child(id).child("Favourites").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
                //Log.d(TAG,"Value is"+ value);
                fab.setVisibility(View.VISIBLE);
                if (value!= null) {
                    fab.setImageResource(R.drawable.ic_menu_send);
                }
                else{
                    fab.setImageResource(R.drawable.ic_menu_camera);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReadersActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like!=null){
                    b1.setText("Like");
                    demoRef.child(id).child("Likes").child(currentUser).removeValue();
                    int likers=Integer.parseInt(likes);
                    likers=likers-1;
                    likes=String.valueOf(likers);
                    demoRef.child(id).child("likes").setValue(likes);
                    Snackbar.make(view, "You Liked this story", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    b1.setText("UnLike");
                    demoRef.child(id).child("Likes").child(currentUser).setValue("1");
                    int likers=Integer.parseInt(likes);
                    likers=likers+1;
                    likes=String.valueOf(likers);
                    demoRef.child(id).child("likes").setValue(likes);
                    Snackbar.make(view, "You Liked this story", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(value!=null){
                    demoRef.child(id).child("Favourites").child(currentUser).removeValue();
                    fab.setImageResource(R.drawable.ic_menu_camera);
                    Snackbar.make(view, "Removed your Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    demoRef.child(id).child("Favourites").child(currentUser).setValue("1");
                    fab.setImageResource(R.drawable.ic_menu_send);
                    Snackbar.make(view, "Added to your Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
    }
}
