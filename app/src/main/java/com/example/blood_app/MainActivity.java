package com.example.blood_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private static int splash_time_out=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // code for animation for splash screen

        imageView=findViewById(R.id.logo);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1500);

        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent go=new Intent(getApplicationContext(),options.class);
                startActivity(go);
            }
        },splash_time_out);
    }
}