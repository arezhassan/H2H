package com.example.h2h;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminConsignmentAdapter extends RecyclerView.Adapter<AdminConsignmentAdapter.ViewHolder> {

    private Context context;
    RiderSpinnerAdapter riderSpinnerAdapter;

    private List<Consignment> consignmentList;



    DatabaseReference dbref1;
    private List<Rider> riderList; // List of Rider items for the Spinner

    public AdminConsignmentAdapter(Context context, List<Consignment> consignmentList,List<Rider>riderList) {
        this.context = context;
        this.consignmentList = consignmentList;
        this.riderList = riderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adminconsignments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consignment consignmentItem = consignmentList.get(position);
        String url=consignmentItem.getUrl();
        Picasso picasso = Picasso.get();
        picasso.load(url)
                .placeholder(R.drawable.placeholder)
                .fit()
                .into(holder.ivConsignment);

        // Bind data to the TextViews
        holder.tvAdminConsignmentId.setText(consignmentItem.getId());
        holder.tvAdminConsignmentReceiver.setText(consignmentItem.getReceiverName());
        holder.tvAdminConsignmentReceiverAddress.setText(consignmentItem.getReceiverAddress());
        holder.tvAdminConsignmentSenderAddress.setText(consignmentItem.getSenderAddress());
        holder.tvAdminConsignmentStatus.setText(consignmentItem.getStatus());
        holder.tvAdminConsignmentDate.setText(consignmentItem.getPickupDateTime()+ " ("+ consignmentItem.getTime()+ ")");
        holder.tvAdminConsignmentCategory.setText(consignmentItem.getItemCategory());
        holder.tvAdminConsignmentQuantity.setText(consignmentItem.getItemQuantity());


        // Bind other TextViews similarly
        Log.d("TAG", "Number of Riders in rider list: " + riderList.size());
        riderSpinnerAdapter = new RiderSpinnerAdapter(context, android.R.layout.simple_spinner_item, riderList);
        riderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.SpinnerRiderNames.setAdapter(riderSpinnerAdapter);
        riderSpinnerAdapter.notifyDataSetChanged();

        // Set up the Rider Spinner









        // Set an item selected listener for the Rider Spinner if needed


        // Set a click listener for the Assign Rider Button
        holder.btnAssignRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the assignment of the selected Rider for this consignment
                Rider selectedRider = (Rider) holder.SpinnerRiderNames.getSelectedItem();

                if (selectedRider != null) {
                    String consignmentId = consignmentItem.getId();
                    String riderId = selectedRider.getId();
                    String userid=consignmentItem.getUserid();
                    Log.d("TAG", "Rider ID: " + riderId);
                    Log.d("TAG", "Consignment ID: " + consignmentId);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("consignment");



                    databaseReference.child(userid).child(consignmentId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                dataSnapshot.getRef().child("assignedto").setValue(riderId);
                            } else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });









                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return consignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAdminConsignmentId, tvAdminConsignmentReceiver, tvAdminConsignmentReceiverAddress,
                tvAdminConsignmentSenderAddress, tvAdminConsignmentStatus, tvAdminConsignmentDate,
                tvAdminConsignmentCategory, tvAdminConsignmentQuantity;
        Spinner SpinnerRiderNames;
        Button btnAssignRider;
        ImageView ivConsignment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivConsignment = itemView.findViewById(R.id.ivConsignment);
            tvAdminConsignmentId = itemView.findViewById(R.id.tvAdminConsignmentId);
            SpinnerRiderNames = itemView.findViewById(R.id.spinnerRiderNames);
            tvAdminConsignmentReceiver = itemView.findViewById(R.id.tvAdminConsignmentReceiver);
            tvAdminConsignmentReceiverAddress = itemView.findViewById(R.id.tvAdminConsignmentReceiverAddress);
            tvAdminConsignmentSenderAddress = itemView.findViewById(R.id.tvAdminConsignmentSenderAddress);
            tvAdminConsignmentStatus = itemView.findViewById(R.id.tvAdminConsignmentStatus);
            tvAdminConsignmentDate = itemView.findViewById(R.id.tvAdminConsignmentDate);
            tvAdminConsignmentCategory = itemView.findViewById(R.id.tvAdminConsignmentCategory);
            tvAdminConsignmentQuantity = itemView.findViewById(R.id.tvAdminConsignmentQuantity);
            btnAssignRider = itemView.findViewById(R.id.btnAssignRider);
        }
    }
}
