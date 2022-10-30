package com.example.javaendassignment;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Item implements Serializable {

    private final int id;
    private boolean status;
    private final String title;
    private final String author;
    private LocalDate date;

    public int getId() {
        return id;
    }

    public String getStatus() {
        if (Objects.equals(status, true)){
        return "Yes";
        }
        else{
            return "No";
        }
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Item(int id, boolean status, String title, String author) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.author = author;
    }
}

