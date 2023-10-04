package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAllConsignments extends AppCompatActivity {

    RecyclerView rvAllAdminConsignments;
    ArrayList<Consignment> list;

    ConsignmentAdapter adapter;

    TextView tvNoConsignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_consignments);
        init();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        rvAllAdminConsignments.setAdapter(adapter);
        rvAllAdminConsignments.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init() {
        tvNoConsignments = findViewById(R.id.tvNoConsignments);
        Intent i = getIntent();
        if (i != null) {
            list = getIntent().getParcelableArrayListExtra("consignments");
            tvNoConsignments.setVisibility(list.size() == 0? View.VISIBLE : View.GONE);
        }
        adapter = new ConsignmentAdapter(list, this);
        adapter.notifyDataSetChanged();
        list = new ArrayList<>();
        rvAllAdminConsignments = findViewById(R.id.rvAllAdminConsignments);
    }

}