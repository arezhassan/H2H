package com.example.h2h;


import android.content.Context;
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

public class ViewRidersAdapter extends RecyclerView.Adapter<ViewRidersAdapter.ViewHolder> {

    private Context context;
    private List<Rider> riderList;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public ViewRidersAdapter(Context context, List<Rider> riderList) {
        this.context = context;
        this.riderList = riderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlerider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rider rider = riderList.get(position);

        // Bind data to the views
        holder.tvRiderName.setText(rider.getName());
        holder.tvRiderPhone.setText(rider.getPhone());
        holder.tvRiderCnic.setText(rider.getCnic());
        holder.tvRiderAddress.setText(rider.getAddress());

        // You may need to load images into ImageView here
        // holder.imgCNIC.setImageBitmap(rider.getCNICImage());
        Picasso picasso = Picasso.get();
        picasso.load(rider.getCnic()).into(holder.imgCNIC);
        DatabaseReference ridersRef = databaseReference.child("Riders");
        String cnicToSearch=rider.getCnic();
        Query query = ridersRef.orderByChild("cnic").equalTo(cnicToSearch);
        holder.ivDeleteRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The rider with the specified CNIC exists in the database
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();


                                Toast.makeText(context, "Rider Deleted!", Toast.LENGTH_SHORT).show();

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





        // Set click listeners for buttons

    }

    @Override
    public int getItemCount() {
        return riderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRiderName, tvRiderPhone, tvRiderCnic, tvRiderAddress;
        ImageView imgCNIC, ivDeleteRider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRiderName = itemView.findViewById(R.id.tvRiderName);
            tvRiderPhone = itemView.findViewById(R.id.tvRiderPhone);
            tvRiderCnic = itemView.findViewById(R.id.tvRiderCnic);
            tvRiderAddress = itemView.findViewById(R.id.tvRiderAddress);
            imgCNIC = itemView.findViewById(R.id.imgCNIC);
            ivDeleteRider = itemView.findViewById(R.id.ivDeleteRider);



        }
    }



}
