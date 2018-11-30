package com.example.dmitry.smarthomeremotecontroller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private static int ROOM_ID = 1;
    private static int KITCHEN_ID = 2;
    private static int BATHROOM_ID = 3;
    private static int HALLWAY_ID = 4;

    private static int GREEN = Color.parseColor("#00a55e");
    private static int RED = Color.parseColor("#e14d42");

    private static String STATUS_OFF = "off";
    private static String STATUS_ON = "on";

    private static String statusString = null;
    private static String oldStatusString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshInfo();
                        oldStatusString = statusString;
                    }
                });
            }
        }, 0, 1500);
    }

    public void kitchenClick(View view) {
        this.buttonClick(view, KITCHEN_ID);
    }

    private void buttonClick(View view, int id) {
        DeviceAction deviceAction = new DeviceAction();
        if (this.buttonColor(view) == GREEN) {
            deviceAction.switchLight(STATUS_OFF, id);

        } else if (this.buttonColor(view) == RED) {
            deviceAction.switchLight(STATUS_ON, id);
        }
        view.setBackgroundColor(Color.parseColor("#f69c34"));

    }

    private int buttonColor(View view) {
        ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
        return buttonColor.getColor();
    }

    public void hallwayClick(View view) {
        this.buttonClick(view, HALLWAY_ID);
    }

    public void roomClick(View view) {
        this.buttonClick(view, ROOM_ID);
    }

    public void bathroomClick(View view) {
        this.buttonClick(view, BATHROOM_ID);
    }

    private void refreshInfo() {
        new AsyncRequest().execute("1", "1", "1");
        if (statusString != null && !statusString.equals(oldStatusString)) {
            String result = statusString;
            boolean[] statusesArrayBool = new boolean[4];
            int[] statusesArray = new int[4];
            char[] sA = result.toCharArray();

            for (int i = 0; i < 4; i++) {
                statusesArray[i] = Character.getNumericValue(sA[i]);
                statusesArrayBool[i] = statusesArray[i] == 1;
            }
            for (int i = 0; i < 4; i++) {
                if (statusesArrayBool[i]) {
                    switch (i) {
                        case 0:
                            findViewById(R.id.room).setBackgroundColor(Color.parseColor("#e14d42"));
                            break;
                        case 1:
                            findViewById(R.id.kitchen).setBackgroundColor(Color.parseColor("#e14d42"));
                            break;
                        case 2:
                            findViewById(R.id.hallway).setBackgroundColor(Color.parseColor("#e14d42"));
                            break;
                        case 3:
                            findViewById(R.id.bathroom).setBackgroundColor(Color.parseColor("#e14d42"));
                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            findViewById(R.id.room).setBackgroundColor(Color.parseColor("#00a55e"));
                            break;
                        case 1:
                            findViewById(R.id.kitchen).setBackgroundColor(Color.parseColor("#00a55e"));
                            break;
                        case 2:
                            findViewById(R.id.hallway).setBackgroundColor(Color.parseColor("#00a55e"));
                            break;
                        case 3:
                            findViewById(R.id.bathroom).setBackgroundColor(Color.parseColor("#00a55e"));
                            break;
                    }
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class AsyncRequest extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String request = "http://192.168.1.66/mobile/info";
            String result = null;

            try {
                URL url = new URL(request);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result = response.append(inputLine).toString();
                }
            } catch (IOException ex) {
                String e = ex.getMessage();
            }
            statusString = result;
            return result;
        }



    }
}
