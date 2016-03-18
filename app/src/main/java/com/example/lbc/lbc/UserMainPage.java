package com.example.lbc.lbc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
            list.add("课程"+i);
        }
        return  list;
    }



    public class Task_for_add_list extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;

            try {
                result = request_list();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            处理http的数据转换成可以装入的形式
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            处理写入adpter
        }
    }
//    路径
    String request_list() throws Exception {
        String url = "http://192.168.1.10:3000/messages";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

}

