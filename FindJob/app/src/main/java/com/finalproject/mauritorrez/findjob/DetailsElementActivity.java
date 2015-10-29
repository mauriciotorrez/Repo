package com.finalproject.mauritorrez.findjob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.finalproject.mauritorrez.findjob.Data.JobPostDTO;

import java.util.ArrayList;

/**
 * Created by mauri on 10/24/2015.
 */
public class DetailsElementActivity  extends AppCompatActivity {

    private static final String LOG_TAG = DetailsElementActivity.class.getSimpleName();

    public static final String RESULT = "RESULT";
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_element);
        ArrayList<String> arrayList = new ArrayList<String>();


        TextView titleTextView = (TextView)findViewById(R.id.title_text_view_detail);
        TextView descriptionTextView = (TextView)findViewById(R.id.description_text_view_detail);
        ListView contactsListView = (ListView)findViewById(R.id.contacts_list_view);
        JobPostDTO result = (JobPostDTO)getIntent().getExtras().getSerializable(RESULT);

        Log.d(LOG_TAG, "getContacts Number: " + result.getContacts().size());
        titleTextView.setText(result.getTitle());
        descriptionTextView.setText(result.getDescription());
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.detail_element_text,
                R.id.content_text_view, result.getContacts());
        contactsListView.setAdapter(arrayAdapter);
    }

}



