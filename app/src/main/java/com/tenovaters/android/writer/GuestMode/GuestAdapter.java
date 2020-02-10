package com.tenovaters.android.writer.GuestMode;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tenovaters.android.writer.Database.ReadersList;
import com.tenovaters.android.writer.R;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ImageViewHolder> {
    public static final int MAX_LINES = 2;
    /* access modifiers changed from: private */
    public Context mContext;
    private DatabaseReference mDatabaseRef;
    private List<ReadersList> mUploads;
    private DatabaseReference rootref;

    public class ImageViewHolder extends ViewHolder {
        public TextView authorname;
        public CardView card;
        public TextView categorytv;
        public TextView categorytv1;
        public TextView categorytv2;
        public TextView categorytv3;
        public ImageView image;
        public TextView likes;
        public RelativeLayout relativeLayout;
        public Button request;
        public TextView story;
        public TextView title;
        public TextView views;

        public ImageViewHolder(final View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_title);
            this.authorname = (TextView) itemView.findViewById(R.id.tv_authorname);
            this.story = (TextView) itemView.findViewById(R.id.tv_story);
            this.image = (ImageView) itemView.findViewById(R.id.img_bookimage);
            this.likes = (TextView) itemView.findViewById(R.id.tv_likes);
            this.views = (TextView) itemView.findViewById(R.id.tv_views);
            this.categorytv = (TextView) itemView.findViewById(R.id.tv_category1);
            this.categorytv1 = (TextView) itemView.findViewById(R.id.tv_category2);
            this.categorytv2 = (TextView) itemView.findViewById(R.id.tv_category3);
            this.categorytv3 = (TextView) itemView.findViewById(R.id.tv_categorymore);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout_reader);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), GuestReaderActivity.class));
                }
            });
        }
    }

    public GuestAdapter(Context context, List<ReadersList> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }

    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.recycle_home, parent, false));
    }

    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final ReadersList readersList = (ReadersList) this.mUploads.get(position);
        this.rootref = FirebaseDatabase.getInstance().getReference("Users").child(readersList.getAuthorid());
        this.rootref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("name").getValue(String.class);
                if (value != null) {
                    holder.authorname.setText(value);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GuestAdapter.this.mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        final String discription = readersList.getDescription();
        holder.title.setText(readersList.getTitle());
        holder.story.setText(discription);
        holder.likes.setText(readersList.getLikes());
        holder.views.setText(readersList.getViews());
        holder.story.post(new Runnable() {
            public void run() {
                if (holder.story.getLineCount() > 2) {
                    int lastCharShown = holder.story.getLayout().getLineVisibleEnd(1);
                    holder.story.setMaxLines(2);
                    String moreString = "More";
                    StringBuilder sb = new StringBuilder();
                    sb.append("  ");
                    sb.append(moreString);
                    String suffix = sb.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(discription.substring(0, (lastCharShown - suffix.length()) - 3));
                    sb2.append("...");
                    sb2.append(suffix);
                    String actionDisplayText = sb2.toString();
                    SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                    int startIndex = actionDisplayText.indexOf(moreString);
                    if (VERSION.SDK_INT >= 23) {
                        truncatedSpannableString.setSpan(new ForegroundColorSpan(GuestAdapter.this.mContext.getColor(R.color.blue)), startIndex, moreString.length() + startIndex, 33);
                    }
                    holder.story.setText(truncatedSpannableString);
                }
            }
        });
        final String category = readersList.getCategory();
        String str = "Category";
        String str2 = "Readers";
        if (category.equals(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str2).child(readersList.getAuthorid()).child(readersList.getId());
            this.mDatabaseRef.child(str).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (i == 0) {
                            String value = (String) postSnapshot.child("category").getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestAdapter.this.mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (category.equals("2")) {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str2).child(readersList.getAuthorid()).child(readersList.getId());
            this.mDatabaseRef.child(str).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String str = "category";
                        if (i == 0) {
                            String value = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        }
                        if (i == 1) {
                            String value2 = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value2);
                            i++;
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestAdapter.this.mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (category.equals("3")) {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str2).child(readersList.getAuthorid()).child(readersList.getId());
            this.mDatabaseRef.child(str).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String str = "category";
                        if (i == 0) {
                            String value = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        } else if (i == 1) {
                            String value2 = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value2);
                            i++;
                        } else if (i == 2) {
                            String value3 = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv2.setVisibility(View.VISIBLE);
                            holder.categorytv2.setText(value3);
                            i++;
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestAdapter.this.mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            this.mDatabaseRef = FirebaseDatabase.getInstance().getReference(str2).child(readersList.getAuthorid()).child(readersList.getId());
            this.mDatabaseRef.child(str).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    int remain = Integer.parseInt(category) - 3;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String str = "category";
                        if (i == 0) {
                            String value = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv.setVisibility(View.VISIBLE);
                            holder.categorytv.setText(value);
                            i++;
                        } else if (i == 1) {
                            String value2 = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv1.setVisibility(View.VISIBLE);
                            holder.categorytv1.setText(value2);
                            i++;
                        } else if (i == 2) {
                            String value3 = (String) postSnapshot.child(str).getValue(String.class);
                            holder.categorytv2.setVisibility(View.VISIBLE);
                            holder.categorytv2.setText(value3);
                            holder.categorytv3.setVisibility(View.VISIBLE);
                            TextView textView = holder.categorytv3;
                            StringBuilder sb = new StringBuilder();
                            sb.append("+");
                            sb.append(remain);
                            sb.append(" more");
                            textView.setText(sb.toString());
                            i++;
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GuestAdapter.this.mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        Picasso.with(this.mContext).load(readersList.getImage()).fit().into(holder.image);
        holder.relativeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GuestAdapter.this.mContext, GuestReaderActivity.class);
                intent.putExtra("Title", readersList.getTitle());
                intent.putExtra("Views", readersList.getViews());
                intent.putExtra("Comment", readersList.getComment());
                intent.putExtra("ID", readersList.getId());
                intent.putExtra("Auther Name", readersList.getAuthorname());
                intent.putExtra("Description", readersList.getDescription());
                intent.putExtra("Image", readersList.getImage());
                intent.putExtra("Author Id", readersList.getAuthorid());
                GuestAdapter.this.mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.mUploads.size();
    }
}
