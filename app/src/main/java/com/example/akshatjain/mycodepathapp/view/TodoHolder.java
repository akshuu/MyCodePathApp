package com.example.akshatjain.mycodepathapp.view;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.akshatjain.mycodepathapp.fragments.EditItemFragment;
import com.example.akshatjain.mycodepathapp.R;
import com.example.akshatjain.mycodepathapp.db.Todo;

/**
 * Created by akshatjain on 6/24/16.
 */
public class TodoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Todo mTodo;
    private AppCompatActivity mContext;
    private TextView name;
    private TextView desc;

    public TodoHolder(AppCompatActivity context, View itemView) {
        super(itemView);
        mContext = context;
        name = (TextView) itemView.findViewById(R.id.textItem);
        desc = (TextView) itemView.findViewById(R.id.textItemDesc);
        itemView.setOnClickListener(this);

    }

    public void bindTodos(Todo todo){
        this.mTodo = todo;
        name.setText(todo.name);
        desc.setText(todo.description);
    }

    @Override
    public void onClick(View v) {
        if(this.mTodo != null){
            showEditDialog(name.getText().toString(),-1);
        }
    }
    private void showEditDialog(String name, int position) {
        FragmentManager fm = mContext.getSupportFragmentManager();
        Todo todo = new Todo(name,desc.getText().toString());
        EditItemFragment editItemFragment =  EditItemFragment.newInstance(todo,position);
        editItemFragment.show(fm, "fragment_edit_item");
    }
}
