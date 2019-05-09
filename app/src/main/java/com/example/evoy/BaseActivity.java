package com.example.evoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BottomNavigationView bnv = findViewById(R.id.myNavigation0);
        if (getIntent().hasExtra("user")) {
            username = this.getIntent().getStringExtra("user");
        }
        username = "Oliver";
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.myFeed):
                        Intent myIntent = new Intent(BaseActivity.this, Evoy.class);
                        myIntent.putExtra("user", username);
                        startActivity(myIntent);
                        break;
                    case (R.id.allFeed):
                        Intent myIntent2 = new Intent(BaseActivity.this, AllFeedEvoy.class);
                        myIntent2.putExtra("user", username);
                        startActivity(myIntent2);
                        break;
                    case (R.id.myProfile):
                        Intent myIntent3 = new Intent(BaseActivity.this, ProfileActivity.class);
                        myIntent3.putExtra("user", username);
                        startActivity(myIntent3);
                        break;
                }
                return false;
            }
        });
    }
}
