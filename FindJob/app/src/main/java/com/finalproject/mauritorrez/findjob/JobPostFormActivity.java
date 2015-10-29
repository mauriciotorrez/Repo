package com.finalproject.mauritorrez.findjob;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.finalproject.mauritorrez.findjob.Data.JobPostDTO;
import com.finalproject.mauritorrez.findjob.Data.JobPostDbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * Created by mauri on 10/27/2015.
 */
public class JobPostFormActivity extends AppCompatActivity {

    private static final String LOG_TAG = JobPostFormActivity.class.getSimpleName();

    private EditText Title;
    private EditText Description;
    private EditText Contact;
    private String CurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_post_form);

        Title = (EditText)findViewById(R.id.title_EditText);
        Description = (EditText)findViewById(R.id.description_EditText);
        Contact = (EditText)findViewById(R.id.contact_EditText);
        Calendar calendar= Calendar.getInstance();
        CurrentDate = calendar.getTime().toString();
    }

    public void onClick_postJob(View view) {
        DoPostcData();
    }


    public void DoPostcData() {
        PostDataAsyncTask asyncTask = new PostDataAsyncTask();

        JobPostDTO tempJobPost = new JobPostDTO();
        tempJobPost.setTitle(Title.getText().toString());
        tempJobPost.setDescription(Description.getText().toString());
        ArrayList<String> ar = new ArrayList<String>();
        ar.add(Contact.getText().toString());
        tempJobPost.setContacts(ar);

        asyncTask.execute(tempJobPost);

        AlertDialog alertDialog = new AlertDialog.Builder(JobPostFormActivity.this).create();
        alertDialog.setTitle("Encontrar Trabajo");
        alertDialog.setMessage("The job has posted successfully");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getBaseContext(), ListJobsActivity.class);

                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }


    private class PostDataAsyncTask extends AsyncTask<JobPostDTO, Void, Void> {

        @Override
        protected Void doInBackground(JobPostDTO... jobPosts) {

            // The URL To connect:
            // http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon()
                    .appendPath("work_posts.json").build();
            try {

                StringBuilder sb = new StringBuilder();

                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());


                JSONObject jsonParams = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title",jobPosts[0].getTitle());
                jsonObject.put("description",jobPosts[0].getDescription());
                jsonObject.put("contacts",new JSONArray().put((jobPosts[0].getContacts()).get(0)));

                jsonParams.put("work_post", jsonObject);


                wr.writeBytes(jsonParams.toString());
                wr.flush();
                wr.close();


                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult==HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"atf-8" ));
                    String line = null;
                    while((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();

                    Log.d(LOG_TAG, "sb result : " + sb.toString());
                }
                else
                {
                    Log.d(LOG_TAG, "url connection getresponse messaje : " + urlConnection.getResponseMessage());

                }



            } catch (IOException e) {


            }
            catch (JSONException es) {


            }
            finally {
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
            //getSupportLoaderManager().getLoader(JOB_POST_LOADER_ID).onContentChanged();
        }
    }


}
