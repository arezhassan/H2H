package com.example.h2h;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation fadein;
    Animation slide_down;
    Animation slide_up;
    TextView tvMotto, tvH2H;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        slide_down = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
       // tvMotto = findViewById(R.id.tvMotto);
     //   tvH2H = findViewById(R.id.tvH2H);
        imgLogo = findViewById(R.id.imgLogo);

        //    tvMotto.startAnimation(fadein);
   //     tvH2H.startAnimation(fadein);
        imgLogo.startAnimation(fadein);

        // Delayed transition to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here, e.g., MainActivity
                Intent intent = new Intent(SplashScreen.this, SplashScreen2.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity to prevent going back to it.
            }
        }, 1200); // 2000 milliseconds (2 seconds)
    }
}
