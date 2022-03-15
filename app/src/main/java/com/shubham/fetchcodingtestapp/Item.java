package com.shubham.fetchcodingtestapp;

public class Item implements Comparable<Item> {

    //Item class to store Items with their values
    int listId;
    int id;
    String name;

    Item(int listID, int id, String name){
        this.listId = listID;
        this.id = id;
        this.name = name;
    }

    //CompareTo method for sorting of the Items based on their ID/Name
    @Override
    public int compareTo(Item i) {
        {
            if (id > i.id) {
                return 1;
            }
            else if (id == i.id) {
                return 0;
            }
            else {
                return -1;
            }
        }

    }
}
