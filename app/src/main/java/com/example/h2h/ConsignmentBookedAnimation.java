package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class ConsignmentBookedAnimation extends AppCompatActivity {
    LottieAnimationView anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_booked_animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here, e.g., MainActivity
                Intent intent = new Intent(ConsignmentBookedAnimation.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity to prevent going back to it.
            }
        }, 2000); // 2000 milliseconds (2 seconds)
    }
}