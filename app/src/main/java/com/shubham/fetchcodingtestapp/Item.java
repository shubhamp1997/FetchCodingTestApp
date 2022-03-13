package com.shubham.fetchcodingtestapp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Item implements Comparable<Item> {

    int listId;
    int id;
    String name;
    String itemDisc;



    Item(int listID, int id, String name){
        this.listId = listID;
        this.id = id;
        this.name = name;
        this.itemDisc = "ListID: "+listId+" Id: "+id+" Name: "+name;
    }


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
