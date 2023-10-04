package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminEditConsignment extends AppCompatActivity {
    RecyclerView rvAdminEditConsignments;

    ArrayList<Consignment> list;
    ArrayList<Consignment> filteredlist;

    RiderConsignmentAdapter adapter;

    TextView tvNoConsignments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_consignment);
        init();
        setUpRecyclerView();
        filterData();
    }

    private void setUpRecyclerView() {
        rvAdminEditConsignments.setAdapter(adapter);
        rvAdminEditConsignments.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init() {
        tvNoConsignments = findViewById(R.id.tvNoConsignments);
        list = new ArrayList<>();
        filteredlist = new ArrayList<>();
        rvAdminEditConsignments = findViewById(R.id.rvAdminEditConsignments);
        Intent i = getIntent();
        if (i != null) {
            list = getIntent().getParcelableArrayListExtra("consignments");
        }
        adapter = new RiderConsignmentAdapter(filteredlist, this);
    }

    private void filterData() {
        for (Consignment consignment : list) {
            if (!consignment.getStatus().equals("Delivered")) {
                filteredlist.add(consignment);
            }
        }
        tvNoConsignments.setVisibility(filteredlist.size() == 0? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();

    }
}