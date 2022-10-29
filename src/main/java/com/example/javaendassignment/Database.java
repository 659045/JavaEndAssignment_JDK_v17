package com.example.javaendassignment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {

    private ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();

    private final ArrayList<Object> objects = new ArrayList<>();

    public ArrayList<User> getUsers(){ return users; }
    public ArrayList<Item> getItems(){ return items; }

    public ArrayList<Item> updateItems(Item item){
        items.add(item);
        objects.add(item);
        return items;
    }

    public ArrayList<User> updateUsers(User user){
        users.add(user);
        objects.add(user);
        return users;
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
//        users.add(new User(1, "J", "Jason", "Xie", "1", LocalDate.of(2000, 1, 1)));
//        users.add(new User(2, "David", "David", "Hoff", "1234", LocalDate.of(2000, 1, 1)));
//        items.add(new Item(1, true, "Java Book", "Jack the Great"));
//        items.add(new Item(2, true, "CSS Book", "Zac Wills"));

        for (Item i: items) {
            objects.add(i);
        }

        for (User u: users) {
            objects.add(u);
        }
    }

    public void update(){

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

    public void writeToFile(){
        try (FileOutputStream fos = new FileOutputStream("objects.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Object item: objects) {
                oos.writeObject(item);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void clearFile(){
        try {
            OutputStream os;
            os = Files.newOutputStream(Path.of("objects.dat"));
            os.close();
        } catch (IOException io) {
            System.out.println("could not clear file");
        }
    }

    public void readFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("objects.dat"))) {
            while (true) {
                try {
                    Object item = ois.readObject();
                    if (item.getClass() == Item.class){
                        items.add((Item)item);
                    }
                    else{
                        users.add((User)item);
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
