package org.example;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public class App {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    String createProduct(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static void modifyProduct(String url, String productName, Double listPrice, String description) throws IOException {
        String jsonBody = "{\"productName\":\"" + productName + "\", \"updateData\": {\"list_price\": " + listPrice + ", \"description\": \"" + description + "\"}}";
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        // Crear la solicitud
        Request request = new Request.Builder()
                .url(url + "/modify-product")
                .post(body)
                .build();

        // Realizar la solicitud y obtener la respuesta
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response code: " + response.code());
            System.out.println("Response body: " + response.body().string());
        }
    }

    public static void main(String[] args) throws IOException {
        App example = new App();
        /*
        String json = "{\"name\": \"Producto de Ejemplo\", \"list_price\": 25.5}";
        String response = example.createProduct("http://localhost:3000/product", json);
        System.out.println(response);
         */


        try {
            String url = "http://localhost:3000"; // Cambia esto por la URL de tu servidor Express
            String productName = "Producto de Ejemplo"; // Asegúrate de que coincida con el nombre del producto que quieres modificar
            double listPrice = 25.99; // Nuevo precio de lista
            String description = "Descripción actualizada del producto"; // Nueva descripción

            modifyProduct(url, productName, listPrice,description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

