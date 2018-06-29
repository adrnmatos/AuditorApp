package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.PhotoLab;
import br.gov.am.tce.auditor.service.PictureUtils;


/**
 * Created by adrnm on 26/10/2017.
 */

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";
    private static final String EXTRA_PHOTO_OBJ = "br.gov.am.tce.auditor.photo";
    private static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private Photo mPhoto;
    private File mPhotoFile;

    private ImageView mPhotoView;
    private final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    public static Intent newIntent(Context packageContext, Photo photo) {
        Intent intent = new Intent(packageContext, PhotoActivity.class);
        intent.putExtra(EXTRA_PHOTO_OBJ, photo);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mPhoto = getIntent().getParcelableExtra(EXTRA_PHOTO_OBJ);
        mPhotoFile = PhotoLab.get(this).getPhotoFile(mPhoto);

        TextView photo_author_tv = findViewById(R.id.PHAuthor_TV);
        photo_author_tv.setText(AuditorPreferences.getUsername(this));

        /* location code initialization ******************/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
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

        /* controls initialization *********************/
        mPhotoView = findViewById(R.id.photo_photo_view);
        updatePhotoView();

        ImageButton mCameraButton = findViewById(R.id.photo_camera);
        boolean canTakePhoto = mPhotoFile != null && captureImageIntent.resolveActivity(getPackageManager()) != null;
        mCameraButton.setEnabled(canTakePhoto);

        EditText mPhotoNote = findViewById(R.id.photo_note);
        mPhotoNote.setText(mPhoto.getTitle());
        mPhotoNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPhoto.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } catch (SecurityException se) {
            Log.d(TAG, se.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        PhotoLab.get(this).updatePhoto(mPhoto);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        finish();
    }

    private void configureLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(PhotoActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            Log.e(TAG, sendEx.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "UNAVAILABLE LOCATION SETTING");
                        break;
                }
            }
        });
    }

    /** take photo ***************/
    public void takePhoto(View v) {
        Uri uri = FileProvider.getUriForFile(this, "br.gov.am.tce.auditor.fileProvider", mPhotoFile);
        captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(captureImageIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity : cameraActivities) {grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        mPhoto.setLatitude(mLastLocation.getLatitude());
        mPhoto.setLongitude(mLastLocation.getLongitude());
        mPhoto.setTime(mLastLocation.getTime());

        startActivityForResult(captureImageIntent, REQUEST_PHOTO);
    }

    /**************** menu methods ***************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_photo:
                /*
                * if(photo.getAuthor() != AuditorPreferences.getUsername(this)) return false;
                * */
                PhotoLab.get(this).deletePhoto(mPhoto);
                /* TODO: to check if there is missing file on filesystem and still need to delete it */
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClearButtonClick(View v) {
        mPhoto.setBemPublico("");
        mPhoto.setContrato("");
        mPhoto.setMedicao("");

        updatePhotoView();
    }

    public void onSearchButtonClick(View v) {
        FindContextHandler.get().findContext(this, mPhoto);
        finish();
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);
            mPhotoView.setImageBitmap(bitmap);
        }

        boolean isContextClear = mPhoto.getBemPublico().equals("");
        if(isContextClear) {
            findViewById(R.id.PHClear_BTN).setVisibility(View.INVISIBLE);
            findViewById(R.id.PHSearch_BTN).setVisibility(View.VISIBLE);
            TextView contextTextView = findViewById(R.id.PHContext_TV);
            contextTextView.setText("");
        } else {
            findViewById(R.id.PHClear_BTN).setVisibility(View.VISIBLE);
            findViewById(R.id.PHSearch_BTN).setVisibility(View.INVISIBLE);
            TextView contextTextView = findViewById(R.id.PHContext_TV);
            String contextStr = String.format("%s / %s / %s",mPhoto.getBemPublico(),mPhoto.getContrato(),mPhoto.getMedicao());
            contextTextView.setText(contextStr);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(this,
                    "br.gov.am.tce.auditor.fileProvider",
                    mPhotoFile);
            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            // TODO: To handle check setting request
        }
    }
}
