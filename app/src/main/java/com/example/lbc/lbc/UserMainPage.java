package com.example.lbc.lbc;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lbc.lbc.list_load.DownLoadImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lbc on 2016/3/4.
 */
public class UserMainPage extends AppCompatActivity {
//    private ArrayAdapter<String> adapter;
    private ListView mylist;
    private ProgressDialog dialog;
    private ProudctAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_page);

        mylist = (ListView) findViewById(R.id.mListView);
//        adapter = new ArrayAdapter<String>(
//                this,
//                R.layout.item_marrayadapter,
//                getDataSource());
//        mylist.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("LOADING");
        adapter = new ProudctAdapter();
        new MyTask().execute("http://192.168.0.164:3000/messages/test_get");
    }

    public List<String> getDataSource() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            list.add("课程" + i);
        }
        return list;
    }

    public  class ProudctAdapter extends BaseAdapter{
       List<Map<String,String>> list;
        public void setData(List<Map<String,String>> list){
            System.out.println("???????");
            System.out.println(list);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            System.out.println("~~~~~~~~~");
            View view = null;
            if(convertView==null){
                view = LayoutInflater.from(UserMainPage.this).inflate(R.layout.item_marrayadapter,null);
            }else{
                view = convertView ;
            }
            TextView textView1 = (TextView)view.findViewById(R.id.list_title);
            System.out.println("123");
            System.out.println(list.get(position).get("name"));
            System.out.println(list.get(position).get("introduction"));

            TextView textView2 = (TextView)view.findViewById(R.id.list_context);
            textView1.setText(list.get(position).get("name"));
            textView2.setText(list.get(position).get("introduction"));

            System.out.println(list.get(position).get("url"));
//            写图片
            final ImageView imageview=(ImageView)view.findViewById(R.id.img);
            DownLoadImage downLoadImage=new DownLoadImage(list.get(position).get("url").toString());
            downLoadImage.loadImage(new DownLoadImage.ImageCallback() {
                @Override
                public void getDrawable(Drawable draw) {
                    imageview.setImageDrawable(draw);
                }
            });
            return view;
        }
    }


    public class MyTask extends AsyncTask<String, Integer, List<Map<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            List<Map<String,String>> list = null;
            try {
                String json = request_list(params[0]);
                list = JsonTools.parseJsonMaps(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            super.onPostExecute(result);

            System.out.println("get结果？");
            System.out.println(result);
            adapter.setData(result);
            mylist.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    String request_list(String param) throws Exception {
        System.out.println("请求列表");
        String url = "http://192.168.0.164:3000/messages/test_get";
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

