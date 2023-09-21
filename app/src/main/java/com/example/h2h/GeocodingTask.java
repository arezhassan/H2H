package com.example.h2h;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class GeocodingTask extends AsyncTask<String, Void, List<Address>> {
    private Context context;

    GeocodingTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Address> doInBackground(String... params) {
        double latitude = Double.parseDouble(params[0]);
        double longitude = Double.parseDouble(params[1]);

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            return geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            EditText etAddr=(EditText) ((FragmentActivity) context).findViewById(R.id.etSenderAddress);
            etAddr.setText(addressLine);
            // You can also extract other address components here if needed.
        }
    }
}