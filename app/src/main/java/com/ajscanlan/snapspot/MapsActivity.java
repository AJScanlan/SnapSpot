package com.ajscanlan.snapspot;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ajscanlan.snapspot.model.Image;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.File;
import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        ImageFragment.OnFragmentInteractionListener, ClusterManager.OnClusterItemClickListener {

    //static HashMap<String, Image> hashMap = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MAPS_DEBUG_TAG";
    private GoogleMap mMap;
    private String mCurrentPhotoPath;

    private ClusterManager<Image> mClusterManager;

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
        //mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false); //removes the buttons on map (NOT COMPASS)
        setUpClusterer();
    }

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        Log.d("HASHMAP", "in method " + marker.getId());
//        Log.d("HASHMAP", "in method " + hashMap.get(marker.getId()));
//
//        Image imageToDisplay = hashMap.get(marker.getId());
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.map, ImageFragment.newInstance(marker.getId(), imageToDisplay))
//                .addToBackStack("")
//                .commit();
//
//        Button cameraButton = (Button) findViewById(R.id.open_camera_button);
//        cameraButton.setVisibility(View.INVISIBLE);
//
//        return false;
//    }

    @Override
    public void onFragmentInteraction() {
        //Finds the OpenCamera button and places it on fragment
        Button cameraButton = (Button) findViewById(R.id.open_camera_button);
        cameraButton.setVisibility(View.VISIBLE);

        //Finds the demo button and places it on fragment
        Button demoButton = (Button) findViewById(R.id.demo_button);
        demoButton.setVisibility(View.VISIBLE);
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

//            Random randy = new Random();
//
//            double randLat = -90.0 + (90.0 - (-90.0)) * randy.nextDouble();
//            double randLng = -180.0 + (180.0 - (-180.0)) * randy.nextDouble();
//
//            Log.d("LAT", "Lat is :" + randLat);
//            Log.d("LNG", "Lng is :" + randLng);

            try {
                float[] latLngFloat = ImageManipulator.getLatLngExif(mCurrentPhotoPath);

                LatLng mPosition = new LatLng(latLngFloat[0], latLngFloat[1]);

                addMarker(mPosition, mCurrentPhotoPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMarker(LatLng position, String path) {
        Bitmap bmp = ImageManipulator.decodeFile(new File(path));
        //Bitmap bmp = ImageManipulator.rotateBitmapFromFile(path);  //BitmapFactory.decodeFile(path);
        Bitmap bmpThumb = ImageManipulator.bitmapToScaledBitmap(bmp);


//        Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(position)
//                .icon(BitmapDescriptorFactory.fromBitmap(bmp)));

        //Constructing image and adding to hashmap with Marker ID as key
        Image tempImage = new Image(position, bmp, bmpThumb, path);
        mClusterManager.addItem(tempImage);

        //hashMap.put(marker.getId(),tempImage);
    }

    private void setUpClusterer() {

        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        //setup custom renderer
        mClusterManager.setRenderer(new CustomIconRenderer(getApplicationContext(),
                mMap, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(MapsActivity.this);
        mMap.setOnMarkerClickListener(mClusterManager);

    }

    public boolean onClusterItemClick(ClusterItem clusterItem) {

        Log.d(TAG, "IN onClusterItemClick");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, ImageFragment.newInstance((Image) clusterItem))
                .addToBackStack("")
                .commit();

        Button cameraButton = (Button) findViewById(R.id.open_camera_button);
        cameraButton.setVisibility(View.INVISIBLE);

        return false;
    }

    public void demoClick(View view) {

        Resources res = getApplicationContext().getResources();
        Bitmap b = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;

            Image offsetItem = new Image(new LatLng(lat, lng), b, b, null);
            mClusterManager.addItem(offsetItem);
        }
    }
}
