package com.shubham.fetchcodingtestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

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

    ActivityMainBinding binding;
    ArrayList<String> itemArrayList;
    ArrayList<String> itemArrayList1;
    ArrayList<String> itemArrayList2;
    ArrayList<String> itemArrayList3;
    ArrayList<String> itemArrayList4;
    LinkedList<Item> itemList;
    LinkedList<Item> itemList1;
    LinkedList<Item> itemList2;
    LinkedList<Item> itemList3;
    LinkedList<Item> itemList4;


    ArrayAdapter<String> listAdapter1;
    ArrayAdapter<String> listAdapter2;
    ArrayAdapter<String> listAdapter3;
    ArrayAdapter<String> listAdapter4;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeItemList1();
        initializeItemList2();
        initializeItemList3();
        initializeItemList4();
        binding.fetchButton.setOnClickListener(view -> new fetchData().start());
        System.out.println("on create complete");
    }

    private void initializeItemList1(){
        itemList1 = new LinkedList<>();
        itemArrayList1 = new ArrayList<>();
        listAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) itemArrayList1);
        binding.itemsList.setAdapter(listAdapter1);
        System.out.println("items List 1 Initialized");
    }

    private void initializeItemList2(){
        itemList2 = new LinkedList<>();
        itemArrayList2 = new ArrayList<>();
        listAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) itemArrayList2);
        binding.itemsList.setAdapter(listAdapter2);
        System.out.println("items List 2 Initialized");
    }

    private void initializeItemList3(){
        itemList3 = new LinkedList<>();
        itemArrayList3 = new ArrayList<>();
        listAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) itemArrayList3);
        binding.itemsList.setAdapter(listAdapter3);
        System.out.println("items List 3 Initialized");
    }

    private void initializeItemList4(){
        itemList4 = new LinkedList<>();
        itemArrayList4 = new ArrayList<>();
        listAdapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) itemArrayList4);
        binding.itemsList.setAdapter(listAdapter4);
        System.out.println("items List 4 Initialized");
    }

    class fetchData extends Thread{

        String data = "";

        @Override
        public void run(){

            mainHandler.post(() -> {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving Data");
                progressDialog.setCancelable(false);
                progressDialog.show();
                System.out.println("Showing progress dialog");
            });

            try {
                URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                System.out.println("try block running");
                int lineP=0;
                while ((line=reader.readLine())!=null){
                    data = data + line;
                    System.out.println("Line "+lineP);
                    lineP = lineP+1;

                }


                if(!data.isEmpty()){

                    JSONArray items = new JSONArray(data);

                    System.out.println("items "+items.length());

                    itemList1.clear();
                    itemList2.clear();
                    itemList3.clear();
                    itemList4.clear();

                    int ids = 0;

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        int listId = Integer.parseInt(item.getString("listId"));
                        int id = Integer.parseInt(item.getString("id"));
                        String name = item.getString("name");
                        if(name.isEmpty()||name.equals("null"))
                            continue;

                        switch (listId) {
                            case 1:  itemList1.add(new Item(listId,id,name));
                                break;
                            case 2:  itemList2.add(new Item(listId,id,name));
                                break;
                            case 3:  itemList3.add(new Item(listId,id,name));
                                break;
                            case 4:  itemList4.add(new Item(listId,id,name));
                                break;
                        }

                        if(listId>ids){
                            ids = listId;
                        }
                    }

                    System.out.println("max id: "+ ids);

                    Collections.sort(itemList1);
                    Collections.sort(itemList2);
                    Collections.sort(itemList3);
                    Collections.sort(itemList4);

                    for (int i = 0; i < itemList1.size(); i++) {
                        itemArrayList1.add(itemList1.get(i).itemDisc);
                    }
                    for (int i = 0; i < itemList2.size(); i++) {
                        itemArrayList2.add(itemList2.get(i).itemDisc);
                    }
                    for (int i = 0; i < itemList3.size(); i++) {
                        itemArrayList3.add(itemList3.get(i).itemDisc);
                    }
                    for (int i = 0; i < itemList4.size(); i++) {
                        itemArrayList4.add(itemList4.get(i).itemDisc);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(() -> {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    listAdapter1.notifyDataSetChanged();
                    listAdapter2.notifyDataSetChanged();
                    listAdapter3.notifyDataSetChanged();
                    listAdapter4.notifyDataSetChanged();
                }
            });
        }
    }
}