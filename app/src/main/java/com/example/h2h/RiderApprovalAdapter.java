package com.example.h2h;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RiderApprovalAdapter extends RecyclerView.Adapter<RiderApprovalAdapter.ViewHolder> {

    private Context context;
    private List<Rider> riderList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



    public RiderApprovalAdapter(Context context, List<Rider> riderList) {
        this.context = context;
        this.riderList = riderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singleapprovalrider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rider rider = riderList.get(position);

        // Bind data to the views
        holder.tvRiderName.setText("Name: "+rider.getName());
        holder.tvRiderPhone.setText("Phone: "+rider.getPhone());
        holder.tvRiderCnic.setText("CNIC: "+rider.getCnic());
        holder.tvRiderAddress.setText("Address: "+rider.getAddress());

        // You may need to load images into ImageView here
        // holder.imgCNIC.setImageBitmap(rider.getCNICImage());
        Picasso picasso = Picasso.get();
        picasso.load(rider.getCnicUrl())
                .placeholder(R.drawable.placeholder)
                .fit()// Replace 'R.drawable.placeholder_image' with your actual placeholder image resource
                .into(holder.imgCNIC);


        // Set click listeners for buttons
        DatabaseReference ridersRef = databaseReference.child("Riders");
        String cnicToSearch=rider.getCnic();
        Query query = ridersRef.orderByChild("cnic").equalTo(cnicToSearch);


        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The rider with the specified CNIC exists in the database
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().child("verificationStatus").setValue(false);
                                snapshot.getRef().child("status").setValue("Rejected");


                                Toast.makeText(context, "Rider Rejected!", Toast.LENGTH_SHORT).show();

                                // Handle the rider data as needed (e.g., display it)
                            }
                        } else {
                            // No rider with the specified CNIC found
                            // You can display a message or handle this case accordingly
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur during the query
                        Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The rider with the specified CNIC exists in the database
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Get a reference to the rider's node
                                DatabaseReference riderRef = snapshot.getRef();

                                // Update the verificationStatus to true
                                riderRef.child("verificationStatus").setValue(true);
                                riderRef.child("status").setValue("Approved");
                                Toast.makeText(context, "Rider Approved", Toast.LENGTH_SHORT).show();


                                // You can also perform any additional actions if needed
                            }
                        } else {
                            // No rider with the specified CNIC found
                            // You can display a message or handle this case accordingly
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();

                        // Handle any errors that occur during the query
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return riderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRiderName, tvRiderPhone, tvRiderCnic, tvRiderAddress;
        ImageView imgCNIC, btnReject, btnApprove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRiderName = itemView.findViewById(R.id.tvARiderName);
            tvRiderPhone = itemView.findViewById(R.id.tvARiderPhone);
            tvRiderCnic = itemView.findViewById(R.id.tvARiderCnic);
            tvRiderAddress = itemView.findViewById(R.id.tvARiderAddress);
            imgCNIC = itemView.findViewById(R.id.imgACNIC);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnApprove = itemView.findViewById(R.id.btnApprove);
        }
    }



}
