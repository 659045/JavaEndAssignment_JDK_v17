package com.example.javaendassignment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    public ArrayList<User> getUsers(){ return users; }
    public ArrayList<Item> getItems(){ return items; }

    public Item getItemByID(int id){
        for (Item i: items) {
            if (Objects.equals(i.getId(), id)){
                return i;
            }
        }
        return null;
    }

    public User getUserByID(int id){
        for (User u: users) {
            if (Objects.equals(u.getId(), id)){
                return u;
            }
        }
        return null;
    }

    public Database() {
        users.add(new User(1, "J", "Jason", "Xie", "1", LocalDate.of(2000, 1, 1)));
        users.add(new User(2, "David", "David", "Hoff", "1234", LocalDate.of(2000, 1, 1)));
        items.add(new Item(1, true, "Java Book", "Jack the Great"));
        items.add(new Item(2, true, "CSS Book", "Zac Wills"));
    }

    public int generateNextID(String object){
        int highestValue = 0;
        if (Objects.equals(object, "item")){
            for (Item i: items) {
                if (i.getId() > highestValue){
                    highestValue = i.getId();
                }
            }
        }
        else {
            for (User u : users) {
                if (u.getId() > highestValue){
                    highestValue = u.getId();
                }
            }
        }

        return highestValue + 1;
    }
}
