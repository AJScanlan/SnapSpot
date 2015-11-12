package com.ajscanlan.snapspot.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Alexander Scanlan on 12/11/2015
 */
public class Image implements ClusterItem{
    private LatLng mPosition;
    private Bitmap mBitmap;
    private Bitmap mThumbnail;
    private String mPath;


    public Image(LatLng position, Bitmap bitmap, Bitmap thumbnail, String mPath) {
        mPosition = position;
        mBitmap = bitmap;
        mThumbnail = thumbnail;
    }

    public String getPath() {
        return mPath;
    }

    public Bitmap getThumb(){
        return mThumbnail;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
