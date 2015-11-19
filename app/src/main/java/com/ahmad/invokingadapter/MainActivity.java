package com.ahmad.invokingadapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLResourceRequest;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends Activity {
    private Button buttonConnect = null;
    private Button buttonInvoke = null;
    private static TextView textView = null;
    private static MainActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _this = this;

        buttonConnect = (Button)findViewById(R.id.buttonConnect);
        buttonInvoke = (Button)findViewById(R.id.buttonInvoke);
        textView = (TextView)findViewById(R.id.textView);

        final WLClient client = WLClient.createInstance(this);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTextView("Connecting...");
                client.connect(new MyConnectListener());
            }
        });

        buttonInvoke.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTextView("Invoking procedure...");

                try {
                    //Define the URI of the resource. For a JS HTTP adapter: /adapters/{AdapterName}/{ProcedureName}
                    URI adapterPath = new URI("/adapters/RSSReader/getFeed");

                    //Create WLResourceRequest object. Choose the HTTP Method (GET, POST, etc).
                    WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

                    //For JavaScript-based adapters, use the parameter name "params" to set an array of parameters
                    //For Java adapters or other resources, you may use setQueryParameter for each parameter
                    request.setQueryParameter("params", "['MobileFirst_Platform']");

                    //There are multiple ways to handle the response. Here a listener is used.
                    request.send(new MyInvokeListener());

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateTextView(final String str){
        Runnable run = new Runnable() {
            public void run() {
                textView.setText(str);
            }
        };
        _this.runOnUiThread(run);
    }

}
