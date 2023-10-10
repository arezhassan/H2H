package com.example.h2h;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;


public class BookConsignment extends Fragment {
    View view;
    BottomNavigationView bottomNavigationView;
    Button btnSubmit, btnGetLocation, btnUploadPhoto;
    ProgressBar progressBar;
    ImageView ivBack;
    String address;
    String date, time;
    EditText etDate, etTime;
    String url;

    FirebaseAuth mAuth;
    EditText etSenderAddress, etReceiverName, etReceiverPhone, etProductDescription, etReceiverAddress;

    TextView tvImageName;
    String SenderAddress, ReceiverName, ReceiverPhone, ProductDescription, ReceiverAddress;
    String itemQuantity, itemCategory, itemDescription;


    String latitude, longitude;

    Spinner spinner, quantitySpinner;

    ProgressBar progressBar2;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 100;
    ImageView ivDone, ivGetLocation;
    public static long ccount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_consignment, container, false);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ivBack = view.findViewById(R.id.ivBack);
        etDate = view.findViewById(R.id.etDate);
        mAuth = FirebaseAuth.getInstance();
        progressBar2 = view.findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPicture);
        etTime = view.findViewById(R.id.etTime);
        tvImageName = view.findViewById(R.id.tvImageName);
        tvImageName.setVisibility(View.GONE);
        etSenderAddress = view.findViewById(R.id.etSenderAddress);
        etReceiverName = view.findViewById(R.id.etReceiverName);
        etReceiverPhone = view.findViewById(R.id.etReceiverPhone);
        etProductDescription = view.findViewById(R.id.etDescription);
        btnGetLocation = view.findViewById(R.id.btnGetLocation);
        etSenderAddress.setEnabled(true);
        spinner = view.findViewById(R.id.spinner);
        etReceiverAddress = view.findViewById(R.id.etReceiverAddress);
        quantitySpinner = view.findViewById(R.id.quantitySpinner);
        ivDone = view.findViewById(R.id.ivDone);
        ivDone.setVisibility(View.GONE);
        storageReference = FirebaseStorage.getInstance().getReference();


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


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("consignment").child(mAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                ccount = count;
                // 'count' contains the number of child nodes under 'your_node'
                // You can use 'count' as needed in your code
                Log.d("Count", "Number of objects: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during the data retrieval
                Log.e("Count", "Error: " + databaseError.getMessage());
            }
        });


        Home mainActivity = (Home) getActivity();
        if (mainActivity != null) {
            BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
            bottomNavigationView.setVisibility(View.GONE);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    final Calendar c = Calendar.getInstance();

                    // on below line we are getting
                    // our day, month and year.
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            // on below line we are passing context.
                            getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // on below line we are setting date to our text view.
                                    etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    date = etDate.getText().toString();
                                }
                            },

                            year, month, day);

                    datePickerDialog.show();
                }
            }

        });


        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    if (hourOfDay > 12) {
                                        int hr = hourOfDay - 12;
                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hr, minute);
                                        etTime.setText(formattedTime);
                                        time = etTime.getText().toString();
                                    } else {
                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                        etTime.setText(formattedTime);
                                    }
                                }
                            }, hour, minute, false);
                    timePickerDialog.show();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem() != null) {
                    itemCategory = spinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Please Select", Toast.LENGTH_SHORT).show();

            }
        });
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem() != null) {
                    itemQuantity = quantitySpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Please Select", Toast.LENGTH_SHORT).show();


            }
        });


        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etSenderAddress.setText("Finding Location....");
                progressBar2.setVisibility(View.VISIBLE);
                requestLocation();
            }
        });
        btnGetLocation.performClick();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity != null) {
                    BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etReceiverName.getText().toString().isEmpty() || etReceiverPhone.getText().toString().isEmpty() || etReceiverAddress.getText().toString().isEmpty() || etTime.getText().toString().isEmpty() || spinner.getSelectedItem().toString().isEmpty() || quantitySpinner.getSelectedItem().toString().isEmpty()) {


                    Toast.makeText(getActivity(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                } else if (etSenderAddress.getText().toString().equals("Finding Location....")) {

                    etSenderAddress.setText("Please Wait....");
                    btnGetLocation.performClick();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
                    String userId = mAuth.getCurrentUser().getUid();
                    SenderAddress = etSenderAddress.getText().toString();
                    ReceiverName = etReceiverName.getText().toString();
                    ReceiverPhone = etReceiverPhone.getText().toString();
                    ReceiverAddress = etReceiverAddress.getText().toString();
                    date = etDate.getText().toString();
                    time = etTime.getText().toString();
                    itemCategory = spinner.getSelectedItem().toString();
                    itemQuantity = quantitySpinner.getSelectedItem().toString();
                    ProductDescription = etProductDescription.getText().toString();
                    latitude = latitude;
                    longitude = longitude;
                    url = url;
                    ccount = ccount + 1;
                    String consignmentId = userId.toLowerCase(Locale.ROOT).substring(0, 4) + ccount;


                    Consignment con = new Consignment(SenderAddress, ReceiverAddress, ReceiverName, ReceiverPhone, "Booked", date, time, itemCategory, itemQuantity, ProductDescription, consignmentId, latitude, longitude, url, "", "", "", "", mAuth.getUid());
                    usersRef.child("consignment").child(userId).child(consignmentId).setValue(con);
                    progressBar.setVisibility(View.GONE);
                    Home mainActivity = (Home) getActivity();
                    if (mainActivity != null) {
                        BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.navigation);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                    String channelId = "Your_Channel_ID";
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(getActivity(), channelId)
                                    .setSmallIcon(R.drawable.appicon)
                                    .setContentTitle("Consignment Status")
                                    .setContentText("Consignment Booked, Consignment ID: " + consignmentId)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Consignment Details")
                                            .bigText("Your Consignment is Booked. \nConsignment ID: " + consignmentId));


                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    // Create a notification channel (for Android Oreo and later)

                    NotificationChannel channel = new NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);


                    notificationManager.notify(0, notificationBuilder.build());


                    Intent intent = new Intent(getActivity(), ConsignmentBookedAnimation.class);
                    startActivity(intent);
                    getActivity().finish();


                }


            }
        });
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btnUploadPhoto.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();
            String filename = getFileNameFromUri(selectedImageUri);
            tvImageName.setText(filename);
            tvImageName.setVisibility(View.VISIBLE);

            // Create a reference to a specific location in Firebase Storage
            StorageReference imageRef = storageReference.child("images/" + selectedImageUri.getLastPathSegment());

            // Upload the image to Firebase Storage
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Handle the download URL (e.g., save it to Firebase Database, display it, etc.)
                                    String downloadUrl = uri.toString();
                                    url = downloadUrl;
                                    ivDone.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    btnUploadPhoto.setVisibility(View.GONE);


                                    Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);

                                    // Handle any errors that occurred during the download URL retrieval
                                    Toast.makeText(getActivity(), "Error getting download URL", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);

                            // Handle any errors that occurred during the image upload
                            Toast.makeText(getActivity(), "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                            // Update the ProgressBar with the current progress
                            progressBar.setProgress((int) progress);
                        }
                    });
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String imageName = null;
        if (uri != null) {
            Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        imageName = cursor.getString(displayNameIndex);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return imageName;
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
            locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(120000); // Update interval in milliseconds

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    Location location = locationResult.getLastLocation();
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    // Check if getActivity() is not null and latitude and longitude are valid before starting the geocoding task.
                    if (getActivity() != null && latitude != null && longitude != null) {
                        new GeocodingTask(getActivity()).execute(latitude, longitude);
                        progressBar2.setVisibility(View.GONE);
                    }
                }
            };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


}