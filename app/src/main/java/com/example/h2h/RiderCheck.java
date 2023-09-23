package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RiderCheck extends AppCompatActivity {
    TextView tvRiderAccountStatus;
    LottieAnimationView lottieView;

    Button btnLogoutRider;

    boolean RiderAccountStatus;

    DatabaseReference riderAccountStatusRef;

    Rider rider;

    Button btnDeleteAccount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_check);
        init();
        btnLogoutRider.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(RiderCheck.this, "Logged Out", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(RiderCheck.this, SplashScreen2.class));
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().delete();
                DatabaseReference riderRef = FirebaseDatabase.getInstance().getReference().child("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                riderRef.removeValue();
                Toast.makeText(RiderCheck.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(RiderCheck.this, RegisterRider.class);
                startActivity(intent);
                finish();

            }
        });

        }
    private void setRiderAccountStatus(){
        if(RiderAccountStatus && rider.getStatus().equals("Approved")){
            tvRiderAccountStatus.setText("Rider Account Verified.");
            lottieView.setAnimation(R.raw.verifedrider);
            lottieView.playAnimation();
            tvRiderAccountStatus.setText("Navigating you to Rider Pannel");
            tvRiderAccountStatus.setTextColor(getResources().getColor(R.color.appblue));
            Intent intent = new Intent(RiderCheck.this, RiderHome.class);
            startActivity(intent);
            finish();
        }else if(!RiderAccountStatus && (rider.getStatus() == null || rider.getStatus().equals("Pending"))) {
            btnLogoutRider.setVisibility(View.VISIBLE);
            tvRiderAccountStatus.setText("Rider Account is still in verification process..");
            lottieView.setAnimation(R.raw.notverified);
            lottieView.playAnimation();
        }else if(!RiderAccountStatus && rider.getStatus().equals("Rejected")){
            tvRiderAccountStatus.setText("Rider Account is rejected.");
            lottieView.setAnimation(R.raw.rejected);
            lottieView.playAnimation();
            btnDeleteAccount.setVisibility(View.VISIBLE);

        }
    }

    private void checkRiderAccountStatus() {
        riderAccountStatusRef = FirebaseDatabase.getInstance().getReference().child("Riders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        riderAccountStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                rider = snapshot.getValue(Rider.class);
                RiderAccountStatus = rider.isVerificationStatus();
                setRiderAccountStatus();
            }else{
                    Toast.makeText(RiderCheck.this, "Your account is not registered as rider account", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RiderCheck.this, RegisterRider.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RiderCheck.this, "Could not Retreive Data.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void init(){

        btnLogoutRider = findViewById(R.id.btnLogoutRider);
        btnDeleteAccount=findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setVisibility(View.GONE);
        btnLogoutRider.setVisibility(View.GONE);
        tvRiderAccountStatus = findViewById(R.id.tvRiderAccountStatus);
        lottieView = findViewById(R.id.lottieView);
        checkRiderAccountStatus();

    }
}