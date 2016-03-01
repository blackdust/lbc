package com.example.lbc.lbc;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by lbc on 2016/2/22.
 */
public class Register extends AppCompatActivity {
    private TextView text;
    String age;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlbc);
        Button startButten =(Button) findViewById(R.id.end);
        startButten.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = getIntent();
//                edittext = （EditText）this.findViewById(R.id.xxx);
                i.putExtra("callback","call back msg");
                setResult(100,i);
                finish();
            };
        });

        Intent intent = getIntent();
        age = intent.getStringExtra("age");
        text =(TextView) findViewById(R.id.a);
        text.setText("~~~~age~~~"+age);

    }
}
