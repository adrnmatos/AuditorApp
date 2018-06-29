package br.gov.am.tce.auditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.control.GridHandler;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;
import br.gov.am.tce.auditor.service.PictureUtils;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by adrnm on 25/10/2017.
 */

public class PhotoGridFragment extends Fragment {
    private static final int FILTER_REQUEST = 0;
    private static final String DIALOG_FILTER = "DialogFilter";

    private GridHandler gridHandler;

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private List<Photo> mSelectedPhotosList = new ArrayList<>();
    private List<Photo> mDisplayedPhotoList = new ArrayList<>();

    public static Fragment newInstance() {
        return new PhotoGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        gridHandler = new GridHandler(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, container, false);

        // move mechanics to gridHandler
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = new Photo();
                PhotoLab.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(getActivity(), photo);
                startActivity(intent);
            }
        });

        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mDisplayedPhotoList = gridHandler.getPhotoList();
        updateUI(mDisplayedPhotoList);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(mDisplayedPhotoList);
    }

    private void updateUI(List<Photo> photos) {
        if(mAdapter == null) {
            mAdapter = new PhotoAdapter(photos);
            mPhotoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPhotos(photos);
            mAdapter.notifyDataSetChanged();
        }
    }

    // **********************   ADAPTER CLASS   ************************//
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        List<Photo> mPhotos;

        private PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
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
        private ImageView mPhotoImageView;
        private CheckBox mPhotoCheckBox;
        private Photo mPhoto;

        private PhotoHolder(View itemView) {
            super(itemView);
            mPhotoImageView = itemView.findViewById(R.id.photo_image_view);
            mPhotoCheckBox = itemView.findViewById(R.id.photo_checkbox);
            itemView.setOnClickListener(this);
        }

        private void bindPhotoItem(final Photo photo) {
            mPhoto = photo;

            mPhotoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        mSelectedPhotosList.add(mPhoto);
                    } else {
                        mSelectedPhotosList.remove(mPhoto);
                    }
                }
            });

            File mPhotoFile = PhotoLab.get(getActivity()).getPhotoFile(photo);
            if(mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoImageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                mPhotoImageView.setImageBitmap(bitmap);
            }
        }

        // edit photo
        @Override
        public void onClick(View view) {
            Intent intent = PhotoActivity.newIntent(getActivity(), mPhoto);
            startActivity(intent);
        }
    }

    // ************************  MENU METHODS  **********************//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_photos:
                FilterFragment dialog = new FilterFragment();
                dialog.setTargetFragment(PhotoGridFragment.this, FILTER_REQUEST);
                dialog.show(getFragmentManager(), DIALOG_FILTER);
                return true;
            case R.id.map_photo:
                if(mSelectedPhotosList.size() != 0) {
                    Intent mapIntent = MapActivity.newIntent(getActivity(), mSelectedPhotosList);
                    startActivity(mapIntent);
                    return true;
                }
            case R.id.upload_photo:
                gridHandler.uploadPhotos(mSelectedPhotosList);
                return true;
            case R.id.check_new_photos_on_server:
                gridHandler.downloadPhotos();
                mDisplayedPhotoList = gridHandler.getPhotoList();
                updateUI(mDisplayedPhotoList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == FILTER_REQUEST) {
            if(data == null) {
                return;
            }
            String[] selectedValues = data.getStringArrayExtra(FilterFragment.EXTRA_ARGS);

            gridHandler.doFilter(selectedValues[0], selectedValues[1], selectedValues[2], selectedValues[3]);
            mDisplayedPhotoList = gridHandler.getPhotoList();
            updateUI(mDisplayedPhotoList);
        }
    }

}
