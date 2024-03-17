package com.example.har;

import android.content.Context;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import sensor.PhoneSensor;


public class Server {

    private Context mContext;
    Socket socket;
    String SERVER_IP;
    int SERVER_PORT=1026;
    private static boolean check;
    private static boolean sendData;
    private Socket client;
    private PrintWriter printwriter;

    public Server(){

    }
    public Server(Context context) {
        this.mContext = context;
    }

    public static boolean check_f(){
        return check;
    }

    public static boolean checkData(){
        return sendData;
    }

    public void connect_disconnect(Button btnIP, EditText IPtext, Button btndis, TextView textconn, EditText UserId) {
        //this connects and disconnects app on button press

        //this is button for connecting
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URI uri = null;
                String myIP = IPtext.getText().toString();
                //String message = mtext.getText().toString();
                check = true;

                textconn.setText("Connected");
                String userid = UserId.getText().toString();
                new Thread(new ClientThread(myIP, userid)).start();




            }

        });

        //code if disconnecting button is pressed
        btndis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    showToast("Disconnected the server");
                    check = false;
                    printwriter.flush();
                    printwriter.close();
                    client.close();
                }
                catch (Exception e){
                    showToast("Server is not connected....");
                }
            }
        });


    }

    public void write_to_server(ArrayList<List<Double>> myList){

        if(client != null){
            for (List<Double> innerList : myList) {
                for (Double element : innerList) {
                    printwriter.print(element + ",");
                }
                printwriter.println(); // Move to the next line for the next inner list
            }
        }

    }




    class ClientThread implements Runnable {
       // private final ArrayList<List<Double>> data;
        private final String ip;
        private final String UserId;
        ClientThread(String ip, String UserId) {
            this.ip = ip;
            this.UserId = UserId;
            //this.data = data;
        }

        @Override
        public void run() {
            try {
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                client = new Socket(ip, 4444);
                // connect to server
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.print(UserId);
                String sensorList = PhoneSensor.getByteSensors();
                printwriter.print(sensorList);
                while(true){
                    if(check_f() == false){
                        break;
                    }
                    String s = PhoneSensor.getString();
                    printwriter.print(s);

                }
                // write the message to output stream

                //write_to_server(data);
                //for (List<Double> innerList : data) {
                  //  for (Double element : innerList) {
                    //    printwriter.print(element + ",");
                    //}
                    //printwriter.println(); // Move to the next line for the next inner list
                //}


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




