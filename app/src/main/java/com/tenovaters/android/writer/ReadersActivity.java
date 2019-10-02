package com.tenovaters.android.writer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadersActivity extends AppCompatActivity {
    private TextView t1,t2,t3,t4;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t1=(TextView)findViewById(R.id.tv_readersTitle);
        t2=(TextView)findViewById(R.id.tv_readerscatogory);
        t3=(TextView)findViewById(R.id.tv_readersdescription);
        t4=(TextView)findViewById(R.id.tv_readersauthorname);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        String title=getIntent().getStringExtra("Title");
        String category=getIntent().getStringExtra("Category");
        String desrciption=getIntent().getStringExtra("Description");
        String authorName=getIntent().getStringExtra("Auther Name");
        String image=getIntent().getStringExtra("Image");

        t1.setText(title);
        t2.setText(category);
        t3.setText(desrciption);
        t4.setText(authorName);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        Drawable d = new BitmapDrawable(getResources(), bmp);
        collapsingToolbarLayout.setBackground(d);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
