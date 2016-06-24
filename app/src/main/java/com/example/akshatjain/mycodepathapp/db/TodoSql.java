package com.example.akshatjain.mycodepathapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by akshatjain on 6/22/16.
 */
public class TodoSql extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public TodoSql(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register our models
        cupboard().register(Todo.class);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public Long insert(SQLiteDatabase db, Todo todo){
       Long id = cupboard().withDatabase(db).put(todo);
        return id;
    }

    public Long update(SQLiteDatabase db, Todo todo){
        Long id = cupboard().withDatabase(db).put(todo);
        return id;
    }

    public boolean delete(SQLiteDatabase db, String name){
        int id = cupboard().withDatabase(db).delete(Todo.class, "name = ?", name);
        return (id == 0 ? false : true);
    }

    public List<Todo> get(SQLiteDatabase db){
        List<Todo> listTodos = new ArrayList<>();
        Cursor cTodos = cupboard().withDatabase(db).query(Todo.class).getCursor();
        try {
            // Iterate ToDos
            QueryResultIterable<Todo> itr = cupboard().withCursor(cTodos).iterate(Todo.class);
            for (Todo todo : itr) {
                Log.d("Todo","Items == " + todo._id + " , name = " + todo.name + " , desc = " + todo.description);
            }
            listTodos = itr.list();
        } finally {
            // close the cursor
            cTodos.close();
        }
        return listTodos;
    }


    public Todo get(SQLiteDatabase db,String name){
// Get the first matching todo with name
        Todo todo = cupboard().withDatabase(db).query(Todo.class).withSelection( "name = ?", name).get();
        return todo;
    }

}
