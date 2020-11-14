package com.projekt.tdp028.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PLatLng;

public class FindLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String pollId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        View placeholder = findViewById(R.id.map);
        pollId = getIntent().getStringExtra("POLL_ID");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(FirebaseKeys.LOCATION).child(pollId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PLatLng pLatLng = dataSnapshot.getValue(PLatLng.class);
                if(pLatLng == null) {
                    noLocation();
                    return;
                }
                LatLng latLng = pLatLng.getLatLng();
                mMap.addMarker(new MarkerOptions().position(latLng).title(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void noLocation() {
        Toast.makeText(getApplicationContext(),"No location was found for this post",Toast.LENGTH_LONG).show();
        finish();
    }
}
