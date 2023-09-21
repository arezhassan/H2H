package com.example.h2h;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Locale;

public class HomeFragment extends Fragment {
    View view;
    FirebaseAuth mAuth;
    LinearLayout linearLayout;
     int backPressCount = 0;
    TextView tvWelcome, tvConsignmentNumber, tvFrom, tvTo, tvTotalPrice, tvItem, tvDate, tvRecent, tvStatus;
    FirebaseDatabase database;
    LottieAnimationView cardView;
    Button btnBookConsignment, btnTrackConsignment, btnYourConsignments;
    String name;
    public static long ccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        name = mAuth.getCurrentUser().getDisplayName();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("consignment").child(mAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                ccount = dataSnapshot.getChildrenCount();
                if (ccount > 0) {
                    cardView.setVisibility(View.GONE);
                    String reference = mAuth.getUid().toLowerCase(Locale.ROOT).substring(0, 4) + ccount;
                    Log.d("Address", "onCreateView: " + reference);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("consignment").child(mAuth.getUid()).child(reference);
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Consignment consignment = snapshot.getValue(Consignment.class);
                            Log.d("Address", "onDataChange: " + consignment.getReceiverAddress());
                            String reference = mAuth.getUid().toLowerCase(Locale.ROOT).substring(0, 4) + ccount;
                            Log.d("Address", "onCreateView: " + reference);
                            setConsignmentDetails(consignment, reference);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    showNoConsignmentsMessage();
                }
                Log.d("Count", "Number of objects: " + ccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Count", "Error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        setupListeners();
        hideConsignmentDetails();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressCount == 1) {
                    // On the second back press, exit the app
                    requireActivity().finish();
                } else {
                    // On the first back press, show a toast message
                    Toast.makeText(requireActivity(), "Press Back again to exit.", Toast.LENGTH_SHORT).show();
                    backPressCount++;
                }
            }
        });
        return view;
    }

    private void initViews() {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        if (name != null) {
            tvWelcome.setText("Welcome " + name);
        } else {
            tvWelcome.setText("Welcome");
        }

        btnBookConsignment = view.findViewById(R.id.btnBookConsignment);
        tvStatus = view.findViewById(R.id.tvStatus);
        linearLayout = view.findViewById(R.id.linearlayout);
        linearLayout.setVisibility(View.GONE);
        btnTrackConsignment = view.findViewById(R.id.btnTrackConsignment);
        btnYourConsignments = view.findViewById(R.id.btnYourConsignments);
        tvConsignmentNumber = view.findViewById(R.id.tvConsignmentNumber);
        tvFrom = view.findViewById(R.id.tvFrom);
        tvTo = view.findViewById(R.id.tvTo);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvItem = view.findViewById(R.id.tvItem);
        tvDate = view.findViewById(R.id.tvDate);
        tvRecent = view.findViewById(R.id.tvRecent);
        cardView = view.findViewById(R.id.cardView);
        cardView.setVisibility(View.VISIBLE);
        tvConsignmentNumber.setVisibility(View.GONE);
        tvFrom.setVisibility(View.GONE);
        tvTo.setVisibility(View.GONE);
        tvTotalPrice.setVisibility(View.GONE);
        tvItem.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
    }

    private void setupListeners() {
        btnBookConsignment.setOnClickListener(view -> getFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookConsignment()).commit());
        btnTrackConsignment.setOnClickListener(view -> getFragmentManager().beginTransaction().replace(R.id.fragment_container, new trackconsignment()).commit());
        btnYourConsignments.setOnClickListener(view -> getFragmentManager().beginTransaction().replace(R.id.fragment_container, new YourConsignments()).commit());
    }

    private void setConsignmentDetails(Consignment consignment, String reference) {
        tvRecent.setText("Recent Consignment");
        tvConsignmentNumber.setText("Consignment Number: " + reference);
        tvFrom.setText("Sender: " + consignment.getReceiverName());
        tvTo.setText("Receiver Address: " + consignment.getSenderAddress());
        tvTotalPrice.setText("Quantity: " + consignment.getItemQuantity());
        tvItem.setText("Item Category: " + consignment.getItemCategory());
        hideNoConsignmentsMessage();
    }

    private void showNoConsignmentsMessage() {
        linearLayout.setVisibility(View.VISIBLE);
        tvRecent.setText("No Recent Consignments");
        tvRecent.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        tvConsignmentNumber.setVisibility(View.GONE);
        tvFrom.setVisibility(View.GONE);
        tvTo.setVisibility(View.GONE);
        tvTotalPrice.setVisibility(View.GONE);
        tvItem.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
    }

    private void hideNoConsignmentsMessage() {
        linearLayout.setVisibility(View.GONE);
        tvRecent.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        tvConsignmentNumber.setVisibility(View.VISIBLE);
        tvFrom.setVisibility(View.VISIBLE);
        tvTo.setVisibility(View.VISIBLE);
        tvTotalPrice.setVisibility(View.VISIBLE);
        tvItem.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        tvDate.setVisibility(View.VISIBLE);
    }

    private void hideConsignmentDetails() {
        cardView.setVisibility(View.VISIBLE);
        tvConsignmentNumber.setVisibility(View.GONE);
        tvFrom.setVisibility(View.GONE);
        tvTo.setVisibility(View.GONE);
        tvTotalPrice.setVisibility(View.GONE);
        tvItem.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
    }
}
