package com.example.bibliotecamongo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    String appID = "biblioteca-nxels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MongoClientURI uri = new MongoClientURI("mongodb+srv://<usuario>:<contraseña>@cluster.mongodb.net/test");
        MongoClient mongoClient = new MongoClient(uri);

    }
}