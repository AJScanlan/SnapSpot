package com.ajscanlan.snapspot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander Scanlan on 08/10/2015
 */
public class ImageManipulator {

    static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //Checking if null for debugging purposes
        Log.d("LOG_MESSAGE", timeStamp == null ? "null" : timeStamp);
        Log.d("LOG_MESSAGE", imageFileName == null ? "null" : imageFileName);
        Log.d("LOG_MESSAGE", storageDir== null ? "null" : storageDir.toString());

        //Creating the file using the strings made above
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("LOG_MESSAGE", image == null ? "null" : image.getAbsolutePath());

        //Log.d("LOG_MESSAGE", mCurrentPhotoPath == null ? "null" : mCurrentPhotoPath);
        return image;
    }

    static Bitmap bitmapToScaledBitmap(Bitmap bmp) {

        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        //Bitmap tempBitmap = Bitmap.createScaledBitmap(bmp, 200, 200, true);

        return ThumbnailUtils.extractThumbnail(bmp, 150, 150);

//        Canvas canvas1 = new Canvas(tempBitmap);
//
//        // paint defines the text color,
//        // stroke width, size
//        Paint color = new Paint();
//        color.setTextSize(35);
//        color.setColor(Color.BLACK);
//
//        //modify canvas
//        canvas1.drawBitmap(tempBitmap, 0, 0, color);
//
//        return tempBitmap;
    }

    static Bitmap rotateBitmapFromFile(String filePath){
        String orientString;

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(filePath, opts);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(exif != null) {
            orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        } else {
            return null;
        }

        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        FileOutputStream fOut;

        try {
            fOut = new FileOutputStream(filePath);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return rotatedBitmap;
    }

    static float[] getLatLngExif(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);

        //setting up float[] and init with Lat and Lng
        float[] latLngFloat = new float[2];
        exif.getLatLong(latLngFloat);

        return latLngFloat;
    }


    static public Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int IMAGE_MAX_SIZE = 1000;
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);

            //Flip the image 90 degrees
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            b = Bitmap.createBitmap(b , 0, 0, b .getWidth(), b.getHeight(), matrix, true);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
