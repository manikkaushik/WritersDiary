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
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.Database.NotificationList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ImageViewHolder> {
    /* access modifiers changed from: private */
    public DatabaseReference demoRef;
    /* access modifiers changed from: private */
    public Context mContext1;
    private DatabaseReference mDatabaseRef;
    private List<NotificationList> mUploads;

    public class ImageViewHolder extends ViewHolder {
        public ImageView img;
        public TextView time;
        public TextView title;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_notification_text);
            this.time = (TextView) itemView.findViewById(R.id.tv_notification_time);
            this.img = (ImageView) itemView.findViewById(R.id.img_notification);
        }
    }

    public NotificationAdapter(Context context, List<NotificationList> uploads) {
        this.mContext1 = context;
        this.mUploads = uploads;
    }

    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.mContext1).inflate(R.layout.recycle_notification, parent, false));
    }

    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final NotificationList notificationList = (NotificationList) this.mUploads.get(position);
        holder.time.setText(notificationList.getDate());
        this.mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        this.demoRef = FirebaseDatabase.getInstance().getReference("Readers");
        this.mDatabaseRef.child(notificationList.getNotiferid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue(String.class);
                String image = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                if (name != null) {
                    TextView textView = holder.title;
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    sb.append(" ");
                    sb.append(notificationList.getTitle());
                    textView.setText(sb.toString());
                    NotificationAdapter.this.demoRef.child(notificationList.getAuthorid()).child(notificationList.getStoryid()).addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String genere = (String) dataSnapshot.child("genere").getValue(String.class);
                            String title = (String) dataSnapshot.child("title").getValue(String.class);
                            String str = (String) dataSnapshot.child(MessengerShareContentUtility.MEDIA_IMAGE).getValue(String.class);
                            if (genere != null && title != null) {
                                TextView textView = holder.title;
                                StringBuilder sb = new StringBuilder();
                                String str2 = " ";
                                sb.append(str2);
                                sb.append(genere);
                                sb.append(str2);
                                sb.append(title);
                                textView.append(sb.toString());
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(NotificationAdapter.this.mContext1, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (image != null) {
                    Picasso.with(NotificationAdapter.this.mContext1).load(image).fit().centerCrop().placeholder((int) R.drawable.picture1).into(holder.img);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotificationAdapter.this.mContext1, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return this.mUploads.size();
    }
}
