package com.example.lbc.lbc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

/**
 * Created by lbc on 2016/3/4.
 */
public class UserMainPage extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_page);
        String[] arrayString = {"item1","item2","item3"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this,
                R.layout.item_marrayadapter,
                arrayString);
    }
}
