package com.example.h2h;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class AdminBookConsignment extends Activity {
    Button btnSubmitAdmin, btnUploadPhotoAdmin;
    ProgressBar progressBarAdmin;
    ImageView ivBackAdmin;
    String address;
    String date, time;
    EditText etDateAdmin, etTimeAdmin;

    String url;

    FirebaseAuth mAuth;
    EditText etSenderAddressAdmin, etReceiverNameAdmin, etReceiverPhoneAdmin, etProductDescriptionAdmin, etReceiverAddressAdmin;

    TextView tvImageNameAdmin;
    String SenderAddress, ReceiverName, ReceiverPhone, ProductDescription, ReceiverAddress;
    String itemQuantity, itemCategory, itemDescription;

    String latitude, longitude;

    Spinner spinnerAdmin, quantitySpinnerAdmin;


    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 100;
    ImageView ivDoneAdmin;
    public static long ccount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_consignment);
        btnSubmitAdmin = findViewById(R.id.btnSubmitAdmin);
        ivBackAdmin = findViewById(R.id.ivBackAdmin);
        etDateAdmin = findViewById(R.id.etDateAdmin);
        mAuth = FirebaseAuth.getInstance();
        progressBarAdmin = findViewById(R.id.progressbarAdmin);
        progressBarAdmin.setVisibility(View.GONE);
        btnUploadPhotoAdmin = findViewById(R.id.btnUploadPictureAdmin);
        etTimeAdmin = findViewById(R.id.etTimeAdmin);
        tvImageNameAdmin = findViewById(R.id.tvImageNameAdmin);
        tvImageNameAdmin.setVisibility(View.GONE);
        etSenderAddressAdmin = findViewById(R.id.etSenderAddressAdmin);
        etReceiverNameAdmin = findViewById(R.id.etReceiverNameAdmin);
        etReceiverPhoneAdmin = findViewById(R.id.etReceiverPhoneAdmin);
        etProductDescriptionAdmin = findViewById(R.id.etDescriptionAdmin);
        etSenderAddressAdmin.setEnabled(true);
        spinnerAdmin = findViewById(R.id.spinnerAdmin);
        etReceiverAddressAdmin = findViewById(R.id.etReceiverAddressAdmin);
        quantitySpinnerAdmin = findViewById(R.id.quantitySpinnerAdmin);
        ivDoneAdmin = findViewById(R.id.ivDoneAdmin);
        ivDoneAdmin.setVisibility(View.GONE);
        storageReference = FirebaseStorage.getInstance().getReference();


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


        etDateAdmin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            AdminBookConsignment.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    etDateAdmin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    date = etDateAdmin.getText().toString();
                                }
                            },
                            year, month, day);

                    datePickerDialog.show();
                }
            }
        });

        etTimeAdmin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.hasFocus()) {
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AdminBookConsignment.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    if (hourOfDay > 12) {
                                        int hr = hourOfDay - 12;
                                        etTimeAdmin.setText(hr + ":" + minute);
                                        time = etTimeAdmin.getText().toString();
                                    } else {
                                        etTimeAdmin.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, hour, minute, false);
                    timePickerDialog.show();
                }
            }
        });

        spinnerAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerAdmin.getSelectedItem() != null) {
                    itemCategory = spinnerAdmin.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AdminBookConsignment.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        });

        quantitySpinnerAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerAdmin.getSelectedItem() != null) {
                    itemQuantity = quantitySpinnerAdmin.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AdminBookConsignment.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        });


        ivBackAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminBookConsignment.this, AdminHome.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmitAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etReceiverNameAdmin.getText().toString().isEmpty() || etReceiverPhoneAdmin.getText().toString().isEmpty() || etReceiverAddressAdmin.getText().toString().isEmpty() || etTimeAdmin.getText().toString().isEmpty() || spinnerAdmin.getSelectedItem().toString().isEmpty() || quantitySpinnerAdmin.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(AdminBookConsignment.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                } else if (etSenderAddressAdmin.getText().toString().equals("Finding Location....")) {
                    etSenderAddressAdmin.setText("Please Wait....");

                } else {
                    progressBarAdmin.setVisibility(View.VISIBLE);
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
                    String userId = mAuth.getCurrentUser().getUid();
                    SenderAddress = etSenderAddressAdmin.getText().toString();
                    ReceiverName = etReceiverNameAdmin.getText().toString();
                    ReceiverPhone = etReceiverPhoneAdmin.getText().toString();
                    ReceiverAddress = etReceiverAddressAdmin.getText().toString();
                    date = etDateAdmin.getText().toString();
                    time = etTimeAdmin.getText().toString();
                    itemCategory = spinnerAdmin.getSelectedItem().toString();
                    itemQuantity = quantitySpinnerAdmin.getSelectedItem().toString();
                    ProductDescription = etProductDescriptionAdmin.getText().toString();
                    latitude = latitude;
                    longitude = longitude;
                    url = url;
                    ccount = ccount + 1;
                    String consignmentId = userId.toLowerCase(Locale.ROOT).substring(0, 4) + ccount;

                    Consignment con = new Consignment(SenderAddress, ReceiverAddress, ReceiverName, ReceiverPhone, "Picked", date, time, itemCategory, itemQuantity, ProductDescription, consignmentId, latitude, longitude, url, "", "", "", "", mAuth.getUid());
                    usersRef.child("consignment").child(userId).child(consignmentId).setValue(con);
                    progressBarAdmin.setVisibility(View.GONE);

                    String channelId = "Your_Channel_ID";
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(AdminBookConsignment.this, channelId)
                                    .setSmallIcon(R.drawable.appicon)
                                    .setContentTitle("Consignment Status")
                                    .setContentText("Consignment Booked, Consignment ID: " + consignmentId)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Consignment Details")
                                            .bigText("Your Consignment is Booked. \nConsignment ID: " + consignmentId));

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(0, notificationBuilder.build());

                    Intent intent = new Intent(AdminBookConsignment.this, ConsignmentBookedAnimation.class);
                    intent.putExtra("userType", "Admin");
                    intent.putExtra("id", consignmentId);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnUploadPhotoAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarAdmin.setVisibility(View.VISIBLE);
                btnUploadPhotoAdmin.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String filename = getFileNameFromUri(selectedImageUri);
            tvImageNameAdmin.setText(filename);
            tvImageNameAdmin.setVisibility(View.VISIBLE);
            StorageReference imageRef = storageReference.child("images/" + selectedImageUri.getLastPathSegment());

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    url = downloadUrl;
                                    ivDoneAdmin.setVisibility(View.VISIBLE);
                                    progressBarAdmin.setVisibility(View.GONE);
                                    btnUploadPhotoAdmin.setVisibility(View.GONE);
                                    Toast.makeText(AdminBookConsignment.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBarAdmin.setVisibility(View.GONE);
                                    Toast.makeText(AdminBookConsignment.this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarAdmin.setVisibility(View.GONE);
                            Toast.makeText(AdminBookConsignment.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressBarAdmin.setProgress((int) progress);
                        }
                    });
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String imageName = null;
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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


}
