package com.zpwit_wsb_gr1_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zpwit_wsb_gr1_project.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPage2;

    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        
        addTabs();

    }

    private void addTabs() {
        List<Integer> drawableResList = new ArrayList<>();
        drawableResList.add(R.drawable.ic_home);
        drawableResList.add(R.drawable.ic_search);
        drawableResList.add(R.drawable.ic_add);
        drawableResList.add(R.drawable.ic_notification);
        drawableResList.add(R.drawable.ic_profile);

        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(drawableResList.get(i)));
        }

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPage2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPage2, ((tab, position) -> tab.setIcon(drawableResList.get(position)))).attach();

    }


    private void init() {

        viewPage2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


    }
}