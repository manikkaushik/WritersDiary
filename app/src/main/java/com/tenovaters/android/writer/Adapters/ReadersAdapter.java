package com.tenovaters.android.writer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.List;

public class ReadersAdapter extends RecyclerView.Adapter<ReadersAdapter.ImageViewHolder> {

    private Context mContext;
    private List<ReadersList> mUploads;

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
        holder.category.setText(readersList.getCategory());
        holder.story.setText(readersList.getStory());

      //  Picasso.with(mContext).load(workersdatabase.getImage()).fit().centerCrop().into(holder.image);
        /*
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase = FirebaseDatabase.getInstance().getReference("Users").child(New_Game.USerID).child("Friends").child(workersdatabase.getUserID());
                UserDatabase artist = new UserDatabase(workersdatabase.getName(), workersdatabase.getUserID(), workersdatabase.getAge());
                String userid=workersdatabase.getUserID();

                holder.card.setVisibility(View.GONE);
                ((Friend_requestActivity)mContext).finish();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView title,authorname,category,story;
        public ImageView image;
        public Button request;
        public RelativeLayout relativeLayout;
        public CardView card;


        public ImageViewHolder(final View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.tv_title);
            authorname = itemView.findViewById(R.id.tv_authorname);
            category = itemView.findViewById(R.id.tv_catogory);
            story = itemView.findViewById(R.id.tv_story);

           // relativeLayout.setOnClickListener(new View.OnClickListener() {
             //   @Override
             //   public void onClick(View v) {
              //      itemView.getContext().startActivity(new Intent(itemView.getContext(),Room_Activity.class));
              //  }
           // });
        }
    }
}
