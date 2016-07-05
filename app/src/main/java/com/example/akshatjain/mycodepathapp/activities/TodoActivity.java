package com.example.akshatjain.mycodepathapp.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshatjain.mycodepathapp.R;
import com.example.akshatjain.mycodepathapp.adapter.TodoAdapter;
import com.example.akshatjain.mycodepathapp.db.Todo;
import com.example.akshatjain.mycodepathapp.db.TodoSql;
import com.example.akshatjain.mycodepathapp.fragments.AddFragment;
import com.example.akshatjain.mycodepathapp.fragments.EditItemFragment;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class TodoActivity extends AppCompatActivity implements EditItemFragment.OnFragmentInteractionListener, AddFragment.AddListener {

        public static interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    private RecyclerView lstView;
    private TodoAdapter todoAdapter;
    private List<Todo> items;
    private FloatingActionButton fab;

    private TodoSql mSql;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lstView = (RecyclerView) findViewById(R.id.listView);
        items = new ArrayList<>();
        mSql = new TodoSql(this);
        db = mSql.getWritableDatabase();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        lstView.setLayoutManager(new LinearLayoutManager(this));
        lstView.setItemAnimator(new DefaultItemAnimator());

        setupListAdd();

        setupListOperations();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(getString(R.string.app_name));
        readFromDb();
    }

    private int readFromDb() {
        List<Todo> list = mSql.get(db);

        items.clear();
        items.addAll(list);
        if(todoAdapter == null)
            todoAdapter = new TodoAdapter(TodoActivity.this,list,R.layout.list_item);
        lstView.setAdapter(todoAdapter);
        todoAdapter.setItems(items);
        todoAdapter.notifyDataSetChanged();

        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("addFragment");
        if(fragment != null){
            fm.popBackStack();
            restoreState();
        }else{
            super.onBackPressed();
        }
    }

    private void setupListAdd() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragment addFragment = AddFragment.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentLayout,addFragment,"addFragment");
                ft.addToBackStack(null);
                ft.commit();
                setViewState(View.GONE);

            }
        });

    }

    private void setViewState(int gone) {
        lstView.setVisibility(gone);
        fab.setVisibility(gone);
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

        lstView.addOnItemTouchListener(new TodoTouchListener(this,
                lstView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Log.d("Todo","Item to be edited : " + position);
                Todo todo = items.get(position);
                showEditDialog(todo.name,position);
            }

            @Override
            public void onLongClick(View view, int position) {
                Todo todo = items.get(position);
                Log.i("Todo","Removing : " + todo.name);
                items.remove(position);
                mSql.delete(db,todo.name);
                todoAdapter.setItems(items);
                todoAdapter.notifyDataSetChanged();
            }
        }));
    }

    private void showEditDialog(String name, int position) {
        FragmentManager fm = getSupportFragmentManager();
        Todo todo = mSql.get(db,name);
        EditItemFragment editItemFragment =  EditItemFragment.newInstance(todo,position);
//        editItemFragment.show(fm, "fragment_edit_item");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentLayout,editItemFragment,"editFragment");
        ft.addToBackStack(null);
        ft.commit();
        setViewState(View.GONE);
    }

    @Override
    public void onCancel() {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = (EditItemFragment) fm.findFragmentByTag("editFragment");
        if(editItemFragment != null){
            fm.popBackStack();
//            editItemFragment.dismiss();
        }
        restoreState();

    }

    private void restoreState() {
        setViewState(View.VISIBLE);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onUpdate(Todo todo, int mPosition) {
        Log.i("Todo","Updating item : " + todo.name + " , desc = " + todo.description);
        List<Todo> tempItems = new ArrayList<>(items);
        tempItems.remove(mPosition);
        boolean found = isFound(tempItems,todo.name);
        if(found){
            Toast.makeText(TodoActivity.this,getString(R.string.itemExists),Toast.LENGTH_LONG).show();
            return;
        }
        Long id = mSql.update(db,todo);
        //replace the item from the list & update the dataset
        items.remove(mPosition);
        items.add(mPosition,todo);
        todoAdapter.setItems(items);
        todoAdapter.notifyDataSetChanged();
        Log.i("Todo","Inserted item : " + items.toString());
        restoreState();
    }

    @Override
    public void onAdd(Todo newItem) {

        items.add(newItem);
        Long id = mSql.insert(db,newItem);
        Log.i("Todo","Inserted item : " + id);
        todoAdapter.setItems(items);
        todoAdapter.notifyDataSetChanged();
        restoreState();

    }



    // Touch listeners for long and single press
    class TodoTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public TodoTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public TodoSql getSql() {
        return mSql;
    }


}
