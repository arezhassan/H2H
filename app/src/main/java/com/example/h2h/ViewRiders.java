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

public class ViewRiders extends AppCompatActivity {

    RecyclerView rvViewRiders;
    ArrayList<Rider>riders;
    TextView tvViewRiders;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_riders);
        init();
        getData();

    }
    private void setRecyclerView(){
        rvViewRiders.setAdapter(new ViewRidersAdapter(this,riders));
        rvViewRiders.setLayoutManager(new LinearLayoutManager(this));

    }
    private void getData(){
        db=FirebaseDatabase.getInstance().getReference().child("Riders");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                riders.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Rider rider=dataSnapshot.getValue(Rider.class);
                    riders.add(rider);
                }
                setRecyclerView();
                if (riders.size() == 0)
                    tvViewRiders.setText("No Riders to Show.. ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewRiders.this, "Error Fetching Data. Check your internet connection..", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void init(){
        tvViewRiders=findViewById(R.id.tvViewRiders);
        rvViewRiders = findViewById(R.id.rvViewRiders);
        db= FirebaseDatabase.getInstance().getReference();
        riders=new ArrayList<>();


    }
}