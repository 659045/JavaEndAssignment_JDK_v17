package com.example.javaendassignment;

import java.time.LocalDate;

public class Item {
    private int id;
    private boolean status;
    private String title;
    private String author;
    private LocalDate date;

    public int getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
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

