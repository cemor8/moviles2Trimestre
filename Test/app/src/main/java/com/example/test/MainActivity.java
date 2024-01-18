package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String url= "https://eu-west-2.aws.data.mongodb-api.com/app/biblioteca-nxels/endpoint/getLibros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("hola");
        new RetrieveFeedTask().execute(url);

    }
    private class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                ArrayList<Libro> libros = new ArrayList<>();
                ArrayList<String> lista = JSONArray(StringBuilder.)
            } catch (IOException e) {
                // Aquí debes manejar la FileNotFoundException y cualquier otra IOException.
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            System.out.println(stringBuilder.toString());
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            super.onPostExecute(result);
            // Aquí puedes actualizar la UI con el resultado de la operación de red.
            // Por ejemplo, mostrar el resultado en un TextView.
        }
    }
}