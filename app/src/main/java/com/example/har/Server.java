package com.example.har;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server {

    private Context mContext;

    public Server(Context context) {
        this.mContext = context;
    }

    public void connect_disconnect(Button btnIP, EditText IPtext, HttpURLConnection[] urlConnection, Button btndis) {
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myIP = IPtext.getText().toString();
                Log.d("Your ip", myIP);
                try {
                    URL url = new URL("http://" + myIP);
                    urlConnection[0] = (HttpURLConnection) url.openConnection();

                    if (urlConnection[0] != null) {
                        showToast("Server connected to: " + myIP);
                    }
                    try {
                        urlConnection[0].setRequestMethod("POST");

                        //enabling input output streams
                        urlConnection[0].setDoInput(true);
                        urlConnection[0].setDoOutput(true);

                        //getting output stream
                        OutputStream fout = new BufferedOutputStream(urlConnection[0].getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fout));

                        // WriteMyData();


                    } catch (IOException e) {
                        //exception handling
                    }

                } catch (IOException e) {
                    //handling exceptions
                }
            }


        });


        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlConnection[0].disconnect();
                showToast("Server Disconnected");
            }
        });

    }

    private void showToast(String ip) {
        Toast.makeText(mContext, ip, Toast.LENGTH_SHORT).show();
    }

}



