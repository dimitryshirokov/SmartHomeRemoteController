package com.example.dmitry.smarthomeremotecontroller;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeviceAction {

    public void switchLight(String currentStatus, int id) {
        String[] stringArrayToAsync = {Integer.toString(id), currentStatus};
        new AsyncSwitch().execute(stringArrayToAsync);
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncSwitch extends AsyncTask<String[], String, String> {

        @Override
        protected String doInBackground(String[]... strings) {
            String request = "http://192.168.1.66/home/switch?id="+strings[0][0]+"&currStat="+strings[0][1];
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
                //
            }
            return result;
        }
    }
}


