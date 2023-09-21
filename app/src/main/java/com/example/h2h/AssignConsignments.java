package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignConsignments extends AppCompatActivity {
    RecyclerView rvAdminConsignments;
    DatabaseReference dbRef;
    TextView tvAssignConsignments;
    DatabaseReference dbRef1;
    ArrayList<Rider>riders;


    ArrayList<Consignment>consignments;
    AdminConsignmentAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_consignments);
        tvAssignConsignments = findViewById(R.id.tvAssignConsignments);

        consignments = new ArrayList<>();
        riders = new ArrayList<>();

        rvAdminConsignments = findViewById(R.id.rvAdminConsignments);
        linearLayoutManager = new LinearLayoutManager(this);
        rvAdminConsignments.setLayoutManager(linearLayoutManager);

        // Initialize and set the adapter to the RecyclerView
        adapter = new AdminConsignmentAdapter(this, consignments, riders);
        rvAdminConsignments.setAdapter(adapter);

        getAllData();

    }

    private void getAllData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("consignment");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                consignments.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot consignmentSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot consignmentSnapshot1 : consignmentSnapshot.getChildren()) {
                        Consignment consignment = consignmentSnapshot1.getValue(Consignment.class);
                        if (consignment != null && consignment.getAssignedto() != null && consignment.getAssignedto().isEmpty()) {
                            consignments.add(consignment);
                        }
                    }
                }

                // Notify the adapter that the data has changed
                if (consignments.size() == 0)
                    tvAssignConsignments.setText("No consignments to assign...");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        dbRef1 = FirebaseDatabase.getInstance().getReference().child("Riders");
        dbRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                riders.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Rider rider = snap.getValue(Rider.class);
                    if (rider != null) {
                        riders.add(rider);
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }
}