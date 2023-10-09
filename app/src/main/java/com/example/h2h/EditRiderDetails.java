package com.example.h2h;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditRiderDetails extends AppCompatActivity {
    String name, phone, userId;
    EditText editName, editPhone;

    Button btnSaveNewDetails;
    Boolean save;

    ImageView ivBackFromEditRider;

    FirebaseAuth auth;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rider_details);
        getIntentData();
        init();
        TextWatcher();
        listener();
    }

    private void listener() {
        btnSaveNewDetails.setOnClickListener(v -> {
            save = true;
            saveData();
            btnSaveNewDetails.setAlpha(0.6f);
            btnSaveNewDetails.setEnabled(false);
        });
        ivBackFromEditRider.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Riders").child(userId);
        db.child("name").setValue(editName.getText().toString());
        String ph= editPhone.getText().toString();
        db.child("phone").setValue(ph);
        btnSaveNewDetails.setAlpha(0.6f);
        btnSaveNewDetails.setEnabled(false);
    }


    private void init() {
        ivBackFromEditRider = findViewById(R.id.ivBackFromEditRider);
        editName = findViewById(R.id.etRiderEditName);
        editPhone = findViewById(R.id.etRiderEditPhone);
        btnSaveNewDetails = findViewById(R.id.btnSaveNewDetails);
        btnSaveNewDetails.setEnabled(false);
        save = false;
        editPhone.setText(phone);
        editName.setText(name);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void getIntentData() {
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        userId = getIntent().getStringExtra("userId");
    }


    private boolean isNewData() {
        String newphone = editPhone.getText().toString();
        String newname = editName.getText().toString();
        return !newphone.equals(phone) || !newname.equals(name);
    }

    private void TextWatcher() {

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

    private void setButtonOpacity() {
        if (isNewData()) {
            btnSaveNewDetails.setAlpha(1f);
            btnSaveNewDetails.setEnabled(true);
        } else if (isSaved() && !isNewData()) {
            btnSaveNewDetails.setAlpha(0.5f);
            btnSaveNewDetails.setEnabled(false);
        }

    }

    private boolean isSaved() {
        return save;
    }

}