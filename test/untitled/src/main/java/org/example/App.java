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

    public void crearFactura() throws IOException {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        String json = "{"
                + "\"nombreCliente\":\"Sitio1\","
                + "\"productos\":["
                + "  {\"nombre\":\"Agua\", \"cantidad\":2, \"precio\":10.0},"
                + "  {\"nombre\":\"Kas\", \"cantidad\":3, \"precio\":15.0}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url("http://localhost:3000/factura")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Imprime la respuesta
            System.out.println(response.body().string());
        }
    }

    public static void main(String[] args) throws IOException {
        new App().crearFactura();

    }
}

