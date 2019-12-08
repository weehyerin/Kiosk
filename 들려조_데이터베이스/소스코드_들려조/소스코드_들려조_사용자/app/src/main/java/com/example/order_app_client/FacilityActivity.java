package com.example.order_app_client;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FacilityActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST = 1;
    public static final String PREFS_FACILITY = "MyFacilityPrefs";

    String hasBrailleToilet ;
    String hasBrailleStair  ;
    String hasBrailleMenu   ;
    String hasElevator      ;
    String hasServing       ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

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

        final Button btn_toilet     = (Button) findViewById(R.id.btn_toilet);
        final Button btn_stair      = (Button) findViewById(R.id.btn_braille_stair);
        final Button btn_menu       = (Button) findViewById(R.id.btn_braille_menu);
        final Button btn_elevator   = (Button) findViewById(R.id.btn_elevator);
        final Button btn_serving    = (Button) findViewById(R.id.btn_serving);

        final SharedPreferences user_facility = getSharedPreferences(PREFS_FACILITY, 0);
        hasBrailleToilet    = user_facility.getString("hasBrailleToilet", "NO");
        hasBrailleStair     = user_facility.getString("hasBrailleStair", "NO");
        hasBrailleMenu      = user_facility.getString("hasBrailleMenu", "NO");
        hasElevator         = user_facility.getString("hasElevator", "NO");
        hasServing          = user_facility.getString("hasServing", "NO");

        // UI initialization
        if(hasBrailleToilet.equals("YES")) {
            btn_toilet.setTextColor(Color.RED);
        }
        if(hasBrailleStair.equals("YES")) {
            btn_stair.setTextColor(Color.RED);
        }
        if(hasBrailleMenu.equals("YES")) {
            btn_menu.setTextColor(Color.RED);
        }
        if(hasElevator.equals("YES")) {
            btn_elevator.setTextColor(Color.RED);
        }
        if(hasServing.equals("YES")) {
            btn_serving.setTextColor(Color.RED);
        }

        btn_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBrailleToilet.equals("YES")) {
                    hasBrailleToilet = "NO";
                    btn_toilet.setTextColor(Color.WHITE);
                }
                else {
                    hasBrailleToilet = "YES";
                    btn_toilet.setTextColor(Color.RED);
                }
            }
        });

        btn_stair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBrailleStair.equals("YES")) {
                    hasBrailleStair = "NO";
                    btn_stair.setTextColor(Color.WHITE);
                }
                else {
                    hasBrailleStair = "YES";
                    btn_stair.setTextColor(Color.RED);
                }
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBrailleMenu.equals("YES")) {
                    hasBrailleMenu = "NO";
                    btn_menu.setTextColor(Color.WHITE);
                }
                else {
                    hasBrailleMenu = "YES";
                    btn_menu.setTextColor(Color.RED);
                }
            }
        });

        btn_elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasElevator.equals("YES")) {
                    hasElevator = "NO";
                    btn_elevator.setTextColor(Color.WHITE);
                }
                else {
                    hasElevator = "YES";
                    btn_elevator.setTextColor(Color.RED);
                }
            }
        });

        btn_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasServing.equals("YES")) {
                    hasServing = "NO";
                    btn_serving.setTextColor(Color.WHITE);
                }
                else {
                    hasServing = "YES";
                    btn_serving.setTextColor(Color.RED);
                }
            }
        });

        Button btn_facility_cancel  = (Button) findViewById(R.id.btn_facility_cancel);
        Button btn_facility_confirm = (Button) findViewById(R.id.btn_facility_confirm);

        btn_facility_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_facility_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = user_facility.edit();
                editor.putString("hasBrailleToilet", hasBrailleToilet);
                editor.putString("hasBrailleStair", hasBrailleStair);
                editor.putString("hasBrailleMenu", hasBrailleMenu);
                editor.putString("hasElevator", hasElevator);
                editor.putString("hasServing", hasServing);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
