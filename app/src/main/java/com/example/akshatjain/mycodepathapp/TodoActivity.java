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
    List<String> items;
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
        for(Todo t:list){
            items.add(t.name);
        }

        if(todoAdapter == null)
            todoAdapter = new TodoAdapter(TodoActivity.this,R.layout.list_item,items);
        lstView.setAdapter(todoAdapter);
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
                    Toast.makeText(TodoActivity.this,"Please enter some text",Toast.LENGTH_LONG).show();
                    txtNewItem.requestFocus();
                }else{
                    items.add(txtNewItem.getText().toString());
                    Todo todo = new Todo(txtNewItem.getText().toString(),"");
                    Long id = mSql.insert(db,todo);
                    Log.i("Todo","Inserted item : " + id);
                    todoAdapter.notifyDataSetChanged();
                    txtNewItem.setText("");
                }
            }
        });
    }

    // Handles the removal & edit listeners to the list view
    private void setupListOperations() {
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = items.get(position);
                Log.i("Todo","Removing : " + name);
                items.remove(position);
                mSql.delete(db,name);
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Todo","Item to be edited : " + position);
                showEditDialog(items.get(position),position);
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
        Long id = mSql.update(db,todo);
        items.remove(mPosition);
        items.add(mPosition,todo.name);
        todoAdapter.notifyDataSetChanged();
        Log.i("Todo","Inserted item : " + id);

    }
}
