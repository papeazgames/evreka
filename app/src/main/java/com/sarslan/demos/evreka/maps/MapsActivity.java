package com.sarslan.demos.evreka.maps;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarslan.demos.evreka.R;
import com.sarslan.demos.evreka.container.Container;
import com.sarslan.demos.evreka.container.ContainerMng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "DB_ERR";
    private GoogleMap mMap;

    private static final int ZOOM_RATE = 30;
    private static final int NUM_CONTAINER = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ContainerMng.getInstance().createContainers(NUM_CONTAINER);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Setting a custom info window adapter for the google map
        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getApplicationContext());
        mMap.setInfoWindowAdapter(markerInfoWindowAdapter);

        setMapToListenDatabase();
        mMap.setOnMapLongClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Container.defPos, ZOOM_RATE));

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }




    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    private void setMapToListenDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Container.dataRef);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Container conDB = dataSnapshot.getValue(Container.class);
                updateContainer(conDB);


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);

    }

    private void updateContainer(Container conDB){

        Container con = ContainerMng.getInstance().getContainer(conDB.containerId);
        if(con!=null) {

            setContainerMarker(con);
            con.onContainerUpdatedByDB(conDB);
            if(con.getMarker().isInfoWindowShown())
                con.getMarker().showInfoWindow();
        }
    }
    private void setContainerMarker(Container con) {

        if(con.getMarker()!=null)
            return;
        LatLng pos = new LatLng(con.latitude, con.longitude);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(con.containerId.toString())
        );
        marker.setTag(con);
        con.setMarker(marker);

    }

}
