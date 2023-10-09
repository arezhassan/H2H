package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    TextView tvSTATUS, tvTIME, tvID;
    Button btnViewRiders, btnAssignConsignments, btnApproveRiders, btnLogoutAdmin,btnAddNewConsignment;
    String email;

    CardView cardview;

    FirebaseAuth mAuth;
    ArrayList<Consignment> consignments;
    ArrayList<Consignment> allconsignments;


    int count=0;

    ProgressBar progressBar3;
    ArrayList<Rider> riders;

    CardView RecentConsignmentCard;

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
        listener();
        getAdmin();
        getData();
    }

    private void listener() {
        RecentConsignmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminHome.this, AdminAllConsignments.class);
                i.putParcelableArrayListExtra("consignments",allconsignments);
                startActivity(i);

            }
        });
        btnAddNewConsignment.setOnClickListener(view -> {
            Intent i=new Intent(AdminHome.this, AdminBookConsignment.class);
            i.putParcelableArrayListExtra("consignments",consignments);
            startActivity(i);
        });

    }

    private void getData(){
        consignments.clear();
        ConsignmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count=0;
                for (DataSnapshot consignmentSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot consignmentSnapshot1 : consignmentSnapshot.getChildren()) {
                        Consignment consignment = consignmentSnapshot1.getValue(Consignment.class);
                        if(consignment!=null){
                            allconsignments.add(consignment);
                        }
                        if (consignment != null && consignment.getAssignedto() != null && consignment.getAssignedto().isEmpty()) {
                            consignments.add(consignment);
                            count++;

                        }
                    }
                }
                //Setting up most recent consigment
                if(allconsignments.size()>0) {
                    tvID.setText("ID: " + allconsignments.get(0).getId());
                    tvSTATUS.setText("Status: " + allconsignments.get(0).getStatus());
                    tvTIME.setText("Time: " + allconsignments.get(0).getTime() + " Date: " + allconsignments.get(0).getPickupDateTime());
                }
                Log.d("Consignments", String.valueOf(consignments.size()));
                //setting up number of consignments
                tvNumberOfConsignments.setText("Number of Un-Assigned Consignments: "+String.valueOf(count));
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
                    if(rider!=null && rider.getStatus().equals("Pending")){
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
        RecentConsignmentCard = findViewById(R.id.RecentConsignmentCard);
        allconsignments= new ArrayList<>();
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
        btnAddNewConsignment = findViewById(R.id.btnAddNewConsignment);
        tvID=findViewById(R.id.tvID);
        tvSTATUS=findViewById(R.id.tvSTATUS);
        tvTIME=findViewById(R.id.tvTIME);
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