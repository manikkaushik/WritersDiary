package com.tenovaters.android.writer.Adapters;

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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.ReadersActivity;

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

       Picasso.with(mContext).load(readersList.getImage()).fit().centerCrop().into(holder.image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
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
                intent.putExtra("Category",readersList.getCategory());
                intent.putExtra("Auther Name",readersList.getAuthorname());
                intent.putExtra("Description",readersList.getStory());
                mContext.startActivity(intent);

            }
        });

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
            image=itemView.findViewById(R.id.img_bookimage);
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
