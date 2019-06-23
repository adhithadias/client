package com.client.myapplication.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    Thread Thread1 = null;

    EditText etIP, etPort;
    TextView tvMessages;
    ScrollView tvScroll;

    EditText etMessage;
    Button btnSend;

    String SERVER_IP;
    int SERVER_PORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        //test
        etIP.setText("192.168.8.101");
        etPort.setText("8070");

        tvMessages = findViewById(R.id.tvMessages);
        tvScroll = findViewById(R.id.tvScroll);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                System.out.println("CREATING THREAD 1 TO CONNECT!!!");
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    System.out.println("STARTING THREAD 3 TO SEND MESSAGES TO THE SERVER");
                    new Thread(new Thread3(message)).start();
                }
            }
        });
    }

    private PrintWriter out;
    private BufferedReader in;

    class Thread1 implements Runnable {

        @Override
        public void run() {
            Socket socket;
            try {
                System.out.println("STARING THREAD 1 TO CONNECT!!!");
                socket = new Socket(SERVER_IP, SERVER_PORT);

                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });

                System.out.println("STARTING THREAD 2 FOR READING MESSAGES FROM THE SERVER");
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // thread 2 is for reading
    class Thread2 implements Runnable {
        @Override
        public void run() {
            System.out.println("THREAD 2 IS RUNNING...");
            while (true) {
                try {
                    System.out.println("msg....");
                    System.out.println("reading in message");

                    final String message = in.readLine();

                    System.out.println("message received: " + message);
                    if (message != null && !message.isEmpty()) {
                        System.out.println("msg is not empty");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                            }
                        });
                    } else {
                        System.out.println("message is empty");

                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // thread 3 is for writing
    class Thread3 implements Runnable {
        private String message;

        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println("THREAD 3 IS RUNNING...");
            System.out.println("writing out the message: " + message);

            out.println(message);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + message + "\n");
                    etMessage.setText("");
                }
            });
        }
    }
}


