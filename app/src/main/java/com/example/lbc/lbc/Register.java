package com.example.lbc.lbc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Register extends AppCompatActivity {
    private TextView text, password_confirmation_error_msg, validate_error_msg, password_error_msg,name_error_msg,phone_error_msg;
    private EditText phone_num_edit_text, edit_text_password_text, edit_text_password_confirmation_text, validate_text,edit_name_text;
    private Button post_phone_num_check_button;
    private Button register_button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlbc);

        Button startButton = (Button) findViewById(R.id.end);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
            ;
        });
        validate_text = (EditText) findViewById(R.id.validate);
//        四个注册用的edittext
        phone_num_edit_text = (EditText) findViewById(R.id.edit_text_phone);
        edit_text_password_text = (EditText) findViewById(R.id.edit_text_password);
        edit_text_password_confirmation_text = (EditText) findViewById(R.id.edit_text_password_confirmation);
        edit_name_text = (EditText) findViewById(R.id.edit_name);

        post_phone_num_check_button = (Button) findViewById(R.id.button_msg_check);
        post_phone_num_check_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                post_to_phone_num_check_mod();
            }
        });

        register_button = (Button) findViewById(R.id.reister_btn);
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                post_to_play_auth_create();
            }
        });
    }

    public void post_to_play_auth_create() {
        String phone_num = phone_num_edit_text.getText().toString();
        String validate_fill = validate_text.getText().toString();
        new Task_for_post_to_play_auth_create().execute(phone_num, validate_fill);
    }

    public class Task_for_post_to_play_auth_create extends AsyncTask<String, Integer, String[][]> {
        @Override
        protected String[][] doInBackground(String... params) {
            String incorrect = "incorrect";
            String[][] wrong_msg_arr = new String[10][2];
            try {
                String result = post_to_phone_num_check_mod_check_validation(params[0], params[1]);
                JSONObject dataJson = new JSONObject(result);
                System.out.println(dataJson.getString("validation_result").equals(incorrect));
                if (dataJson.getString("validation_result").equals(incorrect)) {
                    wrong_msg_arr[0][0]="validation_result";
                    wrong_msg_arr[0][1]="验证码错误";
                } else {
                    String create_user_result = post_to_play_auth();
                    JSONObject json_from_play_auth = new JSONObject(create_user_result);
                    if (json_from_play_auth.getString("create_user").equals("fail")) {
                        Gson gson = new Gson();
                        Map datamap = gson.fromJson(json_from_play_auth.getString("msg"), Map.class);
                        if(datamap.get("name")!=null){
                            wrong_msg_arr[0][0] =  "name";
                            wrong_msg_arr[0][1] = datamap.get("name").toString().replaceAll("\\]|\\[","");
                        }
                        if(datamap.get("phone_num")!=null){
                            wrong_msg_arr[1][0] =  "phone_num";
                            wrong_msg_arr[1][1] = datamap.get("phone_num").toString().replaceAll("\\]|\\[","");
                        }
                        if(datamap.get("password_confirmation")!=null){
                            wrong_msg_arr[2][0] =  "password_confirmation";
                            wrong_msg_arr[2][1] = datamap.get("password_confirmation").toString().replaceAll("\\]|\\[","");
                        }
                        if(datamap.get("password")!=null){
                            wrong_msg_arr[3][0] =  "password";
                            wrong_msg_arr[3][1] = datamap.get("password").toString().replaceAll("\\]|\\[","");
                        }
                    } else {
//                    注册成功 跳到登录页面
                        Intent i = getIntent();
                        i.putExtra("callback", "注册成功");
                        setResult(100, i);
                        finish();
                    }
                }

//
//                String create_user_result = post_to_play_auth();
//                JSONObject json_from_play_auth = new JSONObject(create_user_result);
//                if (json_from_play_auth.getString("create_user").equals("fail")) {
//                    Gson gson = new Gson();
//                    Map datamap = gson.fromJson(json_from_play_auth.getString("msg"), Map.class);
//                    if(datamap.get("name")!=null){
//                        wrong_msg_arr[0][0] =  "name";
//                        wrong_msg_arr[0][1] = datamap.get("name").toString().replaceAll("\\]|\\[","");
//                    }
//                    if(datamap.get("phone_num")!=null){
//                        wrong_msg_arr[1][0] =  "phone_num";
//                        wrong_msg_arr[1][1] = datamap.get("phone_num").toString().replaceAll("\\]|\\[","");
//                    }
//                    if(datamap.get("password_confirmation")!=null){
//                        wrong_msg_arr[2][0] =  "password_confirmation";
//                        wrong_msg_arr[2][1] = datamap.get("password_confirmation").toString().replaceAll("\\]|\\[","");
//                    }
//                    if(datamap.get("password")!=null){
//                        wrong_msg_arr[3][0] =  "password";
//                        wrong_msg_arr[3][1] = datamap.get("password").toString().replaceAll("\\]|\\[","");
//                    }
//                } else {
////                    注册成功 跳到登录页面
//                        Intent i = getIntent();
//                        i.putExtra("callback", "注册成功");
//                        setResult(100, i);
//                        finish();
//                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return wrong_msg_arr;
        }

        @Override
        protected void onPostExecute(String[][] result) {
            super.onPostExecute(result);
            print_error(result);
        }
    }

    public void print_error(String[][] msgs) {
        validate_error_msg = (TextView) findViewById(R.id.validate_error_msg);
        String validate_error_msg_text = "";
        password_confirmation_error_msg = (TextView) findViewById(R.id.password_confirmation_error_msg);
        String password_confirmation_error_msg_text = "";
        name_error_msg = (TextView) findViewById(R.id.name_error_msg);
        String name_error_msg_text = "";
        phone_error_msg = (TextView) findViewById(R.id.phone_error_msg);
        String phone_error_msg_text = "";
        password_error_msg = (TextView) findViewById(R.id.password_error_msg);
        String password_error_msg_text = "";
        for (int x = 0; x < msgs.length; x = x + 1) {
            if (msgs[x][0] != null) {
                switch (msgs[x][0]) {
                    case "validation_result":
                        validate_error_msg_text += msgs[x][1];
                        break;
                    case "name":
                        name_error_msg_text += msgs[x][1];
                        break;
                    case "phone_num":
                        phone_error_msg_text += msgs[x][1];
                        break;
                    case "password_confirmation":
                        password_confirmation_error_msg_text += msgs[x][1];
                        break;
                    case "password":
                        password_error_msg_text += msgs[x][1];
                        break;
                }
            }
        }
        validate_error_msg.setText(validate_error_msg_text);
        name_error_msg.setText(name_error_msg_text);
        password_confirmation_error_msg.setText(password_confirmation_error_msg_text);
        phone_error_msg.setText(phone_error_msg_text);
        password_error_msg.setText(password_error_msg_text);
    }

    //    post1
    String post_to_phone_num_check_mod_check_validation(String phone_num, String fill_validate) throws Exception {
        String url = "http://192.168.0.164:3000/messages/check_validation";
        OkHttpClient client = new OkHttpClient();
        System.out.println("开始验证填写的验证码");
        RequestBody formBody = new FormBody.Builder()
                .add("phone_num", phone_num)
                .add("valid_code", fill_validate)
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

    //    post2
    String post_to_play_auth() throws Exception {
        System.out.println("开始提交注册用户表单");
        String url = "http://192.168.0.164:3000/auth/users";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("user[name]", edit_name_text.getText().toString())
                .add("user[phone_num]", phone_num_edit_text.getText().toString())
                .add("user[password]", edit_text_password_text.getText().toString())
                .add("user[password_confirmation]", edit_text_password_confirmation_text.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
//            System.out.println(response.body().string());
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    //  发送验证码事件
    public void post_to_phone_num_check_mod() {
        String phone_num = phone_num_edit_text.getText().toString();
        new Task_for_post_to_phone_num_check_mod().execute(phone_num);
    }

    public class Task_for_post_to_phone_num_check_mod extends AsyncTask<String, Integer, String> {
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
    }

    String http_post_request(String json) throws Exception {
        String url = "http://192.168.0.164:3000/messages";
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
