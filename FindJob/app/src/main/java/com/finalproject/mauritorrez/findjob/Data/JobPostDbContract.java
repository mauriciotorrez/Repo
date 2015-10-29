package com.finalproject.mauritorrez.findjob.Data;

/**
 * Created by mauri on 10/24/2015.
 */

import android.provider.BaseColumns;

public class JobPostDbContract {
    public static class JobPost implements BaseColumns {
        public static final String TABLE_NAME = "job_posts";

        // ID ya no, ya que esta definido en la interfaz BaseColumns
        // Constantes que representan los atributos de la tabla
        public static final String TITLE_COLUMN = "title";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String POSTED_DATE_COLUMN = "posted_date";
    }

    public static class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contacts";

        public static final String NUMBER_COLUMN = "number";
        public static final String JOB_POST_ID_COLUMN = "job_post_id";
    }
}