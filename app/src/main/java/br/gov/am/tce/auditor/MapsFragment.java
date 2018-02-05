package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String ARG_PHOTOS = "photo_list";

    private List<Photo> photoList;
    private GoogleMap mMap;

    public static MapsFragment newInstance(List<Photo> photoList) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTOS, (ArrayList<Photo>)photoList);
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = (ArrayList<Photo>) getArguments().getSerializable(ARG_PHOTOS);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        final View mapView = this.getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                public void onGlobalLayout() {
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

                    for(Photo photo : photoList) {
                        LatLng coords = new LatLng(photo.getLatitude(), photo.getLongitude());
                        boundsBuilder.include(coords);

                        Marker marker = mMap.addMarker(new MarkerOptions().position(coords));
                        marker.setTag(photo.getId());
                    }

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.12);

                    CameraUpdate update = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), width, height, padding);
                    mMap.animateCamera(update);
                }
            });
        }

    }

    // TODO: CREATE ANOTHER ACTIVITY TO HANDLE PUBLIC PHOTOS ASSOCIATED
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(), marker.getTag().toString(), Toast.LENGTH_LONG).show();
        return false;
    }

}
