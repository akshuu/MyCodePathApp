package com.example.akshatjain.mycodepathapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatjain.mycodepathapp.db.Todo;
import com.example.akshatjain.mycodepathapp.db.TodoSql;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements EditItemFragment.OnFragmentInteractionListener {

    ListView lstView;
    TodoAdapter todoAdapter;
    List<Todo> items;
    Button btnAdd;
    TextView txtNewItem;
    TodoSql mSql;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lstView = (ListView) findViewById(R.id.listView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        txtNewItem = (TextView) findViewById(R.id.txtNew);
        items = new ArrayList<>();
        mSql = new TodoSql(this);
        db = mSql.getWritableDatabase();

        setupListAdd();

        setupListOperations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFromDb();
    }

    private int readFromDb() {
        List<Todo> list = mSql.get(db);

        items.clear();
        items.addAll(list);
        if(todoAdapter == null)
            todoAdapter = new TodoAdapter(TodoActivity.this,list);
        lstView.setAdapter(todoAdapter);
        todoAdapter.setItems(items);
        todoAdapter.notifyDataSetChanged();

        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupListAdd() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtNewItem.getText())){
                    Toast.makeText(TodoActivity.this,getString(R.string.blankItemName),Toast.LENGTH_LONG).show();
                    txtNewItem.requestFocus();
                }else{
                    String name = txtNewItem.getText().toString();
                    boolean found = isFound(items,name);
                    if(found){
                        Toast.makeText(TodoActivity.this,getString(R.string.itemExists),Toast.LENGTH_LONG).show();
                        txtNewItem.requestFocus();
                        return;
                    }
                    Todo todo = new Todo(name,"");
                    items.add(todo);
                    Long id = mSql.insert(db,todo);
                    Log.i("Todo","Inserted item : " + id);
                    todoAdapter.setItems(items);
                    todoAdapter.notifyDataSetChanged();
                    txtNewItem.setText("");
                }
            }
        });
    }

    private boolean isFound(List<Todo> items,String name) {
        boolean found = false;

        for(Todo t: items){
            if(t.name.equalsIgnoreCase(name))
                found = true;
        }
        return found;
    }

    // Handles the removal & edit listeners to the list view
    private void setupListOperations() {
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = items.get(position);
                Log.i("Todo","Removing : " + todo.name);
                items.remove(position);
                mSql.delete(db,todo.name);
                todoAdapter.setItems(items);
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Todo","Item to be edited : " + position);
                Todo todo = items.get(position);
                showEditDialog(todo.name,position);
            }
        });
    }

    private void showEditDialog(String name, int position) {
        FragmentManager fm = getSupportFragmentManager();
        Todo todo = mSql.get(db,name);
        EditItemFragment editItemFragment =  EditItemFragment.newInstance(todo,position);
        editItemFragment.show(fm, "fragment_edit_item");
    }

    @Override
    public void onCancel() {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = (EditItemFragment) fm.findFragmentByTag("fragment_edit_item");
        if(editItemFragment != null){
            editItemFragment.dismiss();
        }
    }

    @Override
    public void onUpdate(Todo todo, int mPosition) {
        Log.i("Todo","Updating item : " + todo.name + " , desc = " + todo.description);
        List<Todo> tempItems = new ArrayList<>(items);
        tempItems.remove(mPosition);
        boolean found = isFound(tempItems,todo.name);
        if(found){
            Toast.makeText(TodoActivity.this,getString(R.string.itemExists),Toast.LENGTH_LONG).show();
            txtNewItem.requestFocus();
            return;
        }
        Long id = mSql.update(db,todo);
        //replace the item from the list & update the dataset
        items.remove(mPosition);
        items.add(mPosition,todo);
        todoAdapter.setItems(items);
        todoAdapter.notifyDataSetChanged();
        Log.i("Todo","Inserted item : " + items.toString());

    }
}
