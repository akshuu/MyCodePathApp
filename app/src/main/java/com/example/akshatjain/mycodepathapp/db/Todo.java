package com.example.akshatjain.mycodepathapp.db;

import nl.qbusict.cupboard.annotation.Column;

/**
 * Created by akshatjain on 6/22/16.
 */
public class Todo {

    public Long _id; // for cupboard

    @Column("name")
    public String name;
    @Column("desc")
    public String description;

    public Todo() {
        name = "";
        description= "";
    }

    public Todo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Todo(Long id,String name, String description) {
        this._id = id;
        this.name = name;
        this.description = description;
    }
}
