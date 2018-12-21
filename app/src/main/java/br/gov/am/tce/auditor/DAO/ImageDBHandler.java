package br.gov.am.tce.auditor.DAO;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
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

import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

public class ImageDBHandler {
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private Context mContext;
    private Photo storedPhoto = null;   // BAD IMPLEMENTATION
    private boolean photosExists = false;
    private List<DatabaseReference> contextHierarchieBeneathReferenceList = new ArrayList<>();
    private List<String > photoDownloadList = new ArrayList<>();


    public ImageDBHandler(Context context) {
        mContext = context;
    }


    // UPLOAD
    public void uploadPhotos(List<Photo> selectedPhotoList) {
        // security check
        if(selectedPhotoList.size() == 0)
            return;

        for(Photo photo: selectedPhotoList) {
            uploadPhoto(photo);
        }
    }

    private void uploadPhoto(Photo photo) {
        if(!isPhotoAlreadyInRemoteStorage(photo)) {
            String newId = getNewID(photo);
            updatePhotoId(photo, newId);
            updatePhotoRemoteReference(photo);
        }
        // if already stored, user may want to update photo (title). the key is kept.
    }

    private boolean isPhotoAlreadyInRemoteStorage(Photo photo) {
        DatabaseReference reference = buildDatabaseReference(photo.getBemPublico(), photo.getContrato(), photo.getMedicao());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference reference = buildDatabaseReference(photo.getBemPublico(), photo.getContrato(), photo.getMedicao());
        return reference.push().getKey();
    }

    private void updatePhotoId(Photo photo, String newId) {
        File file = PhotoLab.get(mContext).getPhotoFile(photo);
        PhotoLab.get(mContext).updatePhotoId(photo, newId);
        file.renameTo(PhotoLab.get(mContext).getPhotoFile(photo));
    }

    private void updatePhotoRemoteReference(final Photo photo) {
        final DatabaseReference reference = buildDatabaseReference(photo.getBemPublico(), photo.getContrato(), photo.getMedicao());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> values = photo.toMap();
                Map<String, Object> photoUpdate = new HashMap<>();
                photoUpdate.put(getPhotoPath(photo), values);
                mDatabaseReference.updateChildren(photoUpdate);
                savePhotoToStorage(reference, photo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ImageHandler", "upload photo cancelled");
            }
        });
    }

    private void savePhotoToStorage(final DatabaseReference photoReference, final Photo photo) {
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


    // DOWNLOAD
    public void searchPhotos(DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            searchPhotos(child.getRef());
                        }

                    } else {
                        photoDownloadList.add(buildStringPathFromReference(dataSnapshot.getRef().getParent()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isInDownloadList(String photoPath) {
        return photoDownloadList.contains(photoPath);
    }

    public void putInDownloadList(String photoPath) {
        photoDownloadList.add(photoPath);
    }

    public boolean removeFromDownloadList(String photoPath) {
        return photoDownloadList.remove(photoPath);
    }

    private void checkIfPhotosExists(DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount() != 0) {
                        ImageDBHandler.this.setPhotosExists(true);
                    } else {
                        ImageDBHandler.this.setPhotosExists(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setPhotosExists(boolean returnedValue) {
        photosExists = returnedValue;
    }

    public boolean photosExists() {
        return photosExists;
    }

    private void searchContextHierarchieBeneath(DatabaseReference reference) {
        // IMMEDIATE SONS OF REFERENCE ARGUMENT (E.G.: PHOTO)
    }

    private void addContextHierarchieInList(DatabaseReference returnedValue) {
        contextHierarchieBeneathReferenceList.add(returnedValue);
    }

    public List contextHierarchieExists() {
        return contextHierarchieBeneathReferenceList;
    }

    public void downloadPhotos() {
        for(String photoPath : photoDownloadList) {
            downloadPhotos(buildDatabaseReference(photoPath));
        }
    }

    private void downloadPhotos(DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photoId = dataSnapshot.getRef().getKey();
                PhotoLab photoLab = PhotoLab.get(mContext);
                if (photoLab.getPhoto(photoId) == null) {
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


    // HELPERS
    public DatabaseReference buildDatabaseReference(String bp, String ct, String md) {
        if(empty(bp))
            return null;
        DatabaseReference reference = mDatabaseReference.child(bp);

        if(empty(ct))
            return reference;
        reference = reference.child(ct);

        if(empty(md))
            return reference;
        return reference.child(md);
    }

    private DatabaseReference buildDatabaseReference(String photoPath) {
        return null;
    }

    private String buildStringPathFromReference(DatabaseReference reference) {
        return null;
    }

    private String getPhotoPath(Photo photo) {
        String path;
        if(empty(photo.getBemPublico()))
            return photo.getId();

        if(empty(photo.getContrato())) {
            path = photo.getBemPublico() + "/" + photo.getId();
            return path;
        } else
            path = photo.getBemPublico() + "/" + photo.getContrato();

        if(empty(photo.getMedicao())) {
            path = path + "/" + photo.getId();
            return path;
        } else
            path = path + "/" + photo.getMedicao() + "/" + photo.getId();

        return path;
    }

    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

}
