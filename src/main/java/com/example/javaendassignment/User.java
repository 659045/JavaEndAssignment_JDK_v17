package com.example.javaendassignment;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User implements Serializable {

    private int id;
    private String username;

    private String firstName;
    private String lastName;

    private String password;
    private LocalDate birthday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //implemented with help of https://www.baeldung.com/java-datetimeformatter
    public String getBirthday() {
        return birthday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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
