package com.example.tieto11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Geocoder;


public class MainActivity extends AppCompatActivity {




    private FusedLocationProviderClient fusedLocationClient;
    private EditText editti;
    private EditText editti1;
    private double lat = 0.0, lon = 0.0;
    private int locationRequestCode = 1000;
    public Geocoder geocoder;
    int maxResults = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editti = (EditText) findViewById(R.id.editti);
        this.editti1 = (EditText) findViewById(R.id.editti1);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        editti.setText(String.format(Locale.US, "%s, %s", lat, lon));
                        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                        try{
                            List<Address>listAddresses = geocoder.getFromLocation(lat,lon,1);
                            if(null!=listAddresses&&listAddresses.size()>0){
                                String _Location = listAddresses.get(0).getAddressLine(0);
                                editti1.setText(_Location);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    } else {
                        editti.setText("toimii");
                    }
                }






            });
        }

    }

}