package br.gov.am.tce.auditor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import br.gov.am.tce.auditor.control.GridHandler;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;
import br.gov.am.tce.auditor.service.PictureUtils;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by adrnm on 25/10/2017.
 */

public class GridActivity extends AppCompatActivity implements FilterFragment.PhotoFilterDialogListener {
    private GridHandler gridHandler;
    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        mPhotoRecyclerView = findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        gridHandler = new GridHandler(this);
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_grid_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_photos:
                FilterFragment dialog = new FilterFragment();
                dialog.show(getSupportFragmentManager(), "DialogFilter");
                return true;
            case R.id.map_photo:
                gridHandler.mapPhotos();
                return true;
            case R.id.upload_photos:
                gridHandler.uploadPhotos();
                return true;
            case R.id.apply_context:
                gridHandler.applyContext();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        if(mAdapter == null) {
            mAdapter = new PhotoAdapter(gridHandler.getPhotoList());
            mPhotoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPhotos(gridHandler.getPhotoList());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onNewPhoto(View v) {
        gridHandler.newPhoto();
    }

    @Override
    public void onPhotoFilterDialogPositiveClick(String bemPublico, String contrato, String medicao) {
        gridHandler.filterPhotos(bemPublico, contrato, medicao);
        updateUI();
    }

    @Override
    public void onPhotoFilterDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this, "negative button", Toast.LENGTH_LONG).show();
    }


    // **********************   ADAPTER CLASS   ************************//
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        List<Photo> mPhotos;

        private PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(GridActivity.this);
            View v = inflater.inflate(R.layout.list_item_photo, parent, false);
            return new PhotoHolder(v);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bindPhotoItem(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        private void setPhotos(List<Photo> photos) {
            mPhotos = photos;
        }

    }

    // ****************   PHOTO HOLDER CLASS   ********************************************* //
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Photo mPhoto;
        private ImageView mPhotoImageView;
        private CheckBox mPhotoCheckBox;

        private PhotoHolder(View itemView) {
            super(itemView);
            mPhotoImageView = itemView.findViewById(R.id.photo_image_view);
            mPhotoCheckBox = itemView.findViewById(R.id.photo_checkbox);
            itemView.setOnClickListener(this);
        }

        private void bindPhotoItem(Photo photo) {
            mPhoto = photo;

            // should go to handler? mPhotoImageView.setImageBitmap(mHandler.getImageBitmap(mPhoto))
            File mPhotoFile = PhotoLab.get(GridActivity.this).getPhotoFile(photo);
            if(mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoImageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), GridActivity.this);
                mPhotoImageView.setImageBitmap(bitmap);
            }

            mPhotoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        gridHandler.selectPhoto(mPhoto);
                    } else {
                        gridHandler.deselectPhoto(mPhoto);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            gridHandler.showPhoto(mPhoto);
        }

    }

}
