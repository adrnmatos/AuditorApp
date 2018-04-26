package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by Adriano on 23/04/2018.
 */

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = new Photo();
                PhotoLab.get(MainActivity.this).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(MainActivity.this, photo.getId());
                startActivity(intent);
            }
        });
    }

    public void searchMethod(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void galerieMethod(View v) {
        Intent intent = new Intent(this, PhotoListActivity.class);
        startActivity(intent);
    }

    public void localDBMethod(View v) {

    }

    public void myNeighborhoodMethod(View v) {

    }
}
