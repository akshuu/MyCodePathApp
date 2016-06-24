package com.example.akshatjain.mycodepathapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.akshatjain.mycodepathapp.db.Todo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshatjain on 6/22/16.
 */
public class TodoAdapter extends BaseAdapter {

    private List<Todo> mItems;
    private Context mContext;

    public TodoAdapter(Context context, List<Todo> items) {
        mItems = new ArrayList<>(items);
        mContext = context;
    }

    public void setItems(List<Todo> items){
        mItems.clear();
        mItems.addAll(items);
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtName ;
        TextView txtDesc ;

        Todo item = (Todo) getItem(position);
        TodoHolder holder;
        if(convertView == null){
            holder = new TodoHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.textItem);
            holder.desc = (TextView) convertView.findViewById(R.id.textItemDesc);
            convertView.setTag(holder);
        }else{
            holder = (TodoHolder) convertView.getTag();
        }
        holder.name.setText(item.name);
        holder.desc.setText(item.description);
        return convertView;
    }

    public static class TodoHolder{
        TextView name;
        TextView desc;
    }
}
