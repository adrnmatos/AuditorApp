package br.gov.am.tce.auditor.control;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

public class GridHandler {

    private Context mContext;
    private List<Photo> photoList;

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;

    public GridHandler(Context context) {
        mContext = context;
        photoList = PhotoLab.get(mContext).getPhotos();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    public void doFilter(String author, String bemPublico, String contrato, String medicao) {
        photoList = PhotoLab.get(mContext).searchPhotos(author, bemPublico, contrato, medicao);
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void selectPhotoContext() {

    }

    public void downloadPhotos() {
        DatabaseReference reference = mDatabaseReference.child("photos");
        downloadPhotos(reference);
    }

    private void downloadPhotos(final DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    if(dataSnapshot.getChildrenCount() != 0) {
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            String itemId = child.getKey();
                            if(child.hasChild("bemPublico")) {
                                PhotoLab photoLab = PhotoLab.get(mContext);
                                if(photoLab.getPhoto(itemId) == null) {
                                    Photo newPhoto = child.getValue(Photo.class);
                                    getImageFromServer(newPhoto);
                                }
                            } else {
                                downloadPhotos(reference.child(itemId));
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("GridHandler", "Failed on reading database: " + databaseError.toException());
            }
        });
    }

    private void getImageFromServer(final Photo photo) {
        StorageReference photoStorageReference = mStorageReference.child(photo.getId());
        final PhotoLab photoLab = PhotoLab.get(mContext);
        File photoFile = photoLab.getPhotoFile(photo);
        photoStorageReference.getFile(photoFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        photoLab.addPhoto(photo);
                        photoList.add(photo);
                        Log.d("GridHandler", "Successfully downloaded photo: " + photo.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GridHandler", "Failed on downloading file");
                    }
                });
    }

    public void uploadPhotos(List<Photo> selectedPhotoList) {
        if(selectedPhotoList.size() == 0) {
            return;
        }
        for (final Photo photo: selectedPhotoList) {
            final DatabaseReference photoReference = getPhotoReference(photo);
            photoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null) {
                        photoReference.setValue(photo);
                        Log.d("GridHandler", "getTime returned: " + photo.getTime());
                        uploadPhotoToStorage(photoReference, photo);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("GridHandler", "Failed on writing to database: " + databaseError.toException());
                }
            });

        }
    }

    private void uploadPhotoToStorage(final DatabaseReference photoReference, final Photo photo) {
        File mPhotoFile = PhotoLab.get(mContext).getPhotoFile(photo);
        Uri uri = FileProvider.getUriForFile(mContext,"br.gov.am.tce.auditor.fileProvider", mPhotoFile);
        StorageReference photoStorageReference = mStorageReference.child(photo.getId());
        photoStorageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("GridHandler", "Successfully uploaded photo: " + photo.getId());
                } else {
                    photoReference.removeValue();
                    Log.e("GridHandler", "Failed on uploading file" + task.getException());
                }
            }
        });
    }

    private DatabaseReference getPhotoReference(Photo photo) {
        DatabaseReference photoDBRef = mDatabaseReference.child("photos");

        if (!empty(photo.getBemPublico())) {
            DatabaseReference photoDBRef1 = photoDBRef.child(photo.getBemPublico());
            if (!empty(photo.getContrato())) {
                DatabaseReference photoDBRef2 = photoDBRef1.child(photo.getContrato());
                if (!empty(photo.getMedicao())) {
                    return photoDBRef2.child(photo.getMedicao()).child(photo.getId());
                } else {
                    return photoDBRef2.child(photo.getId());
                }
            } else {
                return photoDBRef1.child(photo.getId());
            }
        } else {
            return photoDBRef.child(photo.getId());
        }
    }

    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }


}
