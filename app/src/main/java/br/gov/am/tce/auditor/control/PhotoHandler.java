package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.PhotoLab;

public class PhotoHandler {
    private static final String TAG = "PhotoHandler";
    private static final int PHOTO_REQUEST = 0;
    private static final int LOCATION_SETTINGS_REQUEST = 1;

    private Context mContext;
    private Photo mPhoto;
    private File mPhotoFile;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    public PhotoHandler(Context context, Photo photo) {
        mContext = context;
        mPhoto = photo;
        mPhotoFile = PhotoLab.get(mContext).getPhotoFile(mPhoto);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        try{
            mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        mLastLocation = location;
                    }
                }
            });
        } catch (SecurityException se) {
            Log.e(TAG, se.getMessage());
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLastLocation = locationResult.getLastLocation();
            }
        };

        configureLocationRequest();
    }

    public void deletePhoto() {
        PhotoLab.get(mContext).deletePhoto(mPhoto);
        ((Activity)mContext).finish();
    }

    public String getPhotoContext() {
        if(mPhoto.getBemPublico().equals("")) {
            return "";
        } else {
            return String.format("%s/%s/%s", mPhoto.getBemPublico(), mPhoto.getContrato(), mPhoto.getMedicao());
        }
    }

    public void clearPhotoContext() {
        mPhoto.setBemPublico("");
        mPhoto.setContrato("");
        mPhoto.setMedicao("");
    }

    public void editPhotoDescription(String description) {
        mPhoto.setTitle(description);
//        PhotoLab.get(mContext).updatePhoto(mPhoto);
    }

    public boolean canTakePhoto() {
        return(isUserAuthor() && (captureImageIntent.resolveActivity(mContext.getPackageManager()) != null));
    }

    public boolean isUserAuthor() {
        return(AuditorPreferences.getUsername(mContext).equals(mPhoto.getAuthor()));
    }

    public void applyContext() throws Exception {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(mPhoto);
//      THIS SHOULD BE USING REFACTORED CONTEXT HANDLER CLASS
//
//       ContextHandler.get().applyContextSinglePhoto((Activity)mContext, photoList);
    }

    /* ***************** CAMERA **********************/
    public void takePhoto() {
        startLocationUpdates();

        Uri uri = FileProvider.getUriForFile(mContext, "br.gov.am.tce.auditor.fileProvider", mPhotoFile);
        captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = mContext.getPackageManager()
                .queryIntentActivities(captureImageIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity : cameraActivities) {
            mContext.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        mPhoto.setLatitude(mLastLocation.getLatitude());
        mPhoto.setLongitude(mLastLocation.getLongitude());
        mPhoto.setTime(mLastLocation.getTime());
        mPhoto.setAuthor(AuditorPreferences.getUsername(mContext));

        ((Activity)mContext).startActivityForResult(captureImageIntent, PHOTO_REQUEST);
    }

    public void revokePermissions() {
        Uri uri = FileProvider.getUriForFile(mContext,"br.gov.am.tce.auditor.fileProvider", mPhotoFile);
        mContext.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        stopLocationUpdates();
    }


    /* **************** LOCATION **************************/
    private void configureLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(mContext);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener((Activity)mContext, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult((Activity)mContext, LOCATION_SETTINGS_REQUEST);
                        } catch (IntentSender.SendIntentException sendEx) {
                            Log.e(TAG, sendEx.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "Location settings can not be satisfied.");
                        break;
                }
            }
        });
    }

    private void startLocationUpdates() {
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } catch (SecurityException se) {
            Log.d(TAG, se.getMessage());
        }
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}
