package com.example.lbc.lbc;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;
//    EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void log_in_user() {
        Intent user_main_page = new Intent(this, UserMainPage.class);
        startActivity(user_main_page);
    }
    private void nextpage() {
        Intent reister = new Intent(this, Register.class);
//        reister.putExtra("age", "12");
//        edittext = (EditText) findViewById(R.id.user);
//        reister.putExtra("age", edittext.getText());
//        edittext = (EditText) this.findViewById(R.id.user);
//        reister.putExtra("age", edittext.getText());
//        startActivityForResult(reister, 1000);
//
        String url = "http://192.168.0.131:3000/messages/test_get";
        String res = "";
//        OkHttpClient client = new OkHttpClient();
//        String run(String url) throws IOException {
//            Request request = new Request.Builder().url(url).build();
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                return response.body().string();
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
//        }
        reister.putExtra("age", "12");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lbc.lbc/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lbc.lbc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
