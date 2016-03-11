package com.example.lbc.lbc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lbc on 2016/2/22.
 */
public class Register extends AppCompatActivity {
    private TextView text;
    String age;
    private EditText phone_num_edit_text;
    private Button post_phone_num_check_button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlbc);

        Button startButton = (Button) findViewById(R.id.end);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = getIntent();
//
                i.putExtra("callback", "call back msg");
                setResult(100, i);
                finish();
            }

            ;
        });

        Intent intent = getIntent();
        age = intent.getStringExtra("age");
        text = (TextView) findViewById(R.id.a);
        text.setText("~~~~age~~~" + age);

        phone_num_edit_text = (EditText) findViewById(R.id.edit_text_phone);

        post_phone_num_check_button = (Button) findViewById(R.id.button_msg_check);
        post_phone_num_check_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                post_to_phone_num_check_mod();
            }
        });
    }

    public void post_to_phone_num_check_mod() {
        String phone_num = phone_num_edit_text.getText().toString();
        new Task_for_post_to_phone_num_check_mod().execute(phone_num);
    }

    public class Task_for_post_to_phone_num_check_mod extends AsyncTask<String, Integer, String> {
        //        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//        }
        @Override
        protected String doInBackground(String... params) {
            String result = null;

            try {
                result = http_post_request(params[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
        }
    }


    String http_post_request(String json) throws Exception {
        String url = "http://192.168.0.132:3000/messages";
            OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("message[phone_num]", json)
                .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
