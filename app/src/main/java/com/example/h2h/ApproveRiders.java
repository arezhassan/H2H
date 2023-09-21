package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApproveRiders extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<Rider> riders;
    DatabaseReference db;
    TextView tvApproveRiders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_riders);
        init();
        getData();

    }
    private void setRecyclerView(){
        rv.setAdapter(new RiderApprovalAdapter(this,riders));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
    private void getData(){
        db = FirebaseDatabase.getInstance().getReference().child("Riders");
        db.orderByChild("verificationStatus").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                riders.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rider rider = dataSnapshot.getValue(Rider.class);
                    riders.add(rider);
                }
                setRecyclerView();
                if (riders.size() == 0)
                    tvApproveRiders.setText("No New Riders to Approve");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });


    }

    private void init(){
        tvApproveRiders=findViewById(R.id.tvApproveRiders);
        rv = findViewById(R.id.rv);
        db= FirebaseDatabase.getInstance().getReference();
        riders=new ArrayList<>();


    }
}