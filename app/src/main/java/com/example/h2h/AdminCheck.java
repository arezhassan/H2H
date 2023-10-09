package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class AdminCheck extends AppCompatActivity {
    DatabaseReference databaseReference;
    String email, password;

    TextView tvVerifyAdmin;

    Admin admin;

    LottieAnimationView adminCheckAnimation;

    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromPreviousActivity();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check);
        mAuth = FirebaseAuth.getInstance();
        adminCheckAnimation = findViewById(R.id.adminCheckAnimation);
        tvVerifyAdmin = findViewById(R.id.tvVerifyAdmin);
        getDataFromPreviousActivity();
        getAdminFromDB(email, password);

    }

    private void checkAdmin(String email, String password) {
        if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
            adminCheckAnimation.setAnimation(R.raw.verifedrider);
            adminCheckAnimation.playAnimation();

            tvVerifyAdmin.setText("Admin Credentials Verified.");
            tvVerifyAdmin.setTextColor(getResources().getColor(R.color.green));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AdminCheck.this, AdminHome.class);
                    i.putExtra("email", email);
                    i.putExtra("password", password);
                    startActivity(i);
                    finish();
                    // Start the next activity here, e.g., MainActivity
                }
            }, 2000);


        } else {
            adminCheckAnimation.setAnimation(R.raw.notverified);
            adminCheckAnimation.playAnimation();
            tvVerifyAdmin.setText("Admin Credentials Couldn't be verified.");
            tvVerifyAdmin.setTextColor(getResources().getColor(R.color.red));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AdminCheck.this, MainActivity.class);
                    i.putExtra("email", email);
                    mAuth.signOut();
                    startActivity(i);
                    finish();
                    // Start the next activity here, e.g., MainActivity
                }
            }, 2000);


        }

    }

    private void getAdminFromDB(String email, String password) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    admin = snapshot.getValue(Admin.class);
                    // Now that you have the admin object, you can check it
                    checkAdmin(email, password);
                } else {
                    // Handle the case where admin data doesn't exist
                    Toast.makeText(AdminCheck.this, "Admin data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminCheck.this, "Error Checking Admin..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminCheck.this, MainActivity.class));
                mAuth.signOut();
                finish();
            }
        };

        // Determine the admin node based on the email
        String adminNode;
        if (email.equals("admin@h2h.com")) {
            adminNode = "admin1";
        } else if (email.equals("admin2@h2h.com")) {
            adminNode = "admin2";
        } else {
            // Handle other cases or invalid emails here
            return;
        }

        // Add the listener to the specific admin node
        databaseReference.child(adminNode).addListenerForSingleValueEvent(listener);
    }





    private void getDataFromPreviousActivity(){
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        if (email == null || password == null && email.isEmpty() || password.isEmpty()) {
            email=intent.getStringExtra("email1");
            password=intent.getStringExtra("password1");
        }
    }
}