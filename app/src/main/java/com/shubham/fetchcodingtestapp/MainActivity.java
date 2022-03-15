package com.shubham.fetchcodingtestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shubham.fetchcodingtestapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    ItemAdapter itemAdapter;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    //Using Binding instead of findView
    ActivityMainBinding binding;
    LinkedList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initialize the itemList, ItemAdapter [Custom ArrayAdapter],
        initializeItemList();
        binding.fetchButton.setOnClickListener(view -> new fetchData().start());
        System.out.println("on create complete");
    }

    private void initializeItemList(){
        itemList = new LinkedList<>();
        itemAdapter = new ItemAdapter(getApplicationContext(),0,itemList);
        binding.itemsList.setAdapter(itemAdapter);
    }

    //To group/filter the items into different lists based on their ListID: Whichever is provided
    private void filterList(int listID){

        ArrayList<Item> groupedItems = new ArrayList<>();

        //Loops through the entire itemList and adds selected ones into groupedItems List
        for(Item i: itemList){
           if(i.listId == listID){
               groupedItems.add(i);
           }
        }

        System.out.println(groupedItems.size()+"Grouped length");

        //Construct adapter with the list and set the listView to the adapter
        ItemAdapter adapter = new ItemAdapter(getApplicationContext(),0,groupedItems);
        binding.itemsList.setAdapter(adapter);

    }

    //OnClick/OnTap for all the lists
    public void list1Tapped(View view) {
        filterList(1);
    }

    public void list2Tapped(View view) {
        filterList(2);
    }

    public void list3Tapped(View view) {
        filterList(3);
    }

    public void list4Tapped(View view) {
        filterList(4);
    }

    //This is the class which initializes the URL Connection, Shows the progressDialog,
    //Gets the JSON data and parses it into JSON objects and then finally feeds that into the list(itemList)
    class fetchData extends Thread{

        //This is the string in which all the json data will be fed iteratively/line by line
        String data = "";

        @Override
        public void run(){

            //show progressDialog
            mainHandler.post(() -> {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving Data");
                progressDialog.setCancelable(false);
                progressDialog.show();
                System.out.println("Showing progress dialog");
            });

            try {
                //URL Connection Initialized
                URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Getting input stream from URL Connection
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                //String to be appended to the data string
                String line;

                System.out.println("try block running");

                int lineP=0;

                //Read the data until the last line
                while ((line=reader.readLine())!=null){
                    data = data + line;
                    System.out.println("Line "+lineP);
                    lineP = lineP+1;
                }

                //read the data
                if(!data.isEmpty()){

                    //Parse JSON Array/objects from the data
                    JSONArray items = new JSONArray(data);

                    //total number of "items" in the JSON file
                    System.out.println("items "+items.length());

                    //Clear list to not have repetition in case of repeated clicks
                    itemList.clear();

                    int lists = 0;

                    //Parse ListID, ID and Name from every item and add it to the itemList
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        int listId = Integer.parseInt(item.getString("listId"));
                        int id = Integer.parseInt(item.getString("id"));
                        String name = item.getString("name");

                        //discard null or empty values
                        if(name.isEmpty()||name.equals("null"))
                            continue;

                        itemList.add(new Item(listId,id,name));

                        if(listId>lists){
                            lists = listId;
                        }
                    }

                    System.out.println("Max List ids: "+ lists);

                    //Sort the itemList using Collections
                    Collections.sort(itemList);

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            //Close the progress dialog and notify the adapter of the data change
            mainHandler.post(() -> {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    itemAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}