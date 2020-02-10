package com.tenovaters.android.writer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.ReadersActivity;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ImageViewHolder> {
    /* access modifiers changed from: private */
    public Context mContext1;
    private List<ReadersList> mUploads;

    public class ImageViewHolder extends ViewHolder {
        public ImageView img;
        public LinearLayout linearLayout;
        public TextView title;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_recycler_story);
            this.img = (ImageView) itemView.findViewById(R.id.img_recycler_story);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_linearlayout);
        }
    }

    public StoryAdapter(Context context, List<ReadersList> uploads) {
        this.mContext1 = context;
        this.mUploads = uploads;
    }

    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.mContext1).inflate(R.layout.recycler_story, parent, false));
    }

    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final ReadersList readersList = (ReadersList) this.mUploads.get(position);
        holder.title.setText(readersList.getTitle());
        Picasso.with(this.mContext1).load(readersList.getImage()).fit().into(holder.img);
        holder.linearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(StoryAdapter.this.mContext1, ReadersActivity.class);
                intent.putExtra("Title", readersList.getTitle());
                intent.putExtra("Views", readersList.getViews());
                intent.putExtra("Comment", readersList.getComment());
                intent.putExtra("ID", readersList.getId());
                intent.putExtra("Auther Name", readersList.getAuthorname());
                intent.putExtra("Description", readersList.getDescription());
                intent.putExtra("Image", readersList.getImage());
                intent.putExtra("Author Id", readersList.getAuthorid());
                StoryAdapter.this.mContext1.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.mUploads.size();
    }
}
