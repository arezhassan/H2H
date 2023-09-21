package com.example.h2h;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends Fragment {

   View view;
   FirebaseAuth mAuth;
   ProgressBar pgBar;
   Button btnLogout;
    private DatabaseReference userRef;
    TextView tvUserName, tvUserEmail, tvUserPhone;
    String name, email, phone;
    LottieAnimationView card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btnLogout.setVisibility(View.GONE);
                pgBar.setVisibility(View.VISIBLE);
                User user=snapshot.getValue(User.class);
                if (user != null) {
                    name = user.getName();
                    email = user.getEmail();
                    double phone1 = user.getPhone();
                    phone = String.valueOf(phone1).replace(".","");
                    phone = phone.replace("E11","");

                    // Assuming getPhone() is a valid method in your User class

                    //TextViews Setting
                    if (!name.isEmpty() && !email.isEmpty()) {
                        card.setVisibility(View.GONE);
                        tvUserName.setText(name);
                        tvUserEmail.setText(email);
                        tvUserPhone.setText(phone);

                        card.setVisibility(View.GONE);
                        tvUserEmail.setVisibility(View.VISIBLE);
                        tvUserPhone.setVisibility(View.VISIBLE);
                        tvUserName.setVisibility(View.VISIBLE);

                    }
                }
                //tvUserPhone.setText("Phone Number: " +phone);
                pgBar.setVisibility(View.GONE);
                btnLogout.setVisibility(View.VISIBLE);





            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                card.setVisibility(View.GONE);
                pgBar.setVisibility(View.GONE);
                btnLogout.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        pgBar=view.findViewById(R.id.pgBar);
        btnLogout=view.findViewById(R.id.btnLogout);
        pgBar.setVisibility(View.GONE);
        card=view.findViewById(R.id.card);
        card.setVisibility(View.VISIBLE);

        tvUserName=view.findViewById(R.id.tvUserName);
        tvUserEmail=view.findViewById(R.id.tvUserEmail);
        tvUserPhone=view.findViewById(R.id.tvUserPhone);
        tvUserName.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        tvUserPhone.setVisibility(View.GONE);

        name="";
        email="";

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                // Handle the back button press here
                // You can use FragmentManager to navigate back to the parent fragment
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });







        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogout.setVisibility(View.GONE);
                pgBar.setVisibility(View.VISIBLE);
                mAuth.signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
            }
        });


        return view;
    }
}