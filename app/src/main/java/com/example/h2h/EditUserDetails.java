package com.example.h2h;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserDetails extends AppCompatActivity {
    String name,phone,email,userId;
    EditText editName, editPhone, editEmail;

    Button btnSaveNewDetails;

    ImageView ivBackFromEditUser;
    Boolean save;

    FirebaseAuth auth;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        getIntentData();
        init();
        TextWatcher();
        listener();
    }
    private void listener(){
        btnSaveNewDetails.setOnClickListener(v -> {
            save=true;
            saveData();
            btnSaveNewDetails.setAlpha(0.6f);
            btnSaveNewDetails.setEnabled(false);
        });
        ivBackFromEditUser.setOnClickListener(v -> {
            finish();
        });

    }

    private void saveData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        db.child("name").setValue(editName.getText().toString());
        double ph=Double.parseDouble(editPhone.getText().toString());
        db.child("phone").setValue(ph);
        db.child("email").setValue(editEmail.getText().toString());
        user.updateEmail(email);
        btnSaveNewDetails.setAlpha(0.6f);
        btnSaveNewDetails.setEnabled(false);
    }


    private void init(){
        editName = findViewById(R.id.etEditName);
        editPhone = findViewById(R.id.etEditPhone);
        editEmail = findViewById(R.id.etEditEmail);
        ivBackFromEditUser=findViewById(R.id.ivBackFromEditUser);
        btnSaveNewDetails = findViewById(R.id.btnSaveNewDetails);
        btnSaveNewDetails.setEnabled(false);
        save=false;
        editEmail.setText(email);
        editPhone.setText(phone);
        editName.setText(name);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    private void getIntentData(){
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        userId = getIntent().getStringExtra("userId");
    }
    private boolean isNewData(){
        String newemail=editEmail.getText().toString();
        String newphone=editPhone.getText().toString();
        String newname=editName.getText().toString();
        return !newemail.equals(email) || !newphone.equals(phone) || !newname.equals(name);
    }
    private void TextWatcher(){
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonOpacity();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editName.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonOpacity();

            }


            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        editPhone.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonOpacity();

            }


            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
    private void setButtonOpacity(){
        if(isNewData()){
            btnSaveNewDetails.setAlpha(1f);
            btnSaveNewDetails.setEnabled(true);
        }else if(isSaved() && !isNewData()){
            btnSaveNewDetails.setAlpha(0.5f);
            btnSaveNewDetails.setEnabled(false);
        }

    }
    private boolean isSaved(){
        return save;
    }

}