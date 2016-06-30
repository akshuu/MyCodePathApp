package com.example.akshatjain.mycodepathapp.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akshatjain.mycodepathapp.R;
import com.example.akshatjain.mycodepathapp.db.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Recycler view
 * Created by akshatjain on 6/22/16.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    List<Todo> mItems;
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
        notifyItemRangeChanged(0,items.size());
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.mItemResource, parent, false);
        return new TodoHolder(mContext,view);
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


    /**
     * Holder class for the views
     */
    class TodoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Todo mTodo;
        private TextView name;
        private TextView desc;
        private TextView priority;
        private Context mContext;
        public TodoHolder(AppCompatActivity context, View itemView) {
            super(itemView);
            mContext = context;
            name = (TextView) itemView.findViewById(R.id.textItemName);
            desc = (TextView) itemView.findViewById(R.id.textItemDesc);
            priority = (TextView) itemView.findViewById(R.id.txtPriority);
            itemView.setOnClickListener(this);

        }

        public void bindTodos(Todo todo){
            this.mTodo = todo;
            name.setText(todo.name);
            desc.setText(todo.description);
            priority.setText(todo.getPriority());
            switch (todo.priority){
                case 0:
                    priority.setTextColor(mContext.getResources().getColor(R.color.blue));
                    break;
                case 1:
                    priority.setTextColor(mContext.getResources().getColor(R.color.green));
                    break;
                case 2:
                    priority.setTextColor(mContext.getResources().getColor(R.color.red));
                    break;

            }
        }

        @Override
        public void onClick(View v) {
        }

    }
}
