package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

public class RiderProfile extends Fragment {
    TextView tvPRiderCNIC, tvPRiderName, tvPRiderPhone;
    String name, cnic, phone, userid;
    RecyclerView rvDeliveredConsignments;
    Button btnEditDetailsRider;
    FirebaseAuth mAuth;
    ConsignmentAdapter adapter;
    View view;
    ArrayList<Consignment> consignments;
    Button btnLogoutRider;
    TextView tvDeliveredConsignments;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rider_profile, container, false);
        btnLogoutRider = view.findViewById(R.id.btnRiderLogout);
        tvDeliveredConsignments=view.findViewById(R.id.tvDeliveredConsignments);
        consignments = new ArrayList<>();
        adapter = new ConsignmentAdapter(consignments, getActivity());
        btnEditDetailsRider = view.findViewById(R.id.btnEditDetailsRider);

        // Initialize and set up RecyclerView
        rvDeliveredConsignments = view.findViewById(R.id.rvDeliveredConsignments);
        rvDeliveredConsignments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDeliveredConsignments.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        btnLogoutRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), SplashScreen2.class));
                getActivity().finish();
            }
        });
        getData();
        btnEditDetailsRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditRiderDetails.class);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("userId",userid);
                startActivity(intent);
            }
        });






        return view;
    }

    private void getData() {
        tvPRiderCNIC = view.findViewById(R.id.tvPRiderCNIC);
        tvPRiderName = view.findViewById(R.id.tvPRiderName);
        tvPRiderPhone = view.findViewById(R.id.tvPRiderPhone);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        DatabaseReference riderReference = FirebaseDatabase.getInstance().getReference().child("Riders").child(userid);
        riderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                cnic = snapshot.child("cnic").getValue(String.class);
                phone = snapshot.child("phone").getValue(String.class);
                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        DatabaseReference consignmentReference = FirebaseDatabase.getInstance().getReference().child("consignment");
        consignmentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consignments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Consignment consignment = dataSnapshot1.getValue(Consignment.class);
                        if (consignment != null && "Delivered".equals(consignment.getStatus()) && userid.equals(consignment.getAssignedto()) ) {
                            consignments.add(consignment);
                        }
                    }
                }
                if(consignments.size() ==0)
                    tvDeliveredConsignments.setText("No Delivered Consignments Recently...");


                adapter.notifyDataSetChanged();
                // Log the size of consignments
                Log.d("consignments", "Size: " + consignments.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void setData() {
        tvPRiderCNIC.setText(cnic);
        tvPRiderName.setText(name);
        tvPRiderPhone.setText(phone);
    }
}
