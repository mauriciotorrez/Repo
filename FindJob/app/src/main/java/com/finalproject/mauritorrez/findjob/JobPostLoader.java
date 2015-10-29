package com.finalproject.mauritorrez.findjob;

/**
 * Created by mauri on 10/24/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.finalproject.mauritorrez.findjob.Data.JobPostDbContract;
import com.finalproject.mauritorrez.findjob.Data.JobPostDbHelper;

import static com.finalproject.mauritorrez.findjob.Data.JobPostDbContract.*;


public class JobPostLoader extends SimpleCursorLoader {
    private JobPostDbHelper dbHelper;

    public JobPostLoader(Context context) {
        super(context);
        dbHelper = new JobPostDbHelper(context);
    }

    @Override
    public Cursor loadInBackground() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        return database.query(JobPost.TABLE_NAME,
                new String[] {JobPost._ID, JobPost.TITLE_COLUMN, JobPost.POSTED_DATE_COLUMN},
                null, null, null, null, JobPost.POSTED_DATE_COLUMN + " DESC");
    }
}
