package com.ajscanlan.snapspot;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ImageFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    static HashMap<String, Integer> hashMap = new HashMap<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<File> files = new ArrayList<>();
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mImageView = (ImageView) findViewById(R.id.test);
    }

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

    private Bitmap resourceToBitmap(int resource) {
        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resource), 200, 200, true);
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

    private Bitmap bitmapToScaledBitmap(Bitmap bmp) {
        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap tempBitmap = Bitmap.createScaledBitmap(bmp, 200, 200, true);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.d("LOG_MESSAGE", timeStamp == null ? "null" : timeStamp);
        Log.d("LOG_MESSAGE", imageFileName == null ? "null" : imageFileName);
        Log.d("LOG_MESSAGE", storageDir== null ? "null" : storageDir.toString());

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("LOG_MESSAGE", image == null ? "null" : image.toString());
        Log.d("LOG_MESSAGE", mCurrentPhotoPath == null ? "null" : mCurrentPhotoPath);
        return image;
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("LOG_MESSAGE", "exception");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            Log.d("LOG_MESSAGE", photoFile == null ? "null" : "not null");
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);

            //File file = new File(mCurrentPhotoPath);

            //Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);

           // mImageView.setImageBitmap(bmp);

            //if(data == null) Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
        }
    }
}
