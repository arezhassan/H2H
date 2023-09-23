package com.example.h2h;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RiderConsignmentAdapter extends RecyclerView.Adapter<RiderConsignmentAdapter.ConsignmentViewHolder> {

    private List<Consignment> consignmentList;
    static String latitude;

    static String longitude;

    static Context context;

    DatabaseReference dbRef;
    String status,weight,distance,quantity, price;


    public RiderConsignmentAdapter(List<Consignment> consignmentList, Context context) {
        this.consignmentList = consignmentList;
        this.context = context;
    }


    @NonNull
    @Override
    public ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleconsignmentrider, parent, false);
        return new ConsignmentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {
        Consignment consignment = consignmentList.get(position);
        latitude = consignment.getLatitude();
        longitude = consignment.getLongitude();

        holder.tvConsignmentid.setText("Consignment ID: " + consignment.getId());
        holder.tvConsignmentReceiverPhone.setText("Receiver Phone: " + consignment.getReceiverContact());
        holder.tvConsignmentStatus.setText("Consignment Status: " + consignment.getStatus());
        holder.tvConsignmentSender.setText("Consignment Sender: " + consignment.getSenderAddress());
        holder.tvConsignmentReceiver.setText("Consignment Receiver: " + consignment.getReceiverName());
        if (!consignment.getWeight().isEmpty()) {
            holder.textinputweight.getEditText().setText(consignment.getWeight());
        }
        if (!consignment.getPrice().isEmpty()) {
            holder.tvShippingPrice.setText("Rs. " + consignment.getPrice());
        }


        holder.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.textinputdistance.setVisibility(View.VISIBLE);
                holder.textinputweight.setVisibility(View.VISIBLE);
                holder.btnConfirmDetails.setVisibility(View.VISIBLE);
                holder.spinnerStatus.setVisibility(View.VISIBLE);
                holder.tvShippingPrice.setVisibility(View.VISIBLE);
                holder.ShippingPrice.setVisibility(View.VISIBLE);
                holder.spinnerQuantity.setVisibility(View.VISIBLE);
                holder.btnCalculateShipping.setVisibility(View.VISIBLE);


            }
        });
        holder.btnCalculateShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = holder.spinnerStatus.getSelectedItem().toString();
                weight = holder.textinputweight.getEditText().getText().toString();
                distance = holder.textinputdistance.getEditText().getText().toString();
                quantity = holder.spinnerQuantity.getSelectedItem().toString();
                if (weight.isEmpty() || distance.isEmpty()) {
                    Toast.makeText(context, "Please enter weight and distance", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals("Select Quantity")) {
                    Toast.makeText(context, "Select Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    int q = Integer.parseInt(quantity);

                    int onekm = 40;
                    int onekg = 50;
                    int d = Integer.parseInt(distance);
                    int w = Integer.parseInt(weight);
                    int p = (w * onekg) + (d * onekm);
                    p = p * q;
                    price = String.valueOf(p);
                    holder.tvShippingPrice.setText("Rs. " + price);
                }

            }
        });

        holder.btnConfirmDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status = holder.spinnerStatus.getSelectedItem().toString();
                weight = holder.textinputweight.getEditText().getText().toString();
                distance = holder.textinputdistance.getEditText().getText().toString();
                quantity = holder.spinnerQuantity.getSelectedItem().toString();
                if (status.equals("Select Consignment Status")) {
                    Toast.makeText(context, "Please select consignment status", Toast.LENGTH_SHORT).show();
                }else if(holder.tvShippingPrice.getText().equals("Enter Weight and Distance to Calculate.")) {
                    Toast.makeText(context, "Please Calculate Shipping first", Toast.LENGTH_SHORT).show();
                }else{


                String userid = consignment.getUserid();
                String consignmentid = consignment.getId();

                dbRef = FirebaseDatabase.getInstance().getReference().child("consignment").child(userid).child(consignmentid);
                dbRef.child("status").setValue(status);
                dbRef.child("weight").setValue(weight);
                dbRef.child("price").setValue(price);
                holder.textinputdistance.setVisibility(View.GONE);
                holder.textinputweight.setVisibility(View.GONE);
                holder.spinnerQuantity.setVisibility(View.GONE);
                holder.tvShippingPrice.setVisibility(View.GONE);
                holder.ShippingPrice.setVisibility(View.GONE);
                holder.btnConfirmDetails.setVisibility(View.GONE);
                holder.spinnerStatus.setVisibility(View.GONE);
                holder.btnCalculateShipping.setVisibility(View.GONE);
                Toast.makeText(context, "Details Updated", Toast.LENGTH_SHORT).show();
            }}


        });
    }







    @Override
    public int getItemCount() {
        return consignmentList.size();
    }


    public static class ConsignmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvConsignmentid;
        TextView tvConsignmentReceiverPhone;
        TextView tvConsignmentStatus;
        TextView tvConsignmentSender;
        TextView tvConsignmentReceiver,tvShippingPrice, ShippingPrice;

        Button btnShowLocation, btnChangeStatus, btnConfirmDetails, btnCalculateShipping;

        TextInputLayout textinputweight, textinputdistance;

        Spinner spinnerStatus, spinnerQuantity;


        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);


            tvConsignmentid = itemView.findViewById(R.id.tvConsignmentid);
            btnCalculateShipping=itemView.findViewById(R.id.btnCalculateShipping);
            btnCalculateShipping.setVisibility(View.GONE);
            tvShippingPrice = itemView.findViewById(R.id.tvShippingPrice);
            tvShippingPrice.setText("Enter Weight and Distance to Calculate.");
            tvShippingPrice.setVisibility(View.GONE);
            ShippingPrice = itemView.findViewById(R.id.ShippingPrice);
            ShippingPrice.setVisibility(View.GONE);
            tvConsignmentReceiverPhone = itemView.findViewById(R.id.tvConsignmentReceiverPhone);
            tvConsignmentStatus = itemView.findViewById(R.id.tvConsignmentStatus);
            tvConsignmentSender = itemView.findViewById(R.id.tvConsignmentSender);
            tvConsignmentReceiver = itemView.findViewById(R.id.tvConsignmentReceiver);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
            btnShowLocation = itemView.findViewById(R.id.btnShowLocation);
            btnConfirmDetails = itemView.findViewById(R.id.btnConfirmDetails);
            textinputweight = itemView.findViewById(R.id.textinputweight);
            textinputdistance=itemView.findViewById(R.id.textinputdistance);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            spinnerQuantity =itemView.findViewById(R.id.spinnerQuantity);
            spinnerQuantity.setVisibility(View.GONE);
            textinputdistance.setVisibility(View.GONE);
            textinputweight.setVisibility(View.GONE);
            btnConfirmDetails.setVisibility(View.GONE);
            spinnerStatus.setVisibility(View.GONE);



        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a query for the specific location using latitude and longitude
                String query = latitude + "," + longitude;

                // Create a Uri with the query for Google Maps
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);

                // Create an Intent to open Google Maps with the specified location
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps"); // Specify the package for Google Maps

                // Check if there is an app available to handle the Intent
                Intent chooserIntent = Intent.createChooser(mapIntent, "Open with");
                if (chooserIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(chooserIntent);
                } else {
                    // Handle the case where no map app is available
                    Toast.makeText(context, "No map app is available", Toast.LENGTH_SHORT).show();
                }


            }
        });

        }

    }
}