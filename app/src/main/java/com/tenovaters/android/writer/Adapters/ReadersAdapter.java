package com.tenovaters.android.writer.Adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.ReadersActivity;

import java.util.ArrayList;
import java.util.List;

public class ReadersAdapter extends RecyclerView.Adapter<ReadersAdapter.ImageViewHolder> {

    private Context mContext;
    private List<ReadersList> mUploads;


    private DatabaseReference mDatabaseRef;
    private CategoryAdapter mAdapter;
    private List<CategoryList> upload;



    public ReadersAdapter(Context context, List<ReadersList> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ReadersAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_home, parent, false);
        return new ReadersAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReadersAdapter.ImageViewHolder holder, int position) {
        final ReadersList readersList = mUploads.get(position);


        holder.title.setText(readersList.getTitle());
        holder.authorname.setText(readersList.getAuthorname());
        holder.story.setText(readersList.getStory());
        holder.likes.setText(readersList.getLikes());
        holder.views.setText(readersList.getViews());

        final String category= readersList.getCategory();
        if(category.equals("1")){
            //Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(readersList.getId());
            mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(i==0){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });

        }
        else if(category.equals("2")){
           // Toast.makeText(mContext,"2", Toast.LENGTH_SHORT).show();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(readersList.getId());
            mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(i==0){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                        if(i==2){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value);
                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }
        else if(category.equals("3")){
          //  Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(readersList.getId());
            mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(i==0){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                        else if(i==1){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value);
                            i++;
                        }
                        else if(i==2){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv2.setVisibility(View.VISIBLE);
                            holder.categorytv2.setText(value);
                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }
        else{
            //Toast.makeText(mContext, "5", Toast.LENGTH_SHORT).show();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(readersList.getId());
            mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i=0;
                    int remain = Integer.parseInt(category);
                    remain=remain-3;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(i==0){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                        else if(i==1){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value);
                            i++;
                        }
                        else if(i==2){
                            String value = postSnapshot.child("category").getValue(String.class);
                            holder.categorytv2.setVisibility(View.VISIBLE);
                            holder.categorytv2.setText(value);
                            holder.categorytv3.setVisibility(View.VISIBLE);
                            holder.categorytv3.setText("+"+remain+" more");
                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    // mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });
        }


       /*mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reader").child(readersList.getId());
        mDatabaseRef.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                upload = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CategoryList workersdatabase = postSnapshot.getValue(CategoryList.class);
                    upload.add(workersdatabase);

                }
                mAdapter = new CategoryAdapter(mContext,upload);
                holder.mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
               // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });*/

       Picasso.with(mContext).load(readersList.getImage()).fit().centerCrop().into(holder.image);
       /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"You clicked"+readersList.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext, ReadersActivity.class);
                holder.image.buildDrawingCache();
                Bitmap image = holder.image.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);
                intent.putExtra("Title",readersList.getTitle());
                intent.putExtra("ID",readersList.getId());
                intent.putExtra("Category",readersList.getCategory());
                intent.putExtra("Auther Name",readersList.getAuthorname());
                intent.putExtra("Description",readersList.getStory());
                mContext.startActivity(intent);

            }
        }); */

       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //Toast.makeText(mContext,"You clicked"+readersList.getTitle(),Toast.LENGTH_SHORT).show();
               Intent intent=new Intent(mContext, ReadersActivity.class);
               intent.putExtra("Title",readersList.getTitle());
               intent.putExtra("ID",readersList.getId());
               intent.putExtra("Auther Name",readersList.getAuthorname());
               intent.putExtra("Description",readersList.getStory());
               intent.putExtra("Image",readersList.getImage());
               mContext.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView title,authorname,story,likes,views;
        public ImageView image;
        public Button request;
        public RelativeLayout relativeLayout;
        public CardView card;
        public TextView categorytv,categorytv1,categorytv2,categorytv3;




        public ImageViewHolder(final View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.tv_title);
            authorname = itemView.findViewById(R.id.tv_authorname);
            story = itemView.findViewById(R.id.tv_story);
            image=itemView.findViewById(R.id.img_bookimage);
            likes = itemView.findViewById(R.id.tv_likes);
            views = itemView.findViewById(R.id.tv_views);
            categorytv = itemView.findViewById(R.id.tv_category1);
            categorytv1 = itemView.findViewById(R.id.tv_category2);
            categorytv2 = itemView.findViewById(R.id.tv_category3);
            categorytv3 = itemView.findViewById(R.id.tv_categorymore);

            //mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
           // mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            relativeLayout=itemView.findViewById(R.id.relativelayout_reader);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ReadersActivity.class));
                }
            });
        }
    }
}
