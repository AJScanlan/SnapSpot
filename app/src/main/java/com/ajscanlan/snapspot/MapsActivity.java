package com.ajscanlan.snapspot;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ImageFragment.OnFragmentInteractionListener{

    private GoogleMap mMap;
    static HashMap<String, Integer> hashMap = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        //Convert resource to bitmap
        Bitmap bmp1 = resourceToBitmap(R.drawable.test2);
        Bitmap bmp2 = resourceToBitmap(R.drawable.test3);
        Bitmap bmp3 = resourceToBitmap(R.drawable.test4);
        Bitmap bmp4 = resourceToBitmap(R.drawable.test5);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng paris = new LatLng(48, 2.3);
        LatLng newYork = new LatLng(40, -73);
        LatLng brazil = new LatLng(-15, -47);

        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromBitmap(bmp1)));

        hashMap.put(marker1.getId(), R.drawable.test2);
        //Log.d("HASHMAP", hashMap.get(marker1.getId()).toString());

        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(paris)
                .title("Marker in Paris")
                .icon(BitmapDescriptorFactory.fromBitmap(bmp2)));
        hashMap.put(marker2.getId(), R.drawable.test3);

        Marker marker3 = mMap.addMarker(new MarkerOptions()
                .position(newYork)
                .title("Marker in New York")
                .icon(BitmapDescriptorFactory.fromBitmap(bmp3)));
        hashMap.put(marker3.getId(), R.drawable.test4);

        Marker marker4 = mMap.addMarker(new MarkerOptions()
                .position(brazil)
                .title("Marker in Brazil")
                .icon(BitmapDescriptorFactory.fromBitmap(bmp4)));
        hashMap.put(marker4.getId(), R.drawable.test5);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        Log.d("HASHMAP", "m0 " + marker1.getId());
        Log.d("HASHMAP", "2130837640 " + hashMap.get(marker1.getId()));

    }

    private Bitmap resourceToBitmap(int resource){
        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resource),200,200,true);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color,
        // stroke width, size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        //modify canvas
        canvas1.drawBitmap(bmp, 0, 0, color);
        //canvas1.drawText("User Name!", 30, 40, color);

        return bmp;
    }

    private Bitmap resourceToBitmap(Bitmap bmp){
        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap tempBitmap = Bitmap.createScaledBitmap(bmp,200,200,true);
        Canvas canvas1 = new Canvas(tempBitmap);

        // paint defines the text color,
        // stroke width, size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        //modify canvas
        canvas1.drawBitmap(tempBitmap, 0, 0, color);

        return tempBitmap;
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

        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void openCamera(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = resourceToBitmap(imageBitmap);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .icon(BitmapDescriptorFactory.fromBitmap(imageBitmap)));

        }
    }
}
