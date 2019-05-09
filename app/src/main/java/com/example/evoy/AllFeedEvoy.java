package com.example.evoy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class AllFeedEvoy extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feed_evoy);
        RecyclerView feed = findViewById(R.id.myRecycler2);
        //int[] imgs= {R.drawable.american,R.drawable.flixus,R.drawable.got,R.drawable.wire};
        //Ejemplo TODO quitar cuando tengamos implementado hacerlo con servidor
        BottomNavigationView bnv = findViewById(R.id.myNavigation2);
        bnv.setSelectedItemId(R.id.allFeed);
        Bitmap american = BitmapFactory.decodeResource(getResources(),
                R.drawable.american);
        Bitmap flix = BitmapFactory.decodeResource(getResources(),
                R.drawable.flixus);
        Bitmap[] imgs = {american, flix};
        String[] names = {"American Horror X", "Flixus"};
        int[] followers = {20, 25};
        MyCardViewAdapter myAdapter = new MyCardViewAdapter(names, imgs, followers);
        feed.setAdapter(myAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        feed.setLayoutManager(linearLayout);
    }
}
