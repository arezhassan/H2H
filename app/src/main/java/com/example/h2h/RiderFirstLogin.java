package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RiderFirstLogin extends AppCompatActivity {
    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_first_login);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Riders");
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("id").setValue(mAuth.getCurrentUser().getUid());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

             Intent i=new Intent(RiderFirstLogin.this,MainActivity.class);
             i.putExtra("userType","rider");
                finish();
                mAuth.signOut();

            }
        }, 4000);



    }


}