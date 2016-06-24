package com.example.akshatjain.mycodepathapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.akshatjain.mycodepathapp.db.Todo;
import com.example.akshatjain.mycodepathapp.view.TodoHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshatjain on 6/22/16.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {

    private List<Todo> mItems;
    private AppCompatActivity mContext;
    private int mItemResource;


    public TodoAdapter(AppCompatActivity context, List<Todo> items, int itemResource) {
        mItems = new ArrayList<>(items);
        mContext = context;
        mItemResource = itemResource;
    }

    public void setItems(List<Todo> items){
        mItems.clear();
        mItems.addAll(items);
    }
//    @Override
//    public int getCount() {
//        return mItems.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mItems.get(position);
//    }
//


    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.mItemResource, parent, false);
        return new TodoHolder(this.mContext, view);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = mItems.get(position);
        holder.bindTodos(todo);

    }

    @Override
    public int getItemCount() {
        return mItems.size();

    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView txtName ;
//        TextView txtDesc ;
//
//        Todo item = (Todo) getItem(position);
//        TodoHolder holder;
//        if(convertView == null){
//            holder = new TodoHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
//            holder.name = (TextView) convertView.findViewById(R.id.textItem);
//            holder.desc = (TextView) convertView.findViewById(R.id.textItemDesc);
//            convertView.setTag(holder);
//        }else{
//            holder = (TodoHolder) convertView.getTag();
//        }
//        holder.name.setText(item.name);
//        holder.desc.setText(item.description);
//        return convertView;
//    }


}
