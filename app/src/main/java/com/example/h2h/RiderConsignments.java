package com.example.h2h;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class RiderConsignments extends Fragment {

    RecyclerView rvRiderConsignments;
    View view;
    RiderConsignmentAdapter adapter;
    ArrayList<Consignment> consignments;
    FirebaseAuth mAuth;

    TextView tvAssignedConsignments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consignments = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rider_consignments, container, false);
        rvRiderConsignments = view.findViewById(R.id.rvRiderConsignments);
        tvAssignedConsignments = view.findViewById(R.id.tvAssignedConsignments);
        adapter = new RiderConsignmentAdapter(consignments, getActivity());
        rvRiderConsignments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRiderConsignments.setAdapter(adapter);


        // Call getAllData to populate the consignments list
        getAllData();


        return view;

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
                        if (consignment != null && mAuth.getUid() != null && consignment.getRider() != null && consignment.getAssignedto().equals(mAuth.getUid()) && consignment.getStatus() != null && !consignment.getStatus().equals("Delivered")) {
                            consignments.add(consignment);
                        }
                    }
                }
                if(consignments.size() == 0)
                    tvAssignedConsignments.setText("No Consignments to show..");

                // Notify the adapter that the data has changed

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

}
