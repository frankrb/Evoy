package com.example.evoy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Evoy extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bnv = findViewById(R.id.myNavigation2);
        bnv.setSelectedItemId(R.id.myFeed);

        RecyclerView feed = findViewById(R.id.myRecycler2);
        //int[] imgs= {R.drawable.american,R.drawable.flixus,R.drawable.got,R.drawable.wire};
        //Ejemplo TODO quitar cuando tengamos implementado hacerlo con servidor

        Bitmap got = BitmapFactory.decodeResource(getResources(),
                R.drawable.got);
        Bitmap wire = BitmapFactory.decodeResource(getResources(),
                R.drawable.wire);
        Bitmap[] imgs = {got, wire};
        String[] names = {"Game Of Thrones", "The Wire"};
        int[] followers = {23, 44};
        MyCardViewAdapter myAdapter = new MyCardViewAdapter(names, imgs, followers);
        feed.setAdapter(myAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        feed.setLayoutManager(linearLayout);
    }
}
