package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register3 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        mAuth = FirebaseAuth.getInstance();
        flag=true;
        user = mAuth.getCurrentUser();

        if(user!=null&& user.isEmailVerified()){
            flag=true;
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    startActivity(new Intent(Register3.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(Register3.this, Register2.class));
                }

            }
        }, 2000);




    }
}