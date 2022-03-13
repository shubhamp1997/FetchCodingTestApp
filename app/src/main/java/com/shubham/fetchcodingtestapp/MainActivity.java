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
    LinkedList<Item> itemList;
    LinkedList<Item> itemList1;
    LinkedList<Item> itemList2;
    LinkedList<Item> itemList3;
    LinkedList<Item> itemList4;


    ArrayAdapter<String> listAdapter;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeItemList();
        binding.fetchButton.setOnClickListener(view -> new fetchData().start());
        System.out.println("on create complete");
    }

    private void initializeItemList(){
        itemList = new LinkedList<>();
        itemArrayList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (ArrayList<String>) itemArrayList);
        binding.itemsList.setAdapter(listAdapter);
        System.out.println("items List Initialized");
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

                    itemList.clear();

                    int ids = 0;

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        int listId = Integer.parseInt(item.getString("listId"));
                        int id = Integer.parseInt(item.getString("id"));
                        String name = item.getString("name");
                        if(name.isEmpty()||name.equals("null"))
                            continue;

                        itemList.add(new Item(listId,id,name));

                        if(listId>ids){
                            ids = listId;
                        }
                    }

                    System.out.println("max id: "+ ids);

                    Collections.sort(itemList);

                    for (int i = 0; i < itemList.size(); i++) {
                        itemArrayList.add(itemList.get(i).itemDisc);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(() -> {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}