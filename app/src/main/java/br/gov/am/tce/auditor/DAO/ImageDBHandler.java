package br.gov.am.tce.auditor.DAO;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

public class ImageDBHandler {
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private Context mContext;
    private Photo storedPhoto = null;

    private List<String> photosTestList = new ArrayList<>();

    // constructor
    public ImageDBHandler(Context context) {
        mContext = context;
    }

    // UPLOAD PHOTOS
    public void uploadPhotos(List<Photo> selectedPhotoList) {
        if(selectedPhotoList.size() == 0) {
            return;
        }

        for(Photo photo: selectedPhotoList) {
            uploadPhoto(photo);
        }
    }

    private void uploadPhoto(Photo photo) {
        if(!isSavedInStorage(photo)) {
            String newId = getNewID(photo);
            updatePhoto(photo, newId);
            submitPhoto(photo);
        }
        // if already stored, user may want to update photo (title). the key is kept.
    }

    private boolean isSavedInStorage(Photo photo) {
        DatabaseReference photoReference = getPhotoReference(photo);
        photoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storedPhoto = dataSnapshot.getValue(Photo.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(storedPhoto != null) {
            storedPhoto = null;
            return true;
        } else {
            return false;
        }
    }

    private String getNewID(Photo photo) {
        DatabaseReference photoReference = getPhotoReference(photo);
        return photoReference.push().getKey();
    }

    private void updatePhoto(Photo photo, String newId) {
        File file = PhotoLab.get(mContext).getPhotoFile(photo);
        PhotoLab.get(mContext).updatePhotoId(photo, newId);
        file.renameTo(PhotoLab.get(mContext).getPhotoFile(photo));
    }

    private void submitPhoto(final Photo photo) {
        final DatabaseReference photoReference = getPhotoReference(photo);
        photoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> values = photo.toMap();
                Map<String, Object> photoUpdate = new HashMap<>();
                photoUpdate.put(getPhotoPath(photo), values);
                mDatabaseReference.updateChildren(photoUpdate);
                uploadPhotoToStorage(photoReference, photo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ImageHandler", "inner onCancelled method on submitPhoto");
            }
        });
    }

    private void uploadPhotoToStorage(final DatabaseReference photoReference, final Photo photo) {
        File photoFile = PhotoLab.get(mContext).getPhotoFile(photo);
        Uri uri = FileProvider.getUriForFile(mContext,"br.gov.am.tce.auditor.fileProvider", photoFile);
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

    // LATER
    private void schrinkPhotoImage(Photo photo) {}

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

    private String getPhotoPath(Photo photo) {
        String result;
        if (!empty(photo.getBemPublico())) {
            result = photo.getBemPublico();
            if (!empty(photo.getContrato())) {
                result = result + "/" + photo.getContrato();
                if (!empty(photo.getMedicao())) {
                    result = result + "/" + photo.getMedicao() + "/" + photo.getId();
                } else {
                    result = result + "/" + photo.getId();
                }
            } else {
                result = result + "/" + photo.getId();
            }
        } else {
            result = photo.getId();
        }
        return result;
    }

    // DOWNLOAD PHOTOS
    public void downloadPhotos(List<ContextObject> downloadList) {
        DatabaseReference reference = mDatabaseReference.child("EE11491").child("456").child("abc");
        downloadPhotos(reference);
    }

    private void downloadPhotos(final DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount() == 0) {
                        DatabaseReference photoReference = dataSnapshot.getRef().getParent();
                        photoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // test code
/*
                                if (!photosTestList.contains(dataSnapshot.getRef().getKey())) {
                                    photosTestList.add(dataSnapshot.getRef().getKey());
                                    Log.d("ImageDBHandler", dataSnapshot.getRef().getKey());
                                }
*/
                                // production code
                                String photoId = dataSnapshot.getRef().getKey();
                                PhotoLab photoLab = PhotoLab.get(mContext);
                                if(photoLab.getPhoto(photoId) == null) {
                                    Photo newPhoto = dataSnapshot.getValue(Photo.class);
                                    photoLab.addPhoto(newPhoto);
                                    getImageFromServer(newPhoto);
                                    Log.d("ImageDBHandler", dataSnapshot.getRef().getKey());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            downloadPhotos(child.getRef());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                        Log.d("ImageDBHandler", "Successfully downloaded photo: " + photo.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ImageDBHandler", "Failed on downloading file");
                        photoLab.deletePhoto(photo);
                    }
                });
    }

    // HELPER
    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

}
