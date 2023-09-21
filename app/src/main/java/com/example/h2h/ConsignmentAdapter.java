package com.example.h2h;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentAdapter.ConsignmentViewHolder> {

    private List<Consignment> consignmentList;
    Context context;

    public ConsignmentAdapter(List<Consignment> consignmentList, Context context) {
        this.consignmentList = consignmentList;
        this.context = context;
    }


    @NonNull
    @Override
    public ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleconsignment, parent, false);
        return new ConsignmentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {
        Consignment consignment = consignmentList.get(position);

        holder.tvConsignmentid.setText("Consignment ID: " +consignment.getId());
        holder.tvConsignmentReceiverPhone.setText("Receiver Phone: "+consignment.getReceiverContact());
        holder.tvConsignmentStatus.setText("Consignment Status: "+ consignment.getStatus());
        holder.tvConsignmentSender.setText("Consignment Sender: "+consignment.getSenderAddress());
        holder.tvConsignmentReceiver.setText("Consignment Receiver: " +consignment.getReceiverName());
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
        TextView tvConsignmentReceiver;

        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConsignmentid = itemView.findViewById(R.id.tvConsignmentid);
            tvConsignmentReceiverPhone = itemView.findViewById(R.id.tvConsignmentReceiverPhone);
            tvConsignmentStatus = itemView.findViewById(R.id.tvConsignmentStatus);
            tvConsignmentSender = itemView.findViewById(R.id.tvConsignmentSender);
            tvConsignmentReceiver = itemView.findViewById(R.id.tvConsignmentReceiver);
        }

    }
}