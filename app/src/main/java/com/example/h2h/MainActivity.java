package com.example.h2h;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etPass, etReg;
    FirebaseAuth mAuth;

    TextView tvUserType;
    ProgressBar progressBar;

    String userType;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor; // Initialize SharedPreferences.Editor

    TextView tvRegister, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences and SharedPreferences.Editor
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit(); // Initialize editor

        userType = getIntent().getStringExtra("userType");

        // If userType is provided via intent, save it to SharedPreferences
        if (!TextUtils.isEmpty(userType)) {
            editor.putString("userType", userType);
            editor.apply();
        }

        init();
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etReg.getText().toString().trim().isEmpty()){
                mAuth.sendPasswordResetEmail(etReg.getText().toString().trim());
            }else{
                    Toast.makeText(MainActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    etReg.setText("");
                    etReg.requestFocus();
                    etReg.setError("Please enter your email.");
                }
            }
        });

        // Retrieve userType from SharedPreferences
        userType = sharedPreferences.getString("userType", "user");

        // Set UI based on userType
        setUIForUserType(userType);

        progressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });





    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is already authenticated, check the userType
            if (userType.equals("rider")) {
                // Navigate to RiderCheck.class
                Intent intent = new Intent(MainActivity.this, RiderCheck.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish(); // Close MainActivity if navigating to RiderCheck
            } else if (userType.equals("user")){
                Intent intent = new Intent(MainActivity.this, Home.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
            }
        }
    }

    private void init() {
        tvUserType = findViewById(R.id.tvUserType);
        btnLogin = findViewById(R.id.btnLogin);
        etPass = findViewById(R.id.etPass);
        etReg = findViewById(R.id.etReg);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setUIForUserType(String userType) {
        if ("admin".equals(userType)) {
            tvUserType.setText("Admin Login");
            tvRegister.setVisibility(View.GONE);
        } else if ("rider".equals(userType)) {
            tvUserType.setText("Rider Login");
        } else {
            tvUserType.setText("Login");
        }
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        String pass = etPass.getText().toString().trim();
        String reg = etReg.getText().toString().trim();

        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(reg)) {
            showError("Both fields are required.");
            return;
        }

        if (pass.length() < 8 || reg.length() < 8) {
            showError("Password and Registration must be at least 8 characters.");
            return;
        }

        mAuth.signInWithEmailAndPassword(reg, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            startNextActivity(userType);
                        } else {
                            showError("Login failed. Please check your credentials.");
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void startNextActivity(String userType) {
        Intent intent;
        if ("rider".equals(userType)) {
            intent = new Intent(MainActivity.this, RiderCheck.class);
            intent.putExtra("userType", userType);
            startActivity(intent);
            finish();
        } else if("user".equals(userType)) {
            intent = new Intent(MainActivity.this, Home.class);
            intent.putExtra("userType", userType);
            startActivity(intent);
            finish();
        }else if("admin".equals(userType)){
            intent = new Intent(MainActivity.this, AdminCheck.class);
            intent.putExtra("userType", userType);
            intent.putExtra("email",etReg.getText().toString().trim());
            intent.putExtra("password",etPass.getText().toString().trim());
            startActivity(intent);
            finish();
        }

    }

    private void startRegistration() {
        Intent intent;
        if ("rider".equals(userType)) {
            intent = new Intent(MainActivity.this, RegisterRider.class);
        } else if("admin".equals(userType)) {
            intent = new Intent(MainActivity.this, Register.class);
        }else{
            intent = new Intent(MainActivity.this, Register.class);
        }
        startActivity(intent);
    }

    private void showError(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
