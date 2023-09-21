package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {
    TextView tvWelcomeAdmin, tvNumberOfRiders, tvNumberOfConsignments;
    Button btnViewRiders, btnAssignConsignments, btnApproveRiders, btnLogoutAdmin;
    String email;

    CardView cardview;

    FirebaseAuth mAuth;
    ArrayList<Consignment> consignments;

    ProgressBar progressBar3;
    ArrayList<Rider> riders;

    DatabaseReference ConsignmentsRef, RidersRef;
    @Override
    protected void onStart() {
        super.onStart();
        getAdmin();
        getData();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        init();
        getAdmin();
        getData();
    }
    private void getData(){
        consignments.clear();
        ConsignmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot consignmentSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot consignmentSnapshot1 : consignmentSnapshot.getChildren()) {
                        Consignment consignment = consignmentSnapshot1.getValue(Consignment.class);
                        if (consignment != null && consignment.getAssignedto() != null && consignment.getAssignedto().isEmpty()) {
                            consignments.add(consignment);
                        }
                    }
                }
                tvNumberOfConsignments.setText("Number of Un-Assigned Consignments: "+String.valueOf(consignments.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        riders.clear();
        RidersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Rider rider=dataSnapshot.getValue(Rider.class);
                    if(rider!=null && !rider.isVerificationStatus()){
                    riders.add(rider);
                }}
                tvNumberOfRiders.setText("Number of Un-Approved Riders: "+String.valueOf(riders.size()));
                progressBar3.setVisibility(View.GONE);
                cardview.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void init(){
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.VISIBLE);
        cardview = findViewById(R.id.cardview);
        cardview.setVisibility(View.GONE);
        riders= new ArrayList<>();
        consignments= new ArrayList<>();
        tvWelcomeAdmin = findViewById(R.id.tvWelcomeAdmin);
        tvNumberOfRiders = findViewById(R.id.tvNumberOfRiders);
        tvNumberOfConsignments = findViewById(R.id.tvNumberOfConsignments);
        btnViewRiders = findViewById(R.id.btnViewRiders);
        btnAssignConsignments = findViewById(R.id.btnAssignConsignments);
        btnApproveRiders = findViewById(R.id.btnApproveRiders);
        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);
        ConsignmentsRef = FirebaseDatabase.getInstance().getReference().child("consignment");
        RidersRef = FirebaseDatabase.getInstance().getReference().child("Riders");
        mAuth = FirebaseAuth.getInstance();
        setListeners();

    }
    private void setListeners(){
        btnViewRiders.setOnClickListener(v -> navigate("ViewRiders"));
        btnAssignConsignments.setOnClickListener(v -> navigate("AssignConsignments"));
        btnApproveRiders.setOnClickListener(v -> navigate("ApproveRiders"));
        btnLogoutAdmin.setOnClickListener(v -> navigate("LogoutAdmin"));
    }
    private void navigate(String activity){
        if(activity.equals("ViewRiders")){
            Intent intent = new Intent(AdminHome.this, ViewRiders.class);
            startActivity(intent);
        }
        else if(activity.equals("AssignConsignments")){
            Intent intent = new Intent(AdminHome.this, AssignConsignments.class);
            startActivity(intent);
        } else if(activity.equals("ApproveRiders")){
            Intent intent = new Intent(AdminHome.this, ApproveRiders.class);
            startActivity(intent);
        } else if(activity.equals("LogoutAdmin")){
            mAuth.signOut();
            Intent intent = new Intent(AdminHome.this, SplashScreen2.class);
            startActivity(intent);
            finish();
        }


    }
    private void getAdmin(){
        Intent intent = getIntent();
        email=intent.getStringExtra("email");
        tvWelcomeAdmin.setText(email);

    }
}