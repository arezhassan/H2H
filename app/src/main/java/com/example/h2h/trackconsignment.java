package com.example.h2h;

import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class trackconsignment extends Fragment {
    ProgressBar pgbar;
    EditText etTrackConsignment;
    Button btnCheckConsignment;
    View view;
    private DatabaseReference consignmentsRef;

    String id ;
    String senderAddress ;
    String time ;
    String receiverAddress ;
    String receiverName;
    String latitude ;
    String longitude ;
    String receiverContact ;
    String pickupDateTime ;
    String itemCategory;
    String itemQuantity ;
    String itemStatus;
    String itemDescription;
    String url ;

    TextView tvSenderAddress;
    TextView tvSenderName;
    TextView tvTime;
    TextView tvReceiverAddress;
    TextView tvReceiverName;
    TextView tvLatitude;
    TextView tvLongitude;
    TextView tvReceiverContact;
    TextView tvPickupDateTime;
    TextView tvItemCategory;
    TextView tvDescription;
    TextView tvItemStatus;

    CardView cardView;

    ImageView ivGetBack;

    FirebaseAuth mAuth;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_trackconsignment, container, false);
        Home mainActivity = (Home) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
            bottomNavigationView.setVisibility(View.GONE);
        }
        cardView=view.findViewById(R.id.trackshipmentcard);
        cardView.setVisibility(View.GONE);
        pgbar=view.findViewById(R.id.pgbar);
        pgbar.setVisibility(View.GONE);
        etTrackConsignment=view.findViewById(R.id.etTrackShipment);
        btnCheckConsignment=view.findViewById(R.id.btnCheckConsignment);
        consignmentsRef = FirebaseDatabase.getInstance().getReference("consignment");
        mAuth = FirebaseAuth.getInstance();
        tvItemStatus=view.findViewById(R.id.tvItemStatus);
        tvDescription=view.findViewById(R.id.tvItemDesc);
        tvReceiverContact=view.findViewById(R.id.tvReceiverPhone);
        tvReceiverName=view.findViewById(R.id.tvReceiverName);
        tvReceiverAddress=view.findViewById(R.id.tvReceiverAddress);
        tvTime=view.findViewById(R.id.tvBookTime);
        tvPickupDateTime=view.findViewById(R.id.tvBookDate);

        tvSenderAddress=view.findViewById(R.id.tvSenderAddress);
        ivGetBack=view.findViewById(R.id.ivGetBack);
       // tvLatitude=view.findViewById(R.id.tvLatitude);
       // tvLongitude=view.findViewById(R.id.tvLongitude);
       // tvItemCategory=view.findViewById(R.id.tvItemCategory);






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







        btnCheckConsignment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if( !etTrackConsignment.getText().toString().isEmpty()) {
                   String id = etTrackConsignment.getText().toString().toLowerCase().trim();
                   searchConsignmentById(id);
                   btnCheckConsignment.setVisibility(View.GONE);
                   pgbar.setVisibility(View.VISIBLE);
               }else{
                   Toast.makeText(getActivity(), "Please enter a valid consignment ID", Toast.LENGTH_SHORT).show();
                   etTrackConsignment.setText("");
                   etTrackConsignment.requestFocus();
                   pgbar.setVisibility(View.GONE);
               }

           }
       });


        ivGetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home mainActivity = (Home) getActivity();
                if (mainActivity != null) {
                    BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });


        return view;
    }
    private void searchConsignmentById(final String consignmentId) {
        // Add a ValueEventListener to listen for changes in the database
        consignmentsRef.child(mAuth.getUid().toString()).child(consignmentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // Found the consignment with ID 1
                    // Retrieve consignment data
                    Consignment consignment = dataSnapshot.getValue(Consignment.class);


                    // Now you can access the consignment data
                     id = consignment.getId();
                     senderAddress = consignment.getSenderAddress();
                     time = consignment.getTime();
                     receiverAddress = consignment.getReceiverAddress();
                     receiverName = consignment.getReceiverName();
                     latitude = consignment.getLatitude();
                     longitude = consignment.getLongitude();
                     receiverContact = consignment.getReceiverContact();
                     pickupDateTime = consignment.getPickupDateTime();
                     itemCategory = consignment.getItemCategory();
                     itemQuantity = consignment.getItemQuantity();
                     itemDescription = consignment.getItemDescription();
                     url = consignment.getUrl();
                     itemStatus = consignment.getStatus();
                    cardView.setVisibility(View.VISIBLE);




                     tvDescription.setText("Item Description: " + itemDescription);
                     tvReceiverContact.setText("Receiver Phone: " +receiverContact);
                     tvReceiverName.setText("Receiver Name: " + receiverName);
                     tvReceiverAddress.setText("Receiver Address: " + receiverAddress);
                     tvTime.setText("Booking Time: " + time);
                     tvPickupDateTime.setText("Booking Date: " + pickupDateTime);
                     tvSenderAddress.setText("Sender Address: " + senderAddress);
                     tvItemStatus.setText("Item Status: " + itemStatus);
                     Log.d("Date:", "Pickup date time: " + pickupDateTime);


                    pgbar.setVisibility(View.GONE);
                    btnCheckConsignment.setVisibility(View.VISIBLE);

                    // Now you can use these variables as needed
                } else {
                    Toast.makeText(getActivity(), "Please enter a valid consignment ID", Toast.LENGTH_SHORT).show();
                    etTrackConsignment.setText("");
                    etTrackConsignment.requestFocus();
                    pgbar.setVisibility(View.GONE);
                    btnCheckConsignment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pgbar.setVisibility(View.GONE);
                btnCheckConsignment.setVisibility(View.VISIBLE);
                // Handle database error
            }
        });
    }
}




