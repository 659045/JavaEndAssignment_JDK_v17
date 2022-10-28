package com.example.javaendassignment;

import java.time.LocalDate;

public class User {
    private int id;
    private String username;

    private String firstName;
    private String lastName;

    private String password;
    private LocalDate birthday;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public User(int id, String username, String firstName, String lastName, String password, LocalDate birthday) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthday = birthday;
    }
}
