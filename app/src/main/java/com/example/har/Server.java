package com.example.har;

import android.content.Context;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private Context mContext;
    Socket socket;
    String SERVER_IP;
    int SERVER_PORT=1026;

    private Socket client;
    private PrintWriter printwriter;


    public Server(Context context) {
        this.mContext = context;
    }

    public void connect_disconnect(Button btnIP, EditText IPtext, Button btndis, EditText mtext, ArrayList<List<Double>> sensorDataList) {
        //this connects and disconnects app on button press

        //this is button for connecting
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URI uri = null;
                String myIP = IPtext.getText().toString();
                //String message = mtext.getText().toString();

                new Thread(new ClientThread(myIP, sensorDataList)).start();


            }

        });

        //code if disconnecting button is pressed
        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    showToast("Disconnected the server");
                    client.close();
                }
                catch (Exception e){
                    showToast("Server is not connected....");
                }
            }
        });

    }




    class ClientThread implements Runnable {
        private final ArrayList<List<Double>> data;
        private final String ip;
        ClientThread(String ip, ArrayList<List<Double>> data) {
            this.ip = ip;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                client = new Socket(ip, 4444);
                // connect to server

                printwriter = new PrintWriter(client.getOutputStream(), true);
                // write the message to output stream
                for (List<Double> innerList : data) {
                    for (Double element : innerList) {
                        printwriter.print(element + ",");
                    }
                    printwriter.println(); // Move to the next line for the next inner list
                }


                printwriter.flush();
                printwriter.close();

                // closing the connection
                //client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    //





    //function to show toast
    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}




