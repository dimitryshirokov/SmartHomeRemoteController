package com.example.dmitry.smarthomeremotecontroller;

import android.app.AlertDialog;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeviceStatuses {

    /**
     * Метод для получения статусов устройств освещения с сервера
     * @return String
     */
    private String status() {
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

        return result;
    }

    /**
     * Метод, преобразующий строку со статусами в целочисленный массив
     * @return int[]
     */
    private boolean[] statusesArray(String statusString){
        int[] statusesArray = new int[4];
        boolean[] statusesArrayBool = new boolean[4];
        char[] sA = statusString.toCharArray();

        for (int i = 0; i < 4; i++) {
            statusesArray[i] = (int) sA[i];
            statusesArrayBool[i] = statusesArray[i] == 1;
        }

        return statusesArrayBool;
    }

    /**
     * Метод, возвращающий статусы устройств
     * @return String
     */
    public String getStatus() {
        return this.status();
    }

    /**
     * Метод, возвращающий массив статусов устройств
     * @return Integer[]
     */
    public boolean[] getStatusesArray(String statusString) {
        return this.statusesArray(statusString);
    }

}
