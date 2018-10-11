package br.gov.am.tce.auditor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.gov.am.tce.auditor.database.PhotoDbSchema.PhotoTable;

/**
 * Created by adrnm on 24/10/2017.
 */

public class PhotoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "photoBase.db";

    public PhotoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PhotoTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
                PhotoTable.Cols.UUID + ", " +
                PhotoTable.Cols.AUTOR + ", " +
                PhotoTable.Cols.TITLE + ", " +
                PhotoTable.Cols.LATITUDE + ", " +
                PhotoTable.Cols.LONGITUDE + ", " +
                PhotoTable.Cols.TIME + ", " +
                PhotoTable.Cols.BEMPUBLICO + ", " +
                PhotoTable.Cols.CONTRATO + ", " +
                PhotoTable.Cols.MEDICAO + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
