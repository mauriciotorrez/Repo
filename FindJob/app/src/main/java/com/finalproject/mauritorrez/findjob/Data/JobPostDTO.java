package com.finalproject.mauritorrez.findjob.Data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mauri on 10/26/2015.
 */
public class JobPostDTO implements Serializable{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    private String title;

    public void setDescription(String description) {
        this.description = description;
    }


    private String description;


    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    private ArrayList<String> contacts;


}
