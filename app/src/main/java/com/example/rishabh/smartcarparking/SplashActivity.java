package com.example.rishabh.smartcarparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView=(ImageView)findViewById(R.id.splashimage);
        Animation myanimation= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.splashanimation);
        imageView.setAnimation(myanimation);
        imageView.startAnimation(myanimation);
        String random=RandomStringGenerator.generateRandomString();
        SharedPrefrenceData data=new SharedPrefrenceData(SplashActivity.this);
        Log.e("SPLASHRISHABH",String.valueOf(random));
        data.setrandomcode(random);

       myanimation.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) {

           }

           @Override
           public void onAnimationEnd(Animation animation) {
               startActivity(new Intent(SplashActivity.this,ServerClientApp.class));
           }

           @Override
           public void onAnimationRepeat(Animation animation) {

           }
       });
    }
}
