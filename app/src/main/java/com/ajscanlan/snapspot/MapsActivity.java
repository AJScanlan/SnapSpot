package com.ajscanlan.snapspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ImageFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    static HashMap<String, String> hashMap = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("HASHMAP", "in method " + marker.getId());
        Log.d("HASHMAP", "in method " + hashMap.get(marker.getId()));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, ImageFragment.newInstance(null, null, marker.getId()))
                .addToBackStack("")
                .commit();

        Button cameraButton = (Button) findViewById(R.id.open_camera_button);
        cameraButton.setVisibility(View.INVISIBLE);

        return false;
    }

    @Override
    public void onFragmentInteraction() {
        Button cameraButton = (Button) findViewById(R.id.open_camera_button);
        cameraButton.setVisibility(View.VISIBLE);
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                //creates temp file to store image
                photoFile = ImageManipulator.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("LOG_MESSAGE", "exception");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            Log.d("LOG_MESSAGE", photoFile == null ? "null" : "not null");
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Random randy = new Random();

            double randLat = -90.0 + (90.0 - (-90.0)) * randy.nextDouble();
            double randLng = -180.0 + (180.0 - (-180.0)) * randy.nextDouble();

            Log.d("LAT", "Lat is :" + randLat);
            Log.d("LNG", "Lng is :" + randLng);

            addMarker(randLat, randLng, mCurrentPhotoPath);
        }
    }

    private void addMarker(double lat, double lng, String path){
        Bitmap bmp = ImageManipulator.rotateBitmapFromFile(path);  //BitmapFactory.decodeFile(path);
        bmp = ImageManipulator.bitmapToScaledBitmap(bmp);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        hashMap.put(marker.getId(),path);
    }

}
