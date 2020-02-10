package com.tenovaters.android.writer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.CommentList;
import com.tenovaters.android.writer.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ImageViewHolder> {
    /* access modifiers changed from: private */
    public Context mContext1;
    private DatabaseReference mDatabaseRef;
    private List<CommentList> mUploads;

    public class ImageViewHolder extends ViewHolder {
        public TextView comment;
        public TextView date;
        public ImageView img;
        public TextView name;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.comment = (TextView) itemView.findViewById(R.id.tv_comment);
            this.date = (TextView) itemView.findViewById(R.id.tv_date);
            this.name = (TextView) itemView.findViewById(R.id.tv_commmentname);
            this.img = (ImageView) itemView.findViewById(R.id.img_comment);
        }
    }

    public CommentAdapter(Context context, List<CommentList> uploads) {
        this.mContext1 = context;
        this.mUploads = uploads;
    }

    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.mContext1).inflate(R.layout.recycle_comments, parent, false));
    }

    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        CommentList readersList = (CommentList) this.mUploads.get(position);
        holder.comment.setText(readersList.getComment());
        holder.date.setText(readersList.getDate());
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        this.mDatabaseRef.child(readersList.getId()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue(String.class);
                String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                if (name != null) {
                    holder.name.setText(name);
                }
                if (image != null) {
                    Picasso.with(CommentAdapter.this.mContext1).load(image).fit().centerCrop().placeholder((int) R.drawable.picture1).into(holder.img);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentAdapter.this.mContext1, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return this.mUploads.size();
    }
}
