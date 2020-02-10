package com.tenovaters.android.writer.ui.Write;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tenovaters.android.writer.NewWrittingActivity;
import com.tenovaters.android.writer.R;
import com.tenovaters.android.writer.ui.TabFragments.DraftFragment;
import com.tenovaters.android.writer.ui.TabFragments.PublishedFragment;

public class WriteFragment extends Fragment {

    public static int int_items = 2;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private FloatingActionButton fab;

    /* renamed from: com.tenovaters.android.writer.ui.Write.WriteFragment$MyAdapter */
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (position == 0) {
                return new PublishedFragment();
            }
            if (position != 1) {
                return null;
            }
            return new DraftFragment();
        }

        public int getCount() {
            return WriteFragment.int_items;
        }

        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Published";
            }
            if (position != 1) {
                return null;
            }
            return "Draft";
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.write_fragment, container, false);
        tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        viewPager = (ViewPager) root.findViewById(R.id.container);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            public void run() {
                WriteFragment.tabLayout.setupWithViewPager(WriteFragment.viewPager);
            }
        });
        this.fab = (FloatingActionButton) root.findViewById(R.id.fab_write);
        this.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WriteFragment writeFragment = WriteFragment.this;
                writeFragment.startActivity(new Intent(writeFragment.getContext(), NewWrittingActivity.class));
            }
        });
        return root;
    }
}
