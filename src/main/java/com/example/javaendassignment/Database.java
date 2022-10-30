package com.example.javaendassignment;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Database {

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();

    public ArrayList<User> getUsers(){ return users; }
    public ArrayList<Item> getItems(){ return items; }

    public void LendItem(Item item){
        item.setStatus(false);
        item.setDate(LocalDate.now());
        writeItemsToFile();
    }

    public void ReceiveItem(Item item){
        item.setStatus(true);
        item.setDate(null);
        writeItemsToFile();
    }

    public void AddItem(Item item){
        items.add(item);
        writeItemsToFile();
        //readItemsFromFile();
    }

    public void AddUser(User user){
        users.add(user);
        writeUsersToFile();
        //readUsersFromFile();
    }

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


    public void writeUsersToFile(){
        try (FileOutputStream fos = new FileOutputStream("users.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for(User u : users){
                oos.writeObject(u);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeItemsToFile(){
        try (FileOutputStream fos = new FileOutputStream("items.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for(Item i : items){
                oos.writeObject(i);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readItemsFromFile(){
        items.clear();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("items.dat"))) {
            while (true) {
                try {
                        Item item = (Item) ois.readObject();
                        items.add(item);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readUsersFromFile(){
        users.clear();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("users.dat"))) {
            while (true) {
                try {
                        User user = (User) ois.readObject();
                        users.add(user);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
