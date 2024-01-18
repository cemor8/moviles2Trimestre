package com.example.bibliotecamongo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {
    private String url= "https://eu-west-2.aws.data.mongodb-api.com/app/biblioteca-nxels/endpoint/getLibros"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL url1;
        try {
             url1 = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URLConnection urlConnection;
        try {
            urlConnection = url1.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String linea="";
        while (true) {
            try {
                if (!((linea = bufferedReader.readLine())!=null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stringBuilder.append(linea);
        }
        System.out.println(stringBuilder.toString());

    }
}