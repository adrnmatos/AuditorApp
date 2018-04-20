package br.gov.am.tce.auditor.domain;


import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by adrnm on 24/10/2017.
 */

public class Photo implements Parcelable {
    private static final String TAG = "Photo";
    private static final String PHOTOS = "photos";

    private String mId;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;
    private long mTime;
    private String mBemPublico;
    private String mContrato;
    private String mMedicao;

    private static final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private static final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    public Photo(String id) {
        mId = id;
        mTitle = "";
        mBemPublico = "";
        mContrato = "";
        mMedicao = "";
    }

    public Photo() {
        this(UUID.randomUUID().toString());
    }

    public Photo(String id, String title, double latitude, double longitude, long time, String bemPublico, String contrato, String medicao) {
        mId = id;
        mTitle = title;
        mLatitude = latitude;
        mLongitude = longitude;
        mTime = time;
        mBemPublico = bemPublico;
        mContrato = contrato;
        mMedicao = medicao;
    }

    public Photo(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mTime = in.readLong();
        mBemPublico = in.readString();
        mContrato = in.readString();
        mMedicao = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeLong(mTime);
        parcel.writeString(mBemPublico);
        parcel.writeString(mContrato);
        parcel.writeString(mMedicao);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String description) {
        mTitle = description;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getBemPublico() {
        return mBemPublico;
    }

    public void setBemPublico(String bemPublico) {
        this.mBemPublico = bemPublico;
    }

    public String getContrato() {
        return mContrato;
    }

    public void setContrato(String contrato) {
        this.mContrato = contrato;
    }

    public String getMedicao() {
        return mMedicao;
    }

    public void setMedicao(String medicao) {
        this.mMedicao = medicao;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public void registerItselfToDBServer(final Context context) {
        final DatabaseReference photoDBReference = getPhotoReference();
        photoDBReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    photoDBReference.setValue(Photo.this);
                    Log.d(TAG, "getTime returned: " + Photo.this.mTime);
                    uploadItselfToStorageServer(context, photoDBReference);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed on writing to database: " + databaseError.toException());
            }
        });

    }

    private void uploadItselfToStorageServer(Context context, final DatabaseReference photoDBReference) {
        File mPhotoFile = PhotoLab.get(context).getPhotoFile(Photo.this);
        Uri uri = FileProvider.getUriForFile(context,"br.gov.am.tce.auditor.fileProvider", mPhotoFile);
        StorageReference photoStorageReference = mStorageReference.child(Photo.this.mId);
        photoStorageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "Successfully uploaded photo: " + Photo.this.mId);
                } else {
                    photoDBReference.removeValue();
                    Log.e(TAG, "Failed on uploading file" + task.getException());
                }
            }
        });
    }

    private DatabaseReference getPhotoReference() {
        DatabaseReference photoDBRef = mDatabaseReference.child(PHOTOS);

        if (!empty(mBemPublico)) {
            DatabaseReference photoDBRef1 = photoDBRef.child(Photo.this.getBemPublico());
            if (!empty(mContrato)) {
                DatabaseReference photoDBRef2 = photoDBRef1.child(Photo.this.getContrato());
                if (!empty(mMedicao)) {
                    return photoDBRef2.child(Photo.this.getMedicao());
                } else {
                    return photoDBRef2;
                }
            } else {
                return photoDBRef1;
            }
        } else {
            return photoDBRef;
        }
    }

    private static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

}
