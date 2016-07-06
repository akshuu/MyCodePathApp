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
    @Column("priority")
    public int priority = 0;
    @Column("duedate")
    public String dueDate;


    public Todo() {
        name = "";
        description= "";
    }

    public Todo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Todo(Long id,String name, String description,int priority) {
        this._id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public Todo(String name, String desc, int priority) {
        this.name = name;
        this.description = desc;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority =" + getPriority() +
                '}';
    }

    public String getPriority() {
        switch (priority){
            case 0:
                return "LOW";
            case 1:
                return "MEDIUM";
            case 2:
                return "HIGH";
            default:
                return "LOW";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        return name.equalsIgnoreCase(todo.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + priority;
        return result;
    }
}
