package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditAdminBookedConsignment extends AppCompatActivity {
    private String rider;
    private TextView tvSenderAdmin;

    private TextView tvReceiverAdmin;
    private TextView tvBookedTimeAdmin;
    private TextView tvBookedDateAdmin;
    private TextView tvStatusAdmin;
    private TextView tvWeightAdmin;
    private TextView tvItemCategoryAdmin;
    private TextView tvItemQuantityAdmin;

    ArrayList<Rider> riders;

    RiderSpinnerAdapter riderSpinnerAdapter;
    private Consignment consignment;

    private String conId;
    private String userid;

    private Button btnSaveConsignment, btnDiscardConsignment;

    Spinner SpinnerRiders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_booked_consignment);
        init();
        getDataFromIntent();
        getRiderList();
        listener();
    }

    private void getDataFromIntent() {
        conId = getIntent().getStringExtra("id");
        getConsignmentData();
    }

    private void setRiders() {
        riderSpinnerAdapter = new RiderSpinnerAdapter(EditAdminBookedConsignment.this, android.R.layout.simple_spinner_item, riders);
        riderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerRiders.setAdapter(riderSpinnerAdapter);
        riderSpinnerAdapter.notifyDataSetChanged();
    }


    private void getRiderList() {
        riders = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Riders");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rider rider = dataSnapshot.getValue(Rider.class);
                    if (rider != null)
                        riders.add(rider);
                }
                if (riders.size() == 0) {
                    Toast.makeText(EditAdminBookedConsignment.this, "No Available Riders.", Toast.LENGTH_SHORT).show();
                }
                setRiders();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void listener() {
        btnSaveConsignment.setOnClickListener(view -> {
            Rider selectedRider = (Rider) SpinnerRiders.getSelectedItem();
            consignment.setAssignedto(selectedRider.getId());
            consignment.setRider(selectedRider.getId());
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("consignment").child(userid).child(conId);
            db.setValue(consignment);
            Toast.makeText(EditAdminBookedConsignment.this, "Consignment Assigned to Rider", Toast.LENGTH_SHORT).show();
            navigateToAdminHome();
        });
        btnDiscardConsignment.setOnClickListener(view -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("consignment").child(userid).child(conId);
            db.removeValue();
            Toast.makeText(EditAdminBookedConsignment.this, "Consignment Discarded", Toast.LENGTH_SHORT).show();
            navigateToAdminHome();
        });
    }

    private void navigateToAdminHome() {
        startActivity(new Intent(EditAdminBookedConsignment.this, AdminHome.class));
        finish();
    }

    private void getConsignmentData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("consignment").child(userid).child(conId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consignment = snapshot.getValue(Consignment.class);
                if (consignment != null) {
                    tvSenderAdmin.setText(consignment.getSenderAddress());
                    tvReceiverAdmin.setText(consignment.getReceiverName());
                    tvBookedTimeAdmin.setText(consignment.getTime());
                    tvBookedDateAdmin.setText(consignment.getPickupDateTime());
                    tvStatusAdmin.setText(consignment.getStatus());
                    tvWeightAdmin.setText("To be Added by (Rider)");
                    tvItemCategoryAdmin.setText(consignment.getItemCategory());
                    tvItemQuantityAdmin.setText(consignment.getItemQuantity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditAdminBookedConsignment.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        userid = FirebaseAuth.getInstance().getUid();
        SpinnerRiders = findViewById(R.id.SpinnerRiders);
        tvSenderAdmin = findViewById(R.id.tvSenderAdmin);
        tvReceiverAdmin = findViewById(R.id.tvReceiverAdmin);
        tvBookedTimeAdmin = findViewById(R.id.tvBookedTimeAdmin);
        tvBookedDateAdmin = findViewById(R.id.tvBookedDateAdmin);
        tvStatusAdmin = findViewById(R.id.tvStatusAdmin);
        tvWeightAdmin = findViewById(R.id.tvWeightAdmin);
        tvItemCategoryAdmin = findViewById(R.id.tvItemCategoryAdmin);
        tvItemQuantityAdmin = findViewById(R.id.tvItemQuantityAdmin);
        btnSaveConsignment = findViewById(R.id.btnSaveConsignment);
        btnDiscardConsignment = findViewById(R.id.btnDiscardConsignment);
    }
}