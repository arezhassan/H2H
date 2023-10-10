package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ConsignmentBookedAnimation extends AppCompatActivity {
    LottieAnimationView anim;
    String userType;
    String conId, distance;

    TextView tvAnimationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_booked_animation);
        if (getIntent().getStringExtra("userType")!=null) {
             userType = getIntent().getStringExtra("userType");
             conId = getIntent().getStringExtra("id");
             distance=getIntent().getStringExtra("distance");
             tvAnimationMessage = findViewById(R.id.tvAnimationMessage);
            if (userType.equals("Admin")){
                tvAnimationMessage.setText("Consignment Booked. Navigating to next pannel..");
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here, e.g., MainActivity
                Intent intent;// Finish the splash screen activity to prevent going back to it.
                if (userType!=null && !userType.isEmpty() && userType.equals("Admin")) {
                    intent = new Intent(ConsignmentBookedAnimation.this, EditAdminBookedConsignment.class);
                    intent.putExtra("id",conId);
                    intent.putExtra("senderName", getIntent().getStringExtra("senderName"));
                    intent.putExtra("distance",distance);
                } else {
                    intent = new Intent(ConsignmentBookedAnimation.this, MainActivity.class);
                }
                startActivity(intent);
                finish(); // Finish the splash screen activity to prevent going back to it.
            }
        }, 2000); // 2000 milliseconds (2 seconds)
    }
}