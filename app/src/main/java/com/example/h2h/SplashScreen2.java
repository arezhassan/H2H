package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen2 extends AppCompatActivity {
FirebaseAuth mAuth;

ImageView ivUser, ivRider, ivAdmin;

String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        mAuth = FirebaseAuth.getInstance();
        ivAdmin = findViewById(R.id.ivAdmin);
        ivRider = findViewById(R.id.ivRider);
        ivUser = findViewById(R.id.ivUser);




        if(mAuth.getCurrentUser()!= null){
            startActivity(new Intent(SplashScreen2.this,MainActivity.class));
            finish();
        }

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "user";
                zoomInAnimation(ivUser);

            }
        });

        ivAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "admin";
                zoomInAnimation(ivAdmin);

            }
        });
        ivRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "rider";
                zoomInAnimation(ivRider);

            }
        });



    }
    private void resetZoomAnimation(ImageView image) {
        // Reset the ImageView to its original size
        image.clearAnimation();
        image.setScaleX(1f);
        image.setScaleY(1f);
        image.setPivotX(0f);
        image.setPivotY(0f);
    }
    private void zoomInAnimation(ImageView image) {
        // Create a ScaleAnimation for zooming in
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(600); // Duration in milliseconds
        scaleAnimation.setFillAfter(true);

        // Apply the animation to the ImageView
        image.startAnimation(scaleAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen2.this, MainActivity.class);
                i.putExtra("userType", userType);
                startActivity(i);
                finish();
                resetZoomAnimation(image);


                // Start the next activity here, e.g., MainActivity
            }
        }, 400);
    }
}