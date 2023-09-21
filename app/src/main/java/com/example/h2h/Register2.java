package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2 extends AppCompatActivity {
    Button btnRegister;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    ProgressBar pgBAR;
    String name, email, password, phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        init();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        phone = intent.getStringExtra("phone");
        startCountdownTimer();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginTask -> {
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                            if (emailTask.isSuccessful()) {
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                String userId = mAuth.getCurrentUser().getUid();
                                User userObj = new User(null, name, email, Double.parseDouble(phone));
                                usersRef.child(userId).setValue(userObj);
                                Toast.makeText(Register2.this, "Verification email sent to " + email, Toast.LENGTH_SHORT).show();
                                mUser = FirebaseAuth.getInstance().getCurrentUser();
                                mUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
                                pgBAR.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(Register2.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(Register2.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Register2.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent1 = new Intent(Register2.this, Register3.class);
            startActivity(intent1);
            finish();
        });
    }

    private void init() {
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        timerTextView = findViewById(R.id.timerTextView);
        pgBAR = findViewById(R.id.pgBAR);
        pgBAR.setVisibility(View.GONE);
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                timerTextView.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.GONE);
            }

            public void onFinish() {
                timerTextView.setText("Resend Email?");
                btnRegister.setVisibility(View.VISIBLE);
                timerTextView.setOnClickListener(view -> {
                    timerTextView.setText("Email Sent. Make sure to check your spam folder as well.");
                    mUser.sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            countDownTimer = new CountDownTimer(30000, 1000) {
                                @Override
                                public void onTick(long l) {
                                    timerTextView.setText(String.valueOf(l / 1000));
                                    timerTextView.setVisibility(View.VISIBLE);
                                    btnRegister.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFinish() {
                                    timerTextView.setText("You have reached the verification limit. Please try again in a few minutes.");
                                    btnRegister.setVisibility(View.VISIBLE);
                                }
                            };
                            countDownTimer.start();
                            Toast.makeText(Register2.this, "Verification email sent to " + email, Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        };
        countDownTimer.start();
    }
}
