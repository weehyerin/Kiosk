package com.example.order_app_client;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST = 1;
    public static final String PREFS_LOCATION = "MyLocationPrefs";
    public static final String PREFS_FACILITY = "MyFacilityPrefs";

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // this thread waiting for the user's response! Try again to request the permission.
            }
            else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            }

            //onRequestPermissionsResult(MY_PERMISSIONS_REQUEST_READ_CONTACTS, );
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                // this thread waiting for the user's response! Try again to request the permission.
            }
            else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST);
            }

            //onRequestPermissionsResult(MY_PERMISSIONS_REQUEST_READ_CONTACTS, );
        }

        /*
        SharedPreferences user_facility = getSharedPreferences(PREFS_FACILITY, 0);
        String isSelectedFacility = user_facility.getString("isSelectedFacility", "NO");

        if (isSelectedFacility.equals("NO")) {
            Intent intent = new Intent(getApplicationContext(), FacilityActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }*/

        // else: user selected facility conditions
        final Geocoder geocoder = new Geocoder(this);
        final TextView text_current_location = findViewById(R.id.text_current_location);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude    = location.getLatitude();
                longitude   = location.getLongitude();

                try {
                    if (geocoder != null) {
                        List<Address> address;
                        address = geocoder.getFromLocation(latitude, longitude, 10);

                        if (address != null & address.size() > 0) {
                            text_current_location.setText(address.get(0).getLocality().toString()
                                    + " " + address.get(0).getThoroughfare().toString());
                            Log.d("TAG", address.get(0).getAddressLine(0).toString());
                        }
                    }
                } catch (IOException e) {
                    text_current_location.setText("주소 가져오기 실패");
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

        Button btn_current_location = findViewById(R.id.btn_current_GPS);
        Button btn_location_select  = findViewById(R.id.btn_GPS_select);

        btn_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences user_location = getSharedPreferences(PREFS_LOCATION, 0);
                SharedPreferences.Editor editor = user_location.edit();
                editor.putString("userLatitude", Double.toString(latitude));
                editor.putString("userLongitude", Double.toString(longitude));
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        btn_location_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}