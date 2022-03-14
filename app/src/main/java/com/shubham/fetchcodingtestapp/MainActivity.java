package com.shubham.fetchcodingtestapp;

import androidx.appcompat.app.AppCompatActivity;

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

    ActivityMainBinding binding;
    LinkedList<Item> itemList;

    int selectedList = 1;

    ListView itemsList;
    Button all,l1,l2,l3,l4,oBtn;


    ItemAdapter itemAdapter;
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
        itemAdapter = new ItemAdapter(getApplicationContext(),0,itemList);
        binding.itemsList.setAdapter(itemAdapter);
        System.out.println("items List 1 Initialized");
    }

    private void filterList(int listID){

        selectedList = listID;
        ArrayList<Item> groupedItems = new ArrayList<>();

        for(Item i: itemList){
           if(i.listId == listID){
               groupedItems.add(i);
           }
        }
        System.out.println(groupedItems.size()+"Grouped length");
        ItemAdapter adapter = new ItemAdapter(getApplicationContext(),0,groupedItems);
        binding.itemsList.setAdapter(adapter);

    }

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

                        //switch (listId) {
                        //    case 1:  itemList1.add(new Item(listId,id,name));
                        //        break;
                        //    case 2:  itemList2.add(new Item(listId,id,name));
                        //        break;
                        //    case 3:  itemList3.add(new Item(listId,id,name));
                        //        break;
                        //    case 4:  itemList4.add(new Item(listId,id,name));
                        //        break;
                        //}

                        itemList.add(new Item(listId,id,name));

                        if(listId>ids){
                            ids = listId;
                        }
                    }

                    System.out.println("max id: "+ ids);

                    Collections.sort(itemList);

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(() -> {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    itemAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}