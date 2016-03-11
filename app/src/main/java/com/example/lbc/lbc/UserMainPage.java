package com.example.lbc.lbc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbc on 2016/3/4.
 */
public class UserMainPage extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ListView mylist;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_page);

        mylist = (ListView) findViewById(R.id.mListView);
        adapter = new ArrayAdapter<String>(
                this,
                R.layout.item_marrayadapter,
                getDataSource());
        mylist.setAdapter(adapter);
    }
    public List<String>getDataSource(){
        List<String> list = new ArrayList<String>();
        for(int i = 0;i < 30 ;i++){
            list.add("jack"+i);
        }
        return  list;
    }
}
