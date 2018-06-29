package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

/**
 * Created by Adriano on 23/04/2018.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

    public void onNewPhotoFABClick(View v) {
        Photo photo = new Photo();
        PhotoLab.get(this).addPhoto(photo);
        Intent intent = PhotoActivity.newIntent(this, photo);
        startActivity(intent);
        finish();
    }

    public void searchMethod(View v) {
        FindContextHandler.get().findContext(this, null);
    }

    public void galleryMethod(View v) {
        Intent intent = new Intent(this, PhotoGridActivity.class);
        startActivity(intent);
    }

    public void localDBMethod(View v) {

    }

    public void myNeighborhoodMethod(View v) {

    }
}
