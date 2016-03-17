package com.example.lbc.lbc;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity {
    private EditText user,psw;
    private  Button buttonx;
    private ProgressDialog dialog;
    private String result_for_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button nextlbc = (Button) findViewById(R.id.go_to_nextlbc);
        nextlbc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nextpage();
            }
        });
        final Button log_in = (Button) findViewById(R.id.log_in);
        log_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                log_in_user();
            }
        });

        buttonx = (Button) findViewById(R.id.button3);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setCancelable(false);
        dialog.setMessage("下载中。。。。。。。。。。。" );
        buttonx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                result_for_task = null;
                System.out.println("初始化"+ result_for_task);
                new MyTask().execute("传入东西");
                System.out.println( result_for_task);
            }
        });
    }

//    测试getbaidu
    public class MyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            String result = null;
            try {
                result = test_get_request();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected  void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            TextView textb =(TextView) findViewById(R.id.b);
            textb.setText(result);
            result_for_task = result;
            System.out.println(result_for_task);
        }
    }
    String test_get_request() throws Exception {
        String url = "http://www.baidu.com/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private void log_in_user() {
//        登入成功则跳入新页面
        Intent user_main_page = new Intent(this, UserMainPage.class);
        startActivity(user_main_page);
    }
    private void nextpage() {
        Intent reister = new Intent(this, Register.class);
        startActivityForResult(reister, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1000&&resultCode==100){
            String callbackstr =data.getStringExtra("callback");
            TextView callbacktext =(TextView) findViewById(R.id.b);
            callbacktext.setText(callbackstr);
        }
    }
}
