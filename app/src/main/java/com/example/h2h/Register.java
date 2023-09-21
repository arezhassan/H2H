package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    Button btnNext;
    EditText etName, etEmail, etPassword, etConfirmPassword, etPhone;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btnNext.setOnClickListener(v -> {

            if (etName.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty()) {
                etName.setError("Please enter all the details");
                etEmail.setError("Please enter all the details");
                etPassword.setError("Please enter all the details");
                etConfirmPassword.setError("Please enter all the details");
            } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etPassword.setError("Passwords do not match");
                etConfirmPassword.setError("Passwords do not match");
            }else if(etPhone.getText().toString().isEmpty() || etPhone.getText().toString().length()!=11)
            {
                etPhone.setError("Please Enter a Valid Phone Number");
                Toast.makeText(this, "Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String phone = etPhone.getText().toString();

                phone=phone.substring(1,11);
                String ph;
                ph=("+92"+ phone);
                        Intent i=new Intent(Register.this,Register2.class);
                        i.putExtra("name",name);
                        i.putExtra("email",email);
                        i.putExtra("phone",ph);
                        i.putExtra("password",password);
                        startActivity(i);
                        finish();



            }
        });
    }
            private void init () {
                btnNext = findViewById(R.id.btnNext);
                etName = findViewById(R.id.etConsignmentWeight);
                etEmail = findViewById(R.id.etEmail);
                etPassword = findViewById(R.id.etRegPass);
                etConfirmPassword = findViewById(R.id.etRegPass2);
                etPhone = findViewById(R.id.etPhone);
                mAuth = FirebaseAuth.getInstance();
            }
        }