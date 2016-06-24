package com.example.akshatjain.mycodepathapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by akshatjain on 6/22/16.
 */
public class TodoAdapter extends ArrayAdapter<String> {

    public TodoAdapter(Context context, int list_item, List<String> items) {
        super(context,list_item,items);
    }
}
