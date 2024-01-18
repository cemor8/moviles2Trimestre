package com.example.test;

import android.os.AsyncTask;

public class NetworkOperationAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        // Realiza la operación de red aquí
        // Usa urls[0] como la URL para conectarte
        return "resultado";
    }

    @Override
    protected void onPostExecute(String result) {
        // Actualiza la UI con el resultado aquí
    }
}
