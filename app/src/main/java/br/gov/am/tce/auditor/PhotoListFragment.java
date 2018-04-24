package br.gov.am.tce.auditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.PhotoLab;
import br.gov.am.tce.auditor.helpers.PictureUtils;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by adrnm on 25/10/2017.
 */

public class PhotoListFragment extends Fragment {
    private static final String TAG = "PhotoListFragment";
    private static final String PHOTOS = "photos";
    private DatabaseReference photosDBReference = FirebaseDatabase.getInstance().getReference().child(PHOTOS);

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private List<Photo> mSelectedPhotosList = new ArrayList<>();

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;

    public static Fragment newInstance() {
        return new PhotoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, container, false);
        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        PhotoLab photoLab = PhotoLab.get(getActivity());
        List<Photo> photos = photoLab.getPhotos();

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
            Intent intent = PhotoActivity.newIntent(getActivity(), mPhoto.getId());
            startActivity(intent);
        }
    }

    // ************************  MENU METHODS  **********************//
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_photo:
                Photo photo = new Photo();
                PhotoLab.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(getActivity(), photo.getId());
                startActivity(intent);
                return true;
            case R.id.map_photo:
                if(mSelectedPhotosList.size() != 0) {
                    Intent mapIntent = MapsActivity.newIntent(getActivity(), mSelectedPhotosList);
                    startActivity(mapIntent);
                    return true;
                }
            case R.id.upload_photo:
                if(mSelectedPhotosList.size() != 0) {
                    uploadPhotos();
                }
                return true;
            case R.id.check_new_photos_on_server:
                DownloadPhotos(photosDBReference);
                return true;
            case R.id.search:
                Intent searchIntent = new Intent(getActivity(), SearchContratosActivity.class);
                startActivity(searchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ************************  SERVER PHOTOS DOWNLOAD **********************//
    private void DownloadPhotos(final DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    if(dataSnapshot.getChildrenCount() != 0) {
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            String itemId = child.getKey();
                            if(child.hasChild("bemPublico")) {
                                PhotoLab photoLab = PhotoLab.get(getActivity());
                                if(photoLab.getPhoto(itemId) == null) {
                                    Photo newPhoto = child.getValue(Photo.class);
                                    getImageFromServer(newPhoto);
                                }
                            } else {
                                DownloadPhotos(reference.child(itemId));
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed on reading database: " + databaseError.toException());
            }
        });
    }

    private void getImageFromServer(final Photo photo) {
        StorageReference photoStorageReference = mStorageReference.child(photo.getId());
        final PhotoLab photoLab = PhotoLab.get(getActivity());
        File photoFile = photoLab.getPhotoFile(photo);
        photoStorageReference.getFile(photoFile)
            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    photoLab.addPhoto(photo);
                    updateUI();
                    Log.d(TAG, "Successfully downloaded photo: " + photo.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed on downloading file");
                }
            });
    }

    private void uploadPhotos() {
        for (final Photo photo: mSelectedPhotosList) {
            photo.registerItselfToDBServer(getActivity());
        }
        updateUI();
    }

}
