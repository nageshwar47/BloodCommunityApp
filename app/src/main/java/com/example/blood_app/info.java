package com.example.blood_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class info extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigation;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //initialize the firebase
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("donor");

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigation=findViewById(R.id.nav_view1);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();


                if(id==R.id.update)
                {
                    Toast.makeText(info.this, "update is clicked", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new update();
                    FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_cont,fragment).commit();



                }



                if(id==R.id.logout)
                {
                    Toast.makeText(info.this, "logout is clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(info.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }


                return false;
            }
        });


        // to make the Navigation drawer icon always appear on the action bar



    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.)
        {
            Toast.makeText(this, "home is clicked", Toast.LENGTH_SHORT).show();


        }

        if(id==R.id.update)
        {
            Toast.makeText(this, "update is clicked", Toast.LENGTH_SHORT).show();

        }

        if(id==R.id.details)
        {
            Toast.makeText(this, "details is clicked", Toast.LENGTH_SHORT).show();

        }

        if(id==R.id.logout)
        {
            Toast.makeText(this, "logout is clicked", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }*/
}