package com.example.h2h;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourConsignments extends Fragment {
    RecyclerView rvConsignments;
    View view;
    TextView tvYourConsignments;
    ArrayList<Consignment> consignmentsList;
    ImageView ivBack;
    ConsignmentAdapter consignmentAdapter;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth mAuth;
    DatabaseReference db;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consignmentsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
        Home mainActivity = (Home) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
            bottomNavigationView.setVisibility(View.GONE);
        }
        getAllConsignments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_your_consignments, container, false);
        rvConsignments = view.findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvConsignments.setLayoutManager(linearLayoutManager);
        rvConsignments.setHasFixedSize(true);
        ivBack = view.findViewById(R.id.ivback);
        tvYourConsignments = view.findViewById(R.id.tvYourConsignments);


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Home mainActivity = (Home) getActivity();
                if (mainActivity != null) {
                    BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                // Handle the back button press here
                // You can use FragmentManager to navigate back to the parent fragment
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

        ivBack.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit());
        return view;
    }

    private void getAllConsignments() {
        db.child("consignment").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consignmentsList.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Consignment consignment = dataSnapshot.getValue(Consignment.class);
                        consignmentsList.add(consignment);
                    }
                }
                if (consignmentsList.isEmpty()) {
                    tvYourConsignments.setText("No Booked Consignments..");
                    Toast.makeText(getActivity(), "No Consignments Exist..", Toast.LENGTH_SHORT).show();
                }
                consignmentAdapter = new ConsignmentAdapter(consignmentsList, getActivity());
                rvConsignments.setAdapter(consignmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
