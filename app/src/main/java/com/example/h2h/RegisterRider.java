package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegisterRider extends AppCompatActivity {

    EditText etRiderName, etRiderPassword, etRiderPassword2, etRiderCnic, etRiderPhone, etRiderAddress, etRiderEmail;
    Button btnRegisterRider, btnUploadCnic;

    ImageView ivUploaded;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 100;

    Rider rider;
    ProgressBar riderRegistrationPgBar;

    String name,email, password, password2, cnic, phone, address,url;



    FirebaseAuth mAuth;


    DatabaseReference db;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_rider);
        init();
        btnRegisterRider.setOnClickListener(view -> getData());
        btnUploadCnic.setOnClickListener(view -> uploadCnic());



    }
    private void uploadCnic(){
        storageReference = FirebaseStorage.getInstance().getReference();
        riderRegistrationPgBar.setVisibility(View.VISIBLE);
        btnUploadCnic.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Create a reference to a specific location in Firebase Storage
            StorageReference imageRef = storageReference.child("images/" + selectedImageUri.getLastPathSegment());

            // Upload the image to Firebase Storage
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Handle the download URL (e.g., save it to Firebase Database, display it, etc.)
                                    String downloadUrl = uri.toString();
                                    url = downloadUrl;
                                    ivUploaded.setVisibility(View.VISIBLE);
                                    btnUploadCnic.setVisibility(View.GONE);
                                    riderRegistrationPgBar.setVisibility(View.GONE);


                                    Toast.makeText(RegisterRider.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ivUploaded.setVisibility(View.GONE);
                                    riderRegistrationPgBar.setVisibility(View.GONE);

                                    btnUploadCnic.setVisibility(View.VISIBLE);
                                    // Handle any errors that occurred during the download URL retrieval
                                    Toast.makeText(RegisterRider.this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            riderRegistrationPgBar.setVisibility(View.GONE);
                            ivUploaded.setVisibility(View.GONE);
                            riderRegistrationPgBar.setVisibility(View.GONE);
                            btnUploadCnic.setVisibility(View.VISIBLE);

                            // Handle any errors that occurred during the image upload
                            Toast.makeText(RegisterRider.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }







    private void getData(){

        if (etRiderAddress.getText().toString().isEmpty()|| etRiderName.getText().toString().isEmpty() || etRiderPassword.getText().toString().isEmpty() || etRiderPassword2.getText().toString().isEmpty() || etRiderCnic.getText().toString().isEmpty() || etRiderPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter all fields to register", Toast.LENGTH_SHORT).show();
        }else if(etRiderPhone.getText().toString().length()!= 11){
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            etRiderPhone.setText("");
            etRiderPhone.setError("Invalid Phone Number");
        }else if(etRiderPassword.getText().toString().length()<8){
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            etRiderPassword.setText("");
            etRiderPassword.setError("Password must be at least 8 characters");
        }else if(!etRiderPassword.getText().toString().equals(etRiderPassword2.getText().toString())){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            etRiderPassword.setText("");
            etRiderPassword.setError("Passwords do not match");
            etRiderPassword2.setText("");
            etRiderPassword2.setError("Passwords do not match");
        }else if(etRiderCnic.getText().toString().length()!=13){
            Toast.makeText(this, "Cnic must be at least 13 characters", Toast.LENGTH_SHORT).show();
            etRiderCnic.setText("");
            etRiderCnic.setError("Cnic must be at least 13 characters");

        }else if(etRiderEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter email to register", Toast.LENGTH_SHORT).show();
            etRiderEmail.setError("Enter email to register");
            etRiderEmail.setText("");
            etRiderEmail.requestFocus();
        }
        else{
            email = etRiderEmail.getText().toString();
            address=etRiderAddress.getText().toString();
            name = etRiderName.getText().toString();
            password = etRiderPassword.getText().toString();
            password2 = etRiderPassword2.getText().toString();
            cnic = etRiderCnic.getText().toString().trim();
            phone = etRiderPhone.getText().toString();
            rider = new Rider(name, phone, email, address, password,cnic,url,false,"","Pending");
            registerOnFirebase(email, password);
        }
    }
    private void init(){


        ivUploaded = findViewById(R.id.ivUploaded);
        ivUploaded.setVisibility(View.GONE);
        riderRegistrationPgBar = findViewById(R.id.riderRegistrationPgBar);
        riderRegistrationPgBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        etRiderAddress = findViewById(R.id.etRiderAddress);
        etRiderName = findViewById(R.id.etConsignmentWeight);
        etRiderPassword = findViewById(R.id.etRiderPassword);
        etRiderEmail = findViewById(R.id.etRiderEmail);
        etRiderPassword2 = findViewById(R.id.etRiderPassword2);
        etRiderCnic = findViewById(R.id.etRiderCnic);
        etRiderPhone = findViewById(R.id.etRiderPhone);
        btnRegisterRider = findViewById(R.id.btnRegisterRider);
        btnUploadCnic = findViewById(R.id.btnUploadCnic);



    }
    private void navigate(){
        Intent intent = new Intent(RegisterRider.this, RiderFirstLogin.class);
        startActivity(intent);
        finish();
    }
    private void registerOnFirebase(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegisterRider.this, "User registered", Toast.LENGTH_SHORT).show();
                sendToDatabase(rider);
                navigate();
            }else{
                Toast.makeText(RegisterRider.this, "Error in registration", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendToDatabase(Rider rider){
       db = FirebaseDatabase.getInstance().getReference().child("Riders");
        db.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(rider);
    }





}