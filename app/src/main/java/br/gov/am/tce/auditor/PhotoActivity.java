package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.gov.am.tce.auditor.control.PhotoHandler;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;
import br.gov.am.tce.auditor.service.PictureUtils;


/**
 * Created by adrnm on 26/10/2017.
 */

public class PhotoActivity extends AppCompatActivity {
    private static final String EXTRA_PHOTO_OBJ = "br.gov.am.tce.auditor.photo";
    private static final int PHOTO_REQUEST = 0;
    private static final int LOCATION_SETTINGS_REQUEST = 1;

    private Photo mPhoto;
    private File mPhotoFile;
    private PhotoHandler mPhotoHandler;

    private TextView mPhotoContext;
    private ImageButton mClearButton;
    private ImageButton mSearchButton;
    private ImageView mPhotoView;

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
        mPhotoHandler = new PhotoHandler(this, mPhoto);

        TextView mPhotoAuthor = findViewById(R.id.PHAuthor_TV);
        mPhotoAuthor.setText(mPhoto.getAuthor());

        mPhotoContext = findViewById(R.id.PHContext_TV);
        mClearButton = findViewById(R.id.PHClear_BTN);
        mSearchButton = findViewById(R.id.PHSearch_BTN);

        ImageButton mCameraButton = findViewById(R.id.photo_camera);
        mCameraButton.setEnabled(mPhotoHandler.canTakePhoto());

        mPhotoView = findViewById(R.id.photo_photo_view);

        EditText mPhotoDetails = findViewById(R.id.photo_note);
        mPhotoDetails.setText(mPhoto.getTitle());
        mPhotoDetails.setEnabled(mPhotoHandler.isUserAuthor());
        mPhotoDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPhotoHandler.editPhotoDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updatePhotoContext();
        updatePhotoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePhotoContext();
        updatePhotoView();
    }

    @Override
    public void onPause() {
        super.onPause();
        PhotoLab.get(this).updatePhoto(mPhoto);
    }

    public void takePhoto(View v) {
        mPhotoHandler.takePhoto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_photo:
                if(!mPhotoHandler.isUserAuthor()) {
                    Toast.makeText(this, "you cannot edit other authors photo", Toast.LENGTH_LONG).show();
                } else {
                    mPhotoHandler.deletePhoto();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClearContextButtonClick(View v) {
        mPhotoHandler.clearPhotoContext();
        updatePhotoContext();
    }

    public void onApplyContextButtonClick(View v) {
        try {
            mPhotoHandler.applyContext();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            updatePhotoContext();
        }
    }

    private void updatePhotoContext() {
        mPhotoContext.setText(mPhotoHandler.getPhotoContext());
        if(!mPhotoHandler.isUserAuthor()) {
            Toast.makeText(this, "you cannot edit other authors photo", Toast.LENGTH_LONG).show();
        } else {
            if(mPhotoHandler.getPhotoContext().equals("")) {
                mClearButton.setVisibility(View.INVISIBLE);
                mSearchButton.setVisibility(View.VISIBLE);
            } else {
                mClearButton.setVisibility(View.VISIBLE);
                mSearchButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST) {
            mPhotoHandler.revokePermissions();
            updatePhotoView();
        } else if (requestCode == LOCATION_SETTINGS_REQUEST) {
            // TODO: To handle check setting request
        }
    }
}
