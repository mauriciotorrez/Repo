package com.finalproject.mauritorrez.findjob;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.finalproject.mauritorrez.findjob.Data.JobPostDTO;
import com.finalproject.mauritorrez.findjob.Data.JobPostDbContract;
import com.finalproject.mauritorrez.findjob.Data.JobPostDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.finalproject.mauritorrez.findjob.Data.JobPostDbContract.*;

public class ListJobsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = ListJobsActivity.class.getSimpleName();

    private static final int JOB_POST_LOADER_ID = 1;
    private JobPostAdapter jobPostAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs);

        jobPostAdapter = new JobPostAdapter(this, null, 0);
        listView = (ListView)findViewById(R.id.works_list_view);
        listView.setAdapter(jobPostAdapter);
        getSupportLoaderManager().initLoader(JOB_POST_LOADER_ID, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TableRow tableRow = (TableRow)parent.getItemAtPosition(position);
                long idPoss = parent.getItemIdAtPosition(position);
                JobPostDTO jobPost = populateJobPostDTO(idPoss);
                Log.d(LOG_TAG, "listview id: " + idPoss);
                Log.d(LOG_TAG, "title: " + jobPost.getTitle());
                Log.d(LOG_TAG, "description: " + jobPost.getDescription());
                Log.d(LOG_TAG, "listview id: " + idPoss);

                Intent intent = new Intent(getBaseContext(), DetailsElementActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putSerializable(DetailsElementActivity.RESULT, jobPost);
                intent.putExtra(DetailsElementActivity.RESULT, jobPost);
                startActivity(intent);
            }
        });

        syncData();
    }

    public JobPostDTO populateJobPostDTO(long id)
    {
        JobPostDTO jobPost = new JobPostDTO();
        String id1 = String.valueOf(id);
        if (id > 0)
        {
            JobPostDbHelper dbHelper = new JobPostDbHelper(ListJobsActivity.this);
            Cursor cursor =  dbHelper.getReadableDatabase().rawQuery("SELECT * FROM job_posts WHERE _id=?", new String[] {id1});
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    do {
                        jobPost.setTitle(cursor.getString(cursor.getColumnIndex(JobPost.TITLE_COLUMN)));
                        jobPost.setDescription(cursor.getString(cursor.getColumnIndex(JobPost.DESCRIPTION_COLUMN)));
                    }while (cursor.moveToNext());
                }
            }
            else
            {
                jobPost.setTitle("");
                jobPost.setDescription("");
            }
            int idx = 0;
            cursor =  dbHelper.getReadableDatabase().rawQuery("SELECT * FROM contacts WHERE job_post_id=?", new String[] {id1});
            if (cursor != null)
            {
                ArrayList<String> tempArray = new ArrayList<>();
                if (cursor.moveToFirst())
                {


                    do {
                        tempArray.add(cursor.getString(cursor.getColumnIndex(Contact.NUMBER_COLUMN)));
                    }while (cursor.moveToNext());
                }
                jobPost.setContacts(tempArray);
            }
            else
            {
                ArrayList<String> tempArray = new ArrayList<>();
                jobPost.setContacts(tempArray);
            }
        }
        return jobPost;
    }


    public void syncData() {
        GetDataAsyncTask asyncTask = new GetDataAsyncTask();

        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_syncronize:
                // Here we might call LocationManager.requestLocationUpdates()
                syncData();
                Log.d(LOG_TAG, "menu_syncronize click: ");
                return true;

            case R.id.menu_post_job:
                // Here we would open up our settings activity
                Log.d(LOG_TAG, "menu_post_job click: ");
                Intent intent = new Intent(getBaseContext(), JobPostFormActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new JobPostLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        jobPostAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        jobPostAdapter.swapCursor(null);
    }



    private class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // The URL To connect:
            // http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon()
                    .appendPath("work_posts.json").build();
            try {
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                String clientInfoJSON = buffer.toString();
                Log.d(LOG_TAG, "JSON: " + clientInfoJSON);
                JSONArray jsonArray = new JSONArray(clientInfoJSON);
                int length = jsonArray.length();
                JobPostDbHelper dbHelper = new JobPostDbHelper(ListJobsActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                for (int i = 0; i < length; i++) {
                    /*
                     {"id":1,"title":"Something","description":"Something",
                          "posted_date":"03/10/2015","contacts":["1-391-882-2074","846-971-4852 x9658"]}
                    * */
                    JSONObject element = jsonArray.getJSONObject(i);
                    int id = element.getInt("id");
                    String title = element.getString("title");
                    String date = element.getString("posted_date");
                    String description = element.getString("description");

                    // Para llenar la base de datos, usamos la clase ContentValue
                    // Un objeto de esta clase tiene la referencia a los atributos
                    // Que vamos a insertar en la base de datos
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(JobPost._ID, id);
                    contentValues.put(JobPost.TITLE_COLUMN, title);
                    contentValues.put(JobPost.POSTED_DATE_COLUMN, date);
                    contentValues.put(JobPost.DESCRIPTION_COLUMN, description);

                    db.insert(JobPost.TABLE_NAME, null, contentValues);

                    JSONArray contacts = element.getJSONArray("contacts");

                    for (int j = 0; j < contacts.length(); j++) {
                        String number = contacts.getString(j);

                        ContentValues contactContentValues = new ContentValues();

                        contactContentValues.put(Contact.NUMBER_COLUMN, number);
                        contactContentValues.put(Contact.JOB_POST_ID_COLUMN, id);

                        db.insert(Contact.TABLE_NAME, null, contactContentValues);
                    }
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getSupportLoaderManager().getLoader(JOB_POST_LOADER_ID).onContentChanged();
        }
    }

}
