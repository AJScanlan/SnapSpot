package com.ajscanlan.snapspot;

import android.content.Context;

import com.ajscanlan.snapspot.model.Image;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Alexander Scanlan on 12/11/2015
 */

public class CustomIconRenderer extends DefaultClusterRenderer<Image>{

    public CustomIconRenderer(Context context, GoogleMap map, ClusterManager<Image> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Image item, MarkerOptions markerOptions) {
        BitmapDescriptor bmpDescriptor = BitmapDescriptorFactory.fromBitmap(item.getThumb());

        markerOptions.icon(bmpDescriptor);
        //markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getPosition().toString());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
