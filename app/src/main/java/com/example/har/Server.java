package com.example.har;

import android.content.Context;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;



public class Server {

    private Context mContext;
    Socket socket;


    public Server(Context context) {
        this.mContext = context;
    }
    Test mythread;
    public void connect_disconnect(Button btnIP, EditText IPtext, Button btndis) {

        //this connects and disconnects app on button press

        //this is button for connecting
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URI uri = null;
                String myIP = IPtext.getText().toString();
                Log.d("msg", "in on click, onto create client task obj");
                mythread = new Test();
                mythread.start();
                //new ClientTask(myIP, 1030).execute();


            }

        });

        //code if disconnecting button is pressed
        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mythread.close_conn();
                //DisconnectTask dis = new DisconnectTask();
                //dis.execute();
            }
        });

    }




    class ClientTask extends AsyncTask<String, Void, Void> {
        private String serverIp;
        private int serverPort;

        public ClientTask(String serverIp, int serverPort) {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                Log.d("msg", "in do in background function, onto creating socket");
                try {
                    socket = new Socket("172.16.2.77", 1030);
                }
                catch(IOException e){
                    e.printStackTrace();
                    Log.e("msg", "error creating socket");
                }
                Log.d("msg", "socket created");
                showToast("Connected to: "+ serverIp);
                // Send data to the server
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Hello world");
                // Do not close the socket here; it will be closed on disconnect
            } catch (IOException e) {
                Log.d("msg", "an exception occurred");
            }

            return null;
        }
    }
    class DisconnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Disconnect tasks (e.g., close the socket)
            try {
                if (socket != null && socket.isConnected()) {
                    socket.close();
                    showToast("Server Disconnected");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    //function to show toast
    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}




