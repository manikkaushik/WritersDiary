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
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.CategoryList;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.ReadersActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ImageViewHolder> {

    private Context mContext1;
    private List<CategoryList> mUploads;

    public CategoryAdapter(Context context, List<CategoryList> uploads) {
        mContext1 = context;
        mUploads = uploads;
    }

    @Override
    public CategoryAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext1).inflate(R.layout.recylcel_category, parent, false);
        return new CategoryAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ImageViewHolder holder, int position) {
        final CategoryList readersList = mUploads.get(position);

        holder.title.setText(readersList.getCategory());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;



        public ImageViewHolder(final View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.tv_categoryrecylcler);
        }
    }
}
